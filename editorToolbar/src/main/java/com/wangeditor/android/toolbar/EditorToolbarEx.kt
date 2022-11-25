package com.wangeditor.android.toolbar

import com.wangeditor.android.toolbar.impl.RichItem_AlignCenter
import com.wangeditor.android.toolbar.impl.RichItem_AlignFull
import com.wangeditor.android.toolbar.impl.RichItem_AlignLeft
import com.wangeditor.android.toolbar.impl.RichItem_AlignRight
import com.wangeditor.android.toolbar.impl.RichItem_Audio
import com.wangeditor.android.toolbar.impl.RichItem_BlockQuote
import com.wangeditor.android.toolbar.impl.RichItem_Bold
import com.wangeditor.android.toolbar.impl.RichItem_BulletList
import com.wangeditor.android.toolbar.impl.RichItem_Code
import com.wangeditor.android.toolbar.impl.RichItem_Divider
import com.wangeditor.android.toolbar.impl.RichItem_FontSize
import com.wangeditor.android.toolbar.impl.RichItem_Head
import com.wangeditor.android.toolbar.impl.RichItem_Image
import com.wangeditor.android.toolbar.impl.RichItem_Intdent
import com.wangeditor.android.toolbar.impl.RichItem_Italic
import com.wangeditor.android.toolbar.impl.RichItem_Link
import com.wangeditor.android.toolbar.impl.RichItem_NumberList
import com.wangeditor.android.toolbar.impl.RichItem_Subscript
import com.wangeditor.android.toolbar.impl.RichItem_Superscript
import com.wangeditor.android.toolbar.impl.RichItem_Todo
import com.wangeditor.android.toolbar.impl.RichItem_UnderLine


fun RichEditorToolbar.initTextStyle(){
    addItem(RichItem_Head())
    addItem(RichItem_Bold())
    addItem(RichItem_UnderLine())
    addItem(RichItem_Italic())
    addItem(RichItem_FontSize())
    addItem(RichItem_Subscript()) //下标
    addItem(RichItem_Superscript()) //上标
}

fun RichEditorToolbar.initParagraphStyle(){
    addItem(RichItem_Intdent())
    addItem(RichItem_BulletList())
    addItem(RichItem_NumberList())
    addItem(RichItem_AlignCenter())
    addItem(RichItem_AlignLeft())
    addItem(RichItem_AlignRight())
}

fun RichEditorToolbar.initFunStyle(){
    addItem(RichItem_Link())
    addItem(RichItem_Todo())
    addItem(RichItem_Code())
    addItem(RichItem_Divider())
    addItem(RichItem_BlockQuote())
}

fun RichEditorToolbar.initMedia(){
    addItem(RichItem_Image())
    addItem(RichItem_Audio())
    addItem(RichItem_Image())
}

