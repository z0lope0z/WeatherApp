package com.lopeemano.weatherapp.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.util.JsonReader;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lopeemano.weatherapp.WeatherData;
import com.lopeemano.weatherapp.models.Main;
import com.lopeemano.weatherapp.models.Weather;
import com.lopeemano.weatherapp.models.WeatherApp;
import com.lopeemano.weatherapp.models.Wind;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @class AcronymDownloadUtils
 * @brief Handles the actual downloading of Acronym information from
 * the Acronym web service.
 */
public class Utils {
    /**
     * Logging tag used by the debugger.
     */
    private final static String TAG = Utils.class.getCanonicalName();

    /**
     * URL to the Acronym web service.
     */

    private final static String sWeather_Web_Service_URL =
            "http://api.openweathermap.org/data/2.5/weather?q=";

    public static WeatherData toWeatherData(WeatherApp weatherApp) {
        Weather weather = null;
        WeatherData weatherData = null;
        if (weatherApp.getWeatherList().size() > 0) {
            weather = weatherApp.getWeatherList().get(0);
            weatherData = new WeatherData(weatherApp.getName(),
                    weatherApp.getMain().getTemp(),
                    weather.getDescription(),
                    weatherApp.getMain().getHumidity(),
                    weatherApp.getMain().getPressure(),
                    weatherApp.getWind().getSpeed()
            );
        }
        return weatherData;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static WeatherApp fetchWeather(String address) {
        try {
            // Append the location to create the full URL.
            final URL url =
                    new URL(sWeather_Web_Service_URL
                            + address);
            // Opens a connection to the Acronym Service.
            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();

            // Sends the GET request and reads the Json results.
            InputStream in =
                    new BufferedInputStream(urlConnection.getInputStream());
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            WeatherApp weatherApp = readWeatherApp(reader);
            return weatherApp;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static WeatherApp readWeatherApp(JsonReader reader) throws IOException {
        WeatherApp weatherApp = new WeatherApp();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                weatherApp.setId(reader.nextInt());
            } else if (name.equals("name")) {
                weatherApp.setName(reader.nextString());
            } else if (name.equals("main")) {
                weatherApp.setMain(readMain(reader));
            } else if (name.equals("weather")) {
                weatherApp.setWeather(readWeatherArray(reader));
            } else if (name.equals("wind")) {
                weatherApp.setWind(readWind(reader));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return weatherApp;
    }

    /**
     * https://blocksnap.me/api/lookup.php?phone=REWARDS&mnc=03&mcc=515&key=99db7e17cd68aa1c402e5858bcb8b666
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static List<Weather> readWeatherArray(JsonReader reader) throws IOException {
        List<Weather> weatherList = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            weatherList.add(readWeather(reader));
        }
        reader.endArray();
        return weatherList;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Weather readWeather(JsonReader reader) throws IOException {
        Weather weather = new Weather();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                weather.setId(reader.nextInt());
            } else if (name.equals("main")) {
                weather.setMain(reader.nextString());
            } else if (name.equals("description")) {
                weather.setDescription(reader.nextString());
            } else if (name.equals("icon")) {
                weather.setIcon(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return weather;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Main readMain(JsonReader reader) throws IOException {
        Main main = new Main();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("temp")) {
                main.setTemp(reader.nextDouble());
            } else if (name.equals("pressure")) {
                main.setPressure(reader.nextDouble());
            } else if (name.equals("humidity")) {
                main.setHumidity(reader.nextInt());
            } else if (name.equals("temp_min")) {
                main.setTempMin(reader.nextDouble());
            } else if (name.equals("temp_max")) {
                main.setTempMax(reader.nextDouble());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return main;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Wind readWind(JsonReader reader) throws IOException {
        Wind wind = new Wind();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("speed")) {
                wind.setSpeed(reader.nextDouble());
            } else if (name.equals("deg")) {
                wind.setDeg(reader.nextDouble());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return wind;
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void hideKeyboard(Activity activity,
                                    IBinder windowToken) {
        InputMethodManager mgr =
                (InputMethodManager) activity.getSystemService
                        (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                0);
    }

    public static double toCelcius(double kelvin) {
        return kelvin - 273.15;
    }

    public static double toFahrenheit(double celcius) {
        return (celcius * 9) / 5 + 32;
    }

    public static String getDateStringToday() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM");
        //get current date time with Date()
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Show a toast message.
     */
    public static void showToast(Context context,
                                 String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Ensure this class is only used as a utility.
     */
    private Utils() {
        throw new AssertionError();
    }
}