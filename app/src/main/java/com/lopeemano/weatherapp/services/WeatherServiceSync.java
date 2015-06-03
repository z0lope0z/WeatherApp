package com.lopeemano.weatherapp.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lopeemano.weatherapp.IFetchWeatherCall;
import com.lopeemano.weatherapp.WeatherData;
import com.lopeemano.weatherapp.util.Utils;

/**
 * @class AcronymServiceSync
 * @brief This class uses synchronous AIDL interactions to expand
 * acronyms via an Acronym Web service.  The AcronymActivity
 * that binds to this Service will receive an IBinder that's an
 * instance of AcronymCall, which extends IBinder.  The
 * Activity can then interact with this Service by making
 * two-way method calls on the AcronymCall object asking this
 * Service to lookup the meaning of the Acronym string.  After
 * the lookup is finished, this Service sends the Acronym
 * results back to the Activity by returning a List of
 * AcronymData.
 * <p/>
 * AIDL is an example of the Broker Pattern, in which all
 * interprocess communication details are hidden behind the
 * AIDL interfaces.
 */
public class WeatherServiceSync extends LifecycleLoggingService {
    /**
     * Factory method that makes an Intent used to start the
     * AcronymServiceSync when passed to bindService().
     *
     * @param context The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
                WeatherServiceSync.class);
    }

    /**
     * Called when a client (e.g., AcronymActivity) calls
     * bindService() with the proper Intent.  Returns the
     * implementation of AcronymCall, which is implicitly cast as an
     * IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAcronymCallImpl;
    }

    /**
     * The concrete implementation of the AIDL Interface AcronymCall,
     * which extends the Stub class that implements AcronymCall,
     * thereby allowing Android to handle calls across process
     * boundaries.  This method runs in a separate Thread as part of
     * the Android Binder framework.
     * <p/>
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    private final IFetchWeatherCall.Stub mAcronymCallImpl =
            new IFetchWeatherCall.Stub() {
                /**
                 * Implement the AIDL AcronymCall expandAcronym() method,
                 * which forwards to DownloadUtils getResults() to obtain
                 * the results from the Acronym Web service and then
                 * returns the results back to the Activity.
                 */
                @Override
                public WeatherData fetchWeather(String address) throws RemoteException {

                    // Call the Acronym Web service to get the list of
                    // possible expansions of the designated acronym.
                    return Utils.toWeatherData(Utils.fetchWeather(address));
                }
            };
}