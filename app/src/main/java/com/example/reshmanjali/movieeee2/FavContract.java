package com.example.reshmanjali.movieeee2;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavContract implements BaseColumns {
    public static final String AUTHORITY = "com.example.reshmanjali.movieeee2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_myFavourites = "myFavourite";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_myFavourites).build();
    public static String TABLE_NAME = "myFavourite";
    public static String MOVIE_ID_COLUMN = "movieID";
    public static String MOVIE_TITLE_COLUMN = "movieTitle";
    public static String RELEASE_DATE_COLUMN = "releadeDate";
    public static String POSTER_PATH_COLUMN = "posterPath";
    public static String ORIGINAL_TITLE_COLUMN = "originalTitle";
    public static String BACKDROP_PATH_COLUMN = "backDropPath";
    public static String PLOT_SYNOPSIS = "synopsis";
    public static String RATING = "rating";
}
