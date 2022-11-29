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
package com.wangeditor.android.toolbar.windows.colorpicker

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.wangeditor.android.toolbar.R.array
import com.wangeditor.android.toolbar.R.styleable

/**
 * Created by wliu on 2018/3/6.
 */
class ColorPickerView @JvmOverloads constructor(
    private val mContext: Context,
    mAttributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(
    mContext, mAttributeSet, defStyleAttr
) {
    private var mColorsContainer: LinearLayout? = null
    private var mColorPickerListener: ColorPickerListener? = null
    private val mAttributeBundle = Bundle()
    private var mColorViewWidth = 0
    private var mColorViewHeight = 0
    private var mColorViewMarginLeft = 0
    private var mColorViewMarginRight = 0
    private var mColorCheckedViewType = 0
    private var mColors: IntArray? = null

    init {
        val ta = mContext.obtainStyledAttributes(mAttributeSet, styleable.ColorPickerView)
        mColorViewWidth = ta.getDimensionPixelSize(styleable.ColorPickerView_colorViewWidth, 40)
        mColorViewHeight = ta.getDimensionPixelSize(styleable.ColorPickerView_colorViewHeight, 40)
        mColorViewMarginLeft = ta.getDimensionPixelSize(styleable.ColorPickerView_colorViewMarginLeft, 5)
        mColorViewMarginRight = ta.getDimensionPixelSize(styleable.ColorPickerView_colorViewMarginRight, 5)
        mColorCheckedViewType = ta.getInt(styleable.ColorPickerView_colorViewCheckedType, 0)
        val colorsId = ta.getResourceId(styleable.ColorPickerView_colors, array.colorPickerColors)
        mColors = ta.resources.getIntArray(colorsId)
        ta.recycle()
        mAttributeBundle.putInt(ColorView.Companion.ATTR_VIEW_WIDTH, mColorViewWidth)
        mAttributeBundle.putInt(ColorView.Companion.ATTR_VIEW_HEIGHT, mColorViewWidth)
        mAttributeBundle.putInt(ColorView.Companion.ATTR_MARGIN_LEFT, mColorViewMarginLeft)
        mAttributeBundle.putInt(ColorView.Companion.ATTR_MARGIN_RIGHT, mColorViewMarginRight)
        mAttributeBundle.putInt(ColorView.Companion.ATTR_CHECKED_TYPE, mColorCheckedViewType)
        initView()
    }

    private fun initView() {
        mColorsContainer = LinearLayout(mContext)
        val containerLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
        mColorsContainer!!.layoutParams = containerLayoutParams
        for (color in mColors!!) {
            val colorView = ColorView(mContext, color, mAttributeBundle)
            mColorsContainer!!.addView(colorView)
            colorView.setOnClickListener(
                OnClickListener {
                    val isCheckedNow = colorView.checked
                    if (isCheckedNow) {
                        if (mColorPickerListener != null) {
                            mColorPickerListener!!.onPickColor(colorView.color)
                        }
                        return@OnClickListener
                    }
                    val childCount = mColorsContainer!!.childCount
                    for (i in 0 until childCount) {
                        val childView = mColorsContainer!!.getChildAt(i)
                        if (childView is ColorView) {
                            val isThisColorChecked = childView.checked
                            if (isThisColorChecked) {
                                childView.checked = false
                            }
                        }
                    }
                    colorView.checked = true
                    if (mColorPickerListener != null) {
                        mColorPickerListener!!.onPickColor(colorView.color)
                    }
                }
            )
        }
        this.addView(mColorsContainer)
    }

    fun setColorPickerListener(listener: ColorPickerListener?) {
        mColorPickerListener = listener
    }

    fun setColor(selectedColor: Int) {
        val childCount = mColorsContainer!!.childCount
        for (i in 0 until childCount) {
            val childView = mColorsContainer!!.getChildAt(i)
            if (childView is ColorView) {
                val viewColor = childView.color
                childView.checked = viewColor == selectedColor
            }
        }
    }
}
