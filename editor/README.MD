## 一、WangRichEditor 

> Android dp = html px

`wang_app_editor.js` 富文本功能js 部分

## API
### 1. Editor Function API


- setEditorBackgroundColor
- setEditorCursorColor
- setEditorHeight
- setEditorWidth
- setPadding


#### 2. Rich Function API

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
 

#### 4. 监听部分
- addOnTextChangeListener
- removeTextChangeListener
- addOnDecorationChangeListener
- removeDecorationChangeListener

### 修改默认属性'wang_style.css'
```
.w-e-text-container [data-slate-editor] {
 ....省略
  font-size: 18px; // 默认字体大小
  color: #515370; // 默认字体颜色
}
```