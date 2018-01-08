package stormcast.app.phoenix.io.stormcast.common.local;

import android.database.Cursor;

/**
 * Created by sudharti on 1/7/18.
 */

public class LocationForecastBuilder {

    protected Forecast forecast;
    protected Location location;

    public LocationForecastBuilder() {

    }

    public static LocationForecastBuilder from(Cursor cursor) {
        return new LocationForecastBuilder()
                .setForecast(ForecastBuilder.from(cursor).build())
                .setLocation(LocationBuilder.from(cursor).build());
    }

    public LocationForecastBuilder setForecast(Forecast forecast) {
        this.forecast = forecast;
        return this;
    }

    public LocationForecastBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    public LocationForecast build() {
        LocationForecast locationForecast = new LocationForecast(this);
        return locationForecast;
    }
}
