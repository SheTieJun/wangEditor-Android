const {
    SlateEditor,
    SlateTransforms,
    SlateText,
    DomEditor
} = window.wangEditor

var RE = {};

RE.editor = editor;

//容器
RE.editorContainer = document.getElementById('editor-container');

RE.isActive = function (type) {
    return !!DomEditor.getSelectedNodeByType(editor, type)
}


RE.setHtml = function (contents) {
    RE.editor.setHtml(decodeURIComponent(contents.replace(/\+/g, '%20')));
}

RE.getHtml = function () {
    return RE.editor.getHtml();
}


RE.disable = function () {
    return RE.editor.disable();
}

RE.enable = function () {
    return RE.editor.enable();
}


RE.getText = function () {
    return RE.editor.getText();
}


RE.setPadding = function (left, top, right, bottom) {
    RE.editorContainer.style.paddingLeft = left;
    RE.editorContainer.style.paddingTop = top;
    RE.editorContainer.style.paddingRight = right;
    RE.editorContainer.style.paddingBottom = bottom;
}

RE.setBackgroundColor = function (color) {
    RE.editorContainer.style.backgroundColor = color;
}

RE.setBackgroundImage = function (image) {
    RE.editorContainer.style.backgroundImage = image;
}

RE.setCaretColor = function (color) {
    RE.editorContainer.style.caretColor = color;
}

RE.setWidth = function (size) {
    RE.editorContainer.style.minWidth = size;
}

RE.setHeight = function (size) {
    RE.editorContainer.style.height = size;
}

// 专门给键盘出来进行高度变化使用的
RE.setHeight = function (size, focus) {
    if (focus) {
        RE.editor.blur()
    }
    RE.editorContainer.style.height = size;
    if (focus) {
        RE.editor.restoreSelection()
    }
}


RE.setInputEnabled = function (inputEnabled) {
    if (inputEnabled) {
        RE.editor.enable()
    } else {
        RE.editor.disable()
    }
}

RE.undo = function () {
    RE.editor.undo()
}

RE.redo = function () {
    RE.editor.redo()
}

RE.setBold = function () {
    var isTrue = SlateEditor.marks(RE.editor).bold
    if (isTrue) {
        RE.editor.removeMark('bold')
    } else {
        RE.editor.addMark('bold', true)
    }
}

RE.setItalic = function () {
    var isTrue = SlateEditor.marks(RE.editor).italic
    if (isTrue) {
        RE.editor.removeMark('italic')
    } else {
        RE.editor.addMark('italic', true)
    }
}

RE.setSubscript = function () {
    var isTrue = SlateEditor.marks(RE.editor).sub
    if (isTrue) {
        RE.editor.removeMark('sub')
    } else {
        RE.editor.addMark('sub', true)
    }
}

RE.setSuperscript = function () {
    var isTrue = SlateEditor.marks(RE.editor).sup
    if (isTrue) {
        RE.editor.removeMark('sup')
    } else {
        RE.editor.addMark('sup', true)
    }
}

RE.setStrikeThrough = function () {
    var isTrue = SlateEditor.marks(RE.editor).through
    if (isTrue) {
        RE.editor.removeMark('through')
    } else {
        RE.editor.addMark('through', true)
    }
}

RE.setUnderline = function () {
    var isTrue = SlateEditor.marks(RE.editor).underline
    if (isTrue) {
        RE.editor.removeMark('underline')
    } else {
        RE.editor.addMark('underline', true)
    }
}

RE.setBullets = function () {
    SlateTransforms.setNodes(editor, {
        type: 'list-item',
        ordered: false, // 有序 true/无序false
        indent: undefined,
    })
}

RE.setNumbers = function () {
    SlateTransforms.setNodes(editor, {
        type: 'list-item',
        ordered: true, // 有序 true/无序false
        indent: undefined,
    })
}



RE.setCode = function () {
    var active = this.isActive('code')
    if (!active) {
        var codeS = editor.getSelectionText()
        // 插入 pre 节点
        const newPreNode = {
            type: 'pre',
            children: [{
                type: 'code',
                language: 'Java',
                children: [{
                        text: codeS
                    }, // 选中节点的纯文本
                ],
            }, ],
        }
        SlateTransforms.insertNodes(editor, newPreNode, {
            mode: 'highest'
        })
    }
}

RE.setBlockquote = function () {
    if (isDisabledBlockquote()) return
    var active = this.isActive('blockquote')
    var newType = active ? 'paragraph' : 'blockquote'
    console.log(newType + '//' + active)
    SlateTransforms.setNodes(editor, {
        type: newType
    })
}

