package stormcast.app.phoenix.io.stormcast.views.drag_drop_list;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import stormcast.app.phoenix.io.stormcast.adapters.LocationsListAdapter;

/**
 * Created by sudharti on 9/24/17.
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mAdapter;

    public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.END | ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.END) {
            this.mAdapter.onItemSwipeRight(viewHolder.getAdapterPosition());
        } else if (direction == ItemTouchHelper.START) {
            this.mAdapter.onItemSwipeLeft(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            LocationsListAdapter.ViewHolder holder = ((LocationsListAdapter.ViewHolder) viewHolder);
            if (dX > 0) {
                holder.deleteLocationLayout.setVisibility(View.VISIBLE);
                holder.editLocationLayout.setVisibility(View.GONE);
            } else {
                holder.deleteLocationLayout.setVisibility(View.GONE);
                holder.editLocationLayout.setVisibility(View.VISIBLE);
            }
            holder.listItemLayout.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        LocationsListAdapter.ViewHolder holder = ((LocationsListAdapter.ViewHolder) viewHolder);
        holder.listItemLayout.setX(0);
    }
}
