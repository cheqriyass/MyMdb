package com.example.yassine.mymdb;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.yassine.mymdb.models.ItemClickListener;
import com.example.yassine.mymdb.models.ItemViewHolder;
import com.example.yassine.mymdb.models.Serie;

import java.util.ArrayList;
import java.util.List;


public class PaginationAdapterSeries extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<Serie> moviesList;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapterSeries(Context context) {
        this.context = context;
        this.moviesList = new ArrayList<>();
    }

    public List<Serie> getMovies() {
        return moviesList;
    }

    public void setMovies(List<Serie> movieMovies) {
        this.moviesList = movieMovies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        switch (SeriesActivity.layout){
            case 1 :
                v = inflater.inflate(R.layout.movie_list_item_2, parent, false);
                break;
            case 2 :
                v = inflater.inflate(R.layout.movie_list_item_3, parent, false);
                break;
            default:
                v = inflater.inflate(R.layout.movie_list_item_1, parent, false);
                break;
        }

        viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Serie movie = moviesList.get(position); // getMovieTrailer

        switch (getItemViewType(position)) {
            case ITEM:
                final ItemViewHolder movieVH = (ItemViewHolder) holder;

                movieVH.setListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int pos) {

                        Intent detailsIntent = new Intent(context, SerieDetailsActivity.class);
                        detailsIntent.putExtra("serie", movie);
                        context.startActivity(detailsIntent);
                    }
                });

                if (BaseDrawerActivity.layout != 2){
                    movieVH.movie_title.setText(movie.getTitle());
                    movieVH.movie_desc.setText((movie.getOverview().length()>80 ?
                            movie.getOverview().substring(0,80) + "..." : movie.getOverview()));
                }

                String poster;
                switch (SeriesActivity.layout){
                    case 1 :
                        poster = movie.getBackdropPath(context);
                        break;
                    case 2 :
                        poster = movie.getPosterPath(context);
                        break;
                    default:
                        poster = movie.getPosterPath(context);
                        break;
                }


                Glide.with(context)
                        .load(poster)
                        .into(movieVH.thumbnail);

                break;

            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return moviesList == null ? 0 : moviesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == moviesList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Serie r) {
        moviesList.add(r);
        notifyItemInserted(moviesList.size() - 1);
    }

    public void addAll(List<Serie> moveMovies) {
        for (Serie result : moveMovies) {
            add(result);
        }
    }

    public void remove(Serie r) {
        int position = moviesList.indexOf(r);
        if (position > -1) {
            moviesList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Serie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = moviesList.size() - 1;
        Serie result = getItem(position);

        if (result != null) {
            moviesList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Serie getItem(int position) {
        return moviesList.get(position);
    }



    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }




}