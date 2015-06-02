// IWeatherResults.aidl
package com.lopeemano.weatherapp;

import com.lopeemano.weatherapp.WeatherData;

// Declare any non-default types here with import statements

interface IWeatherResult {
    /**
     * This one-way (non-blocking) method allows AcyronymServiceAsync
     * to return the List of AcronymData results associated with a
     * one-way AcronymRequest.callAcronymRequest() call.
     */
    oneway void sendResult(in WeatherData weatherResult);

    /**
     * This one-way (non-blocking) method allows AcyronymServiceAsync
     * to return an error String if the Service fails for some reason.
     */
    oneway void sendError(in String reason);
}