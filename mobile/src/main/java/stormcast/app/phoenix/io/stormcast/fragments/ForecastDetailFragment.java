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

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.activities.ToolbarCallbacks;
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

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            int backgroundColor = Color.parseColor(mLocationForecast.getLocation().getBackgroundColor());
            int textColor = Color.parseColor(mLocationForecast.getLocation().getTextColor());
            float[] hsv = new float[3];
            Color.colorToHSV(backgroundColor, hsv);
            hsv[2] *= 0.8f;
            int statusBarColor = Color.HSVToColor(hsv);

            mToolbarCallbacks = (ToolbarCallbacks) getActivity();
            mToolbarCallbacks.setToolbarTitle(mLocationForecast.getLocation().getName());
            mToolbarCallbacks.setToolbarBackgroundColor(backgroundColor);
            mToolbarCallbacks.setToolbarTextColor(textColor);
            mToolbarCallbacks.setStatusBarColor(statusBarColor);
            mBinding.getRoot().setBackgroundColor(backgroundColor);
        }
    }
}
