package com.example.android.filmepopulares.activitys;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.filmepopulares.dao.MovieDAO;
import com.example.android.filmepopulares.interfaces.AsyncTaskDelegate;
import com.example.android.filmepopulares.services.MovieService;
import com.example.android.filmepopulares.connection.NetworkConnection;
import com.example.android.filmepopulares.R;
import com.example.android.filmepopulares.adapters.AdapterFilmes;
import com.example.android.filmepopulares.model.Movie;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.*;

/**
 * Desconsiderar a primeira vez que enviei este projeto, pois mandei o errado :(
 * **/

public class MainActivity extends AppCompatActivity implements AdapterFilmes.clickFilme,
        AsyncTaskDelegate{
    private List<Movie> resultsFilmes;
    private RecyclerView recyclerView;
    private AdapterFilmes adapter;
    private CoordinatorLayout coordinatorLayout;
    private MovieDAO movieDAO;
    public ProgressBar loadingFilmes;
    public static final int LOADER_MOVIES_ID_TOP_RATED = 19;
    public static final int LOADER_MOVIES_ID_POPULAR = 20;
    public static final int LOADER_MOVIES_ID_FAVORITOS = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.listafilmes);
        loadingFilmes = (ProgressBar) findViewById(R.id.loading_filmes);
        loadingFilmes.setVisibility(View.VISIBLE);
        GridLayoutManager layoutManger =  new GridLayoutManager(this, 2);
        layoutManger.setOrientation(VERTICAL);
        layoutManger.setSpanCount(2);

        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterFilmes(this);
        recyclerView.setAdapter(adapter);

        movieDAO = new MovieDAO(this);
        try {
            carregarFilmes();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.top_rated:
                try {
                    makeRequisition(MovieService.TOP_RATED, LOADER_MOVIES_ID_TOP_RATED);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.popular:
                try {
                    makeRequisition(MovieService.POPULAR, LOADER_MOVIES_ID_POPULAR);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.favorites:
                try {
                    makeRequisition(MovieService.POPULAR, LOADER_MOVIES_ID_FAVORITOS);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    public void carregarFilmes() throws MalformedURLException {
        if(NetworkConnection.isNetworkConnected(getApplicationContext())) {

            makeRequisition(MovieService.POPULAR, LOADER_MOVIES_ID_POPULAR);
        }else{
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, getResources().
                    getString(R.string.mensagem_erro_conexao), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.texto_repetir),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        carregarFilmes();
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
            loadingFilmes.setVisibility(View.INVISIBLE);
            snackbar.show();
        }
    }

    public void makeRequisition(String string, int id) throws MalformedURLException {
        loadingFilmes.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        Bundle bundle = new Bundle();
        bundle.putString("url", string);
        new MovieService(this, getSupportLoaderManager(), bundle, id);
    }

    public LoaderManager voltar(){
        return getSupportLoaderManager();
    }


    @Override
    public void onClickFilme(int position) {
        Intent it = new Intent(getApplicationContext(), MovieDetail.class);
        it.putExtra(Movie.PARCELABLE_KEY, resultsFilmes.get(position));
        startActivity(it);
    }

    @Override
    public void processFinish(List<Movie> filmes){
        if(filmes != null) {
            resultsFilmes = new ArrayList<Movie>();
            resultsFilmes = filmes;
            adapter.setFilmes(resultsFilmes);
            loadingFilmes.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            try {
                carregarFilmes();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("RetornoDoInBackground", e.getMessage(), e);
            }
        }
    }

}
