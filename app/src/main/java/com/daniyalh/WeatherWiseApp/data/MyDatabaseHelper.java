package com.daniyalh.WeatherWiseApp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.daniyalh.WeatherWiseApp.logic.ISearchManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "WeatherWiseApp.db";
    private static final int DB_VERSION = 1;
    private static MyDatabaseHelper instance;
    private static final String TAG = "MyDatabase";

    private MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        copyDatabaseToInternalStorage(context);
    }

    public static synchronized MyDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MyDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private void copyDatabaseToInternalStorage(Context context) {
        File dbFile = context.getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            try (InputStream is = context.getAssets().open(DB_NAME);
                 OutputStream os = new FileOutputStream(dbFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                Log.d(TAG, "Database copied to internal storage.");
            } catch (IOException e) {
                Log.e(TAG, "Error copying database", e);
            }
        } else {
            Log.d(TAG, "Database already exists in internal storage.");
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
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
