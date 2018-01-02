package stormcast.app.phoenix.io.stormcast.utils;

/**
 * Created by sudharti on 1/1/18.
 */

public final class FormatUtils {
    public static String convertColorToHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
