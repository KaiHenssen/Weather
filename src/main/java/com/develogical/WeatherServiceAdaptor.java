package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Forecaster;
import com.weather.Region;

public class WeatherServiceAdaptor implements WeatherService {

    @Override
    public Forecast getWeather(Region region, Day day) {
        Forecaster forecaster = new Forecaster();
        return forecaster.forecastFor(region, day);
    }
}
