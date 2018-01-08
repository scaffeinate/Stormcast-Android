package stormcast.app.phoenix.io.stormcast.utils;

import stormcast.app.phoenix.io.stormcast.R;

/**
 * Created by sudharti on 10/3/17.
 */

public final class IconResource {
    public static int getIconResource(String icon, boolean isDay) {
        if (icon == null) return R.string.wi_na;

        switch (icon) {
            case "clear-day":
                return R.string.wi_day_sunny;
            case "clear-night":
                return R.string.wi_night_clear;
            case "rain":
                return isDay ? R.string.wi_rain : R.string.wi_night_alt_rain;
            case "snow":
                return isDay ? R.string.wi_snow : R.string.wi_night_alt_snow;
            case "sleet":
                return isDay ? R.string.wi_sleet : R.string.wi_night_sleet;
            case "wind":
                return isDay ? R.string.wi_windy : R.string.wi_night_alt_cloudy_windy;
            case "fog":
                return isDay ? R.string.wi_fog : R.string.wi_night_fog;
            case "cloudy":
                return isDay ? R.string.wi_cloudy : R.string.wi_night_cloudy;
            case "partly-cloudy-day":
                return R.string.wi_day_cloudy;
            case "partly-cloudy-night":
                return R.string.wi_night_partly_cloudy;
            case "hail":
                return isDay ? R.string.wi_hail : R.string.wi_night_alt_hail;
            case "thunderstorm":
                return isDay ? R.string.wi_thunderstorm : R.string.wi_night_alt_thunderstorm;
            case "tornado":
                return R.string.wi_tornado;
            case "tsunami":
                return R.string.wi_tsunami;
            case "sandstorm":
                return R.string.wi_sandstorm;
            case "hurricane":
                return R.string.wi_hurricane;
            case "earthquake":
                return R.string.wi_earthquake;
            case "flood":
                return R.string.wi_flood;
            default:
                return R.string.wi_na;
        }
    }
}
