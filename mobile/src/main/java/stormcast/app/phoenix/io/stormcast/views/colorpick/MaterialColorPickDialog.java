package stormcast.app.phoenix.io.stormcast.views.colorpick;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.databinding.DialogColorPickerBinding;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by sudhar on 8/22/17.
 */

public class MaterialColorPickDialog {

    private static final String THEME_LIGHT_BG_COLOR = "#FFFFFF";
    private static final String THEME_DARK_BG = "#404040";
    private static final String THEME_LIGHT_TEXT_COLOR = "#000000";
    private static final String THEME_DARK_TEXT_COLOR = "#F7F7F7";
    private static final List<ColorItem> sColorItems = new ArrayList<>();

    public static Builder with(Context context) {
        if (sColorItems.isEmpty()) {
            getColors(context);
        }

        return new Builder(context);
    }

    private static void getColors(Context context) {
        String[] colors = context.getResources().getStringArray(R.array.colors);
        for (String color : colors) {
            sColorItems.add(new ColorItem(color, false));
        }
    }

    @Retention(SOURCE)
    @IntDef({ColorPickTheme.THEME_LIGHT, ColorPickTheme.THEME_DARK})
    public @interface ColorPickTheme {
        int THEME_LIGHT = 0;
        int THEME_DARK = 1;
    }

    public interface OnColorPickedListener {
        void onClick(String colorHex);
    }

    public static class Builder implements AdapterView.OnItemClickListener {

        private Context mContext;

        private String title = null;
        private int theme = ColorPickTheme.THEME_LIGHT;

        private AlertDialog.Builder mDialogBuilder;
        private AlertDialog mAlertDialog;
        private MaterialColorGridAdapter mAdapter;

        private OnColorPickedListener mOnColorPickedListener = null;
        private List<ColorItem> mColorsList;
        private DialogColorPickerBinding mBinding;

        private int mSelectedIndex = -1;

        protected Builder(Context context) {
            this.mContext = context;
            this.mColorsList = deepCopy(sColorItems);
            this.mBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.dialog_color_picker, null, false);
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTheme(@ColorPickTheme int theme) {
            this.theme = theme;
            return this;
        }

        public Builder setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
            this.mOnColorPickedListener = onColorPickedListener;
            return this;
        }

        public Builder build() {
            this.mDialogBuilder = new AlertDialog.Builder(this.mContext);
            this.mDialogBuilder.setView(mBinding.getRoot());

            if (this.title != null) {
                mDialogBuilder.setTitle(title);
            }

            this.setupTheme();
            this.fillGrid();

            this.mAlertDialog = this.mDialogBuilder.create();
            this.mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.ColorPickerAnimation;
            return this;
        }

        public void show() {
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    this.mAlertDialog.show();
                    this.limitHeight();
                }
            }
        }

        private void fillGrid() {
            this.mAdapter = new MaterialColorGridAdapter(this.mContext, mColorsList);
            mBinding.colorsGridView.setAdapter(mAdapter);
            mBinding.colorsGridView.setOnItemClickListener(this);
        }

        private void limitHeight() {
            WindowManager windowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                if (display != null) {
                    Point size = new Point();
                    display.getSize(size);
                    this.mAlertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (0.65 * size.y));
                }
            }
        }

        private void setupTheme() {
            switch (this.theme) {
                case ColorPickTheme.THEME_LIGHT:
                    mBinding.materialColorPickLayout.setBackgroundColor(Color.parseColor(THEME_LIGHT_BG_COLOR));
                    mBinding.pickAColorTextView.setTextColor(Color.parseColor(THEME_LIGHT_TEXT_COLOR));
                    break;
                case ColorPickTheme.THEME_DARK:
                    mBinding.materialColorPickLayout.setBackgroundColor(Color.parseColor(THEME_DARK_BG));
                    mBinding.pickAColorTextView.setTextColor(Color.parseColor(THEME_DARK_TEXT_COLOR));
                    break;
            }
        }

        private List<ColorItem> deepCopy(List<ColorItem> colorItems) {
            List<ColorItem> copyList = new ArrayList<>();
            for (int i = 0; i < colorItems.size(); i++) {
                ColorItem colorItem = colorItems.get(i);
                copyList.add(new ColorItem(colorItem.getColor(), colorItem.isSelected()));
            }
            return copyList;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            if (this.mOnColorPickedListener != null) {
                ColorItem colorItem = mColorsList.get(position);
                if (colorItem != null) {
                    if (mSelectedIndex != -1) {
                        ColorItem selectedItem = mColorsList.get(mSelectedIndex);
                        selectedItem.setSelected(false);
                    }

                    colorItem.setSelected(true);
                    mSelectedIndex = position;
                    mAdapter.notifyDataSetChanged();

                    this.mAlertDialog.dismiss();
                    this.mOnColorPickedListener.onClick(colorItem.getColor());
                }
            }
        }
    }

    protected static class ColorItem {
        private String color;
        private boolean isSelected;

        public ColorItem(String color, boolean isSelected) {
            this.color = color;
            this.isSelected = isSelected;
        }

        public String getColor() {
            return color;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
