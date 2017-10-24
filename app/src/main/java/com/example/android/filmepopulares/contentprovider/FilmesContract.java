package com.example.android.filmepopulares.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rako on 12/05/2017.
 */

public class FilmesContract {
    public static final String AUTHORITY = "com.example.android.filmepopulares";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FILMES = "filmes";


    public static final class FilmeEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILMES).build();

        public static final String TABLE_NAME = "favoritos";
        public static final String ID_FILME = "idfilme";


        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "releasedate";
        public static final String OVERVIEW = "overview";
        public static final String YEAR = "year";
        public static final String AVERAGE = "average";
        public static final String URL_IMAGE = "urlimage";

    }

    /*
    *   - - - -
        | _id  |
         - - - - - -
        |  878675889 |
         - - - - - -
    * */
}
