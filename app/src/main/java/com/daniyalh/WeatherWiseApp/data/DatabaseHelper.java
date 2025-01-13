package com.daniyalh.WeatherWiseApp.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final String dbName;
    private final Context context;
    private static final int DB_VERSION = 1;
    private static DatabaseHelper instance;
    private static final String TAG = "MyDatabase";
    private SQLiteDatabase inMemoryDb;
    private CityRepository cityRepository;
    private WeatherRepository weatherRepository;

    private DatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);
        this.dbName = dbName;
        this.context = context;
        if ("WeatherWiseApp.db".equals(dbName)) {
            copyDatabaseToInternalStorage(context);
        }
    }

    public static synchronized DatabaseHelper getInstance(Context context, String dbName) {
        // singleton
        if (instance == null || !instance.dbName.equals(dbName)) {
            instance = new DatabaseHelper(context.getApplicationContext(), dbName);
        }
        return instance;
    }

    // Singletons for repositories
    public CityRepository getCityRepository() {
        if (cityRepository == null) {
            cityRepository = new CityRepository(this);
        }
        return cityRepository;
    }

    public WeatherRepository getWeatherRepository() {
        if (weatherRepository == null) {
            weatherRepository = new WeatherRepository(this);
        }
        return weatherRepository;
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

        // nullify repository references
        cityRepository = null;
        weatherRepository = null;
    }
}
