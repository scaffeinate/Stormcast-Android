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
public class LocationsListAdapter extends RecyclerView.Adapter<LocationsListAdapter.ViewHolder> {

    private List<Location> mLocationList;
    private OnStartDragListener mOnStartDragListener;

    public LocationsListAdapter(OnStartDragListener onStartDragListener) {
        this.mOnStartDragListener = onStartDragListener;
        this.mLocationList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(position);
        holder.reorderIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mLocationList.size();
    }

    public void setLocationList(List<Location> locationList) {
        this.mLocationList = locationList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout listItemLayout;
        public final RelativeLayout deleteLocationLayout;
        public final RelativeLayout editLocationLayout;
        private final TextView nameTextView;
        private final ImageView reorderIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            listItemLayout = itemView.findViewById(R.id.list_item_layout);
            deleteLocationLayout = itemView.findViewById(R.id.delete_location_layout);
            editLocationLayout = itemView.findViewById(R.id.edit_location_layout);
            nameTextView = itemView.findViewById(R.id.location_name_text_view);
            reorderIcon = itemView.findViewById(R.id.reorder_icon);
        }

        protected void bind(int position) {
            Location location = mLocationList.get(position);
            if (location != null) {
                nameTextView.setText(location.getAddress());
            }
        }
    }
}
