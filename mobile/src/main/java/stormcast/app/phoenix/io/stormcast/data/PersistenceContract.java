package stormcast.app.phoenix.io.stormcast.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sudharti on 12/31/17.
 */

public class PersistenceContract {

    public static final String DATABASE_NAME = "StormcastDb";

    public static final int DATABASE_VERSION = 1;

    public static final String LOCATIONS_AUTHORITY = "stormcast.app.phoenix.io.stormcast.locations";

    public static final String FORECAST_AUTHORITY = "stormcast.app.phoenix.io.stormcast.forecast";

    public static final String LOCATIONS_PATH = "locations";

    public static final String FORECAST_PATH = "forecast";

    public static final Uri LOCATIONS_CONTENT_URI = Uri.parse("content://" + LOCATIONS_AUTHORITY)
            .buildUpon()
            .appendPath(LOCATIONS_PATH)
            .build();

    public static final Uri FORECAST_CONTENT_URI = Uri.parse("content://" + FORECAST_AUTHORITY)
            .buildUpon()
            .appendPath(FORECAST_PATH)
            .build();

    public static abstract class LocationEntry implements BaseColumns {
        public final static String TABLE_NAME = "locations";
        public final static String POSITION = "position";
        public final static String NAME = "name";
        public final static String ADDRESS = "address";
        public final static String LATITUDE = "latitude";
        public final static String LONGITUDE = "longitude";
        public final static String BG_COLOR = "bg_color";
        public final static String TEXT_COLOR = "text_color";
        public final static String UNIT = "unit";
    }

    public static abstract class ForecastEntry implements BaseColumns {
        public final static String TABLE_NAME = "forecast";
        public final static String TIMEZONE = "timezone";
        public final static String SUMMARY = "summary";
        public final static String ICON = "icon";
        public final static String UNITS = "units";
        public final static String TEMPERATURE = "temperature";
        public final static String APPARENT_TEMPERATURE = "apparent_temperature";
        public final static String MIN_TEMPERATURE = "min_temperature";
        public final static String MAX_TEMPERATURE = "max_temperature";
        public final static String UPDATED_AT = "updated_at";
        public final static String HUMIDITY = "humidity";
        public final static String WIND_SPEED = "wind_speed";
        public final static String PRESSURE = "pressure";
        public final static String OZONE = "ozone";
        public final static String UV_INDEX = "uv_index";
        public final static String VISIBILITY = "visibility";
        public final static String LOCATION_ID = "location_id";
        public final static String CURRENT_TIME = "current_time";
        public final static String DAILY_FORECASTS = "daily_forecasts";
    }
}
