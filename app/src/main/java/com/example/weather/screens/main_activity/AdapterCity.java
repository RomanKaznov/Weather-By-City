package com.example.weather.screens.main_activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.database.entity.City;

import java.util.ArrayList;
import java.util.List;

public class AdapterCity extends RecyclerView.Adapter<AdapterCity.ViewHolder> {
    ArrayList<City> namesCities;
    MainActivityPresent mainActivityPresent;

    AdapterCity(MainActivityPresent mainActivityPresent) {
        namesCities = new ArrayList<>();
        this.mainActivityPresent = mainActivityPresent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(namesCities.get(position));
    }

    @Override
    public int getItemCount() {
        return namesCities.size();
    }

    public void setCity(List<City> list) {
        namesCities.clear();
        namesCities = (ArrayList<City>) list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Button cityItem;
        private Button importance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityItem = itemView.findViewById(R.id.nameCity);
            importance = itemView.findViewById(R.id.importance);
            cityItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivityPresent.outputWeather((String) cityItem.getText());
                }
            });
            importance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivityPresent.setImportanceCity(String.valueOf(cityItem.getText()));
                }
            });
        }

        void bind(City city) {
            mainActivityPresent.setBackgroundButtonMainCity(city.importance,importance);
            cityItem.setText(city.nameCity);
        }
    }

}
