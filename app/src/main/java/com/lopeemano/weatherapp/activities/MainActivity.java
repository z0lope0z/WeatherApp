package com.lopeemano.weatherapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lopeemano.weatherapp.R;
import com.lopeemano.weatherapp.operations.WeatherOps;
import com.lopeemano.weatherapp.operations.WeatherOpsImpl;
import com.lopeemano.weatherapp.util.RetainedFragmentManager;


public class MainActivity extends LifecycleLoggingActivity {
    public static final String ACRONYM_OPS_STATE = "ACRONYM_OPS_STATE";
    /**
     * Used to retain the ImageOps state between runtime configuration
     * changes.
     */
    protected final RetainedFragmentManager mRetainedFragmentManager =
            new RetainedFragmentManager(this.getFragmentManager(),
                    TAG);

    /**
     * Provides acronym-related operations.
     */
    private WeatherOps mWeatherOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the AcronymOps object one time.
        mWeatherOps = new WeatherOpsImpl(this);

        // Handle any configuration change.
        handleConfigurationChanges();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle hardware reconfigurations, such as rotating the display.
     */
    protected void handleConfigurationChanges() {
        // If this method returns true then this is the first time the
        // Activity has been created.
        if (mRetainedFragmentManager.firstTimeIn()) {
            Log.d(TAG,
                    "First time onCreate() call");

            // Create the AcronymOps object one time.  The "true"
            // parameter instructs AcronymOps to use the
            // DownloadImagesBoundService.
            mWeatherOps = new WeatherOpsImpl(this);

            // Store the AcronymOps into the RetainedFragmentManager.
            mRetainedFragmentManager.put(ACRONYM_OPS_STATE,
                    mWeatherOps);

            // Initiate the service binding protocol (which may be a
            // no-op, depending on which type of DownloadImages*Service is
            // used).
            mWeatherOps.bindService();
        } else {
            // The RetainedFragmentManager was previously initialized,
            // which means that a runtime configuration change
            // occured.

            Log.d(TAG,
                    "Second or subsequent onCreate() call");

            // Obtain the AcronymOps object from the
            // RetainedFragmentManager.
            mWeatherOps =
                    mRetainedFragmentManager.get(ACRONYM_OPS_STATE);

            // This check shouldn't be necessary under normal
            // circumtances, but it's better to lose state than to
            // crash!
            if (mWeatherOps == null) {
                // Create the AcronymOps object one time.  The "true"
                // parameter instructs AcronymOps to use the
                // DownloadImagesBoundService.
                mWeatherOps = new WeatherOpsImpl(this);

                // Store the AcronymOps into the RetainedFragmentManager.
                mRetainedFragmentManager.put(ACRONYM_OPS_STATE,
                        mWeatherOps);

                // Initiate the service binding protocol (which may be
                // a no-op, depending on which type of
                // DownloadImages*Service is used).
                mWeatherOps.bindService();
            } else
                // Inform it that the runtime configuration change has
                // completed.
                mWeatherOps.onConfigurationChange(this);
        }
    }

    /*
     * Initiate the synchronous acronym lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void expandAcronymSync(View v) {
        mWeatherOps.fetchWeatherSync(v);
    }

    /*
     * Initiate the asynchronous acronym lookup when the user presses
     * the "Look Up Async" button.
     */
    public void expandAcronymAsync(View v) {
        mWeatherOps.fetchWeatherAsync(v);
    }
}
