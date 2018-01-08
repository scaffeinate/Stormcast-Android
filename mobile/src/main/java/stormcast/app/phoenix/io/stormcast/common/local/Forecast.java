package stormcast.app.phoenix.io.stormcast.common.local;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

import stormcast.app.phoenix.io.stormcast.common.DBMappable;
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract;
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract.ForecastEntry;
import stormcast.app.phoenix.io.stormcast.utils.FormatUtils;
import stormcast.app.phoenix.io.stormcast.utils.IconResource;

import static stormcast.app.phoenix.io.stormcast.common.local.Unit.DU;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.MILE;
import static stormcast.app.phoenix.io.stormcast.common.local.Unit.PERCENT;

/**
 * Created by sudharti on 8/24/17.
 */

public class Forecast implements Parcelable, DBMappable {

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel in) {
            return new Forecast(in);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };
    private String timezone, summary, icon, units;
    private int id = 0, locationId, uvIndex;
    private double temperature, apparentTemperature, minTemperature, maxTemperature;
    private double humidity, ozone, windSpeed, visibility, pressure;
    private long currentTime, updatedAt;
    private List<DailyForecast> dailyForecastList;
    private final Gson gson = new Gson();

    private String formattedTemperature, formattedApparentTemperature,
            formattedMinTemperature, formattedMaxTemperature,
            formattedWindSpeed, formattedHumidity, formattedPressure,
            formattedOzone, formattedVisibility, formattedUVIndex;
    private int iconResource;

    protected Forecast(Parcel in) {
        setId(in.readInt());
        setTimezone(in.readString());
        setCurrentTime(in.readLong());
        setSummary(in.readString());
        setIcon(in.readString());
        setTemperature(in.readDouble());
        setApparentTemperature(in.readDouble());
        setMinTemperature(in.readDouble());
        setMaxTemperature(in.readDouble());
        setHumidity(in.readDouble());
        setWindSpeed(in.readDouble());
        setPressure(in.readDouble());
        setVisibility(in.readDouble());
        setOzone(in.readDouble());
        setUvIndex(in.readInt());
        setUpdatedAt(in.readLong());
        setUnits(in.readString());
        setLocationId(in.readInt());
        in.readTypedList(this.dailyForecastList, DailyForecast.CREATOR);
        setFormattedTemperature(in.readString());
        setFormattedApparentTemperature(in.readString());
        setFormattedMinTemperature(in.readString());
        setFormattedMaxTemperature(in.readString());
        setFormattedWindSpeed(in.readString());
        setFormattedHumidity(in.readString());
        setFormattedPressure(in.readString());
        setFormattedOzone(in.readString());
        setFormattedUVIndex(in.readString());
        setFormattedVisibility(in.readString());
        setIconResource(in.readInt());
    }

    protected Forecast(ForecastBuilder builder) {
        setId(builder.id);
        setTimezone(builder.timezone);
        setCurrentTime(builder.currentTime);
        setSummary(builder.summary);
        setIcon(builder.icon);
        setTemperature(builder.temperature);
        setApparentTemperature(builder.apparentTemperature);
        setMinTemperature(builder.minTemperature);
        setMaxTemperature(builder.maxTemperature);
        setHumidity(builder.humidity);
        setWindSpeed(builder.windSpeed);
        setPressure(builder.pressure);
        setVisibility(builder.visibility);
        setOzone(builder.ozone);
        setUvIndex(builder.uvIndex);
        setUpdatedAt(builder.updatedAt);
        setUnits(builder.units);
        setLocationId(builder.locationId);
        setDailyForecastList(builder.dailyForecastList);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(Double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }

    public double getOzone() {
        return ozone;
    }

    public void setOzone(double ozone) {
        this.ozone = ozone;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }


    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public List<DailyForecast> getDailyForecastList() {
        return dailyForecastList;
    }

    public void setDailyForecastList(List<DailyForecast> dailyForecastList) {
        this.dailyForecastList = dailyForecastList;
    }

    public String getFormattedTemperature() {
        return formattedTemperature;
    }

    public void setFormattedTemperature(String formattedTemperature) {
        this.formattedTemperature = formattedTemperature;
    }

    public String getFormattedApparentTemperature() {
        return formattedApparentTemperature;
    }

    public void setFormattedApparentTemperature(String formattedApparentTemperature) {
        this.formattedApparentTemperature = formattedApparentTemperature;
    }

    public String getFormattedMinTemperature() {
        return formattedMinTemperature;
    }

    public void setFormattedMinTemperature(String formattedMinTemperature) {
        this.formattedMinTemperature = formattedMinTemperature;
    }

    public String getFormattedMaxTemperature() {
        return formattedMaxTemperature;
    }

    public void setFormattedMaxTemperature(String formattedMaxTemperature) {
        this.formattedMaxTemperature = formattedMaxTemperature;
    }

    public String getFormattedWindSpeed() {
        return formattedWindSpeed;
    }

    public void setFormattedWindSpeed(String formattedWindSpeed) {
        this.formattedWindSpeed = formattedWindSpeed;
    }

    public String getFormattedHumidity() {
        return formattedHumidity;
    }

    public void setFormattedHumidity(String formattedHumidity) {
        this.formattedHumidity = formattedHumidity;
    }

    public String getFormattedPressure() {
        return formattedPressure;
    }

    public void setFormattedPressure(String formattedPressure) {
        this.formattedPressure = formattedPressure;
    }

    public String getFormattedOzone() {
        return formattedOzone;
    }

    public void setFormattedOzone(String formattedOzone) {
        this.formattedOzone = formattedOzone;
    }

    public String getFormattedVisibility() {
        return formattedVisibility;
    }

    public void setFormattedVisibility(String formattedVisibility) {
        this.formattedVisibility = formattedVisibility;
    }

    public String getFormattedUVIndex() {
        return formattedUVIndex;
    }

    public void setFormattedUVIndex(String formattedUVIndex) {
        this.formattedUVIndex = formattedUVIndex;
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
        parcel.writeInt(this.id);
        parcel.writeString(this.timezone);
        parcel.writeLong(this.currentTime);
        parcel.writeString(this.summary);
        parcel.writeString(this.icon);
        parcel.writeDouble(this.temperature);
        parcel.writeDouble(this.apparentTemperature);
        parcel.writeDouble(this.minTemperature);
        parcel.writeDouble(this.maxTemperature);
        parcel.writeDouble(this.humidity);
        parcel.writeDouble(this.windSpeed);
        parcel.writeDouble(this.pressure);
        parcel.writeDouble(this.visibility);
        parcel.writeDouble(this.ozone);
        parcel.writeInt(this.uvIndex);
        parcel.writeLong(this.updatedAt);
        parcel.writeString(this.units);
        parcel.writeInt(this.locationId);
        parcel.writeTypedList(this.dailyForecastList);
        parcel.writeString(this.formattedTemperature);
        parcel.writeString(this.formattedApparentTemperature);
        parcel.writeString(this.formattedMinTemperature);
        parcel.writeString(this.formattedMaxTemperature);
        parcel.writeString(this.formattedWindSpeed);
        parcel.writeString(this.formattedHumidity);
        parcel.writeString(this.formattedPressure);
        parcel.writeString(this.formattedOzone);
        parcel.writeString(this.formattedUVIndex);
        parcel.writeString(this.formattedVisibility);
        parcel.writeInt(this.iconResource);
    }

    public void format(String unitType) {
        Unit unit = new Unit(unitType);

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(this.getCurrentTime() * 1000);
        int hourOfDay = currentTime.get(Calendar.HOUR_OF_DAY);
        boolean isDay = (hourOfDay > 7 && hourOfDay < 20) ? true : false;
        String icon = this.getIcon();
        setFormattedTemperature(FormatUtils.formatTemperature(this.getTemperature(), unit));
        setFormattedApparentTemperature(FormatUtils.formatTemperature(this.getApparentTemperature(), unit));
        setFormattedMinTemperature(FormatUtils.formatTemperature(this.getMinTemperature(), unit));
        setFormattedMaxTemperature(FormatUtils.formatTemperature(this.getMaxTemperature(), unit));
        setFormattedWindSpeed(FormatUtils.formatSpeed(this.getWindSpeed(), unit));
        setFormattedHumidity(FormatUtils.formatUnit(this.getHumidity(), PERCENT));
        setFormattedPressure(FormatUtils.formatPressure(this.getPressure(), unit));
        setFormattedOzone(FormatUtils.formatUnit(this.getOzone(), DU));
        setFormattedUVIndex(String.valueOf(this.getUvIndex()));
        setFormattedVisibility(FormatUtils.formatUnit(this.getVisibility(), MILE));
        setIconResource(IconResource.getIconResource(icon, isDay));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(ForecastEntry.TEMPERATURE, this.getTemperature());
        cv.put(ForecastEntry.APPARENT_TEMPERATURE, this.getApparentTemperature());
        cv.put(ForecastEntry.MIN_TEMPERATURE, this.getMinTemperature());
        cv.put(ForecastEntry.MAX_TEMPERATURE, this.getMaxTemperature());
        cv.put(ForecastEntry.HUMIDITY, this.getHumidity());
        cv.put(ForecastEntry.ICON, this.getIcon());
        cv.put(ForecastEntry.PRESSURE, this.getPressure());
        cv.put(ForecastEntry.OZONE, this.getOzone());
        cv.put(ForecastEntry.UV_INDEX, this.getUvIndex());
        cv.put(ForecastEntry.SUMMARY, this.getSummary());
        cv.put(ForecastEntry.WIND_SPEED, this.getWindSpeed());
        cv.put(ForecastEntry.VISIBILITY, this.getVisibility());
        cv.put(ForecastEntry.UNITS, this.getUnits());
        cv.put(ForecastEntry.UPDATED_AT, this.getUpdatedAt());
        cv.put(ForecastEntry.TIMEZONE, this.getTimezone());
        cv.put(ForecastEntry.LOCATION_ID, this.getLocationId());
        cv.put(ForecastEntry.CURRENT_TIME, this.getCurrentTime());
        cv.put(ForecastEntry.DAILY_FORECASTS, gson.toJson(this.getDailyForecastList()));
        return cv;
    }
}
