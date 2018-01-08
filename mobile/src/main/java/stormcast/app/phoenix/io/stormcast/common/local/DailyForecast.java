package stormcast.app.phoenix.io.stormcast.common.local;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sudharti on 1/7/18.
 */

public class DailyForecast implements Parcelable {
    private String icon;
    private int time, updatedAt;
    private double temperature;

    protected DailyForecast(Parcel in) {
        icon = in.readString();
        time = in.readInt();
        updatedAt = in.readInt();
        temperature = in.readDouble();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(icon);
        parcel.writeInt(time);
        parcel.writeInt(updatedAt);
        parcel.writeDouble(temperature);
    }
}
