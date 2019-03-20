package com.develogical;

import com.weather.Day;
import com.weather.Forecast;
import com.weather.Region;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CachedWeatherServiceTest {
    @Test
    public void testCacheAfterFill(){
        WeatherService adapter = mock(WeatherService.class);
        Forecast hot = new Forecast("hot", 44);
        when(adapter.getWeather(Region.LONDON, Day.SATURDAY)).thenReturn(hot);

        CachedWeatherService cachedWeatherService = new CachedWeatherService(adapter);

        assertThat(cachedWeatherService.getWeather(Region.LONDON, Day.SATURDAY), is(hot));
        verify(adapter).getWeather(Region.LONDON, Day.SATURDAY);

        assertThat(cachedWeatherService.getWeather(Region.LONDON, Day.SATURDAY), is(hot));
        verifyNoMoreInteractions(adapter);
    }

    @Test
    public void testCacheTwoEntries(){
        WeatherService adapter = mock(WeatherService.class);
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

    @Test
    public void testCacheLimit(){
        WeatherService adapter = mock(WeatherService.class);
        when(adapter.getWeather(any(Region.class), any(Day.class))).thenReturn(mock(Forecast.class));

        CachedWeatherService cachedWeatherService = new CachedWeatherService(adapter);

        cachedWeatherService.setCacheSize(1);

        cachedWeatherService.getWeather(Region.LONDON, Day.SATURDAY);

        cachedWeatherService.getWeather(Region.LONDON, Day.SUNDAY);

        cachedWeatherService.getWeather(Region.LONDON, Day.SATURDAY);
        verify(adapter, times(2)).getWeather(Region.LONDON, Day.SATURDAY);
    }
}