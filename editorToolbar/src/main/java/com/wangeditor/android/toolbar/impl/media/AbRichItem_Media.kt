package com.wangeditor.android.toolbar.impl.media

import com.wangeditor.android.RichType
import com.wangeditor.android.toolbar.IRichItem


abstract class AbRichItem_Media : IRichItem() {

    private var mMediaStrategy: MediaStrategy?=null

    override fun getType(): String {
        return RichType.Image.name
    }

    override fun onClick() {
        mMediaStrategy?.startSelectMedia(this)
    }


    open fun getMimeType(): String {
        return "*/*"
    }

    abstract fun insertMedia(url: String)


    open fun setMediaStrategy(mediaStrategy: MediaStrategy){
        this.mMediaStrategy = mediaStrategy
    }


}