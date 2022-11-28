package com.wangeditor.android.toolbar.impl.media

import com.wangeditor.android.toolbar.IRichItem

interface MediaStrategy {


    /**
     * Start select image
     * when selected can use [RichItem_Image.insertMedia]
     * @param iRichItem
     */
    fun startSelectMedia(iRichItem: AbRichItem_Media)

}