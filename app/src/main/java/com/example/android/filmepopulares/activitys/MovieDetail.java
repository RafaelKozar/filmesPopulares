package com.example.android.filmepopulares.activitys;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.filmepopulares.services.ReviewsService;
import com.example.android.filmepopulares.services.TrailerService;
import com.example.android.filmepopulares.adapters.AdapterReviews;
import com.example.android.filmepopulares.adapters.AdapterTrailer;
import com.example.android.filmepopulares.connection.NetworkConnection;
import com.example.android.filmepopulares.contentprovider.FilmesContract;
import com.example.android.filmepopulares.dao.MovieDAO;
import com.example.android.filmepopulares.interfaces.AsyncTaskDelegateReview;
import com.example.android.filmepopulares.interfaces.AsyncTaskDelegateTrailer;
import com.example.android.filmepopulares.model.Review;
import com.example.android.filmepopulares.R;
import com.example.android.filmepopulares.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class MovieDetail extends AppCompatActivity implements AdapterTrailer.clickTrailer, AsyncTaskDelegateTrailer,
        AsyncTaskDelegateReview{
    private final String enderecoFilme = "https://api.themoviedb.org/";
    private RelativeLayout relativeLayout;
    private Movie filme;
    private TextView title;
    private TextView year;
    private TextView release_date;
    private TextView overview;
    private TextView media;
    private TextView reviews;
    private ImageView imageView;
    private ScrollView conteudo;
    private FloatingActionButton bt;
    private ActionBar bar;
    private Toast mToast;

    private TextView textTrailer;
    private View linhaTrailer;
    private View linhaReview;

    //private AdapterTrailers adapterTrailers;
    private AdapterTrailer adapterTrailer;
    private RecyclerView listaTrailers;
    private List<String> resultTrailers;

    private AdapterReviews adapterReviews;
    private RecyclerView listaReviews;
    private List<Review> resultReviews;

    private MovieDAO movieDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        bar = getSupportActionBar();
        bar.setTitle("MovieDeatail");
        bar.setHomeButtonEnabled(true);

        title = (TextView) findViewById(R.id.title_movie);
        year = (TextView) findViewById(R.id.ano);
        release_date = (TextView) findViewById(R.id.data);
        overview = (TextView) findViewById(R.id.descricao);
        imageView = (ImageView) findViewById(R.id.image_movie);
        conteudo = (ScrollView) findViewById(R.id.scroll_content);

        /*** Set RecyclerView of trailers ***/
        listaTrailers = (RecyclerView) findViewById(R.id.lista_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listaTrailers.setLayoutManager(layoutManager);
        adapterTrailer = new AdapterTrailer(this);
        listaTrailers.setAdapter(adapterTrailer);

        //** Set RecyclerView of reviews **//
        listaReviews = (RecyclerView) findViewById(R.id.lista_reviews);
        LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this);
        listaReviews.setLayoutManager(layoutManagerReview);
        adapterReviews = new AdapterReviews();
        listaReviews.setAdapter(adapterReviews);



        media = (TextView) findViewById(R.id.vote_average);
        textTrailer = (TextView) findViewById(R.id.text_trailer);
        linhaTrailer = (View) findViewById(R.id.linha_trailers);

        reviews = (TextView) findViewById(R.id.text_review);
        linhaReview = (View) findViewById(R.id.linha_review_moviedetailail);


        bt = (FloatingActionButton) findViewById(R.id.bt_make_favorite);
        bt.setOnClickListener(makeFavorite);
        //showLoading();

        movieDAO = new MovieDAO(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.filme = bundle.getParcelable(Movie.PARCELABLE_KEY);
            atulizaButton();
            try {
                carregarFilme();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MovieDetail", e.toString());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void atulizaButton(){
        Uri uri = FilmesContract.FilmeEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(filme.getIdFilme())).build();
        if(movieDAO.isFavorito(uri)){
            bt.setImageResource(R.drawable.img_desfavoritar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bt.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            }

        }else{
            bt.setImageResource(R.drawable.img_favoritar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bt.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }

    }

    private View.OnClickListener makeFavorite = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Uri uri = FilmesContract.FilmeEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(Integer.toString(filme.getIdFilme())).build();
            if(mToast != null){
                mToast.cancel();
            }
            if(movieDAO.isFavorito(uri)){
                getContentResolver().delete(uri, null, null);
                mToast = Toast.makeText(getApplicationContext(), R.string.mensagem_desmarcar_favorito, Toast.LENGTH_SHORT);
            }else{
                Uri uriReturn = movieDAO.inserir(filme);
                if(uriReturn != null){
                    mToast = Toast.makeText(getApplicationContext(), R.string.mensagem_marcado_favorito, Toast.LENGTH_SHORT);
                }else{
                    mToast = Toast.makeText(getApplicationContext(), "Não foi possível marcar o fime como favorito", Toast.LENGTH_SHORT);
                }            }
            mToast.show();
            atulizaButton();
        }
    };


  /*  public void showLoading(){
        loading.setVisibility(View.VISIBLE);
        conteudo.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
    }

    public void hideLoading(){
        loading.setVisibility(View.INVISIBLE);
        conteudo.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
    }*/

    public void carregarFilme() throws MalformedURLException, JSONException {
        if(NetworkConnection.isNetworkConnected(getApplicationContext())) {
            makeRequisition();
        }else{
            relativeLayout = (RelativeLayout) findViewById(R.id.detalhes);
            Snackbar snackbar = Snackbar.make(relativeLayout, getResources().
                    getString(R.string.mensagem_erro_conexao), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.texto_repetir),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        carregarFilme();
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
            snackbar.show();
           // loading.setVisibility(View.INVISIBLE);
        }
    }

    public void makeRequisition() throws JSONException, MalformedURLException {
        title.setText(filme.getTitle());
        year.setText(filme.getYear());
        release_date.setText(filme.getReleaseDate());

        overview.setText(filme.getOverview());
        media.setText(getResources().getString(R.string.texto_nota,  filme.getAverage()));
        String imageUrl = "http://image.tmdb.org/t/p/w780";
        imageUrl += filme.getUrlImage();
      //  hideLoading();

        //Picasso.with(getApplicationContext()).load(imageUrl).into(imageView);
        Picasso.with(getApplicationContext()).load(imageUrl)
                .placeholder(R.drawable.ic_autorenew_black_24dp)
                .into(imageView);

        //Busca dos Trailers//
        String string = enderecoFilme+"3/movie/"+String.valueOf(filme.getIdFilme())+"/videos?api_key="+getResources().
                getString(R.string.KEY_API);


        Bundle bundle = new Bundle();
        bundle.putString("urlString", string);
        new TrailerService(this, getSupportLoaderManager(), bundle);

        ///chamar os reviews
        String stringReview = enderecoFilme+"3/movie/"+String.valueOf(filme.getIdFilme())+"/reviews?api_key="+getApplication()
                .getString(R.string.KEY_API);
        Bundle bundleReview = new Bundle();
        bundleReview.putString("urlString", stringReview);
        new ReviewsService(this, getSupportLoaderManager(), bundleReview);


    }

    @Override
    public void onClickTrailer(int position) {
        String trailerUrl = "https://www.youtube.com/watch?v=" + resultTrailers.get(position);
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
        if(it.resolveActivity(getPackageManager()) != null){
            startActivity(it);
        }
    }

    @Override
    public void processFinish(List<String> filmes) {
        if(filmes != null){
            resultTrailers = filmes;
            adapterTrailer.setUrlTrailers(resultTrailers);
        }else{
            setHideVisibilityTrailers();
        }
    }

    public void setHideVisibilityTrailers(){
        textTrailer.setVisibility(View.INVISIBLE);
        linhaTrailer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void processFinishedReview(List<Review> reviews) {
        if(reviews != null){
            resultReviews = reviews;
            adapterReviews.setReviews(resultReviews);
        }else{
            setHideVisibilityReviews();
        }
    }

    public void setHideVisibilityReviews(){
        reviews.setVisibility(View.GONE);
        linhaReview.setVisibility(View.GONE);
    }
}

