package com.example.weather.di.module;

import android.content.Context;

import androidx.room.Room;


import com.example.weather.database.AppDataBase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataBaseModule {

    private AppDataBase dataBase;

    public DataBaseModule(Context context) {
        dataBase = Room.databaseBuilder(context
                , AppDataBase.class, "database")
                .build();
    }

    @Singleton
    @Provides
    public AppDataBase getDataBase() {
        return dataBase;
    }

}

