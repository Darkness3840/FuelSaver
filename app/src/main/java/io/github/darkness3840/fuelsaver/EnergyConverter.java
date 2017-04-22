package io.github.darkness3840.fuelsaver;

import java.io.*;
import java.util.*;
import static java.lang.System.*;
import static java.lang.Math.*;

public class EnergyConverter {
    private static HashMap<Double, String> table = new HashMap<>();
    private static final double MIN_GALLONS = 10;
    private static final double MAX_ERROR = 0.1;

    public static void initialize (String path) throws IOException { // use conversions.dat
        Scanner file = new Scanner(new File(path));
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
            return table.get(key).replaceAll("\\[.*\\]", "").replaceAll("\\(|\\)", "").replaceAll("%n", String.format("%,d", round(joules/key))).trim();
        }
    }
}
