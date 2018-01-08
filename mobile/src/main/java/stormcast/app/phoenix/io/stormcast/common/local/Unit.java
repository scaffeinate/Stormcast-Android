package stormcast.app.phoenix.io.stormcast.common.local;

/**
 * Created by sudharti on 1/7/18.
 */

public class Unit {

    public static final String KPH = "kph";
    public static final String MPH = "mph";
    public static final String HECTOPASCALS = "hPa";
    public static final String MILLIBAR = "mb";
    public static final String MILE = "mi";
    public static final String DU = "DU";
    public static final String IMPERIAL = "us";
    public static final String METRIC = "si";
    public static final String CELCIUS = "C";
    public static final String FARANHEIT = "F";
    public static final String DEGREE = "\u00b0";
    public static final String PERCENT = "%";

    private String unitType;

    public Unit(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitType() {
        return unitType;
    }
}