isDisabledBlockquote = function () {
    if (editor.selection == null) return true

    const [nodeEntry] = SlateEditor.nodes(editor, {
        match: n => {
            const type = DomEditor.getNodeType(n)

            // 只可用于 p 和 blockquote
            if (type === 'paragraph') return true
            if (type === 'blockquote') return true

            return false
        },
        universal: true,
        mode: 'highest', // 匹配最高层级
    })

    // 匹配到 p blockquote ，不禁用
    if (nodeEntry) {
        return false;
    }
    // 未匹配到，则禁用
    return true;
}


RE.setTextColor = function (color) {
    RE.editor.addMark('color', color);
}

RE.setTextBackgroundColor = function (color) {
    RE.editor.addMark('bgColor', color);
}

RE.setTextFontSize = function (fontSize) {
    RE.editor.addMark('fontSize', fontSize);
}

RE.setHeading = function (heading) {
    SlateTransforms.setNodes(editor, {
        type: 'header' + heading,
    });
}

RE.setParagraph = function () {
    SlateTransforms.setNodes(editor, {
        type: 'paragraph',
    });
}

RE.setIndent = function () {
    SlateTransforms.setNodes(editor, {
        indent: '2em'
    });
}

RE.setOutdent = function () {
    SlateTransforms.setNodes(RE.editor, {
        indent: '0em'
    });
}

RE.setJustifyLeft = function () {
    SlateTransforms.setNodes(editor, {
        textAlign: 'left',
    });
}

RE.setJustifyCenter = function () {
    SlateTransforms.setNodes(editor, {
        textAlign: 'center',
    });
}

RE.setJustifyRight = function () {
    SlateTransforms.setNodes(editor, {
        textAlign: 'right',
    });
}


RE.insertImageBase64 = function (image) {
    RE.reFocus();
    var image = {
        type: 'image',
        src: image,
        style: {
            width: '100%'
        },
        children: [{
            text: "base64Image"
        }]
    };
    RE.editor.insertNode(image);
}
RE.insertImage = function (url, alt) {
    RE.reFocus();
    var image = {
        type: 'image',
        src: url,
        style: {
            width: '100%'
        },
        children: [{
            text: alt
        }]
    };
    RE.editor.insertNode(image);
}

RE.insertImageW = function (url, alt, width) {
    RE.reFocus();
    var image = {
        type: 'image',
        src: url,
        style: {
            width: width
        },
        children: [{
            text: alt
        }]
    };
    RE.editor.insertNode(image);
}



RE.insertImageWH = function (url, alt, width, height) {
    RE.reFocus();
    var image = {
        type: 'image',
        src: url,
        style: {
            width: width,
            height: height
        },
        children: [{
            text: alt
        }]
    };
    RE.editor.insertNode(image)
}

RE.insertVideo = function (url, thumbURL) {
    var iUrl =  '<iframe width="100%" height="auto"  src="' + url + '" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="true"></iframe><br>'
    RE.reFocus()
    var video = {
        type: 'video',
        poster: thumbURL,
        src: iUrl,
        width: "100%",
        children: [{
            text: ''
        }]
    };
    RE.editor.insertNode(video)
}

RE.insertVideoW = function (url, thumbURL, width) {
    RE.reFocus()
    var video = {
        type: 'video',
        poster:thumbURL,
        src: url,
        width: width,
        children: [{
            text: ''
        }]
    };
    RE.editor.insertNode(video)
}

RE.insertVideoWH = function (url, thumbURL, width, height) {
    RE.reFocus()
    var video = {
        type: 'video',
        poster: thumbURL,
        src: url,
        width: width,
        height: height,
        children: [{
            text: ''
        }]
    };
    RE.editor.insertNode(video)
}

RE.insertAudio = function (url, alt) {
    RE.reFocus()
    var audio = {
        type: 'audio',
        src: url
    };
    RE.editor.insertNode(audio);
}


RE.insertHTML = function (html) {
    RE.reFocus()
    document.execCommand('insertHTML', false, html);
}

RE.insertLink = function (url, defText) {
    RE.reFocus()
    var name = editor.getSelectionText();
    if (name == "" || name == null) {
        name = defText
    }
    var image = {
        type: 'link',
        url: url,
        target: "_blank",
        children: [{
            text: name
        }]
    };
    RE.editor.insertNode(image);
}

RE.setTodo = function (text) {
    var active = !!DomEditor.getSelectedNodeByType(editor, 'todo');
    SlateTransforms.setNodes(editor, {
        type: active ? 'paragraph' : 'todo'
    })
}

RE.setDivider = function () {
    var elem = {
        type: 'divider',
        children: [{
            text: ''
        }]
    }
    RE.editor.insertNode(elem)
}

RE.focus = function () {
    RE.editor.focus()
}

RE.blurFocus = function () {
    RE.editor.blur();
}

//如果没有焦点就重写获取焦点
RE.reFocus = function () {
    if (!RE.editor.isFocused()) {
        RE.editor.restoreSelection()
    }
}

