package stormcast.app.phoenix.io.stormcast.common.local;

/**
 * Created by sudharti on 1/7/18.
 */

public class DailyForecastBuilder {
    protected String icon;
    protected long time;
    protected double temperature;

    public DailyForecastBuilder setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public DailyForecastBuilder setTime(long time) {
        this.time = time;
        return this;
    }

    public DailyForecastBuilder setTemperature(double temperature) {
        this.temperature = temperature;
        return this;
    }

    public DailyForecast build() {
        return new DailyForecast(this);
    }
}
