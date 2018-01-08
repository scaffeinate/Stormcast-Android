package stormcast.app.phoenix.io.stormcast.common.local;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sudharti on 1/7/18.
 */

public class DailyForecast implements Parcelable {
    private String icon;
    private long time;
    private double temperature;

    protected DailyForecast(Parcel in) {
        icon = in.readString();
        time = in.readLong();
        temperature = in.readDouble();
    }

    protected DailyForecast(DailyForecastBuilder dailyForecastBuilder) {
        setIcon(dailyForecastBuilder.icon);
        setTime(dailyForecastBuilder.time);
        setTemperature(dailyForecastBuilder.temperature);
    }

    public static final Creator<DailyForecast> CREATOR = new Creator<DailyForecast>() {
        @Override
        public DailyForecast createFromParcel(Parcel in) {
            return new DailyForecast(in);
        }

        @Override
        public DailyForecast[] newArray(int size) {
            return new DailyForecast[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(icon);
        parcel.writeLong(time);
        parcel.writeDouble(temperature);
    }
}
