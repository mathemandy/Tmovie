package com.ng.tselebro.tmovie.data;

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
import android.util.Log;

import static com.ng.tselebro.tmovie.data.MovieContract.MovieEntry.CONTENT_URI;
import static com.ng.tselebro.tmovie.data.MovieContract.MovieEntry.TABLE_NAME;

public class MoviesProvider extends ContentProvider {


    private static final int FAVORITES = 100;
    private static final int FAVORITES_WITH_ID = 101;
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private MoviesDBHelper mMovieDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TASKS, FAVORITES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TASKS + "/#", FAVORITES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MoviesDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase database = mMovieDbHelper.getReadableDatabase();
        int match = mUriMatcher.match(uri);
        Cursor mCursor;
        switch (match){
            case FAVORITES:
                mCursor = database.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        Uri mUri;

        switch (match){

            case FAVORITES:
                long id = database.insert(TABLE_NAME, null, contentValues);
                Log.v("MoviesProvider", id + "");

                if (id > 0){
                    mUri = ContentUris.withAppendedId(CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        //Notify the resolver of the changed uri, and return the newly inserted item URI
        getContext().getContentResolver().notifyChange(uri, null);
        return mUri;
}

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);
        //The variable to be used to track number of favorite video items deleted
        int rowDeleted;
        if(s == null)
            s = "1";

        switch (match){

            //Handle single Item delete case, recognized by the ID added in the URI path
            case FAVORITES:
                //Get the ID of the favorite movie to be deleted from the URI path
                //Use the selection/selectionArgs to filter for this ID
                rowDeleted = database.delete(TABLE_NAME, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //Notify the resolver of the change made and return the number
        // of favorite video items deleted
        if (rowDeleted != 0){

            // An item delete task was done, set notification of data change
            getContext().getContentResolver().notifyChange(uri, null);
        }

        //Returns the number of items deleted int the task
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();

        final int match = mUriMatcher.match(uri);
        int rows_changed;

        switch (match){
            case FAVORITES:
                rows_changed = database.update(TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Insertion not supported for " + uri);
        }

        if (rows_changed != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows_changed;

    }
}
