package stormcast.app.phoenix.io.stormcast.utils;

import stormcast.app.phoenix.io.stormcast.views.colorpick.MaterialColorPickDialog;

/**
 * Created by sudharti on 8/18/17.
 */

public class ColorPickerHelper {

    public static void showColorPicker(MaterialColorPickDialog.Builder builder, String title, final ColorPickerCallback callback) {
        builder.setTitle(title)
                .setTheme(MaterialColorPickDialog.ColorPickTheme.THEME_LIGHT)
                .build()
                .setOnColorPickedListener(new MaterialColorPickDialog.OnColorPickedListener() {
                    @Override
                    public void onClick(String colorHex) {
                        callback.onColorSelected(colorHex);
                    }
                }).show();
    }

    public interface ColorPickerCallback {
        void onColorSelected(String colorHex);
    }
}
