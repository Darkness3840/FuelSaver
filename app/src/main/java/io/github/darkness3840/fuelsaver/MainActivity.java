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
import android.widget.ImageView;
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
        String factStr = GasStats.getFact();
        fact.setText(factStr);

        ImageView factImage = (ImageView) findViewById(R.id.FactImage);
        if (!factStr.isEmpty() && !factStr.equals("That's a lot of energy saved!")) {
            switch (String.format(Locale.US, "%.1e", EnergyConverter.getKey()).toLowerCase().trim()) {
                case "3.8e+11": factImage.setImageResource(R.drawable.sun); break;
                case "6.3e+09": factImage.setImageResource(R.drawable.whale); break;
                case "8.8e+10": factImage.setImageResource(R.drawable.uranium); break;
                case "6.3e+13": factImage.setImageResource(R.drawable.nuke); break;
                case "4.5e+09": factImage.setImageResource(R.drawable.fridge); break;
                case "4.1e+07": factImage.setImageResource(R.drawable.heart); break;
                case "6.0e+11": factImage.setImageResource(R.drawable.hurricane); break;
                case "5.5e+08": factImage.setImageResource(R.drawable.meteor); break;
                case "1.1e+09": factImage.setImageResource(R.drawable.lightning); break;
                case "4.2e+06": factImage.setImageResource(R.drawable.tnt); break;
                case "3.1e+02": factImage.setImageResource(R.drawable.jumping); break;
                case "9.0e+09": factImage.setImageResource(R.drawable.antimatter); break;
                case "2.2e+11": factImage.setImageResource(R.drawable.tsarbomba); break;
                case "2.4e+11": factImage.setImageResource(R.drawable.apple); break;
                case "6.4e+12": factImage.setImageResource(R.drawable.plane); break;
                case "1.2e+06": factImage.setImageResource(R.drawable.chocolate); break;
                case "4.2e+07": factImage.setImageResource(R.drawable.wave); break;
                default: fact.setText(String.format(Locale.US, "%.1e", EnergyConverter.getKey()).toLowerCase().trim());
            }
            factImage.setImageAlpha(255);
        } else factImage.setImageAlpha(0);

        TextView transport = (TextView) findViewById(R.id.Transport);
        transport.setText("Mode of transport: " + ModeOfTransport.getType(ModeOfTransport.getMode()));

        ProgressBar Progress = (ProgressBar) findViewById(R.id.progress_bar);
        Progress.setProgress((int) GasStats.getGallons());

        if (GasStats.getGallons() >= 1) ((ImageView)findViewById(R.id.BronzeTrophy)).setImageResource(R.drawable.bronzetrophy);
        if (GasStats.getGallons() >= 10) ((ImageView)findViewById(R.id.SilverTrophy)).setImageResource(R.drawable.silvertrophy);
        if (GasStats.getGallons() >= 100) ((ImageView)findViewById(R.id.GoldTrophy)).setImageResource(R.drawable.goldtrophy);

        if (UnitConverter.metersToMiles(LocationTracker.getWalkDistance()) / 22 >= 40) ((ImageView)findViewById(R.id.WalkRibbon)).setImageResource(R.drawable.walkribbon);
        if (UnitConverter.metersToMiles(LocationTracker.getRunDistance()) / 22 >= 20) ((ImageView)findViewById(R.id.RunRibbon)).setImageResource(R.drawable.runribbon);
        if (UnitConverter.metersToMiles(LocationTracker.getBikeDistance()) / 22 >= 80) ((ImageView)findViewById(R.id.BikeRibbon)).setImageResource(R.drawable.bikeribbon);
    }
    public void artificialTravel (View view) {
        LocationTracker.artificialAdd(50000 + Math.random() * 50000);
    }
    public void resetMiles (View view) {
        LocationTracker.reset();
        ((ImageView)findViewById(R.id.BronzeTrophy)).setImageResource(R.drawable.greytrophy);
        ((ImageView)findViewById(R.id.SilverTrophy)).setImageResource(R.drawable.greytrophy);
        ((ImageView)findViewById(R.id.GoldTrophy)).setImageResource(R.drawable.greytrophy);

        ((ImageView)findViewById(R.id.WalkRibbon)).setImageResource(R.drawable.greywalkribbon);
        ((ImageView)findViewById(R.id.RunRibbon)).setImageResource(R.drawable.greyrunribbon);
        ((ImageView)findViewById(R.id.BikeRibbon)).setImageResource(R.drawable.greybikeribbon);
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
