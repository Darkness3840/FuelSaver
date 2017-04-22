package io.github.darkness3840.fuelsaver;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EnergyConverter.initialize();

        int minTime = 1000; // in milliseconds
        float minDistance = 5; // in meters
        LocationTracker myLocListener = new LocationTracker(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setSpeedRequired(false);

        String bestProvider = locationManager.getBestProvider(criteria, false);

        try {
            locationManager.requestLocationUpdates(bestProvider, minTime, minDistance, myLocListener);
        } catch (SecurityException s) {

        }
    }
    public void update () {
        TextView miles = (TextView) findViewById(R.id.Miles);
        miles.setText(String.format(Locale.US, "Miles Travelled: %.3f", UnitConverter.metersToMiles(LocationTracker.getDistance())));

        TextView counter = (TextView) findViewById(R.id.Counter);
        counter.setText(String.format(Locale.US, "Gallons Saved: %.0f", GasStats.getGallons()));

        TextView updates = (TextView) findViewById(R.id.Updates);
        updates.setText(String.format(Locale.US, "Updates: %d", LocationTracker.getUpdates()));

        TextView fact = (TextView) findViewById(R.id.Facts);
        fact.setText(GasStats.getFact());
    }
    public void artificialTravel (View view) {
        LocationTracker.artificialAdd(50000 + Math.random() * 50000);
    }
    public void resetMiles (View view) {
        LocationTracker.reset();
    }
}
