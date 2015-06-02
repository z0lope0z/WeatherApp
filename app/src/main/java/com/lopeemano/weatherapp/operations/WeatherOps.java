package com.lopeemano.weatherapp.operations;

import android.view.View;

import com.lopeemano.weatherapp.activities.MainActivity;

/**
 * This class defines all the acronym-related operations.
 */
public interface WeatherOps {

    /**
     * Initiate the service binding protocol.
     */
    public void bindService();

    /**
     * Initiate the service unbinding protocol.
     */
    public void unbindService();

    /*
     * Initiate the synchronous acronym lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void fetchWeatherSync(View v);

    /*
     * Initiate the asynchronous acronym lookup when the user presses
     * the "Look Up Async" button.
     */
    public void fetchWeatherAsync(View v);

    /**
     * Called after a runtime configuration change occurs to finish
     * the initialization steps.
     */
    public void onConfigurationChange(MainActivity activity);
}
