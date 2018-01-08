package stormcast.app.phoenix.io.stormcast.common.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class _Forecast {

    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("offset")
    @Expose
    private double offset;
    @SerializedName("currently")
    @Expose
    private _Currently currently;
    @SerializedName("minutely")
    @Expose
    private _Minutely minutely;
    @SerializedName("hourly")
    @Expose
    private _Hourly hourly;
    @SerializedName("daily")
    @Expose
    private _Daily daily;
    @SerializedName("flags")
    @Expose
    private _Flags flags;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public _Currently getCurrently() {
        return currently;
    }

    public void setCurrently(_Currently currently) {
        this.currently = currently;
    }

    public _Minutely getMinutely() {
        return minutely;
    }

    public void setMinutely(_Minutely minutely) {
        this.minutely = minutely;
    }

    public _Hourly getHourly() {
        return hourly;
    }

    public void setHourly(_Hourly hourly) {
        this.hourly = hourly;
    }

    public _Daily getDaily() {
        return daily;
    }

    public void setDaily(_Daily daily) {
        this.daily = daily;
    }

    public _Flags getFlags() {
        return flags;
    }

    public void setFlags(_Flags flags) {
        this.flags = flags;
    }
}
