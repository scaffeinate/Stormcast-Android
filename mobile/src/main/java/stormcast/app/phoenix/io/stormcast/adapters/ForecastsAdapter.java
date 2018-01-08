package stormcast.app.phoenix.io.stormcast.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

import java.util.ArrayList;
import java.util.List;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.common.local.Forecast;
import stormcast.app.phoenix.io.stormcast.common.local.Location;
import stormcast.app.phoenix.io.stormcast.common.local.LocationForecast;

/**
 * Created by sudhar on 8/15/17.
 */
public class ForecastsAdapter extends RecyclerView.Adapter<ForecastsAdapter.ViewHolder> {

    private Context mContext;
    private List<LocationForecast> mLocationForecastList = null;
    private OnItemClickHandler mOnItemClickHandler;

    public ForecastsAdapter(Context context, OnItemClickHandler onItemClickHandler) {
        this.mContext = context;
        this.mLocationForecastList = new ArrayList<>();
        this.mOnItemClickHandler = onItemClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        ViewHolder viewHolder = new ViewHolder(parent, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.mLocationForecastList.size();
    }

    public void setLocationForecastList(List<LocationForecast> locationList) {
        this.mLocationForecastList = locationList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ViewGroup mParent;
        private final TextView mLocationNameTextView;
        private final TextView mTemperatureTextView;
        private final TextView mSummaryTextView;
        private final TextView mMinTemperatureTextView;
        private final TextView mMaxTemperatureTextView;
        private final WeatherIconView mWeatherIconView;
        private final ImageView mIconMinTemperatureImageView;
        private final ImageView mIconMaxTemperatureImageView;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.mParent = parent;
            this.itemView.setOnClickListener(this);

            this.mLocationNameTextView = itemView.findViewById(R.id.text_view_location_name);
            this.mTemperatureTextView = itemView.findViewById(R.id.text_view_temperature);
            this.mMinTemperatureTextView = itemView.findViewById(R.id.text_view_min_temperature);
            this.mMaxTemperatureTextView = itemView.findViewById(R.id.text_view_max_temperature);
            this.mSummaryTextView = itemView.findViewById(R.id.text_view_summary);
            this.mWeatherIconView = itemView.findViewById(R.id.weather_icon_view);
            this.mIconMinTemperatureImageView = itemView.findViewById(R.id.icon_min_temperature);
            this.mIconMaxTemperatureImageView = itemView.findViewById(R.id.icon_max_temperature);
        }

        protected void bind(int position) {
            LocationForecast locationForecast = mLocationForecastList.get(position);

            Location location = locationForecast.getLocation();
            Forecast forecast = locationForecast.getForecast();
            if (location != null) {
                int backgroundColor = Color.parseColor(location.getBackgroundColor());
                int textColor = Color.parseColor(location.getTextColor());

                GradientDrawable drawable = (GradientDrawable) itemView.getBackground().mutate();
                drawable.setColor(backgroundColor);

                this.mLocationNameTextView.setTextColor(textColor);
                this.mTemperatureTextView.setTextColor(textColor);
                this.mSummaryTextView.setTextColor(textColor);
                this.mMinTemperatureTextView.setTextColor(textColor);
                this.mMaxTemperatureTextView.setTextColor(textColor);
                this.mIconMinTemperatureImageView.setColorFilter(textColor);
                this.mIconMaxTemperatureImageView.setColorFilter(textColor);
                this.mWeatherIconView.setIconColor(textColor);

                this.mLocationNameTextView.setText(location.getName());

                if (forecast != null) {
                    forecast.format(location.getUnit());
                    this.mTemperatureTextView.setText(forecast.getFormattedTemperature());
                    this.mMinTemperatureTextView.setText(forecast.getFormattedMinTemperature());
                    this.mMaxTemperatureTextView.setText(forecast.getFormattedMaxTemperature());
                    this.mSummaryTextView.setText(forecast.getSummary());
                    this.mWeatherIconView.setIconResource(mContext.getResources().getString(forecast.getIconResource()));
                }
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ForecastsAdapter.this.mOnItemClickHandler.onItemClicked(mParent, view, position);
        }
    }
}
