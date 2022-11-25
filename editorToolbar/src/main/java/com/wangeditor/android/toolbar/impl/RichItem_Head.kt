package com.wangeditor.android.toolbar.impl

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.core.view.setPadding
import com.wangeditor.android.RichType
import com.wangeditor.android.toolbar.IRichItem
import com.wangeditor.android.toolbar.R


class RichItem_Head : IRichItem() {


    init {
        mCurrentValue = -1
        mDefValue = -1
    }

    override fun getType(): String {
        return RichType.Header.name
    }

    override fun onClick() {
        if (mCurrentValue as Int > 0) {
            mWangEditor?.setHeading(mCurrentValue as Int)
        } else {
            mWangEditor!!.setParagraph()
        }
    }

    override fun updateSelected(isSelected: Boolean, currentValue: Any?) {
        super.updateSelected(isSelected, currentValue)

    }

    @SuppressLint("SetTextI18n")
    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.selector_note_heading)
        }
    }
}