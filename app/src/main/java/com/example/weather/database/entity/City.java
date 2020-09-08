package com.example.weather.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class City {

    public City() {
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nameCity")
    public String nameCity;

    @ColumnInfo(name = "importance")
    public int importance;

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
