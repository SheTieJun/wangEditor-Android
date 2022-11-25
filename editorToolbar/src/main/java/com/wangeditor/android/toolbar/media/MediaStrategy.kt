package com.wangeditor.android.toolbar.media

import com.wangeditor.android.toolbar.IRichItem
import com.wangeditor.android.toolbar.impl.RichItem_Image

interface MediaStrategy {


    /**
     * Start select image
     * when selected can use [RichItem_Image.insertMedia]
     * @param iRichItem
     */
    fun startSelectMedia(iRichItem: IRichItem)

}