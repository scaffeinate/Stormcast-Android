package stormcast.app.phoenix.io.stormcast.common.local;

import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static stormcast.app.phoenix.io.stormcast.data.PersistenceContract.ForecastEntry;

/**
 * Created by sudharti on 8/25/17.
 */

public class ForecastBuilder {
    protected String timezone, summary, icon, units;
    protected int id, locationId, uvIndex;
    protected double temperature, apparentTemperature,
            humidity, windSpeed, pressure, ozone, visibility, minTemperature, maxTemperature;
    protected long updatedAt, currentTime;
    protected List<DailyForecast> dailyForecastList;
    protected static final Gson gson = new Gson();

    public ForecastBuilder() {

    }

    public static ForecastBuilder from(Cursor cursor) {
        String dailyForecastModelsJson = cursor.getString(cursor.getColumnIndex(ForecastEntry.DAILY_FORECASTS));
        List<DailyForecast> dailyForecastList = gson.fromJson(dailyForecastModelsJson,
                new TypeToken<List<DailyForecast>>() {
                }.getType());
        return new ForecastBuilder()
                .setId(cursor.getInt(cursor.getColumnIndex(ForecastEntry._ID)))
                .setTimezone(cursor.getString(cursor.getColumnIndex(ForecastEntry.TIMEZONE)))
                .setCurrentTime(cursor.getLong(cursor.getColumnIndex(ForecastEntry.CURRENT_TIME)))
                .setSummary(cursor.getString(cursor.getColumnIndex(ForecastEntry.SUMMARY)))
                .setIcon(cursor.getString(cursor.getColumnIndex(ForecastEntry.ICON)))
                .setTemperature(cursor.getDouble(cursor.getColumnIndex(ForecastEntry.TEMPERATURE)))
                .setApparentTemperature(cursor.getDouble(cursor.getColumnIndex(ForecastEntry.APPARENT_TEMPERATURE)))
                .setMinTemperature(cursor.getDouble(cursor.getColumnIndex(ForecastEntry.MIN_TEMPERATURE)))
                .setMaxTemperature(cursor.getDouble(cursor.getColumnIndex(ForecastEntry.MAX_TEMPERATURE)))
                .setHumidity(cursor.getDouble(cursor.getColumnIndex(ForecastEntry.HUMIDITY)))
                .setWindSpeed(cursor.getDouble(cursor.getColumnIndex(ForecastEntry.WIND_SPEED)))
                .setPressure(cursor.getDouble(cursor.getColumnIndex(ForecastEntry.PRESSURE)))
                .setVisibility(cursor.getDouble(cursor.getColumnIndex(ForecastEntry.VISIBILITY)))
                .setOzone(cursor.getDouble(cursor.getColumnIndex(ForecastEntry.OZONE)))
                .setUvIndex(cursor.getInt(cursor.getColumnIndex(ForecastEntry.UV_INDEX)))
                .setUpdatedAt(cursor.getLong(cursor.getColumnIndex(ForecastEntry.UPDATED_AT)))
                .setUnits(cursor.getString(cursor.getColumnIndex(ForecastEntry.UNITS)))
                .setLocationId(cursor.getInt(cursor.getColumnIndex(ForecastEntry.LOCATION_ID)))
                .setDailyForecastList(dailyForecastList);
    }

    public ForecastBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public ForecastBuilder setTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public ForecastBuilder setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public ForecastBuilder setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public ForecastBuilder setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public ForecastBuilder setTemperature(double temperature) {
        this.temperature = temperature;
        return this;
    }

    public ForecastBuilder setApparentTemperature(double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
        return this;
    }

    public ForecastBuilder setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
        return this;
    }

    public ForecastBuilder setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
        return this;
    }

    public ForecastBuilder setHumidity(double humidity) {
        this.humidity = humidity;
        return this;
    }

    public ForecastBuilder setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
        return this;
    }

    public ForecastBuilder setPressure(double pressure) {
        this.pressure = pressure;
        return this;
    }

    public ForecastBuilder setOzone(double ozone) {
        this.ozone = ozone;
        return this;
    }

    public ForecastBuilder setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
        return this;
    }

    public ForecastBuilder setVisibility(double visibility) {
        this.visibility = visibility;
        return this;
    }

    public ForecastBuilder setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public ForecastBuilder setUnits(String units) {
        this.units = units;
        return this;
    }

    public ForecastBuilder setLocationId(int locationId) {
        this.locationId = locationId;
        return this;
    }

    public ForecastBuilder setDailyForecastList(List<DailyForecast> dailyForecastList) {
        this.dailyForecastList = dailyForecastList;
        return this;
    }

    public Forecast build() {
        return new Forecast(this);
    }
}
