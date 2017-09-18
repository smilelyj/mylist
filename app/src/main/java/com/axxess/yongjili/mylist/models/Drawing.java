package com.axxess.yongjili.mylist.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.stetho.Stetho;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yongjili on 9/16/17.
 */

/**
 * This model represents the Drawing element.
 */

public class Drawing extends RealmObject implements Parcelable{
    // Example schema
    //    id: "2639",
    //    type: "image",
    //    date: "9/4/2015",
    //    data: "http://lorempixel.com/620/320/technics/"

    @PrimaryKey
    private String id;

    private String type;
    private String date;
    private String data;

    public Drawing(){}

    public Drawing(String id, String type, String date, String data) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.data = data;
    }

    public Drawing(Parcel parcel) {
        id = parcel.readString();
        type = parcel.readString();
        date = parcel.readString();
        data = parcel.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        date = date;
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        data = data;
    }

    public static final Parcelable.Creator<Drawing> CREATOR = new Parcelable.Creator<Drawing>() {
        public Drawing createFromParcel(Parcel in) {
            return new Drawing(in);
        }
        public Drawing[] newArray(int size) {
            return new Drawing[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(type);
        parcel.writeString(date);
        parcel.writeString(data);
    }
}
