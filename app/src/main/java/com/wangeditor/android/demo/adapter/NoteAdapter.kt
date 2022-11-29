package com.wangeditor.android.demo.adapter

import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.BaseDraggableModule
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wangeditor.android.demo.R.id
import com.wangeditor.android.demo.R.layout
import com.wangeditor.android.demo.model.Note
import kotlinx.coroutines.launch
import me.shetj.base.base.BaseKTAdapter
import me.shetj.base.ktx.saverDB


class NoteAdapter(data: MutableList<Note>? = null,owner: Lifecycle) : BaseKTAdapter<Note, BaseViewHolder>(owner,
    layout.item_note, data),
    DraggableModule {

    init {
        draggableModule.apply {
            isSwipeEnabled = true
        }
    }

    override fun convert(holder: BaseViewHolder, item: Note) {
        holder.setText(id.title, item.title)
            .setText(id.content, HtmlCompat.fromHtml(item.content, HtmlCompat.FROM_HTML_MODE_LEGACY))
    }

    override fun addDraggableModule(baseQuickAdapter: BaseQuickAdapter<*, *>): BaseDraggableModule {
        return object : BaseDraggableModule(baseQuickAdapter) {
            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder) {
                val pos = getViewHolderPosition(viewHolder)
                data.removeAt(pos).apply {
                    lifeKtScope.launch {
                        this@apply.saver?.let { saverDB.deleteSaver(it) }
                    }
                }
                baseQuickAdapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)
                if (isSwipeEnabled) {
                    mOnItemSwipeListener?.onItemSwiped(viewHolder, pos)
                }
            }
        }
    }
}