package com.wangeditor.android.toolbar.windows;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.google.android.material.slider.Slider;
import com.wangeditor.android.Utils;
import com.wangeditor.android.toolbar.R;

public class FontSizePickerWindow extends PopupWindow {

    private static final int FONT_SIZE_BASE = 12;

    private Context mContext;

    private View mRootView;

    private TextView mPreview;

    private Slider mSeekbar;

    private FontSizeChangeListener mListener;

    public FontSizePickerWindow(Context context, FontSizeChangeListener fontSizeChangeListener) {
        mContext = context;
        mListener = fontSizeChangeListener;
        this.mRootView = inflateContentView();
        this.setContentView(mRootView);
        int[] wh = Utils.getScreenWidthAndHeight(context);
        this.setWidth(wh[0]);
        int h = Utils.dp2px(100);
        this.setHeight(h);
        this.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        this.setOutsideTouchable(true);
        this.setFocusable(false);
        this.initView();
        this.setupListeners();
    }

    private View inflateContentView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.win_fontsize_picker, null);
        return view;
    }

    private <T extends View> T findViewById(int id) {
        return mRootView.findViewById(id);
    }

    public void setFontSize(int size) {
        mSeekbar.setValue(size);
    }

    private void initView() {
        this.mPreview = findViewById(R.id.are_fontsize_preview);
        this.mSeekbar = findViewById(R.id.are_fontsize_seekbar);
    }

    private void setupListeners() {
        mSeekbar.addOnChangeListener((slider, value, fromUser) -> {
            changePreviewText((int) value);
        });
    }

    private void changePreviewText(int progress) {
        mPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
        mPreview.setText(progress + "sp: Preview");
        if (mListener != null) {
            mListener.onFontSizeChange(progress);
        }
    }

}
