package com.example.weather.screens.main_activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weather.App;
import com.example.weather.R;
import com.example.weather.database.AppDataBase;
import com.example.weather.model.WeatherApi;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity implements MainActivityView {
    TextView temp, weather, wind, pressure, visibility;
    TextView nameCity;
    ImageView background;
    DrawerLayout drawer;
    @Inject
    WeatherApi weatherApi;
    @Inject
    AppDataBase dataBase;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MainActivityPresent mainActivityPresent;
    private AdapterCity adapterCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        App.getAppComponent().inject(this);
        setTitle("");

        EditText input = findViewById(R.id.input);
        drawer = findViewById(R.id.drawer_layout);
        Button button = findViewById(R.id.click);
        Button addCity = findViewById(R.id.addCity);
        temp = findViewById(R.id.temp);
        weather = findViewById(R.id.weather);
        wind = findViewById(R.id.wind);
        pressure = findViewById(R.id.pressure);
        visibility = findViewById(R.id.visibiLity);
        background = findViewById(R.id.back);
        nameCity = findViewById(R.id.nameCity);
        RecyclerView recyclerView = findViewById(R.id.list);

        adapterCity = new AdapterCity(new MainActivityPresent(this, weatherApi, disposable,
                input, dataBase, adapterCity));

        mainActivityPresent = new MainActivityPresent(this, weatherApi, disposable,
                input, dataBase, adapterCity);
        mainActivityPresent.outputCities();
        mainActivityPresent.getImportantCity();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterCity);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityPresent.outputWeather();
                mainActivityPresent.hideCities();
            }
        });

        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityPresent.openCities();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setValueWeather(HashMap<String, String> valueWeather) {
        temp.setText(valueWeather.get(MainActivityPresent.TEMP) + " " + getString(R.string.temp));
        weather.setText(valueWeather.get(MainActivityPresent.WEATHER));
        wind.setText(getString(R.string.itemWeather1) + " " + valueWeather.get(MainActivityPresent.WIND) + " " + "М/С");
        visibility.setText(getString(R.string.visibility) + " " + valueWeather.get(MainActivityPresent.VISIBILITY));
        pressure.setText(getString(R.string.itemWeather2) + " " + valueWeather.get(MainActivityPresent.PRESSURE) + "M");

    }

    @Override
    public void setLabel(String nameCity) {
        this.nameCity.setText(nameCity);
    }

    @Override
    public void hideCities() {
        drawer.close();
    }

    @Override
    public void openCities() {
        drawer.open();
    }

    @Override
    public void setSun() {
        Glide.with(this).load("https://i.gifer.com/AbwQ.gif").into(background);
    }

    @Override
    public void setSnow() {
        Glide.with(this).load("https://i.gifer.com/1jxj.gif").into(background);
    }

    @Override
    public void setStormy() {
        Glide.with(this).load("https://i.gifer.com/Gler.gif").into(background);
    }

    @Override
    public void setFoggy() {
        Glide.with(this).load("https://i.gifer.com/1Eqt.gif").into(background);
    }

    @Override
    public void setRain() {
        Glide.with(this).load("https://i.gifer.com/Ae6H.gif").into(background);
    }

    @Override
    public void setClouds() {
        Glide.with(this).load("https://i.gifer.com/hBx.gif").into(background);
    }

    @Override
    public void showMessage(String str) {
        Toast toast = Toast.makeText(getApplicationContext(),
                str, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityPresent.cancelView();
    }

}