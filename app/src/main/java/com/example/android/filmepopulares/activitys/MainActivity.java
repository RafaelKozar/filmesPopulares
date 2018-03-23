package com.example.android.filmepopulares.activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.filmepopulares.adapters.CardPagerAdapter;
import com.example.android.filmepopulares.contentprovider.FilmesContract;
import com.example.android.filmepopulares.dao.MovieDAO;
import com.example.android.filmepopulares.interfaces.AsyncTaskDelegate;
import com.example.android.filmepopulares.model.CardItem;
import com.example.android.filmepopulares.recursos.FeedProcessor;
import com.example.android.filmepopulares.recursos.ShadowTransformer;
import com.example.android.filmepopulares.services.MovieService;
import com.example.android.filmepopulares.connection.NetworkConnection;
import com.example.android.filmepopulares.R;
import com.example.android.filmepopulares.adapters.AdapterFilmes;
import com.example.android.filmepopulares.model.Movie;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.filmepopulares.recursos.FeedProcessor.convertDpToPixel;

/**
 * Desconsiderar a primeira vez que enviei este projeto, pois mandei o errado :(
 * **/

public class MainActivity extends AppCompatActivity implements AdapterFilmes.clickFilme,
        AsyncTaskDelegate{
    private RecyclerView recyclerView;
    private AdapterFilmes adapter;

    private List<Movie> resultsFilmes;
    private CardPagerAdapter mCardFilmeAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private ViewPager mViewPager;

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


        mViewPager = (ViewPager) findViewById(R.id.lista_filme_view_pager);
        loadingFilmes = (ProgressBar) findViewById(R.id.loading_filmes);
        loadingFilmes.setVisibility(View.VISIBLE);

//        LinearLayoutManager layoutManger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView = (RecyclerView) findViewById(R.id.listafilmes);
//        multiSnapRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.listafilmes);
//        GridLayoutManager layoutManger =  new GridLayoutManager(this, 2);
//        layoutManger.setOrientation(VERTICAL);
        //layoutManger.setSpanCount(2);
//          multiSnapRecyclerView.setLayoutManager(layoutManger);
//        recyclerView.setLayoutManager(layoutManger);
//        recyclerView.setHasFixedSize(true);

//        adapter = new AdapterFilmes(this);
//        recyclerView.setAdapter(adapter);
//        multiSnapRecyclerView.setAdapter(adapter);
        mCardFilmeAdapter = new CardPagerAdapter();


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
      //  recyclerView.setVisibility(View.INVISIBLE);
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
//            adapter.setFilmes(resultsFilmes);
           // recyclerView.setVisibility(View.VISIBLE);
            for (int tam = filmes.size(), i =0; i < tam; i++) {
                mCardFilmeAdapter.addCardItem(new CardItem(
                        resultsFilmes.get(i).getTitle(),
                        isFavoritoFilme(resultsFilmes.get(i).getIdFilme()),
                        resultsFilmes.get(i).getBitmap()));
            }

            mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardFilmeAdapter);
            mViewPager.setAdapter(mCardFilmeAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
            mViewPager.setOffscreenPageLimit(3);
            setPaddingViewPager();
            loadingFilmes.setVisibility(View.INVISIBLE);
            mCardShadowTransformer.enableScaling(true);
        }else{
            try {
                carregarFilmes();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("RetornoDoInBackground", e.getMessage(), e);
            }
        }
    }

    public boolean isFavoritoFilme(int idFilme) {
        Uri uri = FilmesContract.FilmeEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(idFilme)).build();
        if(movieDAO.isFavorito(uri)){
            return true;
        }else{
            return false;
        }
    }



    public void setPaddingViewPager() {
        float density = getResources().getDisplayMetrics().densityDpi;
        float width = getResources().getDisplayMetrics().widthPixels;

        float widthPerDensity = width / density;

        if (widthPerDensity < 3) {
            mViewPager.setPadding((int)FeedProcessor.convertDpToPixel(100, getApplicationContext()),
                    0, (int) convertDpToPixel(100, getApplicationContext()), 0);
        } else if (widthPerDensity >= 3 && widthPerDensity < 5) {
            mViewPager.setPadding((int)FeedProcessor.convertDpToPixel(100, getApplicationContext()),
                    0, (int) convertDpToPixel(100, getApplicationContext()), 0);
        } else if (widthPerDensity > 5) {
            mViewPager.setPadding((int)FeedProcessor.convertDpToPixel(100, getApplicationContext()),
                    0, (int) convertDpToPixel(100, getApplicationContext()), 0);
        }

    }

}
