package com.phedev.app.travelista.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.phedev.app.travelista.R;
import com.phedev.app.travelista.db.Data;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class DetailActivity extends AppCompatActivity implements RealmChangeListener<Data> {

    public static final String EXTRA_DATA = "extra_data";

    private Realm realm;
    private Data data;

    @BindView(R.id.name_detail) TextView nameTxt;
    @BindView(R.id.deskripsi) TextView descTxt;
    @BindView(R.id.location_detail) TextView locationTxt;
    @BindView(R.id.konten_gambar_by) TextView imageBytxt;
    @BindView(R.id.konten_detail_by) TextView contentByTxt;
    @BindView(R.id.image_detail) RelativeLayout image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        int keyId = savedInstanceState == null ? getIntent().getIntExtra(EXTRA_DATA, 0)
                : savedInstanceState.getInt(EXTRA_DATA);
        realm = Realm.getDefaultInstance();
        data = realm.where(Data.class).equalTo("keyId", keyId).findFirstAsync();
        data.addChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_DATA, data.keyId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (data.isValid())
            data.removeChangeListener(this);

        realm.close();
        super.onDestroy();
    }

    @Override
    public void onChange(@NonNull Data data) {
        if (!data.isValid()) {
            return;
        }
        //get data from parcelable
        String name = data.name;
        String desc = data.description;
        String location = data.province + " - " + data.location;
        String contentBy = data.contentText;
        String imageBy = getString(R.string.image_text, data.contentImage);

        //bind data to views
        nameTxt.setText(name);
        descTxt.setText(desc);
        locationTxt.setText(location);
        contentByTxt.setText(contentBy);
        imageBytxt.setText(imageBy);
        //get data from assets by path and bind data to view
        Glide.with(this).load(data.image).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                image.setBackground(resource);
            }
        });
    }
}
