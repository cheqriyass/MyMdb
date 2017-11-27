package com.example.yassine.mymdb.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yassine.mymdb.R;
import com.example.yassine.mymdb.SeriesActivity;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView movie_title;
    public TextView movie_desc;
    public ImageView thumbnail;
    private ItemClickListener listener;

    public ItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.thumbnail = itemView.findViewById(R.id.thumbnail);
        if (SeriesActivity.layout != 2){
            this.movie_title = itemView.findViewById(R.id.movie_title);
            this.movie_desc = itemView.findViewById(R.id.movie_desc);
        }
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition());
    }

}