package com.wangeditor.android.toolbar

import com.wangeditor.android.RichType
import com.wangeditor.android.toolbar.media.MediaStrategy


abstract class AbRichItem_Media : IRichItem() {

    private var mMediaStrategy:MediaStrategy ?=null

    override fun getType(): String {
        return RichType.Image.name
    }

    override fun onClick() {
        mMediaStrategy?.startSelectMedia(this)
    }

    abstract fun insertMedia(url: String)


    open fun setMediaStrategy(mediaStrategy: MediaStrategy){
        this.mMediaStrategy = mediaStrategy
    }


}