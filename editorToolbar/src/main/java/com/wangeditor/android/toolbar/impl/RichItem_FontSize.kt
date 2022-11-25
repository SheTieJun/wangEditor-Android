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

/**
 *
 * <b>@author：</b> shetj<br>
 * <b>@createTime：</b> 2022/11/11<br>
 * <b>@email：</b> 375105540@qq.com<br>
 * <b>@describe</b>  <br>
 */
class RichItem_FontSize : IRichItem() {

    init {
        mCurrentValue = 18
        mDefValue = 18
    }

    override fun getType(): String {
        return RichType.FontSize.name
    }

    override fun onClick() {
        mWangEditor?.setFontSize(mCurrentValue as Int)
    }


    @SuppressLint("SetTextI18n")
    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.note_icon_fontsize)
        }
    }
}