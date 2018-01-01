package stormcast.app.phoenix.io.stormcast.views.tab_pills;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class TabPills extends LinearLayout implements View.OnTouchListener {

    private final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 120);
    private Context mContext;
    private LayoutInflater inflater;
    private SwitchTab[] switchTabs;
    private Map<Integer, View> viewsMap = null;
    private Map<Integer, TextView> textViewsMap = null;
    private int mSelectedIndex = 0;
    private int mColorAccent = 0;

    public TabPills(Context context) {
        this(context, null);
    }

    public TabPills(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabPills(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    public void addTabs(SwitchTab[] switchTabs) {
        this.switchTabs = switchTabs;

        for (int i = 0; i < switchTabs.length; i++) {
            RelativeLayout switchTab = (RelativeLayout) inflateView(i, switchTabs[i].entry.toString());
            this.addView(switchTab);
            switchTab.setOnTouchListener(this);
            setState(i, false);
        }

        setState(mSelectedIndex, true);
    }

    public int getSelectedIndex() {
        return this.mSelectedIndex;
    }

    public Object getSelectedValue() {
        return switchTabs[mSelectedIndex].value;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        setState(mSelectedIndex, false);
        mSelectedIndex = view.getId();
        setState(mSelectedIndex, true);
        return true;
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

    public static class SwitchTab<K, V> {
        private K entry;
        private V value;

        public SwitchTab(K entry, V value) {
            this.entry = entry;
            this.value = value;
        }
    }
}
