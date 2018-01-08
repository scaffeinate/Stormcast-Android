package stormcast.app.phoenix.io.stormcast.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

import java.util.List;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.activities.ToolbarCallbacks;
import stormcast.app.phoenix.io.stormcast.common.local.DailyForecast;
import stormcast.app.phoenix.io.stormcast.common.local.Forecast;
import stormcast.app.phoenix.io.stormcast.common.local.Location;
import stormcast.app.phoenix.io.stormcast.common.local.LocationForecast;
import stormcast.app.phoenix.io.stormcast.databinding.FragmentForecastDetailBinding;

/**
 * Created by sudharti on 1/7/18.
 */

public class ForecastDetailFragment extends Fragment {

    private static final String LOCATION_FORECAST = "locationForecast";

    private Context mContext;
    private LocationForecast mLocationForecast;
    private FragmentForecastDetailBinding mBinding;
    private ToolbarCallbacks mToolbarCallbacks;
    private int mBackgroundColor = 0, mTextColor = 0;

    public static ForecastDetailFragment newInstance(LocationForecast locationForecast) {
        ForecastDetailFragment fragment = new ForecastDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(LOCATION_FORECAST, locationForecast);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_detail, container, false);

        mLocationForecast = getArguments().getParcelable(LOCATION_FORECAST);
        mBackgroundColor = Color.parseColor(mLocationForecast.getLocation().getBackgroundColor());
        mTextColor = Color.parseColor(mLocationForecast.getLocation().getTextColor());

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            float[] hsv = new float[3];
            Color.colorToHSV(mBackgroundColor, hsv);
            hsv[2] *= 0.8f;
            int statusBarColor = Color.HSVToColor(hsv);

            mToolbarCallbacks = (ToolbarCallbacks) getActivity();
            mToolbarCallbacks.setToolbarTitle(mLocationForecast.getLocation().getName());
            mToolbarCallbacks.setToolbarBackgroundColor(mBackgroundColor);
            mToolbarCallbacks.setToolbarTextColor(mTextColor);
            mToolbarCallbacks.setStatusBarColor(statusBarColor);
            mBinding.getRoot().setBackgroundColor(mBackgroundColor);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateView();
    }

    private void populateView() {
        Forecast forecast = mLocationForecast.getForecast();
        Location location = mLocationForecast.getLocation();

        mBinding.weatherIconView.setIconColor(mTextColor);
        mBinding.summaryTextView.setTextColor(mTextColor);
        mBinding.temperatureTextView.setTextColor(mTextColor);
        mBinding.minTemperatureTextView.setTextColor(mTextColor);
        mBinding.maxTemperatureTextView.setTextColor(mTextColor);
        mBinding.iconMinTemperature.setColorFilter(mTextColor);
        mBinding.iconMaxTemperature.setColorFilter(mTextColor);

        mBinding.weatherIconView.setIconResource(mContext.getString(forecast.getIconResource()));
        mBinding.summaryTextView.setText(forecast.getSummary());
        mBinding.temperatureTextView.setText(forecast.getFormattedTemperature());
        mBinding.minTemperatureTextView.setText(forecast.getFormattedMinTemperature());
        mBinding.maxTemperatureTextView.setText(forecast.getFormattedMaxTemperature());

        List<DailyForecast> dailyForecastList = forecast.getDailyForecastList();
        for (DailyForecast dailyForecast : dailyForecastList) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_daily, null);

            TextView timeTextView = view.findViewById(R.id.time_text_view);
            TextView temperatureTextView = view.findViewById(R.id.temperature_text_view);
            WeatherIconView dailyWeatherIconView = view.findViewById(R.id.daily_weather_icon_view);

            timeTextView.setTextColor(mTextColor);
            temperatureTextView.setTextColor(mTextColor);
            dailyWeatherIconView.setIconColor(mTextColor);

            dailyForecast.format(location.getUnit());

            timeTextView.setText(dailyForecast.getFormattedTime());
            temperatureTextView.setText(dailyForecast.getFormattedTemperature());
            dailyWeatherIconView.setIconResource(mContext.getString(dailyForecast.getIconResource()));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            view.setLayoutParams(layoutParams);

            mBinding.dailyForecastLayout.addView(view);
        }
    }
}
