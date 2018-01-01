package stormcast.app.phoenix.io.stormcast.views.colorpick;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import stormcast.app.phoenix.io.stormcast.R;

import static stormcast.app.phoenix.io.stormcast.views.colorpick.MaterialColorPickDialog.ColorItem;

/**
 * Created by sudharti on 8/22/17.
 */

public class MaterialColorGridAdapter extends ArrayAdapter<MaterialColorPickDialog.ColorItem> {

    private Context mContext;
    private List<MaterialColorPickDialog.ColorItem> colorItems;

    public MaterialColorGridAdapter(@NonNull Context context, List<MaterialColorPickDialog.ColorItem> colorItems) {
        super(context, 0, colorItems);
        this.colorItems = colorItems;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        ColorItem colorItem = colorItems.get(position);
        return colorItem.isSelected() ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        final ColorItem colorItem = colorItems.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);

            switch (getItemViewType(position)) {
                case 0:
                    convertView = inflater.inflate(R.layout.item_color, null);
                    break;
                case 1:
                    convertView = inflater.inflate(R.layout.item_color_selected, null);
                    break;
            }
            holder.colorImageView = convertView.findViewById(R.id.color_image_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GradientDrawable drawable = (GradientDrawable) holder.colorImageView.getBackground();
        drawable.setColor(Color.parseColor(colorItem.getColor()));

        return convertView;
    }

    static class ViewHolder {
        private ImageView colorImageView;
    }
}
