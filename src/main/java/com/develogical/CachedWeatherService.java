package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachedWeatherService implements WeatherService {
    private final WeatherService adapter;
    private final InternalClock internalClock;
    private final Map<SearchKey, CacheForecast> cache = new HashMap<>();
    private final List<SearchKey> ordering = new ArrayList<>();
    private int cacheSize = 10;

    public CachedWeatherService(WeatherService adapter, InternalClock internalClock) {
        this.adapter = adapter;
        this.internalClock = internalClock;
    }

    @Override
    public Forecast getWeather(Region region, Day day) {

        SearchKey sk = new SearchKey(region, day);
        if (ordering.contains(sk)){
            ordering.remove(sk);
        }
        ordering.add(sk);

        while (ordering.size() > cacheSize) {
            ordering.remove(0);
            cache.remove(sk);

        }

        if(cache.containsKey(sk) && cache.get(sk).timestamp > (internalClock.millis()-1000*60*60L)) {
            return cache.get(sk).forecast;
        }
        Forecast forecast = adapter.getWeather(region, day);
        if (!cache.containsKey(region)) {
            cache.put(new SearchKey(region, day), new CacheForecast(forecast, internalClock.millis()));
        }
        return forecast;
    }

    public void setCacheSize(int i) {
        this.cacheSize = i;
    }


    private static class SearchKey {
        Region region;
        Day day;

        public SearchKey(Region region, Day day) {
            this.region = region;
            this.day = day;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof SearchKey)){
                return  false;
            }
            SearchKey sk = (SearchKey) o;
            return  sk.region == this.region && sk.day == this.day;
        }

        @Override
        public int hashCode() {
            return region.hashCode() * day.hashCode();
        }
    }

    private static class CacheForecast {
        public CacheForecast(Forecast forecast, Long timestamp) {
            this.forecast = forecast;
            this.timestamp = timestamp;
        }

        Forecast forecast;
        Long timestamp;
    }
}
