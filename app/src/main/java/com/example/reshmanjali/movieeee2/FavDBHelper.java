package com.example.reshmanjali.movieeee2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavDBHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "ForFavourites";
    static int DATABASE_VERSION = 8;
    Context context;

    public FavDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_query = "CREATE TABLE " + FavContract.TABLE_NAME + "( " + FavContract._ID + " INTEGER AUTO INCREMENT,"
                + FavContract.MOVIE_ID_COLUMN + " TEXT PRIMARY KEY,"
                + FavContract.MOVIE_TITLE_COLUMN + " TEXT NOT NULL,"
                + FavContract.RELEASE_DATE_COLUMN + " TEXT NOT NULL,"
                + FavContract.POSTER_PATH_COLUMN + " TEXT NOT NULL,"
                + FavContract.ORIGINAL_TITLE_COLUMN + " TEXT NOT NULL,"
                + FavContract.BACKDROP_PATH_COLUMN + " TEXT NOT NULL,"
                + FavContract.PLOT_SYNOPSIS + " TEXT NOT NULL,"
                + FavContract.RATING + " DOUBLE(4,2) NOT NULL);";
        db.execSQL(create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists " + FavContract.TABLE_NAME);
        onCreate(db);
    }
}