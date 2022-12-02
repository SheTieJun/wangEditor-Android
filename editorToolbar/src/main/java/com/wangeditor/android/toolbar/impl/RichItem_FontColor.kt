/*
 * MIT License
 *
 * Copyright (c) 2022 SheTieJun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
import com.wangeditor.android.toolbar.getColorInt
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
            val colorString = mCurrentValue.toString().replace(" ","").trim()
            val parseColor = getColorInt(colorString,mDefValue)
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
        val colorString = mCurrentValue.toString().replace(" ","").trim()
        val parseColor = getColorInt(colorString,mDefValue)
        if (mColorPickerWindow == null) {
            mColorPickerWindow = ColorPickerWindow(mWangEditor!!.context, this)
            //第一次要选中颜色
            setColorChecked(parseColor)
        }
        val yOff: Int = Utils.dp2px(-5f)
        mColorPickerWindow!!.showAsDropDown(getItemView(), 0, yOff)
        mColorPickerWindow!!.setBackgroundColor(parseColor)
    }

    @SuppressLint("SetTextI18n")
    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.note_icon_text_color)
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
