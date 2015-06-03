package com.lopeemano.weatherapp.operations;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lopeemano.weatherapp.IFetchWeatherCall;
import com.lopeemano.weatherapp.IFetchWeatherRequest;
import com.lopeemano.weatherapp.IWeatherResult;
import com.lopeemano.weatherapp.R;
import com.lopeemano.weatherapp.WeatherData;
import com.lopeemano.weatherapp.activities.MainActivity;
import com.lopeemano.weatherapp.activities.ShowWeatherActivity;
import com.lopeemano.weatherapp.services.WeatherServiceAsync;
import com.lopeemano.weatherapp.services.WeatherServiceSync;
import com.lopeemano.weatherapp.util.GenericServiceConnection;
import com.lopeemano.weatherapp.util.Utils;

import java.lang.ref.WeakReference;

/**
 * This class defines all the acronym-related operations.
 */
public class WeatherOpsImpl implements WeatherOps {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final String TAG = getClass().getSimpleName();

    /**
     * Used to enable garbage collection.
     */
    protected WeakReference<MainActivity> mActivity;

    /**
     * Address entered by the user.
     */
    protected WeakReference<EditText> mEditText;

    /**
     * Result to display (if any).
     */
    protected WeatherData mResult;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceSync Service using bindService().
     */
    private GenericServiceConnection<IFetchWeatherCall> mServiceConnectionSync;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceAsync Service using bindService().
     */
    private GenericServiceConnection<IFetchWeatherRequest> mServiceConnectionAsync;

    /**
     * This Handler is used to post Runnables to the UI from the
     * mWeatherResults callback methods to avoid a dependency on the
     * Activity, which may be destroyed in the UI Thread during a
     * runtime configuration change.
     */
    private final Handler mDisplayHandler = new Handler();

    /**
     * The implementation of the AcronymResults AIDL Interface, which
     * will be passed to the Acronym Web service using the
     * AcronymRequest.expandAcronym() method.
     * <p/>
     * This implementation of AcronymResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private final IWeatherResult.Stub mFetchWeatherResult =
            new IWeatherResult.Stub() {
                /**
                 * This method is invoked by the AcronymServiceAsync to
                 * return the results back to the AcronymActivity.
                 */
                @Override
                public void sendResult(final WeatherData weatherData)
                        throws RemoteException {
                    // Since the Android Binder framework dispatches this
                    // method in a background Thread we need to explicitly
                    // post a runnable containing the results to the UI
                    // Thread, where it's displayed.  We use the
                    // mDisplayHandler to avoid a dependency on the
                    // Activity, which may be destroyed in the UI Thread
                    // during a runtime configuration change.
                    mDisplayHandler.post(new Runnable() {
                        public void run() {
                            displayResults(weatherData);
                        }
                    });
                }

