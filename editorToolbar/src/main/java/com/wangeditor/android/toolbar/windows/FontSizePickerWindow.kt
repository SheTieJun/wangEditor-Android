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
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.google.android.material.slider.Slider
import com.wangeditor.android.Utils
import com.wangeditor.android.toolbar.R.id
import com.wangeditor.android.toolbar.R.layout

class FontSizePickerWindow(private val mContext: Context, private val mListener: FontSizeChangeListener?) :
    PopupWindow() {
    private val mRootView: View
    private var mPreview: TextView? = null
    private var mSeekbar: Slider? = null

    init {
        mRootView = inflateContentView()
        this.contentView = mRootView
        val wh = Utils.getScreenWidthAndHeight(mContext)
        this.width = wh[0]
        val h = Utils.dp2px(100f)
        this.height = h
        setBackgroundDrawable(ColorDrawable(Color.WHITE))
        this.isOutsideTouchable = true
        this.isFocusable = false
        initView()
        setupListeners()
    }

    private fun inflateContentView(): View {
        val layoutInflater = LayoutInflater.from(mContext)
        return layoutInflater.inflate(layout.win_fontsize_picker, null)
    }

    private fun <T : View?> findViewById(id: Int): T {
        return mRootView.findViewById(id)
    }

    fun setFontSize(size: Int) {
        mSeekbar!!.value = size.toFloat()
    }

    private fun initView() {
        mPreview = findViewById<TextView>(id.are_fontsize_preview)
        mSeekbar = findViewById<Slider>(id.are_fontsize_seekbar)
    }

    private fun setupListeners() {
        mSeekbar!!.addOnChangeListener(
            Slider.OnChangeListener { slider: Slider?, value: Float, fromUser: Boolean ->
                changePreviewText(
                    value.toInt()
                )
            }
        )
    }

    private fun changePreviewText(progress: Int) {
        mPreview!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress.toFloat())
        mPreview!!.text = progress.toString() + "sp: Preview"
        mListener?.onFontSizeChange(progress)
    }
}
