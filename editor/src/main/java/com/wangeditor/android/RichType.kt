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
package com.wangeditor.android

enum class RichType {
    Bold, // 加粗
    Italic, // 斜体
    SubScript,// 下标
    SuperScript,// 上标
    StrikeThrough,// 删除线
    FontSize, // 字体大小
    FontColor, // 字体大小
    TextBgColor, // 背景颜色
    UnderLine, // 下滑先
    Header, // header

    Indent, // 缩进
    NumberList, // 列表排序
    BulletList, // 没有1。2。3的列表
    JustifyCenter, // 居中
    JustifyFull, // 两端对齐
    JustifyLeft, // 靠左
    JustifyRight, // 靠右

    Todo, // check box  to do
    Link, // 链接

    Image, // 图片 image
    Video, // 视频
    Audio, // 音频

    BlockQuote, // 引用 blockquote
    Code, // 代码块
    Divider, // 分割线


    Clear,//橡皮擦功能
}
