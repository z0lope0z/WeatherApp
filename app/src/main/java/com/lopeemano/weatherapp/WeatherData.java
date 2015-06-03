package com.lopeemano.weatherapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a Plain Old Java Object (POJO) used for data
 * transport within the Acronym app.  This POJO implements the
 * Parcelable interface to enable IPC between the AcronymActivity and
 * the AcronymServiceSync and AcronymServiceAsync. It represents the
 * response Json obtained from the Acronym API, e.g., a call to
 * http://www.nactem.ac.uk/software/acromine/dictionary.py?sf=BBC
 * might return the following Json data:
 * <p/>
 * [{"sf": "BBC", "lfs": [{"lf": "British Broadcasting Corporation",
 * "freq": 8, "since": 1986, "vars": [{"lf": "British Broadcasting
 * Corporation", "freq": 8, "since": 1986}]}, {"lf": "backbone
 * cyclic", "freq": 5, "since": 1999, "vars": [{"lf": "backbone
 * cyclic", "freq": 5, "since": 1999}]}, {"lf": "bilateral breast
 * cancer", "freq": 5, "since": 2000, "vars": [{"lf": "bilateral
 * breast cancer", "freq": 4, "since": 2000}, {"lf": "Bilateral breast
 * cancer", "freq": 1, "since": 2006}]}, {"lf": "bone bisphosphonate
 * clearance", "freq": 3, "since": 1992, "vars": [{"lf": "bone
 * bisphosphonate clearance", "freq": 3, "since": 1992}]}, {"lf":
 * "bovine brain capillary", "freq": 3, "since": 1989, "vars": [{"lf":
 * "bovine brain capillary", "freq": 2, "since": 1989}, {"lf": "bovine
 * brain capillaries", "freq": 1, "since": 2000}]}]}]
 * <p/>
 * Parcelable defines an interface for marshaling/de-marshaling
 * https://en.wikipedia.org/wiki/Marshalling_(computer_science)
 * to/from a format that Android uses to allow data transport between
 * processes on a device.  Discussion of the details of Parcelable is
 * outside the scope of this assignment, but you can read more at
 * https://developer.android.com/reference/android/os/Parcelable.html.
 */
public class WeatherData implements Parcelable {
    /*
     * These data members are the local variables that will store the
     * AcronymData's state
     */

    /**
     * The long form of the acronym (spelled out version).
     */
    public String name;

    public double tempKelvin;

    public String description;

    public int humidity;

    public int pressure;

    public double windSpeed;

    /**
     * The relative frequency of usage in print, of this meaning of
     * the acronym.
     */
//    public int mFreq;

    /**
     * The year the acronym was added to this database of acronyms, or
     * was originally termed.
     */
//    public int msince;

    /**
     * Private constructor provided for the CREATOR interface, which
     * is used to de-marshal an AcronymData from the Parcel of data.
     */
    private WeatherData(Parcel in) {
        name = in.readString();
        tempKelvin = in.readDouble();
        description = in.readString();
        humidity = in.readInt();
        pressure = in.readInt();
        windSpeed = in.readDouble();
    }

    public WeatherData(String name, double tempKelvin, String description, int humidity, int pressure, double windSpeed) {
        this.name = name;
        this.tempKelvin = tempKelvin;
        this.description = description;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
    }

    /**
     * The toString() custom implementation.
     */
    @Override
    public String toString() {
        return "AcronymData [name="
                + name
                + ", tempKelvin="
                + tempKelvin
                + ", description="
                + description
                + ", humidity="
                + humidity
                + ", pressure="
                + pressure
                + ", windSpeed="
                + windSpeed
                + "]";
    }

    /*
     * Parcelable related methods.
     */

    /**
     * A bitmask indicating the set of special object types marshaled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Marshal this AcronymData to the target Parcel.
     */
    @Override
    public void writeToParcel(Parcel dest,
                              int flags) {
        // TODO Auto-generated method stub
        dest.writeString(name);
        dest.writeDouble(tempKelvin);
        dest.writeString(description);
        dest.writeInt(humidity);
        dest.writeInt(pressure);
        dest.writeDouble(windSpeed);
    }

    /**
     * public Parcelable.Creator for AcronymData, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<WeatherData> CREATOR =
            new Parcelable.Creator<WeatherData>() {
                public WeatherData createFromParcel(Parcel in) {
                    return new WeatherData(in);
                }

                public WeatherData[] newArray(int size) {
                    return new WeatherData[size];
                }
            };
}