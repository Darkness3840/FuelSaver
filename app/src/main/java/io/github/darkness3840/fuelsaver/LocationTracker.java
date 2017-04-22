package io.github.darkness3840.fuelsaver;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocationTracker implements LocationListener {

    private static double distanceTravelled = 0; // in meters
    private static int updates = 0;
    private static Location prevLoc;
    private static MainActivity main;

    public LocationTracker (MainActivity main) {
        this.main = main;
    }

    @Override
    public void onLocationChanged(Location loc)
    {
        if (loc != null)
        {
            if (prevLoc != null) {
                distanceTravelled += prevLoc.distanceTo(loc);
                updates++;
                main.update();
            }
            prevLoc = loc;
        }
    }

    @Override
    public void onProviderDisabled(String arg0)
    {
        // Do something here if you would like to know when the provider is disabled by the user
    }

    @Override
    public void onProviderEnabled(String arg0)
    {
        // Do something here if you would like to know when the provider is enabled by the user
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2)
    {
        // Do something here if you would like to know when the provider status changes
    }

    public static double getDistance () {
        return distanceTravelled;
    }
    public static double getLatitude () {
        return prevLoc.getLatitude();
    }
    public static double getLongitude () {
        return prevLoc.getLongitude();
    }
    public static int getUpdates () {
        return updates;
    }
    public static void artificialAdd (double dist) {
        distanceTravelled += dist;
        main.update();
    }
    public static void reset () {
        distanceTravelled = 0;
        updates = 0;
        main.update();
    }
}
