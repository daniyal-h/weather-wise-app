package com.daniyalh.WeatherWiseApp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.daniyalh.WeatherWiseApp.logic.IWeatherManager;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;
import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.City;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public final String dbName;
    private final Context context;
    private WeatherManager weatherManager;
    private static final int OUTDATED_LIMIT = 10 * 60 * 1000; // ten minutes in milliseconds
    private static final int DB_VERSION = 1;
    private static MyDatabaseHelper instance;
    private static final String TAG = "MyDatabase";
    private SQLiteDatabase inMemoryDb;

    private MyDatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);
        this.dbName = dbName;
        this.context = context;
        if ("WeatherWiseApp.db".equals(dbName)) {
            copyDatabaseToInternalStorage(context);
        }
    }

    public static synchronized MyDatabaseHelper getInstance(Context context, String dbName) {
        // singleton
        if (instance == null || !instance.dbName.equals(dbName)) {
            instance = new MyDatabaseHelper(context.getApplicationContext(), dbName);
        }
        return instance;
    }

    private void copyDatabaseToInternalStorage(Context context) {
        /*
        Copy the file to internal storage
        If it already exists, do not copy
         */
        File dbFile = context.getDatabasePath(dbName);

        if (!dbFile.exists()) {
            try (InputStream is = context.getAssets().open(dbName);
                 OutputStream os = new FileOutputStream(dbFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                Log.d(TAG, "Database "  + dbName + " copied to internal storage.");
            } catch (IOException e) {
                Log.e(TAG, "Error copying database " + dbName, e);
            }
        } else {
            Log.d(TAG, "Database " + dbName + " already exists in internal storage.");
        }
    }

    public void setWeatherManager(WeatherManager weatherManager) {
        this.weatherManager = weatherManager;
    }

    public Cursor getFavouriteCities() {
        // return a cursor with all favourite cities
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT cityID, name || ', ' || country_code AS display_name, country "
                   + "FROM cities WHERE is_favourite = 1 "
                   + "ORDER BY display_name";
        return database.rawQuery(sql, null);
    }

    public Cursor getCitiesByQuery(String query) {
        // return a cursor with top 10 cities through a following wild card search
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT cityID AS _id, country_code, name || ', ' || country AS display_name, is_favourite"
                  +  " FROM CITIES WHERE name LIKE ? LIMIT 10";
        return database.rawQuery(sql, new String[]{query + "%"});
    }

    public void getWeatherDetails(City city, IWeatherManager.IWeatherDetailsCallback callback) {
        /*
        Asynchronously acquire weather details for given city
        Weather Table stores cached immediate weather
        If the forecast stored is updated (<= 10 minutes), return that
        Otherwise, update the value and return the new one
         */
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT lastUpdated, temp, feels_like, description, humidity, wind_speed, offset, sunrise, sunset, tod " +
                "FROM Weather WHERE cityID = ? AND lastUpdated != -1;";

        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(city.getCityID())});
        long currentTime = System.currentTimeMillis();

        // check if the result is empty
        if (cursor.moveToFirst()) {
            // check if the result is viable (<= 10 minutes)
            long lastUpdated = cursor.getLong(cursor.getColumnIndexOrThrow("lastUpdated"));

            if (currentTime - lastUpdated <= OUTDATED_LIMIT) {
                String[] weatherDetails = extractWeatherDetailsFromCursor(cursor); // return cached forecast
                cursor.close();
                callback.onSuccess(weatherDetails);
                return;
            }
        }
        cursor.close();

        // cache is outdated or empty; fetch new data
        updateCache(city, new ICacheUpdateCallback() {
            @Override
            public void onCacheUpdated(String[] newWeatherData) {
                callback.onSuccess(newWeatherData);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private String[] extractWeatherDetailsFromCursor(Cursor cursor) {
        // convert the cursor to an array of strings for the forecast
        return new String[] {
                cursor.getString(cursor.getColumnIndexOrThrow("temp")),
                cursor.getString(cursor.getColumnIndexOrThrow("feels_like")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getString(cursor.getColumnIndexOrThrow("humidity")),
                cursor.getString(cursor.getColumnIndexOrThrow("wind_speed")),
                cursor.getString(cursor.getColumnIndexOrThrow("offset")),
                cursor.getString(cursor.getColumnIndexOrThrow("sunrise")),
                cursor.getString(cursor.getColumnIndexOrThrow("sunset")),
                cursor.getString(cursor.getColumnIndexOrThrow("tod"))
        };
    }

    private void updateCache(City city, ICacheUpdateCallback callback) {
        /*
        Update Weather Table with the new weather forecast
        Fetch weather from WeatherManager and insert or update
        Should be done asynchronously
         */
        weatherManager.getWeatherJSON(city, new IWeatherManager.IWeatherCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    String[] newWeatherData = weatherManager.fetchImmediateWeather(response);

                    // Update the Weather table with the new data
                    SQLiteDatabase database = getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("cityID", city.getCityID());
                    values.put("lastUpdated", System.currentTimeMillis());
                    values.put("temp", newWeatherData[0]);
                    values.put("feels_like", newWeatherData[1]);
                    values.put("description", newWeatherData[2]);
                    values.put("humidity", newWeatherData[3]);
                    values.put("wind_speed", newWeatherData[4]);
                    values.put("offset", newWeatherData[5]);
                    values.put("sunrise", newWeatherData[6]);
                    values.put("sunset", newWeatherData[7]);
                    values.put("tod", newWeatherData[8]);

                    // insert or update existing cache (outdated)
                    database.insertWithOnConflict("Weather", null, values, SQLiteDatabase.CONFLICT_REPLACE);

                    // Notify success
                    callback.onCacheUpdated(newWeatherData);
                }
                catch (InvalidJsonParsingException e) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void updateFavouriteStatus(int cityID, boolean isFavourite) {
        // toggle the favourite status based on the given boolean
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "UPDATE cities SET is_favourite = ? WHERE cityID = ?";
        database.execSQL(sql, new Object[]{isFavourite ? 1 : 0, cityID});
    }

    public void clearFavourites() {
        // clear all favourite cities by setting them to 0
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "UPDATE cities SET is_favourite = 0 WHERE is_favourite = 1";
        database.execSQL(sql, new Object[]{});
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        // return in memory DB for tests
        if ("WeatherWiseApp_stub1.db".equals(dbName)) {
            if (inMemoryDb == null || !inMemoryDb.isOpen()) {
                inMemoryDb = SQLiteDatabase.create(null);
                Log.d(TAG, "In-memory readable database created.");
                populateInMemoryDatabase(context, inMemoryDb);
            }
            return inMemoryDb;
        // return main DB from internal storage for general use
        } else {
            SQLiteDatabase db = super.getReadableDatabase();
            Log.d(TAG, "Readable database path: " + db.getPath());
            return db;
        }
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        // return in memory DB for tests
        if ("WeatherWiseApp_stub1.db".equals(dbName)) {
            if (inMemoryDb == null || !inMemoryDb.isOpen()) {
                inMemoryDb = SQLiteDatabase.create(null);
                Log.d(TAG, "In-memory writable database created.");
                populateInMemoryDatabase(context, inMemoryDb);
            }
            return inMemoryDb;
        // return main DB from internal storage for general use
        } else {
            SQLiteDatabase db = super.getWritableDatabase();
            Log.d(TAG, "Writable database path: " + db.getPath());
            return db;
        }
    }

    private void populateInMemoryDatabase(Context context, SQLiteDatabase db) {
        // populate from stub into memory
        try (InputStream is = context.getAssets().open("WeatherWiseApp_stub1.sql")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            String sqlScript = new String(buffer);
            is.close();

            String[] sqlStatements = sqlScript.split(";");
            db.beginTransaction();
            try {
                for (String statement : sqlStatements) {
                    statement = statement.trim();
                    if (!statement.isEmpty()) {
                        db.execSQL(statement);
                    }
                }
                db.setTransactionSuccessful();
                Log.d(TAG, "In-memory database populated from SQL script.");
            } finally {
                db.endTransaction();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error populating in-memory database", e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    @Override
    public synchronized void close() {
        super.close();
        if (inMemoryDb != null && inMemoryDb.isOpen()) {
            inMemoryDb.close();
            inMemoryDb = null;
        }
    }
}