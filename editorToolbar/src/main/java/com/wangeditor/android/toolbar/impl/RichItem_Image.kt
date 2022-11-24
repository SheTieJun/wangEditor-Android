package com.wangeditor.android.toolbar.impl

import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import android.widget.LinearLayout.LayoutParams
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import com.wangeditor.android.RichType
import com.wangeditor.android.toolbar.IRichItem
import com.wangeditor.android.toolbar.R


class RichItem_Image: IRichItem() {
    override fun getType(): String {
        return RichType.Image.name
    }

    override fun onClick() {
        kotlin.runCatching {
            (mWangEditor!!.context as FragmentActivity)
        }

    }

    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT)
            scaleType = CENTER_INSIDE
            setPadding(15)
            setImageResource(R.drawable.note_icon_pic)
        }
    }
}