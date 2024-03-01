## 一、WangRichEditor 

> Android dp = html px

[`wang_app_editor.js`](src/main/assets/wang_app_editor.js):用于[**wangEditor**](https://github.com/wangeditor-team/wangEditor)和Android交互的核心文件

## API
### 1. Editor Function API-编辑器样式相关API


- setEditorBackgroundColor
- setEditorCursorColor
- setEditorHeight
- setEditorWidth
- setPadding


#### 2. Rich Function API- 富文本功能相关API

- setAlignCenter
- setAlignLeft
- setAlignRight
- setBackgroundResource
- setBlockquote
- setBold
- setBullets
- setFontSize
- setHeading
- setIndent
- setInputEnabled
- setItalic
- setNumbers
- setOutdent
- setParagraph
- setPlaceholder
- setStrikeThrough
- setSubscript
- setSuperscript
- setTextBackgroundColor
- setTextColor
- setUnderline
- insertAudio
- insertImage
- insertVideo
- insertLink
- insertTodo
- insertCode

- removeFormat 

- undo
- redo

- focusEditor
- clearFocusEditor

#### 3. Extensions API

- initKeyboardChange   

> 主要是为了修复打开键盘的时候，无法自动滚到选中的位置
> 可以继承后自行重写使用

默认实现操作：

- 打开键盘时滚动到当前选中位置或者上一次选中的位置
- 关闭键盘时，清除焦点 

#### 4. 监听部分
- addOnTextChangeListener
- removeTextChangeListener
- addOnDecorationChangeListener
- removeDecorationChangeListener

### 修改[assets](src/main/assets)下'wang_style.css'默认属性
```
.w-e-text-container [data-slate-editor] {
 ....省略
  font-size: 18px; // 默认字体大小
  color: #515370; // 默认字体颜色
}
```