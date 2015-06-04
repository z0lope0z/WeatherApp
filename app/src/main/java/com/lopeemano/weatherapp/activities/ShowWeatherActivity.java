package com.lopeemano.weatherapp.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lopeemano.weatherapp.R;
import com.lopeemano.weatherapp.WeatherData;
import com.lopeemano.weatherapp.util.Utils;

public class ShowWeatherActivity extends LifecycleLoggingActivity {
    public static final String WEATHER_DATA = "WEATHER_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);

        WeatherData weatherData = getIntent().getParcelableExtra(WEATHER_DATA);

        TextView txtAddress = (TextView) findViewById(R.id.txt_address);
        TextView txtDate = (TextView) findViewById(R.id.txt_date);
        TextView txtTempCelsius = (TextView) findViewById(R.id.txt_tmp_celsius);
        TextView txtTempFahrenheit = (TextView) findViewById(R.id.txt_tmp_fahrenheit);
        TextView txtDescription = (TextView) findViewById(R.id.txt_description);
        TextView txtHumidity = (TextView) findViewById(R.id.txt_humidity);
        TextView txtPressure = (TextView) findViewById(R.id.txt_pressure);
        TextView txtWind = (TextView) findViewById(R.id.txt_wind);
        double tempCelsius = Utils.toCelcius(weatherData.tempKelvin);

        txtAddress.setText(weatherData.name);
        txtDate.setText(Utils.getDateStringToday());
        txtTempCelsius.setText(tempCelsius + "°C");
        txtTempFahrenheit.setText(Utils.toFahrenheit(tempCelsius) + "°F");
        txtDescription.setText("Description: " + weatherData.description);
        txtHumidity.setText("Humidity: " + weatherData.humidity + "%");
        txtPressure.setText("Pressure: " + weatherData.pressure + " hPa");
        txtWind.setText("Wind: " + weatherData.windSpeed + " km/h NW");
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

}
