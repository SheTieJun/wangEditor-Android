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
package com.wangeditor.android.toolbar.windows

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.wangeditor.android.Utils
import com.wangeditor.android.toolbar.R.layout
import com.wangeditor.android.toolbar.windows.colorpicker.ColorPickerListener
import com.wangeditor.android.toolbar.windows.colorpicker.ColorPickerView

class ColorPickerWindow(private val mContext: Context, private val mColorPickerListener: ColorPickerListener) :
    PopupWindow() {
    private val colorPickerView: ColorPickerView

    init {
        colorPickerView = inflateContentView()
        this.contentView = colorPickerView
        val wh = Utils.getScreenWidthAndHeight(mContext)
        this.width = wh[0]
        val h = Utils.dp2px(50f)
        this.height = h
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.isOutsideTouchable = true
        this.isFocusable = false
        setupListeners()
    }

    private fun inflateContentView(): ColorPickerView {
        val layoutInflater = LayoutInflater.from(mContext)
        return layoutInflater.inflate(layout.win_color_picker, null) as ColorPickerView
    }

    private fun <T : View?> findViewById(id: Int): T {
        return colorPickerView.findViewById(id)
    }

    fun setColor(color: Int) {
        colorPickerView.setColor(color)
    }

    private fun setupListeners() {
        colorPickerView.setColorPickerListener(mColorPickerListener)
    }

    fun setBackgroundColor(backgroundColor: Int) {
        colorPickerView.setBackgroundColor(backgroundColor)
    }
}
