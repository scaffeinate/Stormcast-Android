package stormcast.app.phoenix.io.stormcast.utils;

import stormcast.app.phoenix.io.stormcast.common.local.Unit;

import static stormcast.app.phoenix.io.stormcast.common.local.Unit.CELCIUS;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.DEGREE;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.FARANHEIT;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.HECTOPASCALS;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.IMPERIAL;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.KPH;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.MILLIBAR;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.MPH;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.PERCENT;

/**
 * Created by sudharti on 1/1/18.
 */

public final class FormatUtils {

    public static String convertColorToHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static String formatSpeed(double speed, Unit unit) {
        if (unit.getUnitType().equals(IMPERIAL)) {
            speed = (speed * 2.232);
        } else {
            speed = (speed * 3.6);
        }
        return new StringBuilder()
                .append((int) speed)
                .append(unit.getUnitType().equals(IMPERIAL) ? MPH : KPH)
                .toString();
    }

    public static String formatTemperature(double temp, Unit unit) {
        return new StringBuilder()
                .append((int) temp)
                .append(DEGREE)
                .append(unit.getUnitType().equals(IMPERIAL) ? FARANHEIT : CELCIUS)
                .toString();
    }

    public static String formatPressure(double pressure, Unit unit) {
        return new StringBuilder()
                .append((int) pressure)
                .append(" ")
                .append(unit.getUnitType().equals(IMPERIAL) ? MILLIBAR : HECTOPASCALS)
                .toString();
    }

    public static String formatUnit(double value, String unit) {
        return new StringBuilder().append((int) value).append((unit == PERCENT) ? "" : " ").append(unit).toString();
    }
}
