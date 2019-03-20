package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Region;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CachedWeatherServiceTest {


    @Test
    public void testCacheAfterFill(){

        WeatherServiceAdaptor adapter = mock(WeatherServiceAdaptor.class);
        when(adapter.getWeather(Region.LONDON, Day.SATURDAY)).thenReturn(mock(Forecast.class));

        CachedWeatherService cachedWeatherService = new CachedWeatherService(adapter);

        cachedWeatherService.getWeather(Region.LONDON, Day.SATURDAY);

        verify(adapter).getWeather(Region.LONDON, Day.SATURDAY);

        cachedWeatherService.getWeather(Region.LONDON, Day.SATURDAY);
        verifyNoMoreInteractions(adapter);

    }

    @Test
    public void testCacheTwoEntries(){

        WeatherServiceAdaptor adapter = mock(WeatherServiceAdaptor.class);
        when(adapter.getWeather(any(Region.class), any(Day.class))).thenReturn(mock(Forecast.class));


        CachedWeatherService cachedWeatherService = new CachedWeatherService(adapter);


        cachedWeatherService.getWeather(Region.LONDON, Day.SATURDAY);

        verify(adapter).getWeather(Region.LONDON, Day.SATURDAY);

        cachedWeatherService.getWeather(Region.LONDON, Day.SATURDAY);

        verifyNoMoreInteractions(adapter);


        cachedWeatherService.getWeather(Region.EDINBURGH, Day.SUNDAY);

        verify(adapter).getWeather(Region.EDINBURGH, Day.SUNDAY);


        cachedWeatherService.getWeather(Region.EDINBURGH, Day.SUNDAY);

        verifyNoMoreInteractions(adapter);


    }


}