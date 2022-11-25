package com.wangeditor.android.toolbar.impl

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
 * checkBox
 */
class RichItem_Todo: IRichItem() {
    override fun getType(): String {
        return RichType.Todo.name
    }

    override fun onClick() {
        mWangEditor?.insertTodo()
    }

    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.selector_note_todo)
        }
    }
}