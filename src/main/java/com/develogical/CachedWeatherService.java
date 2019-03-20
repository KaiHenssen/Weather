package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachedWeatherService implements WeatherService {
    private WeatherService adapter;
    private Map<Region, Map<Day, Forecast>> cache = new HashMap<>();
    private List<SearchKey> ordering = new ArrayList<>();
    private int cacheSize = 10;

    public CachedWeatherService(WeatherService adapter) {
        this.adapter = adapter;
    }

    @Override
    public Forecast getWeather(Region region, Day day) {

        SearchKey sk = new SearchKey(region, day);
        if (ordering.contains(sk)){
            ordering.remove(sk);
        }
        ordering.add(sk);

        while (ordering.size() > cacheSize) {
            SearchKey oldest = ordering.remove(0);
            cache.get(oldest.region).remove(oldest.day);

        }

        if(cache.containsKey(region) && cache.get(region).containsKey(day)) {
            return cache.get(region).get(day);
        }
        Forecast forecast = adapter.getWeather(region, day);
        if (!cache.containsKey(region)) {
            cache.put(region, new HashMap<Day, Forecast>());
        }
        cache.get(region).put(day,forecast);
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
}
