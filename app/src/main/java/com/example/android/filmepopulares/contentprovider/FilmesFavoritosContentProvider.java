package com.example.android.filmepopulares.contentprovider;

import android.annotation.TargetApi;
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

/**
 * Created by rako on 12/05/2017.
 */

public class FilmesFavoritosContentProvider extends ContentProvider{
    private static final int FILMES = 100;
    private static final int FILMES_WITH_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(FilmesContract.AUTHORITY, FilmesContract.PATH_FILMES, FILMES);
        sUriMatcher.addURI(FilmesContract.AUTHORITY, FilmesContract.PATH_FILMES + "/#", FILMES_WITH_ID);
        return sUriMatcher;
    }

    private FilmesDbHelper filmesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        filmesDbHelper = new FilmesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = filmesDbHelper.getReadableDatabase();

        int match = uriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case FILMES:
                retCursor = db.query(FilmesContract.FilmeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case FILMES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String[] selectionArguments = new String[]{id};
                retCursor = db.query(FilmesContract.FilmeEntry.TABLE_NAME,
                        projection,
                        FilmesContract.FilmeEntry.ID_FILME + " = ?",
                        selectionArguments,
                        null,
                        null,
                        null);
                break;
            default:
                throw  new android.database.SQLException("Unkown uri: "+ uri);
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
        final SQLiteDatabase db = filmesDbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case FILMES:
                long id = db.insert(FilmesContract.FilmeEntry.TABLE_NAME, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(FilmesContract.FilmeEntry.CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into "+ uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = filmesDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int filmesDeleted;
        switch (match){
            case FILMES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                filmesDeleted = db.delete(FilmesContract.FilmeEntry.TABLE_NAME, "idfilme=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        if(filmesDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return filmesDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }




}
