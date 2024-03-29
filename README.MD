## wangEditor-Android
[![](https://jitpack.io/v/SheTieJun/wangEditor-Android.svg)](https://jitpack.io/#SheTieJun/wangEditor-Android)
![buildWorkflow](https://github.com/SheTieJun/wangEditor-Android/actions/workflows/android.yml/badge.svg)

基于[**wangEditor**](https://github.com/wangeditor-team/wangEditor) 在Android实现的富文本功能，如果觉得好用可以去给wangEditor点个star

我站在巨人的肩膀上开发的，感谢wangEditor的开发者门

## 支持的功能 Supported Functions

- [X] Bold ：加粗
- [X] Italic ：斜体
- [X] Subscript ： 下标
- [X] Superscript： 商标
- [X] Strikethrough： 删除线
- [X] Underline： 下划线
- [X] Justify Left/ Justify Center / Justify Right：左对齐/居中对齐/右对齐
- [X] Heading 1、2、3 、4、5、6 ：H1~h6
- [X] Undo/Redo: 回撤/取消回撤
- [X] Indent/Outdent :缩进/取消缩进
- [X] Blockquote ：引用
- [X] Insert Code : 高亮代码块，如果是设置选择文本使用code，会清除其他内容，只保留文本
- [X] Insert Image：插入图片
- [X] Insert Video: 插入视频，Editor 插入video节点不支持width ='100%'(这是bug)，暂时用iframe播放代替,让他可以正常的播放
- [X] Insert Link:插入链接， 给选择文字添加链接，如果没有选择文字默认‘链接地址’4个字
- [x] Checkbox：【Todo】
- [X] Text Color ：文字颜色
- [X] Text Background Color：文字背景颜色
- [X] Text Font Size : 文字大小
- [X] Unordered List (Bullets) ：无序号排序
- [X] Ordered List (Numbers)： 有需要排序
- [X] Cursor Color： 光标颜色 ， 小圆点按钮：小圆点按钮颜色是通过Android主题修改的
- [X] removeFormat : 清除选中文本的格式，不可以清除heading
- [ ] Insert Audio : 编辑器不支持~，但是可以扩展一下，[定义新元素](https://www.wangeditor.com/v5/development.html#%E5%AE%9A%E4%B9%89%E6%96%B0%E5%85%83%E7%B4%A0)
- [ ] Table :编辑器支持，后面可以看情况加入，感觉手机上可能用不到

## Demo APK
![](image/下载地址.png)

## 效果图
|                        | |                          |        |
|------------------------|-|----------------------|-------|
| ![](image/颜色修改.webp)   |-| ![](image/heading修改.webp) |-|
| ![](image/字体大小修改.webp) |-| ![](image/demo样式.webp)    |-|


## 接入项目

Add it in your root build.gradle at the end of repositories:
```
maven { url 'https://jitpack.io' }
```
Step 2. Add the dependency
```
dependencies {
implementation 'com.github.SheTieJun:wangEditor-Android:Tag'

//或者
implementation `com.github.SheTieJun.wangEditor-Android:rich_editor:Tag`
implementation `com.github.SheTieJun.wangEditor-Android:editor_toolbar:Tag`
}
```

## Default Setting for Editor
```xml
    <com.wangeditor.android.WangRichEditor
        android:id="@+id/rich_Editor"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
  />
```

### Height

``` 
editor.setEditorHeight(200);  //dp = html px
```

### Text

``` 
editor.setTextBackgroundColor
editor.setTextFontSize
editor.setTextColor
```

### Background

``` 
editor.setEditorBackgroundColor(Color.BLUE);
```

### Padding

``` 
editor.setPadding(10, 10, 10, 10); //android dp = html px
```

### 预览模式和编辑模式切换
``` 
mViewBinding.richEditor.setInputEnabled(enable) //是否编辑状态
```

## 键盘监听

`initKeyboardChange`，可以自行重写使用

> 主要是为了修复打开键盘的时候，无法自动滚到选中的位置

默认实现操作：

- 打开键盘时滚动到当前选中位置或者上一次选中的位置
- 关闭键盘时，清除焦点

## 工具栏实现

```XML

<com.wangeditor.android.toolbar.RichEditorToolbar 
    android:id="@+id/editToolbar" 
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

```Kotlin
editToolbar.setEditor(mViewBinding.richEditor)
editToolbar.initTextStyle()
editToolbar.initFunStyle()
editToolbar.initParagraphStyle()
editToolbar.initMedia()
```

1. 可以自己选择添加对应工具，也可以自己自定义item,item的view可以自己决定是Textview/Imageview/ViewGroup
2. 可以添加多个toolbar 控制一个editor

## 链接项目

- API参考项目：[richeditor-android](https://github.com/wasabeef/richeditor-android)

- 核心内容：[wangeditor](https://www.wangeditor.com/v5/API.html)

## 修改Android System WebView

> wangEditor不支持低版本的Chrome，如果主要用户群体是消费水平低的人的话，不建议使用这个库

> 为什么不用X5，1. X5是动态加载的，内核由于包大小较大，存在一定失败率，也就是说用户可能会存在无法使用的情况
> 2.如果你要上架google，那么就不能使用X5

如果存在个别用户Chrome版本过低，可以在下面的链接下载apk给用户安装，~需要梯子
[android-system-webview.APK](https://www.apkmirror.com/apk/google-inc/android-system-webview/android-system-webview-89-0-4389-90-release/android-system-webview-89-0-4389-90-3-android-apk-download/?redirected=thank_you_invalid_nonce)

## 相关提示说明

1. 其中的交互部分`wang_app_editor.js`可以在iOS上通用，就是说iOS可以复制使用相关功能，不过可能需要修改部分调用native的方法部分。

2. 为什么项目中没有使用原生Span实现，主要原因是标签解析的问题。
   原生我也实现了一版demo，大部分功能都已实现,发现中间会有很多意外的情况需要进行处理，例如颜色rgb,arg,#fff。

3. 为什么使用`wangEditor`，  这是考虑多端处理的结果，其中web端使用了`wangEditor`项目进行开发的，我尝试移植后发现也可以使用，这样可以保证相同的格式处理问题。

4. `wangEditor`功能很全，但是我只懂皮毛的`js`和`html`，所以我这里只用到**部分**的功能，还有部分功能被我移除了,我用的是`simple`

5. > 缺点：`wang_editor_index.js`体积太大，功能内容太多，已我的知识储备无法进行优化简化,同时不支持低版本chrome
   >
   > 优化：`wangEditor`功能齐全强大，后续可以功能同步web

6. **大图不要用base64插入，否则会很卡顿**

## 相关API

核心文件：[wang_app_editor.js](editor/src/main/assets/wang_app_editor.js)

---------
- disable
- enable
- setInputEnabled
---------
- getHtml
- setHtml
---------
- addOnDecorationChangeListener
- addOnTextChangeListener
- setOnInitialLoadListener
---------
- focusEditor
- clearFocusEditor
---------
- setEditorBackgroundColor
- setEditorCursorColor
- setEditorHeight
- setEditorHeight
- setEditorWidth
---------
- setTextBackgroundColor
- setTextFontSize
- setTextColor
---------
- redo
- undo
---------
- setBackgroundColor
- setBlockquote
- setBold
- setStrikeThrough
- setSubscript
- setSuperscript
- setUnderline
- setItalic
---------
- setIndent
- setOutdent
---------
- setHeading
- setParagraph
- setNumbers
- setBullets
- setAlignCenter
- setAlignLeft
- setAlignRight
---------
- initKeyboardChange
---------
- insertAudio
- insertCode
- insertDivider
- insertImage
- insertLink
- insertTodo
- insertVideo

