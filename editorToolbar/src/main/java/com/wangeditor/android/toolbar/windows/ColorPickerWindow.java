package com.wangeditor.android.toolbar.windows;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import com.wangeditor.android.Utils;
import com.wangeditor.android.toolbar.R;
import com.wangeditor.android.toolbar.windows.colorpicker.ColorPickerListener;
import com.wangeditor.android.toolbar.windows.colorpicker.ColorPickerView;

public class ColorPickerWindow extends PopupWindow {

    private Context mContext;

    private ColorPickerView colorPickerView;

    private ColorPickerListener mColorPickerListener;

    public ColorPickerWindow(Context context, ColorPickerListener colorPickerListener) {
        mContext = context;
        mColorPickerListener = colorPickerListener;
        this.colorPickerView = inflateContentView();
        this.setContentView(this.colorPickerView);
        int[] wh = Utils.getScreenWidthAndHeight(context);
        this.setWidth(wh[0]);
        int h = Utils.dp2px(50);
        this.setHeight(h);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setOutsideTouchable(true);
        this.setFocusable(false);
        this.setupListeners();
    }

    private ColorPickerView inflateContentView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ColorPickerView colorPickerView = (ColorPickerView) layoutInflater.inflate(R.layout.win_color_picker, null);
        return colorPickerView;
    }

    private <T extends View> T findViewById(int id) {
        return colorPickerView.findViewById(id);
    }

    public void setColor(int color) {
        this.colorPickerView.setColor(color);
    }

    private void setupListeners() {
        this.colorPickerView.setColorPickerListener(mColorPickerListener);
    }

    public void setBackgroundColor(int backgroundColor) {
        this.colorPickerView.setBackgroundColor(backgroundColor);
    }
}
