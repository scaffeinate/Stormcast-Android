package stormcast.app.phoenix.io.stormcast.adapters;

import android.database.Cursor;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.common.Location;
import stormcast.app.phoenix.io.stormcast.views.drag_drop_list.OnStartDragListener;

/**
 * Created by sudhar on 8/15/17.
 */
public class ForecastsAdapter extends RecyclerView.Adapter<LocationsListAdapter.ViewHolder> {

    private Cursor mCursor = null;
    private OnItemClickHandler mOnItemClickHandler;

    public LocationsListAdapter(OnItemClickHandler onItemClickHandler) {
        this.mOnItemClickHandler = onItemClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
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
            Location location = mLocationList.get(position);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ForecastsAdapter.this.mOnItemClickHandler.onItemClicked(mParent, view, position);
        }
    }
}
