package com.example.android.filmepopulares.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.filmepopulares.contentprovider.FilmesContract.FilmeEntry;

import java.io.File;

/**
 * Created by rako on 12/05/2017.
 */

public class FilmesDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "filmesPopulares.db";

    private static final int VERSION = 1;


    public FilmesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //final String CREATE_TABLE = "CREATE TABLE " + FilmeEntry.TABLE_NAME + " ("+
        //        FilmeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        //        FilmeEntry.ID_FILME + " INTEGER NOT NULL);";
        final String CREATE_TABLE_FAVORITOS = "CREATE TABLE "+ FilmeEntry.TABLE_NAME + " ("+
                FilmeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FilmeEntry.ID_FILME + " TEXT NOT NULL, " +
                FilmeEntry.TITLE + " TEXT NOT NULL, "+
                FilmeEntry.RELEASE_DATE + " TEXT NOT NULL, "+
                FilmeEntry.YEAR + " TEXT NOT NULL, "+
                FilmeEntry.OVERVIEW + " TEXT NOT NULL, "+
                FilmeEntry.AVERAGE + " TEXT NOT NULL, "+
                FilmeEntry.URL_IMAGE + " TEXT NOT    NULL);";

        db.execSQL(CREATE_TABLE_FAVORITOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FilmeEntry.TABLE_NAME);
    }

}
