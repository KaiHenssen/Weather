package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Region;

public interface WeatherService {

    Forecast getWeather(Region region, Day day);


}
