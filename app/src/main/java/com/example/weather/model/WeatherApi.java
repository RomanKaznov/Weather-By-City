package com.example.weather.model;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("data/2.5/weather/")
    Single<ContentWeather> getWeatherByCity(@Query("q") String name);

}
