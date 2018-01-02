package stormcast.app.phoenix.io.stormcast.views.drag_drop_list;

/**
 * Created by sudharti on 9/24/17.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemSwipeRight(int position);

    void onItemSwipeLeft(int position);
}
