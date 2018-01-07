package stormcast.app.phoenix.io.stormcast.adapters;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.common.Location;
import stormcast.app.phoenix.io.stormcast.common.LocationBuilder;

/**
 * Created by sudhar on 8/15/17.
 */
public class ForecastsAdapter extends RecyclerView.Adapter<ForecastsAdapter.ViewHolder> {

    private Cursor mCursor = null;
    private OnItemClickHandler mOnItemClickHandler;

    public ForecastsAdapter(OnItemClickHandler onItemClickHandler) {
        this.mOnItemClickHandler = onItemClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        ViewHolder viewHolder = new ViewHolder(parent, view);
        viewHolder.adjustPosterHeight(parent.getMeasuredHeight());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (this.mCursor == null) ? 0 : (this.mCursor.getCount());
    }

    public void swapCursor(Cursor cursor) {
        this.mCursor = cursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ViewGroup mParent;
        private final TextView mLocationNameTextView;
        private final TextView mTemperatureTextView;
        private final TextView mSummaryTextView;
        private final WeatherIconView mWeatherIconView;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.mParent = parent;
            this.itemView.setOnClickListener(this);

            this.mLocationNameTextView = itemView.findViewById(R.id.text_view_location_name);
            this.mTemperatureTextView = itemView.findViewById(R.id.text_view_temperature);
            this.mSummaryTextView = itemView.findViewById(R.id.text_view_summary);
            this.mWeatherIconView = itemView.findViewById(R.id.weather_icon_view);
        }

        protected void bind(int position) {
            mCursor.moveToPosition(position);
            Location location = LocationBuilder.from(mCursor).build();
            int backgroundColor = Color.parseColor(location.getBackgroundColor());
            int textColor = Color.parseColor(location.getTextColor());

            GradientDrawable drawable = (GradientDrawable) itemView.getBackground().mutate();
            drawable.setColor(backgroundColor);

            this.mLocationNameTextView.setTextColor(textColor);
            this.mTemperatureTextView.setTextColor(textColor);
            this.mSummaryTextView.setTextColor(textColor);
            this.mWeatherIconView.setIconColor(textColor);

            this.mLocationNameTextView.setText(location.getName());
            this.mTemperatureTextView.setText("27\u00b0F");
            this.mSummaryTextView.setText("Clear");

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ForecastsAdapter.this.mOnItemClickHandler.onItemClicked(mParent, view, position);
        }

        public void adjustPosterHeight(int height) {
            itemView.setMinimumHeight(height / 4);
        }
    }
}
