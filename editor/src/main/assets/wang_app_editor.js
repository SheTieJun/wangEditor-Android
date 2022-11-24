const { SlateEditor, SlateTransforms, SlateText, DomEditor } = window.wangEditor

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
    var isTrue = SlateEditor.marks(RE.editor).sup
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
            children: [
                {
                    type: 'code',
                    language: 'Java',
                    children: [
                        { text: codeS }, // 选中节点的纯文本
                    ],
                },
            ],
        }
        SlateTransforms.insertNodes(editor, newPreNode, { mode: 'highest' })
    }
}

RE.setBlockquote = function () {
    if (isDisabledBlockquote()) return
    var active = this.isActive('blockquote')
    var newType = active ? 'paragraph' : 'blockquote'
    console.log(newType + '//' + active)
    SlateTransforms.setNodes(editor, { type: newType })
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

RE.setFontSize = function (fontSize) {
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


RE.insertImage = function (url, alt) {
    RE.reFocus();
    var image = {
        type: 'image',
        src: url,
        style: {
            width: '100%'
        },
        children: [{ text: alt }]
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
        children: [{ text: alt }]
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
        children: [{ text: alt }]
    };
    RE.editor.insertNode(image)
}

RE.insertVideo = function (url, alt) {
    RE.reFocus()
    var video = {
        type: 'video',
        src: url,
        style: {
            width: '100%',
        },
        children: [{ text: '' }]
    };
    RE.editor.insertNode(video)
}

RE.insertVideoW = function (url, width) {
    RE.reFocus()
    var video = {
        type: 'video',
        src: url,
        style: {
            width: width
        },
        children: [{ text: '' }]
    };
    RE.editor.insertNode(video)
}

RE.insertVideoWH = function (url, width, height) {
    RE.reFocus()
    var video = {
        type: 'video',
        src: url,
        style: {
            width: width,
            height: height
        },
        children: [{ text: '' }]
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
        children: [{ text: name }]
    };
    RE.editor.insertNode(image);
}

RE.setTodo = function (text) {
    var active = !!DomEditor.getSelectedNodeByType(editor, 'todo');
    SlateTransforms.setNodes(editor, {
        type: active ? 'paragraph' : 'todo'
    })
}

RE.setTodo = function (text) {
    var active = !!DomEditor.getSelectedNodeByType(editor, 'todo')
    SlateTransforms.setNodes(editor, {
        type: active ? 'paragraph' : 'todo'
    })
}

RE.setDivider = function () {
    var elem = { type: 'divider', children: [{ text: '' }] }
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
    document.execCommand('removeFormat', false, null);
}


RE.editor.on('change', function () {

    var editStyle = SlateEditor.marks(RE.editor)
    var items = [];
    if (editStyle.bold) {
        items.push('Bold');
    }
    if (editStyle.italic) {
        items.push('Italic');
    }
    if (editStyle.sub) {
        items.push('SubScript');
    }
    if (editStyle.sup) {
        items.push('SuperScript');
    }
    if (editStyle.underline) {
        items.push('UnderLine');
    }
    if (editStyle.through) {
        items.push('Through');
    }

   if (editStyle.divider) {
        items.push('Divider');
    }

    const fragment = editor.getFragment()

    if (fragment != null) {
        var type = fragment[0].type
        if (type == "header1") {
            items.push('header1');
        }
        if (type == "header2") {
            items.push('header2');
        }
        if (type == "header3") {
            items.push('header3');
        }
        if (type == "header4") {
            items.push('header4');
        }
        if (type == "header5") {
            items.push('header5');
        }
        if (type == "header6") {
            items.push('header6');
        }
        if (type == "list-item" && fragment[0].ordered == true) {
            items.push('NumberList');
        }
        if (type == "list-item" && fragment[0].ordered == false) {
            items.push('BulletsList');
        }
        if (type == "todo") {
            items.push('Todo');
        }

        if (type == 'pre' && fragment[0] != null) {
            if (fragment[0].children[0].type == 'code') {
                items.push('Code');
            }
        }

        var textAlign = fragment[0].textAlign
        if (textAlign == "center") {
            items.push('JustifyCenter');
        }
        if (textAlign == "left") {
            items.push('JustifyLeft');
        }
        if (textAlign == "right") {
            items.push('JustifyRight');
        }
        if (textAlign == "justify") {
            items.push('JustifyFull');
        }

        //判断是否有缩进
        var indent = fragment[0].indent
        if (indent == '2em') {
            items.push('Indent');
        }

    }
    window.location.href = "re-state://" + encodeURI(items.join(','));
    window.location.href = "re-callback://" + encodeURIComponent(RE.getHtml());
    console.log(editStyle)
    console.log(fragment)
})


