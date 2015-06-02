// IFetchWeatherCall.aidl
package com.lopeemano.weatherapp;

import com.lopeemano.weatherapp.WeatherData;

// Declare any non-default types here with import statements

interface IFetchWeatherCall {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    WeatherData fetchWeather(String address);
}
