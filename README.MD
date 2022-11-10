## wangEditor-Android

基于[wangEditor](https://github.com/wangeditor-team/wangEditor)在Android实现的富文本功能

> 其中的交互部分可以在iOS上通用

> 为什么项目中没有使用原生Span实现，主要原因是标签解析的问题。
 
> 为什么使用wangEditor， 因为web端使用了改项目进行开发

## Supported Functions 
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
- [ ] Indent
- [ ] Outdent
- [X] Insert Image
- [ ] Insert Video
- [ ] Insert Audio
- [ ] Insert Link
- [ ] Checkbox
- [ ] Text Color
- [ ] Text Background Color
- [ ] Text Font Size
- [X] Unordered List (Bullets)
- [X] Ordered List (Numbers)
- [ ] Cursor Color 光标颜色  ，和光标一起的按钮颜色是通过主题修改的


## 键盘监听 `initKeyboardChange`，可以自行重写
默认操作：
- 打开键盘时滚动到当前选中位置或者上一次选中的位置
- 关闭键盘时，清除焦点

## 工具栏实现【TODO】



## 参考项目
- [richeditor-android](https://github.com/wasabeef/richeditor-android)