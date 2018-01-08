package stormcast.app.phoenix.io.stormcast.common.local;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sudharti on 1/7/18.
 */

public class LocationForecast implements Parcelable {

    private Forecast forecast;
    private Location location;

    protected LocationForecast(Parcel in) {
        forecast = in.readParcelable(Forecast.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());
    }

    protected LocationForecast(LocationForecastBuilder locationForecastBuilder) {
        setForecast(locationForecastBuilder.forecast);
        setLocation(locationForecastBuilder.location);
    }

    public static final Creator<LocationForecast> CREATOR = new Creator<LocationForecast>() {
        @Override
        public LocationForecast createFromParcel(Parcel in) {
            return new LocationForecast(in);
        }

        @Override
        public LocationForecast[] newArray(int size) {
            return new LocationForecast[size];
        }
    };

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(forecast, i);
        parcel.writeParcelable(location, i);
    }
}
