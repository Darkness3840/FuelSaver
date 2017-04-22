package io.github.darkness3840.fuelsaver;

public class GasStats {
    public static double getGallons () {
        return UnitConverter.metersToMiles(LocationTracker.getDistance()) / 22;
    }
    public static String getFact () {
        return EnergyConverter.convert(getGallons());
    }
}
