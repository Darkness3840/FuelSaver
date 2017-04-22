package io.github.darkness3840.fuelsaver;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Sansation-Regular.ttf");

        EnergyConverter.initialize();

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

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
        update();
    }
    public void update () {
        TextView miles = (TextView) findViewById(R.id.Miles);
        miles.setText(String.format(Locale.US, "Miles Travelled: %.3f\n Green Miles Travelled: %.3f", UnitConverter.metersToMiles(LocationTracker.getDistance()), UnitConverter.metersToMiles(LocationTracker.getGreenDistance())));

        TextView counter = (TextView) findViewById(R.id.Counter);
        counter.setText(String.format(Locale.US, "Gallons Saved: %.3f", GasStats.getGallons()));

        TextView updates = (TextView) findViewById(R.id.Updates);
        updates.setText(String.format(Locale.US, "Updates: %d", LocationTracker.getUpdates()));

        TextView fact = (TextView) findViewById(R.id.Facts);
        fact.setText(GasStats.getFact());

        TextView transport = (TextView) findViewById(R.id.Transport);
        transport.setText("Mode of transport: " + ModeOfTransport.getType(ModeOfTransport.getMode()));

        ProgressBar Progress = (ProgressBar) findViewById(R.id.progress_bar);
        Progress.setProgress((int) GasStats.getGallons());
    }
    public void artificialTravel (View view) {
        LocationTracker.artificialAdd(50000 + Math.random() * 50000);
    }
    public void resetMiles (View view) {
        LocationTracker.reset();
    }

    public GoogleApiClient mApiClient;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        redetermineTransport();
    }
    private void redetermineTransport () {
        final Handler h = new Handler();
        final int delay = 3000; //milliseconds
        final MainActivity main = this;

        h.postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent( main, ModeOfTransport.class );
                PendingIntent pendingIntent = PendingIntent.getService( main, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
                ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 10000, pendingIntent );
                h.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
