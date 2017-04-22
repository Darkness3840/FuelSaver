package io.github.darkness3840.fuelsaver;

import java.io.*;
import java.util.*;
import static java.lang.System.*;
import static java.lang.Math.*;

public class EnergyConverter {
    private static HashMap<Double, String> table = new HashMap<>();
    private static final double MIN_GALLONS = 10;
    private static final double MAX_ERROR = 0.1;

    public static void initialize () {
        Scanner file = new Scanner("3.8e11 That's equal to the amount of energy the sun produces in %n femtosecond(s).\n" +
                "6.3e9 That is the same as the energy intake of a blue whale in %n day(s).\n" +
                "8.8e10 That is the amount of energy in %n gram(s) of Uranium-235.\n" +
                "6.3e13 That is [%n times ]the amount of energy released by the bomb dropped on Hiroshima.\n" +
                "4.5e9 That is the average energy usage that a standard refrigerator uses in %n year(s).\n" +
                "4.1e7 That is the average amount of energy expended by a human heart in %n year(s).\n" +
                "6e11 That is the energy released by the average hurricane in %n millisecond(s).\n" +
                "5.5e8 That is the kinetic energy of a %n-kilogram meteor hitting Earth.\n" +
                "1.10e9 That is the energy in %n lightning bolt(s).\n" +
                "4.2e6 That is the energy released from %n kilogram(s) of TNT.\n" +
                "313  That is the amount of energy used by %n [person](people) jumping as high as they can.\n" +
                "9e9 That is the mass-energy in %n microgram(s) of antimatter.\n" +
                "2.2e11 That is amount of energy used in %n milligram(s) of the largest nuclear weapon ever tested.\n" +
                "2.4e11 That is food energy consumed by %n [person](people) in their lifetime(s).\n" +
                "6.4e12 That is equivalent to the energy in %n 747 aircraft fuel tank(s).\n" +
                "1.2e6 That is the amount of energy in %n Snickers bars.\n" +
                "4.2e7 That is the amount of energy consumed by Michael Phelps in %n day(s) of olympic training.");
        while (file.hasNextDouble()) {
            table.put(file.nextDouble(), file.nextLine());
        }
    }
    public static String convert (double gallons) {
        if (MIN_GALLONS > gallons) return "Keep going!";
        double joules = gallons * 1.3e8;

        ArrayList<Double> goodKeys = new ArrayList<>();

        for (Double d : table.keySet()) {
            double error = abs(round(joules/d) - joules/d);

            if (error < MAX_ERROR && joules >= d) {
                goodKeys.add(d);
            }
        }

        if (goodKeys.size() == 0) return "That's a lot of energy saved!";
        double key = goodKeys.get((int)(random() * goodKeys.size()));

        if (round(joules/key) == 1) {
            return table.get(key).replaceAll("\\(.*\\)", "").replaceAll("\\[|\\]", "").replaceAll("%n", "one").trim();
        } else {
            return table.get(key).replaceAll("\\[.*\\]", "").replaceAll("\\(|\\)", "").replaceAll("%n", String.format(Locale.US, "%,d", round(joules/key))).trim();
        }
    }
}
