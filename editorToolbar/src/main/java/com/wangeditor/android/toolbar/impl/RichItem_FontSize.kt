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
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import androidx.core.view.setPadding
import com.wangeditor.android.RichType
import com.wangeditor.android.Utils
import com.wangeditor.android.toolbar.IRichItem
import com.wangeditor.android.toolbar.R
import com.wangeditor.android.toolbar.windows.FontSizeChangeListener
import com.wangeditor.android.toolbar.windows.FontSizePickerWindow

/**
 *
 * <b>@author：</b> shetj<br>
 * <b>@createTime：</b> 2022/11/11<br>
 * <b>@email：</b> 375105540@qq.com<br>
 * <b>@describe</b>  <br>
 */
class RichItem_FontSize : IRichItem(), FontSizeChangeListener {
    private var mFontPickerWindow: FontSizePickerWindow? = null
    init {
        mCurrentValue = 18
        mDefValue = 18
    }

    override fun getType(): String {
        return RichType.FontSize.name
    }

    override fun onClick() {
        showFontSizePickerWindow()
    }

    private fun showFontSizePickerWindow() {
        if (mFontPickerWindow == null) {
            mFontPickerWindow = FontSizePickerWindow(mWangEditor!!.context, this)
        }
        mFontPickerWindow!!.setFontSize(mCurrentValue as Int)
        val yOff: Int = Utils.dp2px(-5f)
        mFontPickerWindow!!.showAsDropDown(getItemView(), 0, yOff)
    }

    @SuppressLint("SetTextI18n")
    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.note_icon_fontsize)
        }
    }

    override fun onFontSizeChange(fontSize: Int) {
        mCurrentValue = fontSize
        mWangEditor!!.setTextFontSize(fontSize)
    }
}
