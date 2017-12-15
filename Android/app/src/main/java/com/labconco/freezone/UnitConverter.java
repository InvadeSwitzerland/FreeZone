package com.labconco.freezone;

/**
 * James Holdcroft
 * Assumes values come as numbers held in strings and that temperatures are in celsius and pressure is in millibars
 */

public class UnitConverter {
    public UnitConverter() {

    }

    protected String toTorr(String millibars){
        double torr = Double.parseDouble(millibars) * 0.750062;
        return torr + "";
    }

    protected String toPascal(String millibars){
        double pascal = Double.parseDouble(millibars) * 1000;
        return pascal + "";
    }

    protected String toFahrenheit(String celsius){
        double fahrenheit = Double.parseDouble(celsius) * 1.8 +32;
        return fahrenheit + "";
    }
}
