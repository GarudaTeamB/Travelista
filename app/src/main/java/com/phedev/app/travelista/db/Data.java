package com.phedev.app.travelista.db;

import android.database.Cursor;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Karena sudah menggunakan Realm Database Object model, tidak perlu lagi menerapkan Parcelable.
 */
public class Data extends RealmObject {

    //key id
    @PrimaryKey
    public int keyId;
    //place name
    public String name;
    //description
    public String description;
    //image path
    public String image;
    //province
    public String province;
    //location detail
    public String location;
    //author content text
    public String contentText;
    //image content
    public String contentImage;

    /**
     * Constructor default untuk Realm database. Penggunaan private dan final field tidak diizinkan oleh Realm.
     */
    public Data(){}

    /**
     * create data from discrete values
     * @deprecated Kita mungkin tidak akan pernah memakai ini.
     * */
    public Data(String name, String description, String image, String province, String location, String contentText, String contentImage, int keyId) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.province = province;
        this.location = location;
        this.contentText = contentText;
        this.contentImage = contentImage;
        this.keyId = keyId;
    }

    /**
     * create new data from cursor
     * @deprecated Menggunakan database <code>Realm</code> tidak memerlukan <code>Cursor</code>
     * */
    public Data(Cursor cursor){
        this.name = DatabaseManager.getColumnString(cursor, DbHelper.NAME);
        this.description = DatabaseManager.getColumnString(cursor, DbHelper.DESCRIPTION);
        this.image = DatabaseManager.getColumnString(cursor, DbHelper.IMAGE_PATH);
        this.province = DatabaseManager.getColumnString(cursor, DbHelper.PROVINCE);
        this.location = DatabaseManager.getColumnString(cursor, DbHelper.LOCATION);
        this.contentText = DatabaseManager.getColumnString(cursor, DbHelper.CONTENT_TEXT_BY);
        this.contentImage = DatabaseManager.getColumnString(cursor, DbHelper.CONTENT_IMAGE_BY);
    }
}
