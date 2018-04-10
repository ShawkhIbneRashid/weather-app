package com.example.andrei.weatherapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity{
    TextView t1,t2,t3,t4;
    Handler handler;
    JSONObject json;

   public Main2Activity(){
         handler = new Handler();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject main = json.getJSONObject("main");
            t1.setText(String.format("%.2f", main.getDouble("temp")) + " °C");
        } catch (JSONException e) {
            e.printStackTrace();
        }
       updateWeatherData("Sydney, AU");

        setContentView(R.layout.activity_main2);


        }


    private void updateWeatherData(final String city) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject json = RemoteFetch.getJSON( getBaseContext(),city);
                if(json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(),
                                    getBaseContext().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
    private void renderWeather(JSONObject json) {
        try {
            t1.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " + json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            t2.setText(details.getString("description").toUpperCase(Locale.US) + "\n"
                    + "Humidity: " + main.getString("humidity") + "%" + "\n" + "Pressure: "
                    + main.getString("pressure") + "hPa");

            t3.setText(String.format("%.2f", main.getDouble("temp")) + " °C");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updateOn = df.format(new Date(json.getLong("dt") * 1000));
            t4.setText("Last update: " + updateOn);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
