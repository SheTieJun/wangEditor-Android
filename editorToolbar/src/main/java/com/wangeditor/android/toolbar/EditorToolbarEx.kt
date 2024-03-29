/*
 * MIT License
 *
 * Copyright (c) 2022 SheTieJun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.wangeditor.android.toolbar

import com.wangeditor.android.toolbar.impl.RichItem_AlignCenter
import com.wangeditor.android.toolbar.impl.RichItem_AlignLeft
import com.wangeditor.android.toolbar.impl.RichItem_AlignRight
import com.wangeditor.android.toolbar.impl.RichItem_BlockQuote
import com.wangeditor.android.toolbar.impl.RichItem_Bold
import com.wangeditor.android.toolbar.impl.RichItem_BulletList
import com.wangeditor.android.toolbar.impl.RichItem_ClearStyle
import com.wangeditor.android.toolbar.impl.RichItem_Code
import com.wangeditor.android.toolbar.impl.RichItem_Divider
import com.wangeditor.android.toolbar.impl.RichItem_FontColor
import com.wangeditor.android.toolbar.impl.RichItem_FontSize
import com.wangeditor.android.toolbar.impl.RichItem_Head
import com.wangeditor.android.toolbar.impl.RichItem_Intdent
import com.wangeditor.android.toolbar.impl.RichItem_Italic
import com.wangeditor.android.toolbar.impl.RichItem_Link
import com.wangeditor.android.toolbar.impl.RichItem_NumberList
import com.wangeditor.android.toolbar.impl.RichItem_StrikeThrough
import com.wangeditor.android.toolbar.impl.RichItem_Subscript
import com.wangeditor.android.toolbar.impl.RichItem_Superscript
import com.wangeditor.android.toolbar.impl.RichItem_TextBgColor
import com.wangeditor.android.toolbar.impl.RichItem_Todo
import com.wangeditor.android.toolbar.impl.RichItem_UnderLine
import com.wangeditor.android.toolbar.impl.media.RichItem_Audio
import com.wangeditor.android.toolbar.impl.media.RichItem_Image
import com.wangeditor.android.toolbar.impl.media.RichItem_Video

fun RichEditorToolbar.initTextStyle() {
    addItem(RichItem_Head())
    addItem(RichItem_Bold())
    addItem(RichItem_UnderLine())
    addItem(RichItem_Italic())
    addItem(RichItem_FontSize())
    addItem(RichItem_FontColor())
    addItem(RichItem_TextBgColor())
    addItem(RichItem_Subscript()) // 下标
    addItem(RichItem_Superscript()) // 上标
    addItem(RichItem_StrikeThrough()) // 删除线
}

fun RichEditorToolbar.initParagraphStyle() {
    addItem(RichItem_Intdent())
    addItem(RichItem_BulletList())
    addItem(RichItem_NumberList())
    addItem(RichItem_AlignLeft())
    addItem(RichItem_AlignCenter())
    addItem(RichItem_AlignRight())
}

fun RichEditorToolbar.initFunStyle() {
    addItem(RichItem_ClearStyle())
    addItem(RichItem_Link())
    addItem(RichItem_Todo())
    addItem(RichItem_Code())
    addItem(RichItem_Divider())
    addItem(RichItem_BlockQuote())
}

fun RichEditorToolbar.initMedia() {
    addItem(RichItem_Image())
    addItem(RichItem_Audio())
    addItem(RichItem_Video())
}
