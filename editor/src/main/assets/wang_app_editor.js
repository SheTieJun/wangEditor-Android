const { SlateEditor, SlateTransforms, SlateText } = window.wangEditor

var RE = {};

RE.editor = editor;

//容器
RE.editorContainer = document.getElementById('editor-container');


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

RE.setBaseTextColor = function (color) {
    RE.editor.style.color = color;
}

RE.setBaseFontSize = function (size) {
    RE.editor.style.fontSize = size;
}

RE.setPadding = function (left, top, right, bottom) {
    RE.editorContainer.style.paddingLeft = left;
    RE.editorContainer.style.paddingTop = top;
    RE.editorContainer.style.paddingRight = right;
    RE.editorContainer.style.paddingBottom = bottom;
}

RE.setBackgroundColor = function (color) {
    document.body.style.backgroundColor = color;
}

RE.setBackgroundImage = function (image) {
    RE.editorContainer.style.backgroundImage = image;
}

RE.setWidth = function (size) {
    RE.editorContainer.style.minWidth = size;
}

RE.setHeight = function (size) {
    RE.editorContainer.style.height = size;
}

// 专门给键盘出来进行高度变化使用的
RE.setHeight = function (size,focus) {
    if(focus){
        RE.editor.blur()
    }
    RE.editorContainer.style.height = size;
    if(focus){
        RE.editor.restoreSelection()
    }
}

RE.setTextAlign = function (align) {
    RE.editor.style.textAlign = align;
}

RE.setVerticalAlign = function (align) {
    RE.editor.style.verticalAlign = align;
}

RE.setPlaceholder = function (placeholder) {
    editorConfig.placeholder = placeholder;
}

RE.setInputEnabled = function (inputEnabled) {
    if (inputEnabled) {
        editor.enable()
    } else {
        editor.disable()
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

RE.setTextColor = function (color) {
    RE.editor.addMark('color', color)
}

RE.setTextBackgroundColor = function (color) {
    RE.editor.addMark('bgColor', color)
}

RE.setFontSize = function (fontSize) {
    RE.editor.addMark('fontSize', fontSize)
}

RE.setHeading = function (heading) {
    SlateTransforms.setNodes(editor, {
        type: 'header' + heading,
    })
}

RE.setParagraph = function () {
    SlateTransforms.setNodes(editor, {
        type: 'paragraph',
    })
}

RE.setIndent = function () {
    SlateTransforms.setNodes(editor, {
        indent: '2em'
    })
}

RE.setOutdent = function () {
    SlateTransforms.setNodes(RE.editor, {
        indent: '0em'
    })
}

RE.setJustifyLeft = function () {
    SlateTransforms.setNodes(editor, {
        textAlign: 'left',
    })
}

RE.setJustifyCenter = function () {
    SlateTransforms.setNodes(editor, {
        textAlign: 'center',
    })
}

RE.setJustifyRight = function () {
    SlateTransforms.setNodes(editor, {
        textAlign: 'right',
    })
}

RE.setBlockquote = function () {
    document.execCommand('formatBlock', false, '<blockquote>');
}

RE.insertImage = function (url, alt) {
    RE.reFocus()
    var image = {
        type: 'image',
        src: url,
        style: {
            width: '100%'
        },
        children: [{ text: alt }]
    }
    RE.editor.insertNode(image)
}

RE.insertImageW = function (url, alt, width) {
    RE.reFocus()
    var image = {
        type: 'image',
        src: url,
        style: {
            width: width
        },
        children: [{ text: alt }]
    }
    RE.editor.insertNode(image)
}



RE.insertImageWH = function (url, alt, width, height) {
    RE.reFocus()
    var image = {
        type: 'image',
        src: url,
        style: {
            width: width,
            height: height
        },
        children: [{ text: alt }]
    }
    RE.editor.insertNode(image)
}

RE.insertVideo = function (url, alt) {
    RE.reFocus()
    var image = {
        type: 'video',
        src: url,
        style: {
            width: '100%',
        },
        children: [{ text: '' }]
    }
    RE.editor.insertNode(image)
}

RE.insertVideoW = function (url, width) {
    RE.reFocus()
    var image = {
        type: 'video',
        src: url,
        style: {
            width: width
        },
        children: [{ text: '' }]
    }
    RE.editor.insertNode(image)
}

RE.insertVideoWH = function (url, width, height) {
    RE.reFocus()
    var image = {
        type: 'video',
        src: url,
        style: {
            width: width,
            height: height
        },
        children: [{ text: '' }]
    }
    RE.editor.insertNode(image)
}

RE.insertAudio = function (url, alt) {
    var html = '<audio src="' + url + '" controls></audio><br>';
    RE.insertHTML(html);
}


RE.insertHTML = function (html) {
    RE.reFocus()
    document.execCommand('insertHTML', false, html);
}

RE.insertLink = function (url, title) {
    var image = {
        type: 'link',
        url: url,
        target: title,
        children: [{ text: "title" }]
    }
    RE.editor.insertNode(image)
}

RE.setTodo = function (text) {
    var html = '<input type="checkbox" name="' + text + '" value="' + text + '"/> &nbsp;';
    document.execCommand('insertHTML', false, html);
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

RE.removeFormat = function () {
    document.execCommand('removeFormat', false, null);
}


RE.editor.on('change', function () {
    var editStyle = SlateEditor.marks(RE.editor)
    var items = [];
    if (editStyle.bold) {
        items.push('bold');
    }
    if (editStyle.italic) {
        items.push('italic');
    }
    if (editStyle.sub) {
        items.push('subscript');
    }
    if (editStyle.sup) {
        items.push('superscript');
    }
    if (editStyle.underline) {
        items.push('underline');
    }
    if (editStyle.through) {
        items.push('through');
    }

    const fragment = editor.getFragment()
    if (fragment != null) {
        var type = fragment[0].type
        if (type == "header1") {
            items.push('H1');
        }
        if (type == "header2") {
            items.push('H2');
        }
        if (type == "header3") {
            items.push('H3');
        }
        if (type == "header4") {
            items.push('H4');
        }
        if (type == "header5") {
            items.push('H5');
        }
        if (type == "header6") {
            items.push('H6');
        }

        if (type == "list-item" && fragment[0].ordered == true) {
            items.push('orderedList');
        }
        if (type == "list-item" && fragment[0].ordered == false) {
            items.push('unorderedList');
        }
        var textAlign = fragment[0].textAlign
        if (textAlign == "center") {
            items.push('justifyCenter');
        }
        if (textAlign == "left") {
            items.push('justifyLeft');
        }
        if (textAlign == "right") {
            items.push('justifyRight');
        }

        //判断是否有缩进
       var indent =  fragment[0].indent
       if(indent != '2em'){
          items.push('indent');
       }
    }
    window.location.href = "re-state://" + encodeURI(items.join(','));
    window.location.href = "re-callback://" + encodeURIComponent(RE.getHtml());
})


