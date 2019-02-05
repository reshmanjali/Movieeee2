package com.example.reshmanjali.movieeee2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DataContentProvider extends ContentProvider {
    public static final int myFavourite = 100;
    public static final int myFavourite_with_ID = 101;
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    FavDBHelper mFavDBHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavContract.AUTHORITY, FavContract.PATH_myFavourites, myFavourite);
        uriMatcher.addURI(FavContract.AUTHORITY, FavContract.PATH_myFavourites + "/*", myFavourite_with_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavDBHelper = new FavDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor = null;
        switch (match) {
            case myFavourite:
                retCursor = db.query(FavContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case myFavourite_with_ID:
                retCursor = db.query(FavContract.TABLE_NAME, projection, FavContract.MOVIE_ID_COLUMN + " = '" + selection + "'", null, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Sorry, Unknown uri : " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returedUri = null;
        switch (match) {
            case myFavourite:
                long id = db.insert(FavContract.TABLE_NAME, null, values);
                if (id > 0)
                    returedUri = ContentUris.withAppendedId(FavContract.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deletedMoviesCount = 0;
        switch (match) {
            case myFavourite_with_ID:
                deletedMoviesCount = db.delete(FavContract.TABLE_NAME, selection, null);
                break;
            default:
                throw new UnsupportedOperationException("Sorry Unknown uri: " + uri);
        }
        if (deletedMoviesCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedMoviesCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}