package com.example.weather.screens.main_activity;

import java.util.HashMap;

public interface MainActivityView {
    void setValueWeather(HashMap<String,String> valueWeather);
    void setLabel(String nameCity);
    void hideCities();
    void openCities();
    void setSun();
    void setSnow();
    void setStormy();
    void setFoggy();
    void setRain();
    void setClouds();
    void showMessage(String str);
}
