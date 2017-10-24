package com.example.android.filmepopulares.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.filmepopulares.contentprovider.FilmesContract;
import com.example.android.filmepopulares.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rako on 17/05/2017.
 */

public class MovieDAO {

    private Context context;
    public MovieDAO(Context c){
        context = c;
    }

    public boolean isFavorito (Uri uri){
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                FilmesContract.FilmeEntry.ID_FILME);
        if(!cursor.moveToFirst()){
            return false;
        }else{
            return true;
        }
    }

    public Uri inserir(Movie filme){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FilmesContract.FilmeEntry.ID_FILME, Integer.toString(filme.getIdFilme()));
        contentValues.put(FilmesContract.FilmeEntry.TITLE, filme.getTitle());
        contentValues.put(FilmesContract.FilmeEntry.RELEASE_DATE, filme.getReleaseDate());
        contentValues.put(FilmesContract.FilmeEntry.YEAR, filme.getYear());
        contentValues.put(FilmesContract.FilmeEntry.OVERVIEW, filme.getOverview());
        contentValues.put(FilmesContract.FilmeEntry.AVERAGE, filme.getAverage());
        contentValues.put(FilmesContract.FilmeEntry.URL_IMAGE, filme.getUrlImage());
        return context.getContentResolver().insert(FilmesContract.FilmeEntry.CONTENT_URI, contentValues);
    }

    public List<Movie> listar(){
        Cursor cursor = context.getContentResolver().query(
                FilmesContract.FilmeEntry.CONTENT_URI,
                null,
                null,
                null,
                FilmesContract.FilmeEntry.ID_FILME
        );
        if(cursor == null || !cursor.moveToFirst()){
            return null;
        }else{
            List<Movie> filmes = new ArrayList();
            int columnId = cursor.getColumnIndex("idfilme");
            int columnTitle = cursor.getColumnIndex("title");
            int columnDate = cursor.getColumnIndex("releasedate");
            int columnOverview = cursor.getColumnIndex("overview");
            int columnYear = cursor.getColumnIndex("year");
            int columnAverage = cursor.getColumnIndex("average");
            int columnUrlImage = cursor.getColumnIndex("urlimage");
            do {
                String idFilme = cursor.getString(columnId);
                String title = cursor.getString(columnTitle);
                String date = cursor.getString(columnDate);
                String overview = cursor.getString(columnOverview);
                String year = cursor.getString(columnYear);
                String average = cursor.getString(columnAverage);
                String urlImage = cursor.getString(columnUrlImage);
                Movie movie = new Movie(title, date, year, overview, average,
                        urlImage, Integer.parseInt(String.valueOf(idFilme)));
                filmes.add(movie);
            } while (cursor.moveToNext());
            return filmes;
        }
    }
}
