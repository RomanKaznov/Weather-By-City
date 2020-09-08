package com.example.weather.di.component;

import com.example.weather.di.module.DataBaseModule;
import com.example.weather.screens.main_activity.MainActivity;
import com.example.weather.di.module.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RetrofitModule.class, DataBaseModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
