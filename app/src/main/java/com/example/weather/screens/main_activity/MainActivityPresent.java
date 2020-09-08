package com.example.weather.screens.main_activity;

import android.widget.Button;
import android.widget.EditText;

import com.example.weather.R;
import com.example.weather.database.AppDataBase;
import com.example.weather.database.entity.City;
import com.example.weather.model.ContentWeather;
import com.example.weather.model.WeatherApi;

import java.util.HashMap;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresent {
    private MainActivityView mainActivityView;
    private WeatherApi weatherApi;
    private CompositeDisposable disposable;
    private EditText input;
    private AppDataBase dataBase;
    private AdapterCity adapterCity;
    public static final String NAME_CITY = "NAME_CITY";
    public static final String WEATHER = "WEATHER";
    public static final String TEMP = "TEMP";
    public static final String PRESSURE = "PRESSURE";
    public static final String WIND = "WIND";
    public static final String VISIBILITY = "VISIBILITY";

    MainActivityPresent(MainActivityView mainActivityView, WeatherApi weatherApi, CompositeDisposable disposable, EditText input, AppDataBase dataBase, AdapterCity adapterCity) {
        this.input = input;
        this.disposable = disposable;
        this.weatherApi = weatherApi;
        this.mainActivityView = mainActivityView;
        this.dataBase = dataBase;
        this.adapterCity = adapterCity;
    }

    void outputWeather() {
        disposable.add(
                weatherApi.getWeatherByCity(convertNameCity(String.valueOf(input.getText())))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BiConsumer<ContentWeather, Throwable>() {
                            @Override
                            public void accept(ContentWeather weather, Throwable throwable) throws Exception {
                                if (weather != null) {
                                    mainActivityView.setValueWeather(convertWeather(weather));
                                    checkRepeatCity(convertNameCity(String.valueOf(input.getText())));
                                } else {
                                    mainActivityView.showMessage("Город не найден");
                                }
                            }
                        })
        );
    }

    void outputWeather(final String nameCity) {
        disposable.add(
                weatherApi.getWeatherByCity(nameCity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BiConsumer<ContentWeather, Throwable>() {
                            @Override
                            public void accept(ContentWeather weather, Throwable throwable) throws Exception {
                                if (weather != null) {
                                    mainActivityView.setValueWeather(convertWeather(weather));
                                    mainActivityView.setLabel(convertNameCity(nameCity));
                                } else {
                                    mainActivityView.showMessage("Ошибка");
                                }
                            }
                        })
        );
    }


    void outputCities() {
        disposable.add(
                dataBase.cityDao().getAllCities()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<City>>() {
                            @Override
                            public void accept(List<City> cities) throws Exception {
                                adapterCity.setCity(cities);
                            }
                        }));
    }

    void setImportanceCity(String nameCity) {
        dataBase.cityDao().getCityByName(nameCity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<City>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(City city) {
                        downgradeCity();
                        city.importance = 1;
                        updateCities(city);
                        outputWeather(convertNameCity(city.nameCity));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    void getImportantCity() {
        dataBase.cityDao().getCityByImportance(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<City>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(City city) {
                        outputWeather(convertNameCity(city.nameCity));
                        mainActivityView.setLabel(convertNameCity(city.nameCity));
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    void setBackgroundButtonMainCity(int importance, Button button) {
        if (importance == 1) {
            button.setBackgroundResource(R.drawable.main__city);
        } else {
            button.setBackgroundResource(R.drawable.add_main__city);
        }
    }

    private void saveCity(String nameCity) {
        City city = new City();
        city.nameCity = nameCity;
        city.importance = 0;
        dataBase.cityDao().insert(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        outputCities();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void updateCities(City city) {
        dataBase.cityDao().update(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private HashMap<String, String> convertWeather(ContentWeather contentWeather) {
        HashMap<String, String> weatherContent = new HashMap<>();
        switch (contentWeather.getWeather().get(0).getMain().toLowerCase()) {
            case "clouds":
                mainActivityView.setClouds();

                weatherContent.put(WEATHER, "Облачно");
                weatherContent.put(NAME_CITY, convertNameCity(String.valueOf(input.getText())));
                weatherContent.put(TEMP, String.valueOf(Math.round(contentWeather.getMain().getTemp())));
                weatherContent.put(PRESSURE, String.valueOf(contentWeather.getMain().getPressure()));
                weatherContent.put(WIND, String.valueOf(contentWeather.getWind().getSpeed()));
                weatherContent.put(VISIBILITY, String.valueOf(contentWeather.getVisibility()));

                mainActivityView.setLabel(convertNameCity(String.valueOf(input.getText())));
                break;
            case "clear":

                mainActivityView.setSun();

                weatherContent.put(WEATHER, "Ясно");
                weatherContent.put(NAME_CITY, convertNameCity(String.valueOf(input.getText())));
                weatherContent.put(TEMP, String.valueOf(Math.round(contentWeather.getMain().getTemp())));
                weatherContent.put(PRESSURE, String.valueOf(contentWeather.getMain().getPressure()));
                weatherContent.put(WIND, String.valueOf(contentWeather.getWind().getSpeed()));
                weatherContent.put(VISIBILITY, String.valueOf(contentWeather.getVisibility()));

                mainActivityView.setLabel(convertNameCity(String.valueOf(input.getText())));
                break;
            case "rain":
                mainActivityView.setRain();

                weatherContent.put(WEATHER, "Дождь");
                weatherContent.put(NAME_CITY, convertNameCity(String.valueOf(input.getText())));
                weatherContent.put(TEMP, String.valueOf(Math.round(contentWeather.getMain().getTemp())));
                weatherContent.put(PRESSURE, String.valueOf(contentWeather.getMain().getPressure()));
                weatherContent.put(WIND, String.valueOf(contentWeather.getWind().getSpeed()));
                weatherContent.put(VISIBILITY, String.valueOf(contentWeather.getVisibility()));

                mainActivityView.setLabel(convertNameCity(String.valueOf(input.getText())));

                break;
            case "storm":
                mainActivityView.setStormy();

                weatherContent.put(WEATHER, "Гроза");
                weatherContent.put(NAME_CITY, convertNameCity(String.valueOf(input.getText())));
                weatherContent.put(TEMP, String.valueOf(Math.round(contentWeather.getMain().getTemp())));
                weatherContent.put(PRESSURE, String.valueOf(contentWeather.getMain().getPressure()));
                weatherContent.put(WIND, String.valueOf(contentWeather.getWind().getSpeed()));
                weatherContent.put(VISIBILITY, String.valueOf(contentWeather.getVisibility()));

                mainActivityView.setLabel(convertNameCity(String.valueOf(input.getText())));
                break;
            default:
                weatherContent.put(WEATHER, String.valueOf(contentWeather.getWeather()));
                weatherContent.put(NAME_CITY, convertNameCity(String.valueOf(input.getText())));
                weatherContent.put(TEMP, String.valueOf(Math.round(contentWeather.getMain().getTemp())));
                weatherContent.put(PRESSURE, String.valueOf(contentWeather.getMain().getPressure()));
                weatherContent.put(WIND, String.valueOf(contentWeather.getWind().getSpeed()));
                weatherContent.put(VISIBILITY, String.valueOf(contentWeather.getVisibility()));

                mainActivityView.setLabel(convertNameCity(String.valueOf(input.getText())));
                break;
        }
        return weatherContent;
    }

    private void downgradeCity() {
        dataBase.cityDao().getCityByImportance(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<City>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(City city) {
                        city.importance = 0;
                        updateCities(city);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    private void checkRepeatCity(final String nameCity) {
        disposable.add(
                dataBase.cityDao().getAllCities()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<City>>() {
                            @Override
                            public void accept(List<City> cities) throws Exception {
                                if (cities.size() == 0) {
                                    saveCity(convertNameCity(nameCity));
                                }
                                for (int i = 0; i < cities.size(); i++) {
                                    if (cities.get(i).nameCity.equals(nameCity)) {
                                        return;
                                    }
                                }
                                if (cities.size() != 0) {
                                    saveCity(convertNameCity(nameCity));
                                    mainActivityView.showMessage(String.valueOf(cities.size()));
                                    outputCities();
                                }
                            }
                        }));
    }


    private String convertNameCity(String str) {
        StringBuilder nameCity = new StringBuilder();
        if (str.length() != 0) {
            str = str.trim().replaceAll("[\\s]{2,}", " ");
            nameCity = new StringBuilder(str.substring(0, 1).toUpperCase());
            for (int i = 1; i < str.length(); i++) {
                if (" ".equals(nameCity.substring(i - 1, i))) {
                    nameCity.append(str.substring(i, i + 1).toUpperCase());
                } else {
                    nameCity.append(str.substring(i, i + 1));
                }
            }

        }
        return nameCity.toString();
    }

    void cancelView() {
        disposable.clear();
    }

    void openCities() {
        mainActivityView.openCities();
    }

    void hideCities() {
        mainActivityView.hideCities();
    }


}
