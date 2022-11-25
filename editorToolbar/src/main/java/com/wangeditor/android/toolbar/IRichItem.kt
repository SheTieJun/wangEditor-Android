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
    protected var mDefValue:Any = 1

    fun setEditor(editor: WangRichEditor) {
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
     * selector资源进行自动变换选中样式，如果不适用可以重写该方法进行变化样式
     * @param isSelected
     */
    @JvmOverloads
    open fun updateSelected(isSelected: Boolean, currentValue: Any? = null) {
        if (isSelected){
            this.mCurrentValue = currentValue
        }else{
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
}