                /**
                 * This method is invoked by the AcronymServiceAsync to
                 * return error results back to the AcronymActivity.
                 */
                @Override
                public void sendError(final String reason)
                        throws RemoteException {
                    // Since the Android Binder framework dispatches this
                    // method in a background Thread we need to explicitly
                    // post a runnable containing the results to the UI
                    // Thread, where it's displayed.  We use the
                    // mDisplayHandler to avoid a dependency on the
                    // Activity, which may be destroyed in the UI Thread
                    // during a runtime configuration change.
                    mDisplayHandler.post(new Runnable() {
                        public void run() {
                            Utils.showToast(mActivity.get(),
                                    reason);
                        }
                    });
                }
            };

    /**
     * Constructor initializes the fields.
     */
    public WeatherOpsImpl(MainActivity activity) {
        // Initialize the WeakReference.
        mActivity = new WeakReference<>(activity);

        // Finish the initialization steps.
        initializeViewFields();
        initializeNonViewFields();
    }

    /**
     * Initialize the View fields, which are all stored as
     * WeakReferences to enable garbage collection.
     */
    private void initializeViewFields() {
        // Get references to the UI components.
        mActivity.get().setContentView(R.layout.activity_main);

        // Store the EditText that holds the urls entered by the user
        // (if any).
        mEditText = new WeakReference<>
                ((EditText) mActivity.get().findViewById(R.id.editText1));

        // Display results if any (due to runtime configuration change).
        if (mResult != null)
            displayResults(mResult);
    }

    /**
     * (Re)initialize the non-view fields (e.g.,
     * GenericServiceConnection objects).
     */
    private void initializeNonViewFields() {
        mServiceConnectionSync =
                new GenericServiceConnection<IFetchWeatherCall>
                        (IFetchWeatherCall.class);

        mServiceConnectionAsync =
                new GenericServiceConnection<IFetchWeatherRequest>
                        (IFetchWeatherRequest.class);
    }

    /**
     * Initiate the service binding protocol.
     */
    @Override
    public void bindService() {
        Log.d(TAG,
                "calling bindService()");

        // Launch the Acronym Bound Services if they aren't already
        // running via a call to bindService(), which binds this
        // activity to the AcronymService* if they aren't already
        // bound.
        if (mServiceConnectionSync.getInterface() == null)
            mActivity.get().bindService
                    (WeatherServiceSync.makeIntent(mActivity.get()),
                            mServiceConnectionSync,
                            Context.BIND_AUTO_CREATE);

        if (mServiceConnectionAsync.getInterface() == null)
            mActivity.get().bindService
                    (WeatherServiceAsync.makeIntent(mActivity.get()),
                            mServiceConnectionAsync,
                            Context.BIND_AUTO_CREATE);
    }

    /**
     * Initiate the service unbinding protocol.
     */
    @Override
    public void unbindService() {
        if (mActivity.get().isChangingConfigurations())
            Log.d(TAG,
                    "just a configuration change - unbindService() not called");
        else {
            Log.d(TAG,
                    "calling unbindService()");

            // Unbind the Async Service if it is connected.
            if (mServiceConnectionAsync.getInterface() != null)
                mActivity.get().getApplicationContext().unbindService
                        (mServiceConnectionAsync);

            // Unbind the Sync Service if it is connected.
            if (mServiceConnectionSync.getInterface() != null)
                mActivity.get().getApplicationContext().unbindService
                        (mServiceConnectionSync);
        }
    }

    /**
     * Called after a runtime configuration change occurs to finish
     * the initialization steps.
     */
    public void onConfigurationChange(MainActivity activity) {
        Log.d(TAG,
                "onConfigurationChange() called");

        // Reset the mActivity WeakReference.
        mActivity = new WeakReference<>(activity);

        // (Re)initialize all the View fields.
        initializeViewFields();
    }

    /*
     * Initiate the asynchronous acronym lookup when the user presses
     * the "Look Up Async" button.
     */
    @Override
    public void fetchWeatherAsync(View v) {
        final IFetchWeatherRequest weatherRequest =
                mServiceConnectionAsync.getInterface();

        if (weatherRequest != null) {
            // Get the acronym entered by the user.
            final String address =
                    mEditText.get().getText().toString();

            resetDisplay();

            try {
                // Invoke a one-way AIDL call, which does not block
                // the client.  The results are returned via the
                // sendResults() method of the mFetchWeatherResult
                // callback object, which runs in a Thread from the
                // Thread pool managed by the Binder framework.
                weatherRequest.fetchWeather(address,
                        mFetchWeatherResult);
            } catch (RemoteException e) {
                Log.e(TAG,
                        "RemoteException:"
                                + e.getMessage());
            }
        } else {
            Log.d(TAG,
                    "acronymRequest was null.");
        }
    }

    /*
     * Initiate the synchronous acronym lookup when the user presses
     * the "Look Up Sync" button.
     */
    @Override
    public void fetchWeatherSync(View v) {
        final IFetchWeatherCall fetchWeatherCall =
                mServiceConnectionSync.getInterface();

        if (fetchWeatherCall != null) {
            // Get the acronym entered by the user.
            final String address =
                    mEditText.get().getText().toString();
            resetDisplay();

            // Use an anonymous AsyncTask to download the Acronym data
            // in a separate thread and then display any results in
            // the UI thread.
            new AsyncTask<String, Void, WeatherData>() {
                /**
                 * Acronym we're trying to expand.
                 */
                private String mAcronym;

                /**
                 * Retrieve the expanded acronym results via a
                 * synchronous two-way method call, which runs in a
                 * background thread to avoid blocking the UI thread.
                 */
                protected WeatherData doInBackground(String... address) {
                    try {
                        mAcronym = address[0];
                        return fetchWeatherCall.fetchWeather(mAcronym);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                /**
                 * Display the results in the UI Thread.
                 */
                protected void onPostExecute(WeatherData weatherData) {
                    if (weatherData != null)
                        displayResults(weatherData);
                    else
                        Utils.showToast(mActivity.get(),
                                "no expansions for "
                                        + mAcronym
                                        + " found");
                }
                // Execute the AsyncTask to expand the acronym without
                // blocking the caller.
            }.execute(address);
        } else {
            Log.d(TAG, "mAcronymCall was null.");
        }
    }

    /**
     * Display the results to the screen.
     *
     * @param result List of Results to be displayed.
     */
    private void displayResults(WeatherData result) {
        Intent intent = new Intent(mActivity.get(), ShowWeatherActivity.class);
        intent.putExtra(ShowWeatherActivity.WEATHER_DATA, result);
        mActivity.get().startActivity(intent);
    }

    /**
     * Reset the display prior to attempting to expand a new acronym.
     */
    private void resetDisplay() {
        Utils.hideKeyboard(mActivity.get(),
                mEditText.get().getWindowToken());
        mResult = null;
    }
}
