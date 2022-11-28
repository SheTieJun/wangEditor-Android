package com.wangeditor.android.toolbar.impl

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.wangeditor.android.RichType
import com.wangeditor.android.Utils
import com.wangeditor.android.toolbar.IRichItem
import com.wangeditor.android.toolbar.R
import com.wangeditor.android.toolbar.windows.ColorPickerWindow
import com.wangeditor.android.toolbar.windows.colorpicker.ColorPickerListener
import com.wangeditor.android.toolbar.windows.colorpicker.ColorPickerView


class RichItem_FontColor : IRichItem(), ColorPickerListener {
    private var mColorPickerView: ColorPickerView? = null
    private var mColorPickerWindow: ColorPickerWindow? = null

    init {
        mCurrentValue = "#515370"
        mDefValue = "#515370"
    }

    fun setColorPicker(colorPickerView: ColorPickerView) {
        this.mColorPickerView = colorPickerView
    }

    override fun updateSelected(isSelected: Boolean, currentValue: Any?) {
        super.updateSelected(isSelected, currentValue)
        runCatching {
            val parseColor = Color.parseColor(mCurrentValue.toString())
            setColorChecked(parseColor)
        }
    }


    override fun getType(): String {
        return RichType.FontColor.name
    }

    override fun onClick() {
        if (mColorPickerView != null) {
            toggleColorPickerView()
        } else {
            showFontColorPickerWindow()
        }
    }

    private fun toggleColorPickerView() {
        mColorPickerView?.visibility = if (mColorPickerView!!.isVisible) View.GONE else View.VISIBLE
    }

    private fun showFontColorPickerWindow() {
        if (mColorPickerWindow == null) {
            mColorPickerWindow = ColorPickerWindow(mWangEditor!!.context, this)
        }
        val yOff: Int = Utils.dp2px(-5f)
        mColorPickerWindow!!.showAsDropDown(getItemView(), 0, yOff)
        val parseColor = Color.parseColor(mCurrentValue.toString())
        mColorPickerWindow!!.setBackgroundColor(parseColor)
    }


    @SuppressLint("SetTextI18n")
    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.note_icon_fontsize)
        }
    }

    override fun onPickColor(color: Int) {
        mWangEditor!!.setTextColor(color)
    }

    private fun setColorChecked(color: Int) {
        if (mColorPickerView != null) {
            mColorPickerView!!.setColor(color)
        } else if (mColorPickerWindow != null) {
            mColorPickerWindow!!.setColor(color)
        }
    }
}