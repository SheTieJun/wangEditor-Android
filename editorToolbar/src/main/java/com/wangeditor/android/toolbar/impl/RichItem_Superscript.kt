package com.wangeditor.android.toolbar.impl

import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import android.widget.LinearLayout.LayoutParams
import com.wangeditor.android.RichType
import com.wangeditor.android.toolbar.IRichItem


class RichItem_Superscript: IRichItem() {
    override fun getType(): String {
        return RichType.SUPERSCRIPT.name
    }

    override fun onClick() {
        mWangEditor?.setSuperscript()
    }

    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT)
            scaleType = CENTER_INSIDE
        }
    }
}