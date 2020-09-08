package com.example.weather.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.weather.database.dao.CityDao;
import com.example.weather.database.entity.City;


@Database(entities = {City.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract CityDao cityDao();

}