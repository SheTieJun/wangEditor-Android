package com.wangeditor.android.toolbar

import android.view.View
import com.wangeditor.android.RichWangEditor

/**
 * 富文本工具item
 */
abstract class IRichItem {

    private var mWangEditor: RichWangEditor? = null
    private var mItemView: View? = null

    fun setEditor(editor: RichWangEditor) {
        this.mWangEditor = editor
    }

    /**
     * On click
     * 当被点击
     */
    abstract fun onClick()

    /**
     * Update check
     * 更新选中状态
     * @param isCheck
     */
    abstract fun updateCheck(isCheck: Boolean)

    /**
     * Get item view
     * 获取这个item展示的样式
     * @return view 不固定，可以Textview ,viewGroup or ImageView
     */
    fun getItemView(): View {
        if (mItemView == null) {
            mItemView = buildView()
        }
        return mItemView!!
    }

    protected abstract fun buildView(): View
}