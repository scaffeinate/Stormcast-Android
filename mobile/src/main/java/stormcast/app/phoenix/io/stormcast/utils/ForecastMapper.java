package stormcast.app.phoenix.io.stormcast.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stormcast.app.phoenix.io.stormcast.common.local.DailyForecast;
import stormcast.app.phoenix.io.stormcast.common.local.DailyForecastBuilder;
import stormcast.app.phoenix.io.stormcast.common.local.Forecast;
import stormcast.app.phoenix.io.stormcast.common.local.ForecastBuilder;
import stormcast.app.phoenix.io.stormcast.common.network._Currently;
import stormcast.app.phoenix.io.stormcast.common.network._Daily;
import stormcast.app.phoenix.io.stormcast.common.network._DailyData;
import stormcast.app.phoenix.io.stormcast.common.network._Forecast;

/**
 * Created by sudharti on 1/7/18.
 */

public final class ForecastMapper {
    public static Forecast map(final _Forecast _forecast) {
        ForecastBuilder forecastBuilder = new ForecastBuilder();
        if (_forecast != null) {
            _Currently _currently = _forecast.getCurrently();
            _Daily _daily = _forecast.getDaily();
            List<_DailyData> dailyDataList = _daily.getData();
            if (_currently != null) {
                forecastBuilder.setTimezone(_forecast.getTimezone())
                        .setCurrentTime(_currently.getTime())
                        .setTemperature(_currently.getTemperature())
                        .setApparentTemperature(_currently.getApparentTemperature())
                        .setSummary(_currently.getSummary())
                        .setIcon(_currently.getIcon())
                        .setHumidity(_currently.getHumidity())
                        .setPressure(_currently.getPressure())
                        .setOzone(_currently.getOzone())
                        .setUvIndex(_currently.getUvIndex())
                        .setVisibility(_currently.getVisibility())
                        .setWindSpeed(_currently.getWindSpeed())
                        .setUpdatedAt(new Date().getTime())
                        .setUnits(_forecast.getFlags().getUnits());

                if (!dailyDataList.isEmpty()) {
                    forecastBuilder.setMinTemperature(dailyDataList.get(0).getTemperatureMin())
                            .setMaxTemperature(dailyDataList.get(0).getTemperatureMax());
                } else {
                    forecastBuilder.setMinTemperature(_currently.getTemperature())
                            .setMaxTemperature(_currently.getTemperature());
                }
            }

            List<DailyForecast> dailyForecastList = new ArrayList<>();
            int size = Math.min(4, dailyDataList.size());
            for (int i = 1; i <= size; i++) {
                _DailyData dailyData = dailyDataList.get(i);
                DailyForecast dailyForecast = new DailyForecastBuilder()
                        .setIcon(dailyData.getIcon())
                        .setTime(dailyData.getTime())
                        .setTemperature((int) ((dailyData.getTemperatureMin() + dailyData.getTemperatureMax()) / 2))
                        .build();
                dailyForecastList.add(dailyForecast);
            }

            forecastBuilder.setDailyForecastList(dailyForecastList);
        }
        
        return forecastBuilder.build();
    }
}
