package com.example.andrei.weatherapp;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by ratul roy on 15.10.2015.
 */
public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    String getCity() {
        return prefs.getString("city", "Dhaka, BD");
    }

    void setCity(String city) {
        prefs.edit().putString("city", city).commit();
    }
}
