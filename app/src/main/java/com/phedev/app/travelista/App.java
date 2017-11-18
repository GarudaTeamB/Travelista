package com.phedev.app.travelista;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.phedev.app.travelista.db.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by anggrayudi on 18/11/17.
 */
public class App extends Application {

    public static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        // inisialisasi database Realm
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(0) // --> versi database
                .migration(new DataMigration())
                .build());
        try {
            insertDefaultData(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void insertDefaultData(Context context) throws IOException, JSONException {
        final Realm realm = Realm.getDefaultInstance();
        if (realm.where(Data.class).count() > 0){
            realm.close();
            return;
        }

        StringBuilder builder = new StringBuilder();
        InputStream in = context.getResources().openRawResource(R.raw.datawisata);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();
        //Parse JSON data and insert into the provided database instance
        Log.d(TAG, rawJson);
        JSONObject jsonObject = new JSONObject(rawJson);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        final RealmList<Data> dataList = new RealmList<>();

        Log.d(TAG, String.valueOf(jsonArray.length()));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Data data = new Data();
            data.keyId = object.getInt("no");
            data.name = object.getString("nama");
            data.description = object.getString("deskripsi");
            data.province = object.getString("provinsi");
            data.image = object.getString("image");
            data.location = object.getString("tempat");
            data.contentText = object.getString("konten_teks");
            data.contentImage = object.getString("konten_gambar");
            dataList.add(data);
        }

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.insertOrUpdate(dataList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                Log.i(TAG, "onSuccess: Data sukses dimasukkan");
            }
        });
    }

    private static class DataMigration implements RealmMigration {
        @Override
        public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
            if (oldVersion == 0) {
//                oldVersion++;
            }
        }
    }
}
