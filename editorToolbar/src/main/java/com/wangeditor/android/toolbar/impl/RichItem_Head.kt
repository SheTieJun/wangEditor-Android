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
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import android.widget.PopupMenu
import android.widget.PopupMenu.OnMenuItemClickListener
import androidx.core.view.setPadding
import com.wangeditor.android.RichType
import com.wangeditor.android.WangRichEditor
import com.wangeditor.android.toolbar.IRichItem
import com.wangeditor.android.toolbar.R

class RichItem_Head : IRichItem(), OnMenuItemClickListener {

    private var headingMenu: PopupMenu? = null

    init {
        mCurrentValue = -1
        mDefValue = -1
    }

    override fun setEditor(editor: WangRichEditor) {
        super.setEditor(editor)
        setHeadingMenu(getItemView())
    }

    private fun setHeadingMenu(view: View?) {
        view ?: return
        headingMenu = PopupMenu(view.context, view)
        headingMenu?.setOnMenuItemClickListener(this)
        headingMenu?.inflate(R.menu.heading)
        headingMenu?.setOnDismissListener {
            if (getSelectedHeadingMenuItem() == null || getSelectedHeadingMenuItem() == -1) {
                updateSelected(false, -1)
            } else {
                updateSelected(true, getSelectedHeadingMenuItem())
            }
        }
    }

    private fun getSelectedHeadingMenuItem(): Int? = when {
        headingMenu?.menu?.findItem(R.id.paragraph)?.isChecked == true -> -1
        headingMenu?.menu?.findItem(R.id.heading_1)?.isChecked == true -> 1
        headingMenu?.menu?.findItem(R.id.heading_2)?.isChecked == true -> 2
        headingMenu?.menu?.findItem(R.id.heading_3)?.isChecked == true -> 3
        headingMenu?.menu?.findItem(R.id.heading_4)?.isChecked == true -> 4
        headingMenu?.menu?.findItem(R.id.heading_5)?.isChecked == true -> 5
        headingMenu?.menu?.findItem(R.id.heading_6)?.isChecked == true -> 6
        else -> null
    }

    override fun getType(): String {
        return RichType.Header.name
    }

    override fun onClick() {
        headingMenu?.show()
    }

    override fun updateSelected(isSelected: Boolean, currentValue: Any?) {
        super.updateSelected(isSelected, currentValue)
        selectHeadingMenuItem(mCurrentValue as Int)
    }

    private fun selectHeadingMenuItem(level: Int) {
        when (level) {
            1 -> headingMenu?.menu?.findItem(R.id.heading_1)?.isChecked = true
            2 -> headingMenu?.menu?.findItem(R.id.heading_2)?.isChecked = true
            3 -> headingMenu?.menu?.findItem(R.id.heading_3)?.isChecked = true
            4 -> headingMenu?.menu?.findItem(R.id.heading_4)?.isChecked = true
            5 -> headingMenu?.menu?.findItem(R.id.heading_5)?.isChecked = true
            6 -> headingMenu?.menu?.findItem(R.id.heading_6)?.isChecked = true
            else -> headingMenu?.menu?.findItem(R.id.paragraph)?.isChecked = true
        }
    }

    @SuppressLint("SetTextI18n")
    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.selector_note_heading)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val checked = (item?.isChecked == false)
        item?.isChecked = checked
        when (item?.itemId) {
            // Heading Menu
            R.id.paragraph -> {
                mWangEditor!!.setParagraph()
                return true
            }
            R.id.heading_1 -> {
                mCurrentValue = 1
                mWangEditor!!.setHeading(mCurrentValue as Int)
                return true
            }
            R.id.heading_2 -> {
                mCurrentValue = 2
                mWangEditor!!.setHeading(mCurrentValue as Int)
                return true
            }
            R.id.heading_3 -> {
                mCurrentValue = 3
                mWangEditor!!.setHeading(mCurrentValue as Int)
                return true
            }
            R.id.heading_4 -> {
                mCurrentValue = 4
                mWangEditor!!.setHeading(mCurrentValue as Int)
                return true
            }
            R.id.heading_5 -> {
                mCurrentValue = 5
                mWangEditor!!.setHeading(mCurrentValue as Int)
                return true
            }
            R.id.heading_6 -> {
                mCurrentValue = 6
                mWangEditor!!.setHeading(mCurrentValue as Int)
                return true
            }
            else -> return false
        }
    }
}
