package com.example.weather.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.weather.database.entity.City;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public interface CityDao {

    @Query("SELECT * FROM City")
    Flowable<List<City>> getAllCities();

    @Query("SELECT * FROM City WHERE nameCity = :nameCity ")
    Single<City> getCityByName(String nameCity);

    @Query("SELECT * FROM City WHERE importance = :importance ")
    Single<City> getCityByImportance(int importance);

    @Insert()
    Single<Void> insert(City city);

    @Update
    Single<Void> update(City city);

    @Delete
    void delete(City city);

}
