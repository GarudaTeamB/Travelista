package com.phedev.app.travelista.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.phedev.app.travelista.R;
import com.phedev.app.travelista.db.Data;
import com.phedev.app.travelista.db.PlaceAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Data>> {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private PlaceAdapter adapter;
    private Realm realm;

    // jadikan field ini local modofier agar realm notification aktif
    RealmResults<Data> dataResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        realm = Realm.getDefaultInstance();
        dataResults = realm.where(Data.class).findAllAsync();
        dataResults.addChangeListener(this);
        adapter = new PlaceAdapter(dataResults);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @OnClick(R.id.fab)
    void fabClicked(){
        Toast.makeText(getApplicationContext(), "this add place menu is under develop",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChange(@NonNull RealmResults<Data> data) {
        adapter.notifyDataSetChanged();
    }
}
