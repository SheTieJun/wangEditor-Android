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
package com.wangeditor.android.toolbar

import android.view.View
import com.wangeditor.android.WangRichEditor

/**
 * 富文本工具item
 */
abstract class IRichItem {

    protected var mWangEditor: WangRichEditor? = null
    private var mItemView: View? = null
    protected var mCurrentValue: Any? = null
    protected var mDefValue: Any = 1

    open fun setEditor(editor: WangRichEditor) {
        this.mWangEditor = editor
    }

    /**
     * Get type 获取类型
     * @return
     */
    abstract fun getType(): String

    /**
     * On click
     * 当被点击
     */
    abstract fun onClick()

    /**
     * Update check
     * 更新选中状态
     * 建议用时
     * selector资源进行自动变换选中样式，
     * 如果不适用可以重写该方法给对应的view进行变化样式,值变化
     * @param isSelected
     */
    @JvmOverloads
    open fun updateSelected(isSelected: Boolean, currentValue: Any? = null) {
        if (isSelected) {
            this.mCurrentValue = currentValue
        } else {
            this.mCurrentValue = mDefValue
        }
        getItemView().isSelected = isSelected
    }

    /**
     * Get item view
     * 获取这个item展示的样式
     * @return view 不固定，可以Textview ,viewGroup or ImageView
     */
    open fun getItemView(): View {
        if (mItemView == null) {
            mItemView = buildView().also {
                it.setOnClickListener { onClick() }
            }
        }
        return mItemView!!
    }

    protected abstract fun buildView(): View

    open fun getEditor() = mWangEditor!!

    open fun performClick() {
        onClick()
    }

    open fun onKeyboardHide() {}

    open fun onKeyboardShow() {}
}
