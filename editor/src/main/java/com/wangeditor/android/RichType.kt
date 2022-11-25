package com.wangeditor.android


enum class RichType {
    Bold, //加粗
    Italic,//斜体
    SubScript,
    SuperScript,
    StrikeThrough,
    FontSize,//字体大小
    UnderLine,//下滑先
    Header,//header
    
    
    Indent,//缩进
    NumberList,//列表排序
    BulletList,//没有1。2。3的列表
    JustifyCenter,//居中
    JustifyFull, //两端对齐
    JustifyLeft,//靠左
    JustifyRight,//靠右
    
    
    Todo,//check box  to do
    Link,//链接
   
    Image,//图片 image
    Video,//视频
    Audio,//音频
    BlockQuote,//引用 blockquote
    Code,//代码块
    Divider,//分割线
}