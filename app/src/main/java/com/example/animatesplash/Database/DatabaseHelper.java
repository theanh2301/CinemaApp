package com.example.animatesplash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.animatesplash.Domains.Film;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FavoriteMovies.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_POSTER = "poster";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_TRAILER = "trailer";
    private static final String COLUMN_IMBD = "imbd";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_GENRE = "genre"; // Genre lưu dưới dạng chuỗi JSON

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_POSTER + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_TRAILER + " TEXT, " +
                COLUMN_IMBD + " INTEGER, " +
                COLUMN_YEAR + " INTEGER, " +
                COLUMN_GENRE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addFavorite(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, film.getTitle());
        values.put(COLUMN_DESCRIPTION, film.getDescription());
        values.put(COLUMN_POSTER, film.getPoster());
        values.put(COLUMN_TIME, film.getTime());
        values.put(COLUMN_TRAILER, film.getTrailer());
        values.put(COLUMN_IMBD, film.getImbd());
        values.put(COLUMN_YEAR, film.getYear());

        // Chuyển Genre thành chuỗi JSON để lưu trữ
        String genreJson = new com.google.gson.Gson().toJson(film.getGenre());
        values.put(COLUMN_GENRE, genreJson);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean deleteFavorite(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_TITLE + "=?", new String[]{title});
        return result > 0;
    }

    public ArrayList<Film> getAllFavorites() {
        ArrayList<Film> favoriteMovies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Film film = new Film();
                film.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                film.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                film.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER)));
                film.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)));
                film.setTrailer(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRAILER)));
                film.setImbd(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMBD)));
                film.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR)));

                // Chuyển chuỗi JSON thành danh sách thể loại
                String genreJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE));
                ArrayList<String> genres = new com.google.gson.Gson().fromJson(genreJson, ArrayList.class);
                film.setGenre(genres);

                favoriteMovies.add(film);
            }
            cursor.close();
        }
        return favoriteMovies;
    }

}

