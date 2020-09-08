package com.example.weather;

import android.app.Application;

import com.example.weather.di.component.AppComponent;

import com.example.weather.di.component.DaggerAppComponent;
import com.example.weather.di.module.DataBaseModule;
import com.example.weather.di.module.RetrofitModule;

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        appComponent = DaggerAppComponent.builder().retrofitModule(new RetrofitModule()).dataBaseModule(new DataBaseModule(this)).build();
        super.onCreate();
    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }
}
