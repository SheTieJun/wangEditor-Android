package com.wangeditor.android.toolbar.impl

import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.core.view.setPadding
import com.wangeditor.android.RichType
import com.wangeditor.android.toolbar.IRichItem


class RichItem_Link: IRichItem() {
    override fun getType(): String {
        return RichType.Link.name
    }

    override fun onClick() {
        mWangEditor?.insertLink("https://baidu.com","链接百度")
    }

    override fun buildView(): View {
        return TextView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT)
//            scaleType = CENTER_INSIDE
            setPadding(15)
            text = getType()
        }
    }
}