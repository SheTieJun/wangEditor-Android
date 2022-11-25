package com.wangeditor.android.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.wangeditor.android.RichType
import com.wangeditor.android.StyleItem
import com.wangeditor.android.WangRichEditor
import com.wangeditor.android.WangRichEditor.OnDecorationStateListener
import com.wangeditor.android.toolbar.media.MediaStrategy

/**
 * 富文本工具栏
 */
open class RichEditorToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : HorizontalScrollView(context, attrs), OnDecorationStateListener {

    private var mEditor: WangRichEditor? = null
    private var mItems = mutableSetOf<IRichItem>()
    private var linearLayout:LinearLayout
    private var mMediaStrategy:MediaStrategy ?=null

    init {
        linearLayout = LinearLayout(context)
        initView()
    }

    private fun initView() {
        addView(linearLayout.apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        },LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT))
        overScrollMode = OVER_SCROLL_NEVER
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
    }


    fun setEditor(editor: WangRichEditor) {
        this.mEditor = editor
        editor.addOnDecorationChangeListener(this)
    }


    fun addItem(item: IRichItem) {
        checkNotNull(mEditor) { "请先setEditor" }
        item.setEditor(mEditor!!)
        mItems.add(item)
        setMediaStrategy(item, mMediaStrategy)
        linearLayout.addView(item.getItemView())
    }

    fun addItems(items: Collection<IRichItem>) {
        checkNotNull(mEditor) { "请先setEditor" }
        mItems.addAll(items)
        items.forEach {
            it.setEditor(mEditor!!)
            setMediaStrategy(it, mMediaStrategy)
            linearLayout.addView(it.getItemView())
        }
    }

    fun removeItem(item: IRichItem) {
        checkNotNull(mEditor) { "请先setEditor" }
        mItems.remove(item)
        linearLayout.removeView(item.getItemView())
    }


    fun removeItems(items: Collection<IRichItem>) {
        checkNotNull(mEditor) { "请先setEditor" }
        mItems.removeAll(items)
        items.forEach {
            linearLayout.removeView(it.getItemView())
        }
    }

    override fun onAttachedToWindow() {
        mEditor?.addOnDecorationChangeListener(this)
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        mEditor?.removeDecorationChangeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onStateChangeListener(types: List<StyleItem>) {
        mItems.forEach { richItem ->
            val find = types.find { it.type == richItem.getType() }
            richItem.updateSelected(find != null,find?.value)
        }
    }

    fun performClickItem(item: IRichItem){
        item.performClick()
    }

    fun performClickItem(type: RichType){
        performClickItem(type.name)
    }

    fun performClickItem(type: String){
        mItems.find { it.getType() == type }?.performClick()
    }

    fun setMediaStrategy(mediaStrategy: MediaStrategy){
        this.mMediaStrategy = mediaStrategy
        mItems.forEach {
            setMediaStrategy(it, mediaStrategy)
        }
    }

    private fun setMediaStrategy(
        item: IRichItem,
        mediaStrategy: MediaStrategy?
    ) {
        mediaStrategy?.let {
            if (item is AbRichItem_Media) {
                item.setMediaStrategy(mediaStrategy)
            }
        }
    }
}