package stormcast.app.phoenix.io.stormcast.views.tab_pills;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import stormcast.app.phoenix.io.stormcast.R;

/**
 * Created by sudharti on 9/24/17.
 */

public class TabPillsSelector extends LinearLayout {

    private final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 120);
    private Context mContext;
    private LayoutInflater inflater;
    private TabPill[] switchTabs;
    private Map<Integer, View> viewsMap = null;
    private Map<Integer, TextView> textViewsMap = null;
    private OnTabClickListener mOnTabClickListener;

    private int mSelectedIndex = 0;
    private int mColorAccent = 0;

    public TabPillsSelector(Context context) {
        this(context, null);
    }

    public TabPillsSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabPillsSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.viewsMap = new HashMap<>();
        this.textViewsMap = new HashMap<>();
        layoutParams.weight = 1;

        mColorAccent = ContextCompat.getColor(mContext, R.color.colorAccent);

        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_tab_pills, null);
        setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_rounded_corners_border_accent));
    }

    public void addTabs(TabPill[] switchTabs) {
        this.switchTabs = switchTabs;

        for (int i = 0; i < switchTabs.length; i++) {
            RelativeLayout switchTab = (RelativeLayout) inflateView(i, switchTabs[i].entry.toString());
            this.addView(switchTab);
            switchTab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setState(mSelectedIndex, false);
                    mSelectedIndex = view.getId();
                    setState(mSelectedIndex, true);

                    if (mOnTabClickListener != null) {
                        mOnTabClickListener.onTabClick(mSelectedIndex);
                    }
                }
            });
            setState(i, false);
        }

        setState(mSelectedIndex, true);
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.mOnTabClickListener = onTabClickListener;
    }

    public void setSelectedIndex(int index) {
        setState(mSelectedIndex, false);
        mSelectedIndex = index;
        setState(index, true);
    }

    public int getSelectedIndex() {
        return this.mSelectedIndex;
    }

    public Object getValueAt(int index) {
        return switchTabs[index].getValue();
    }

    private View inflateView(int index, String entryText) {
        RelativeLayout tabSwitch = (RelativeLayout) inflater.inflate(R.layout.item_tab_pill, null);
        tabSwitch.setLayoutParams(layoutParams);

        TextView textView = tabSwitch.findViewById(R.id.tab_pill_text_view);
        textView.setText(entryText);
        textView.setTextColor(mColorAccent);

        int res = ((index == 0) ? R.drawable.shape_switch_tab_btn_left_rounded :
                ((index == switchTabs.length - 1) ? R.drawable.shape_switch_tab_btn_right_rounded : R.drawable.shape_switch_tab_btn));
        tabSwitch.setBackground(ContextCompat.getDrawable(mContext, res));

        this.viewsMap.put(index, tabSwitch);
        this.textViewsMap.put(index, textView);

        tabSwitch.setId(index);
        return tabSwitch;
    }

    private void setState(int index, boolean selected) {
        View view = viewsMap.get(index);
        TextView textView = textViewsMap.get(index);
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        if (selected) {
            drawable.setColor(mColorAccent);
            textView.setTextColor(Color.WHITE);
        } else {
            drawable.setColor(Color.TRANSPARENT);
            textView.setTextColor(mColorAccent);
        }
    }

    public static class TabPill<K, V> {
        private K entry;
        private V value;

        public TabPill(K entry, V value) {
            this.entry = entry;
            this.value = value;
        }

        public V getValue() {
            return value;
        }
    }

    public interface OnTabClickListener {
        void onTabClick(int index);
    }
}
