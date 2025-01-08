package com.daniyalh.WeatherWiseApp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public final String dbName;
    private final Context context;
    private static final int DB_VERSION = 1;
    private static MyDatabaseHelper instance;
    private static final String TAG = "MyDatabase";
    private SQLiteDatabase inMemoryDb;

    /*private MyDatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);
        this.dbName = dbName;
        copyDatabaseToInternalStorage(context);
    }*/

    private MyDatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);
        this.dbName = dbName;
        this.context = context;
        if ("WeatherWiseApp.db".equals(dbName)) {
            copyDatabaseToInternalStorage(context);
        }
    }

    public static synchronized MyDatabaseHelper getInstance(Context context, String dbName) {
        if (instance == null || !instance.dbName.equals(dbName)) {
            instance = new MyDatabaseHelper(context.getApplicationContext(), dbName);
        }
        return instance;
    }

    private void copyDatabaseToInternalStorage(Context context) {
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

    public void logAllDatabases(Context context) {
        // Get the internal storage path for databases
        String[] databaseList = context.databaseList();

        // Log each database
        if (databaseList.length > 0) {
            Log.d("DatabaseList", "Databases in internal storage:");
            for (String dbName : databaseList) {
                Log.d("DatabaseList", dbName);
            }
        } else {
            Log.d("DatabaseList", "No databases found in internal storage.");
        }
    }


    public Cursor getFavouriteCities() {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT cityID, name || ', ' || country_code AS display_name, country "
                   + "FROM cities WHERE is_favourite = 1 "
                   + "ORDER BY display_name";
        return database.rawQuery(sql, null);
    }

    public Cursor getCitiesByQuery(String query) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT cityID AS _id, country_code, name || ', ' || country AS display_name, is_favourite"
                  +  " FROM CITIES WHERE name LIKE ? LIMIT 10";
        return database.rawQuery(sql, new String[]{query + "%"});
    }

    public void updateFavouriteStatus(int cityID, boolean isFavourite) {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "UPDATE cities SET is_favourite = ? WHERE cityID = ?";
        database.execSQL(sql, new Object[]{isFavourite ? 1 : 0, cityID});
        Log.d(TAG, "Updated favourite status for cityID: " + cityID);
    }

    public void clearFavourites() {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "UPDATE cities SET is_favourite = 0 WHERE is_favourite = 1";
        database.execSQL(sql, new Object[]{});
        Log.d(TAG, "Removed all favourites");
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        if ("WeatherWiseApp_stub1.db".equals(dbName)) {
            if (inMemoryDb == null || !inMemoryDb.isOpen()) {
                inMemoryDb = SQLiteDatabase.create(null);
                Log.d(TAG, "In-memory readable database created.");
                populateInMemoryDatabase(context, inMemoryDb);
            }
            return inMemoryDb;
        } else {
            SQLiteDatabase db = super.getReadableDatabase();
            Log.d(TAG, "Readable database path: " + db.getPath());
            return db;
        }
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if ("WeatherWiseApp_stub1.db".equals(dbName)) {
            if (inMemoryDb == null || !inMemoryDb.isOpen()) {
                inMemoryDb = SQLiteDatabase.create(null);
                Log.d(TAG, "In-memory writable database created.");
                populateInMemoryDatabase(context, inMemoryDb);
            }
            return inMemoryDb;
        } else {
            SQLiteDatabase db = super.getWritableDatabase();
            Log.d(TAG, "Writable database path: " + db.getPath());
            return db;
        }
    }

    private void populateInMemoryDatabase(Context context, SQLiteDatabase db) {
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
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close() {
        super.close();
        if (inMemoryDb != null && inMemoryDb.isOpen()) {
            inMemoryDb.close();
            inMemoryDb = null;
        }
    }
}
