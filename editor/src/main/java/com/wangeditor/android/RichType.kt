package com.wangeditor.android


enum class RichType {
    BOLD, //加粗
    ITALIC,
    SUBSCRIPT,
    SUPERSCRIPT,
    STRIKETHROUGH,
    UNDERLINE,//下滑先
    INDENT,//缩进
    H1,
    H2,
    H3,
    H4,
    H5,
    H6,
    ORDEREDLIST,//列表排序
    UNORDEREDLIST,//没有1。2。3的列表
    JUSTIFYCENTER,//居中
    JUSTIFYFULL, //两端对齐
    JUSTIFYLEFT,//靠左
    JUSTIFYRIGHT,//靠右
    TODO,//check box  to do
    LINK,//链接
    FONTSIZE,//字体大小
    IMAGE,//图片 image
    BLOCKQUOTE,//引用 blockquote
    CODE,//代码块
    DIVIDER,//分割线
}