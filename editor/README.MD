## 一、WangRichEditor 
 

### 1. 监听部分
- addOnTextChangeListener
- removeTextChangeListener
- addOnDecorationChangeListener
- removeDecorationChangeListener

### 2. Editor API
- scrollToBottom
- setEditorBackgroundColor
- setEditorFontColor
- setEditorFontSize
- setEditorHeight
- setEditorWidth

- setAlignCenter
- setAlignLeft
- setAlignRight
- setBackground
- setBackground
- setBackgroundColor
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
- setOnInitialLoadListener
- setOutdent
- setPadding
- setPaddingRelative
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
- insertImage
- insertLink
- insertTodo
- insertVideo
- undo
- redo
- removeFormat
- focusEditor
- clearFocusEditor

### 修改默认属性'wang_style.css'
```
.w-e-text-container [data-slate-editor] {
 ....省略
  font-size: 18px; // 默认字体大小
  color: #515370; // 默认字体颜色
}
```
