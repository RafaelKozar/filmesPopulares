package com.example.android.filmepopulares.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.android.filmepopulares.R;
import com.example.android.filmepopulares.contentprovider.FilmesContract;
import com.example.android.filmepopulares.dao.MovieDAO;
import com.example.android.filmepopulares.model.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rako on 13/03/2017.
 */
public class AdapterFilmes extends RecyclerView.Adapter<AdapterFilmes.FilmesAdapterViewHolder> {
    private List<Movie> filmes = new ArrayList<Movie>();
    private MovieDAO movieDAO;
    private Toast mToast;

    private final clickFilme listennerClick;

    public AdapterFilmes(clickFilme listennerClick) {
        this.listennerClick = listennerClick;
        movieDAO = new MovieDAO((Context) listennerClick);

    }

    public interface clickFilme {
        void onClickFilme(int position);
    }

    @Override
    public FilmesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_filme, parent, false);
        return new FilmesAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final FilmesAdapterViewHolder holder, final int position) {
        final Movie filme = filmes.get(position);

//        Glide.with(holder.context).load(filme.getUrlImage())
//                .thumbnail(Glide.with(holder.context).load(R.drawable.loading))
//                .into(holder.imageView);   Nao funciona
        holder.imageView.setImageBitmap(filme.getBitmap());
        holder.nomeFilme.setText(filme.getTitle());
        Uri uri = FilmesContract.FilmeEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(filme.getIdFilme())).build();
        if(movieDAO.isFavorito(uri)){
            holder.buttonFavorito.setImageResource(R.drawable.img_favorito_green);
        }else{
            holder.buttonFavorito.setImageResource(R.drawable.img_favorito);
        }
        holder.buttonFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FilmesContract.FilmeEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(Integer.toString(filme.getIdFilme())).build();
                if(mToast != null){
                    mToast.cancel();
                }
                if(movieDAO.isFavorito(uri)){
                    holder.context.getContentResolver().delete(uri, null, null);
                    holder.buttonFavorito.setImageResource(R.drawable.img_favorito);
                    mToast = Toast.makeText(holder.context, R.string.mensagem_desmarcar_favorito, Toast.LENGTH_SHORT);
                }else{
                    Uri uriReturn = movieDAO.inserir(filme);
                    if(uriReturn != null){
                        mToast = Toast.makeText(holder.context, R.string.mensagem_marcado_favorito, Toast.LENGTH_SHORT);
                        holder.buttonFavorito.setImageResource(R.drawable.img_favorito_green);
                    }else{
                        mToast = Toast.makeText(holder.context, "Não foi possível marcar o fime como favorito", Toast.LENGTH_SHORT);
                    }
                }
                mToast.show();
            }
        });

    }



    public void setFilmes(List<Movie> filmes) {
        this.filmes = filmes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (filmes != null) {
            return filmes.size();
        } else {
            return 0;
        }
    }

    class FilmesAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nomeFilme;
        ImageView buttonFavorito;
        Context context;


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listennerClick.onClickFilme(getAdapterPosition());
            }
        };

        public FilmesAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_movie_item);
            nomeFilme = (TextView) itemView.findViewById(R.id.nome_filme);
            buttonFavorito = (ImageView) itemView.findViewById(R.id.button_favoritar);
            imageView.setOnClickListener(listener);
            context = itemView.getContext();
        }
    }
}
