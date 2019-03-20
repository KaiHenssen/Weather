package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Region;

import java.util.HashMap;
import java.util.Map;

public class CachedWeatherService implements WeatherService {

    private WeatherServiceAdaptor adapter;

    private Map<Region, Map<Day, Forecast>> cache = new HashMap<>();

    public CachedWeatherService(WeatherServiceAdaptor adapter) {
        this.adapter = adapter;


    }

    @Override
    public Forecast getWeather(Region region, Day day) {
        if(cache.get(region) != null && cache.get(region).get(day) != null) {
            return cache.get(region).get(day);
        }
        Forecast forecast = adapter.getWeather(region, day);
        cache.put(region, new HashMap<Day, Forecast>());
        cache.get(region).put(day,forecast);
        return forecast;
    }
}
