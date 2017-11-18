package com.phedev.app.travelista.db;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.phedev.app.travelista.R;
import com.phedev.app.travelista.activity.DetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by phedev in 2017.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private final RealmResults<Data> dataResults;

    public PlaceAdapter(RealmResults<Data> dataResults){
        this.dataResults = dataResults;
    }

    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new PlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlaceAdapter.ViewHolder holder, int position) {
        Data data = dataResults.get(position);
        //bind data to views
        holder.place.setText(data.name);
        holder.location.setText(data.province + " - " + data.location);
        holder.person.setText(data.contentText);
        //get data from assets by path and bind data to background layout
        Glide.with(holder.itemView.getContext()).load(data.image).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                holder.image.setBackground(resource);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataResults.isLoaded() ? dataResults.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.place) TextView place;
        @BindView(R.id.location) TextView location;
        @BindView(R.id.konten_by) TextView person;
        @BindView(R.id.layout) RelativeLayout image;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent launchDetail = new Intent(context, DetailActivity.class);
            Data data = dataResults.get(getAdapterPosition());
            launchDetail.putExtra(DetailActivity.EXTRA_DATA, data.keyId);
            context.startActivity(launchDetail);
        }
    }
}
