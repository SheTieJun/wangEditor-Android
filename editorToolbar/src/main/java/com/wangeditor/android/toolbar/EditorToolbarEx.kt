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
    addItem(RichItem_Head())//标题
    addItem(RichItem_Bold())//加粗
    addItem(RichItem_UnderLine())//下划线
    addItem(RichItem_Italic())//斜体
    addItem(RichItem_FontSize())//字体大小
    addItem(RichItem_FontColor())//字体颜色
    addItem(RichItem_TextBgColor())//背景颜色
    addItem(RichItem_Subscript()) // 下标
    addItem(RichItem_Superscript()) // 上标
    addItem(RichItem_StrikeThrough()) // 删除线
}

fun RichEditorToolbar.initParagraphStyle() {
    addItem(RichItem_Intdent()) //缩进
    addItem(RichItem_BulletList())//无序列表
    addItem(RichItem_NumberList())//有序列表
    addItem(RichItem_AlignLeft())//左对齐
    addItem(RichItem_AlignCenter())//居中对齐
    addItem(RichItem_AlignRight())//右对齐
}

fun RichEditorToolbar.initFunStyle() {
    addItem(RichItem_ClearStyle())//清除样式
    addItem(RichItem_Link())//链接
    addItem(RichItem_Todo())//Check to do
    addItem(RichItem_Code())//代码块
    addItem(RichItem_Divider())//分割线
    addItem(RichItem_BlockQuote())//引用
}

fun RichEditorToolbar.initMedia() {
    addItem(RichItem_Image())//图片
    addItem(RichItem_Audio())//音频
    addItem(RichItem_Video())//视频
}
