// IFetchWeatherRequest.aidl
package com.lopeemano.weatherapp;

import com.lopeemano.weatherapp.IWeatherResult;
// Declare any non-default types here with import statements

interface IFetchWeatherRequest {
  /**
    * A one-way (non-blocking) call to the AcronymServiceAsync that
    * retrieves information about an acronym from the Acronym Web
    * service.  The AcronymServiceAsync subsequently uses the
    * AcronymResults parameter to return a List of AcronymData
    * containing the results from the Web service back to the
    * AcronymActivity.
    */
    oneway void fetchWeather(in String address,
                               in IWeatherResult result);
}
