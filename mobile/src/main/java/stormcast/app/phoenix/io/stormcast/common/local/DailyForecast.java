package stormcast.app.phoenix.io.stormcast.common.local;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

import stormcast.app.phoenix.io.stormcast.utils.FormatUtils;
import stormcast.app.phoenix.io.stormcast.utils.IconResource;

/**
 * Created by sudharti on 1/7/18.
 */

public class DailyForecast implements Parcelable {
    private String icon;
    private long time;
    private double temperature;

    private String formattedTime, formattedTemperature;
    private int iconResource;

    protected DailyForecast(Parcel in) {
        icon = in.readString();
        time = in.readLong();
        temperature = in.readDouble();
        setFormattedTime(in.readString());
        setFormattedTemperature(in.readString());
        setIconResource(in.readInt());
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

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getFormattedTemperature() {
        return formattedTemperature;
    }

    public void setFormattedTemperature(String formattedTemperature) {
        this.formattedTemperature = formattedTemperature;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
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
        parcel.writeString(formattedTime);
        parcel.writeString(formattedTemperature);
        parcel.writeInt(iconResource);
    }

    public void format(String unitType) {
        Unit unit = new Unit(unitType);
        String[] daysOfWeek = new String[]{
                "SUN", "MON", "TUE", "WED", "THUR", "FRI", "SAT"
        };
        int temp = (int) this.getTemperature();
        String icon = this.getIcon();
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(this.getTime() * 1000);
        int dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK);

        setFormattedTemperature(FormatUtils.formatTemperature(temp, unit));
        setIconResource(IconResource.getIconResource(icon, true));
        setFormattedTime(daysOfWeek[dayOfWeek - 1]);
    }
}
