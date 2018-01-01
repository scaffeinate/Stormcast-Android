package stormcast.app.phoenix.io.stormcast.views.tab_switch;

import android.content.Context;
import android.databinding.DataBindingUtil;
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
import stormcast.app.phoenix.io.stormcast.databinding.LayoutSwitchTabBinding;
import stormcast.app.phoenix.io.stormcast.databinding.LayoutSwitchTabSelectorBinding;

/**
 * Created by sudharti on 9/24/17.
 */

public class SwitchTabSelector extends LinearLayout implements View.OnTouchListener {

    private final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 120);
    private Context mContext;
    private LayoutInflater inflater;
    private SwitchTab[] switchTabs;
    private Map<Integer, View> viewsMap = null;
    private Map<Integer, TextView> textViewsMap = null;
    private int mSelectedIndex = 0;
    private int mColorAccent = 0;

    private LayoutSwitchTabBinding mBinding;
    private LayoutSwitchTabSelectorBinding mSelectorBinding;

    public SwitchTabSelector(Context context) {
        this(context, null);
    }

    public SwitchTabSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchTabSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.viewsMap = new HashMap<>();
        this.textViewsMap = new HashMap<>();
        layoutParams.weight = 1;

        mColorAccent = ContextCompat.getColor(mContext, R.color.colorAccent);

        inflater = LayoutInflater.from(context);

        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_switch_tab, null, false);
        mSelectorBinding = DataBindingUtil.inflate(inflater, R.layout.layout_switch_tab_selector, null, false);

        setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_rounded_corners_border_accent));
    }

    public void addTabs(SwitchTab[] switchTabs) {
        this.switchTabs = switchTabs;

        for (int i = 0; i < switchTabs.length; i++) {
            RelativeLayout switchTab = inflateView(i, switchTabs[i].entry.toString());
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

    private RelativeLayout inflateView(int index, String entryText) {
        RelativeLayout switchTab = (RelativeLayout) inflater.inflate(R.layout.layout_switch_tab, null);
        switchTab.setLayoutParams(layoutParams);

        TextView textView = switchTab.findViewById(R.id.switch_tab_text);
        textView.setText(entryText);
        textView.setTextColor(mColorAccent);

        int res = ((index == 0) ? R.drawable.shape_switch_tab_btn_left_rounded :
                ((index == switchTabs.length - 1) ? R.drawable.shape_switch_tab_btn_right_rounded : R.drawable.shape_switch_tab_btn));
        switchTab.setBackground(ContextCompat.getDrawable(mContext, res));

        this.viewsMap.put(index, switchTab);
        this.textViewsMap.put(index, textView);

        switchTab.setId(index);
        return switchTab;
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
