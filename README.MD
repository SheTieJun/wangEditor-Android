## wangEditor-Android

基于[**wangEditor**](https://github.com/wangeditor-team/wangEditor) 在Android实现的富文本功能

> 1.其中的交互部分`wang_app_editor.js`可以在iOS上通用。

> 2.为什么项目中没有使用原生Span实现，主要原因是标签解析的问题。
> 原生我也实现了一版demo，大部分功能都已实现,发现中间会有很多意外的情况需要进行处理。

> 3.为什么使用`wangEditor`， 因为web端使用了`wangEditor`项目进行开发的。

> 4.`wangEditor`功能很全，但是我只懂皮毛的`js`和`html`，所以我这里只用到**部分**的功能，也有一些功能被我移除了,我用的是`simple`

>mode: 'default' 默认模式 - 集成了`wangEditor`所有功能
>mode: 'simple' 简洁模式 - 仅有部分常见功能，但更加简洁易用

> 缺点：`wang_editor_index.js`体积太大，功能内容太多，已我的知识储备无法进行优化简化

> 优化：`wangEditor`功能齐全强大，后续可以功能同步web

## 支持的功能 Supported Functions 
- [X] Bold
- [X] Italic
- [X] Subscript
- [X] Superscript
- [X] Strikethrough
- [X] Underline
- [X] Justify Left
- [X] Justify Center
- [X] Justify Right
- [X] Heading 1、2、3 、4、5、6
- [X] Undo
- [X] Redo
- [X] Indent
- [X] Outdent
- [X] Blockquote
- [X] Code, 如果是设置选择文本使用code，会清除其他格式只保留文本
- [X] Insert Image
- [X] Insert Video
- [X] Insert Audio
- [X] Insert Link
- [x] Checkbox：【Todo】
- [X] Text Color
- [X] Text Background Color
- [X] Text Font Size
- [X] Unordered List (Bullets)
- [X] Ordered List (Numbers)
- [X] Cursor Color 光标颜色  ，和 Android光标下的小圆点按钮颜色是通过主题修改的
- [X] removeFormat 清除选中文本的格式，不可以清除heading


## Default Setting for Editor
### Height
``` 
editor.setEditorHeight(200);  //dp = html px
```

### Font
``` 
editor.setEditorFontSize(22);  //android dp = html px
editor.setEditorFontColor(Color.RED);
```
### Background
``` 
editor.setEditorBackgroundColor(Color.BLUE);
editor.setBackgroundColor(Color.BLUE);
editor.setBackgroundResource(R.drawable.bg);
editor.setBackground(url);
```

### Padding
``` 
editor.setPadding(10, 10, 10, 10); //android dp = html px
```


## 键盘监听
`initKeyboardChange`，可以自行重写使用

> 主要是为了修复打开键盘的时候，无法自动滚到选中的位置

默认实现操作：

- 打开键盘时滚动到当前选中位置或者上一次选中的位置
- 关闭键盘时，清除焦点

## 工具栏实现
TODO: 
- [ ] 动态绑定工具栏
- [ ] 动态添加工具
- [ ] 通过接口实现，代理、模板模式


## 链接项目
- API参考项目：[richeditor-android](https://github.com/wasabeef/richeditor-android)

- 核心内容：[wangeditor](https://www.wangeditor.com/v5/API.html)


### 如果对你们有帮助就star一下吧