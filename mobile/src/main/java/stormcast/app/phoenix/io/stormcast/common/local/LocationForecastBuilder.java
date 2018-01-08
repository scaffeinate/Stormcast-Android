package stormcast.app.phoenix.io.stormcast.common.local;

import android.database.Cursor;

import stormcast.app.phoenix.io.stormcast.data.PersistenceContract.LocationEntry;

/**
 * Created by sudharti on 1/7/18.
 */

public class LocationForecastBuilder {

    protected Forecast forecast;
    protected Location location;

    public LocationForecastBuilder() {

    }

    public static LocationForecastBuilder from(Cursor cursor) {
        Location location = LocationBuilder.from(cursor).build();
        Forecast forecast = ForecastBuilder.from(cursor).build();
        location.setId(cursor.getInt(cursor.getColumnIndex(LocationEntry.LOC_ID)));
        return new LocationForecastBuilder()
                .setLocation(location)
                .setForecast(forecast);
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