//清除选中的格式
RE.removeFormat = function () {
    // 获取所有 text node
    const nodeEntries = SlateEditor.nodes(editor, {
        match: n => SlateText.isText(n),
        universal: true,
    })
    for (const nodeEntry of nodeEntries) {
        // 单个 text node
        const n = nodeEntry[0]
        RE.removeMarks(editor, n)
    }
}


RE.removeMarks = function (editor, textNode) {
    // 遍历 text node 属性，清除样式
    const keys = Object.keys(textNode);
    keys.forEach(key => {
        if (key === 'text') {
            // 保留 text 属性，text node 必须的
            return
        }
        // 其他属性，全部清除
        RE.editor.removeMark(key)
    })
}

RE.editor.on('change', function () {

    var editStyle = SlateEditor.marks(RE.editor)
    var items = [];
    if (editStyle.bold) {
        var item = {
            type: 'Bold',
            value: null
        }
        items.push(item);
    }
    if (editStyle.italic) {
        var item = {
            type: 'Italic',
            value: null
        }
        items.push(item);
    }
    if (editStyle.sub) {
        var item = {
            type: 'SubScript',
            value: null
        }
        items.push(item);

    }
    if (editStyle.sup) {
        var item = {
            type: 'SuperScript',
            value: null
        }
        items.push(item);
    }
    if (editStyle.underline) {
        var item = {
            type: 'UnderLine',
            value: null
        }
        items.push(item);
    }
    if (editStyle.through) {
        var item = {
            type: 'StrikeThrough',
            value: null
        }
        items.push(item);
    }
    if (editStyle.bgColor != null) {

        var item = {
            type: 'TextBgColor',
            value: editStyle.bgColor
        }
        items.push(item);
    }

    if (editStyle.color != null) {
        var item = {
            type: 'FontColor',
            value: editStyle.color
        }
        items.push(item);
    }
    if (editStyle.fontSize != null) {
        var item = {
            type: 'FontSize',
            value: parseInt(editStyle.fontSize.replace("px", ""))
        }
        items.push(item);
    }

    const fragment = editor.getFragment()

    if (fragment != null) {
        var type = fragment[0].type
        if (type == "header1") {
            var item = {
                type: 'Header',
                value: 1,
            }
            items.push(item);
        }
        if (type == "header2") {
            var item = {
                type: 'Header',
                value: 2
            }
            items.push(item);
        }
        if (type == "header3") {
            var item = {
                type: 'Header',
                value: 3
            }
            items.push(item);
        }
        if (type == "header4") {
            var item = {
                type: 'Header',
                value: 4
            }
            items.push(item);
        }
        if (type == "header5") {
            var item = {
                type: 'Header',
                value: 5
            }
            items.push(item);
        }
        if (type == "header6") {
            var item = {
                type: 'Header',
                value: 6
            }
            items.push(item);
        }
        if (type == "list-item" && fragment[0].ordered == true) {
            var item = {
                type: 'NumberList',
                value: null
            }
            items.push(item);
        }
        if (type == "list-item" && fragment[0].ordered == false) {
            var item = {
                type: 'BulletsList',
                value: null
            }
            items.push(item);
        }
        if (type == "todo") {
            var item = {
                type: 'Todo',
                value: null
            }
            items.push(item);
        }

        if (type == "blockquote") {
            var item = {
                type: 'BlockQuote',
                value: null
            }
            items.push(item);
        }

        if (type == 'pre' && fragment[0].children[0] != null) {
            if (fragment[0].children[0].type == 'code') {
                var item = {
                    type: 'Code',
                    value: null
                }
                items.push(item);
            }
        }

        var textAlign = fragment[0].textAlign
        if (textAlign == "center") {
            var item = {
                type: 'JustifyCenter',
                value: null
            }
            items.push(item);
        }
        if (textAlign == "left") {
            var item = {
                type: 'JustifyLeft',
                value: null
            }
            items.push(item);
        }
        if (textAlign == "right") {
            var item = {
                type: 'JustifyRight',
                value: null
            }
            items.push(item);
        }
        if (textAlign == "justify") {
            var item = {
                type: 'JustifyFull',
                value: null
            }
            items.push(item);
        }

        //判断是否有缩进
        var indent = fragment[0].indent
        if (indent == '2em') {
            var item = {
                type: 'Indent',
                value: null
            }
            items.push(item);
        }

        var fontSize = fragment[0].fontSize
        if (fontSize != null) {
            var item = {
                type: 'FontSize',
                value: parseInt(fontSize.replace("px", ""))
            }
            items.push(item);
        }

    }
    window.WreApp.onTextChange(RE.getText())
    window.WreApp.onContentChange(RE.getHtml());
    window.WreApp.onStyleChange(JSON.stringify(items));
    //    console.log(JSON.stringify(items))
    //    console.log(editStyle)
    // console.log(fragment)
})
