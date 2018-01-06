package stormcast.app.phoenix.io.stormcast.adapters;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return new ViewHolder(parent, view);
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
        
        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.mParent = parent;
            this.itemView.setOnClickListener(this);
        }

        protected void bind(int position) {
            mCursor.moveToPosition(position);
            Location location = LocationBuilder.from(mCursor).build();
            this.itemView.setBackgroundColor(Color.parseColor(location.getBackgroundColor()));
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ForecastsAdapter.this.mOnItemClickHandler.onItemClicked(mParent, view, position);
        }
    }
}
