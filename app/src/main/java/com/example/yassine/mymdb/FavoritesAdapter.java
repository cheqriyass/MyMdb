package com.example.yassine.mymdb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.yassine.mymdb.models.ItemClickListener;
import com.example.yassine.mymdb.models.ItemViewHolder;
import com.example.yassine.mymdb.models.Movie;
import com.example.yassine.mymdb.models.Serie;

import java.util.List;



public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Movie> moviesList;
    private Context context;

    public FavoritesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.moviesList = movies;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item_1, parent, false);
        viewHolder = new ItemViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder vh = (ItemViewHolder) holder;
        final Movie movie = moviesList.get(position);
        vh.movie_title.setText(movie.getTitle());
        vh.movie_desc.setText((movie.getOverview().length() > 80 ?
                movie.getOverview().substring(0, 80) + "..." : movie.getOverview()));

        String poster = movie.getPosterPath(context);

        Glide.with(context)
                .load(poster)
                .into(vh.thumbnail);


        vh.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Intent detailsIntent;

                if (movie.isMovie() == 0) {
                    detailsIntent = new Intent(context, SerieDetailsActivity.class);
                    Serie serie = new Serie(movie.getId(), movie.getTitle(), movie.posterPath, movie.getOverview(),
                            movie.backdropPath, movie.getVoteAverage());
                    detailsIntent.putExtra("serie", serie);

                } else {
                    detailsIntent = new Intent(context, MovieDetailsActivity.class);
                    detailsIntent.putExtra("movie", movie);
                }

                context.startActivity(detailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}
