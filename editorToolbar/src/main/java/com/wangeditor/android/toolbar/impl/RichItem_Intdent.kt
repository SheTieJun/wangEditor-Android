package com.wangeditor.android.toolbar.impl

import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import android.widget.LinearLayout.LayoutParams
import com.wangeditor.android.RichType
import com.wangeditor.android.toolbar.IRichItem

/**
 *
 * <b>@author：</b> shetj<br>
 * <b>@createTime：</b> 2022/11/11<br>
 * <b>@email：</b> 375105540@qq.com<br>
 * <b>@describe</b>  <br>
 */
class RichItem_Intdent: IRichItem() {
    override fun getType(): String {
        return RichType.Indent.name
    }

    override fun onClick() {
        if (getItemView().isSelected){
            mWangEditor?.setOutdent()
        }else{
            mWangEditor?.setIndent()
        }
    }

    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT)
            scaleType = CENTER_INSIDE
        }
    }
}