package com.lopeemano.weatherapp.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lopeemano.weatherapp.IFetchWeatherRequest;
import com.lopeemano.weatherapp.IWeatherResult;
import com.lopeemano.weatherapp.WeatherData;
import com.lopeemano.weatherapp.util.Utils;

/**
 * @class AcronymServiceAsync
 * @brief This class uses asynchronous AIDL interactions to expand
 * acronyms via an Acronym Web service.  The AcronymActivity
 * that binds to this Service will receive an IBinder that's an
 * instance of AcronymRequest, which extends IBinder.  The
 * Activity can then interact with this Service by making
 * one-way method calls on the AcronymRequest object asking
 * this Service to lookup the Acronym's meaning, passing in an
 * AcronymResults object and the Acronym string.  After the
 * lookup is finished, this Service sends the Acronym results
 * back to the Activity by calling sendResults() on the
 * AcronymResults object.
 * <p/>
 * AIDL is an example of the Broker Pattern, in which all
 * interprocess communication details are hidden behind the
 * AIDL interfaces.
 */
public class WeatherServiceAsync extends LifecycleLoggingService {
    /**
     * Factory method that makes an Intent used to start the
     * AcronymServiceAsync when passed to bindService().
     *
     * @param context The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
                WeatherServiceAsync.class);
    }

    /**
     * Called when a client (e.g., AcronymActivity) calls
     * bindService() with the proper Intent.  Returns the
     * implementation of AcronymRequest, which is implicitly cast as
     * an IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mFetchWeatherRequestImpl;
    }

    /**
     * The concrete implementation of the AIDL Interface
     * AcronymRequest, which extends the Stub class that implements
     * AcronymRequest, thereby allowing Android to handle calls across
     * process boundaries.  This method runs in a separate Thread as
     * part of the Android Binder framework.
     * <p/>
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    private final IFetchWeatherRequest.Stub mFetchWeatherRequestImpl =
            new IFetchWeatherRequest.Stub() {

                /**
                 * Implement the AIDL AcronymRequest expandAcronym()
                 * method, which forwards to DownloadUtils getResults() to
                 * obtain the results from the Acronym Web service and
                 * then sends the results back to the Activity via a
                 * callback.
                 */
                @Override
                public void fetchWeather(String address, IWeatherResult callback) throws RemoteException {

                    // Call the Acronym Web service to get the list of
                    // possible expansions of the designated acronym.
                    final WeatherData weather =
                            Utils.fetchWeather(address);

                    if (weather != null) {
                        // Invoke a one-way callback to send list of
                        // acronym expansions back to the AcronymActivity.
                        callback.sendResult(weather);
                    } else
                        // Invoke a one-way callback to send an error
                        // message back to the AcronymActivity.
                        callback.sendError("Nothing found for "
                                + address
                                + " found");
                }
            };
}