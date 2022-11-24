// Establish connection with app
let port = browser.runtime.connectNative("browser");
port.onMessage.addListener(response => {

         console.log(response)

    if (response.event == "RE.setHtml") {
        RE.setHtml(response.content)
        return
    }
    if (response.event == "RE.disable") {
        RE.disable()
        return
    }
    if (response.event == "RE.enable") {
        RE.enable()
        return
    }

    if (response.event == "RE.setPadding") {
        RE.setPadding(response.content)
        return
    }


    if (response.event == "RE.setBackgroundColor") {
        RE.setBackgroundColor(response.content)
        return
    }

    if (response.event == "RE.setBackgroundImage") {
        RE.setBackgroundImage(response.content)
        return
    }
    if (response.event == "RE.setCaretColor") {
        RE.setCaretColor(response.content)
        return
    }

    if (response.event == "RE.setWidth") {
        RE.setWidth(response.content)
        return
    }
    if (response.event == "RE.setHeight") {
        RE.setHeight(response.content)
        return
    }


    if (response.event == "RE.setInputEnabled") {
        RE.setInputEnabled(response.content)
        return
    }
    if (response.event == "RE.undo") {
        RE.undo()
        return
    }

    if (response.event == "RE.setBold") {
        RE.setBold()
        return
    }
    if (response.event == "RE.redo") {
        RE.redo()
        return
    }

    if (response.event == "RE.setItalic") {
        RE.setItalic()
        return
    }
    if (response.event == "RE.setSubscript") {
        RE.setSubscript()
        return
    }

    if (response.event == "RE.setSuperscript") {
        RE.setSuperscript()
        return
    }


    if (response.event == "RE.setStrikeThrough") {
        RE.setStrikeThrough()
        return
    }

    if (response.event == "RE.setUnderline") {
        RE.setUnderline()
        return
    }



    if (response.event == "RE.setBullets") {
        RE.setBullets()
        return
    }

    if (response.event == "RE.setNumbers") {
        RE.setNumbers()
        return
    }



    if (response.event == "RE.setCode") {
        RE.setCode()
        return
    }

    if (response.event == "RE.setBlockquote") {
        RE.setBlockquote()
        return
    }

    if (response.event == "RE.setIndent") {
        RE.setIndent()
        return
    }


    if (response.event == "RE.setOutdent") {
        RE.setOutdent()
        return
    }

    if (response.event == "RE.setParagraph") {
        RE.setParagraph()
        return
    }



    if (response.event == "RE.setTextColor") {
        RE.setTextColor(response.content)
        return
    }

    if (response.event == "RE.setFontSize") {
        RE.setFontSize(response.content)
        return
    }

    if (response.event == "RE.setTextBackgroundColor") {
        RE.setTextBackgroundColor(response.content)
        return
    }

    if (response.event == "RE.setFontSize") {
        RE.setFontSize(response.content)
        return
    }

    if (response.event == "RE.setHeading") {
        RE.setHeading(response.content)
        return
    }



    if (response.event == "RE.setJustifyLeft") {
        RE.setJustifyLeft()
        return
    }

    if (response.event == "RE.setJustifyCenter") {
        RE.setJustifyCenter()
        return
    }


    if (response.event == "RE.setJustifyRight") {
        RE.setJustifyLeft()
        return
    }


    if (response.event == "RE.insertImage") {
        RE.insertImage(response.content)
        return
    }

    if (response.event == "RE.insertImageW") {
        RE.insertImageW(response.content)
        return
    }

    if (response.event == "RE.insertImageWH") {
        RE.insertImageWH(response.content)
        return
    }

    if (response.event == "RE.insertVideo") {
        RE.insertVideo(response.content)
        return
    }

    if (response.event == "RE.insertVideoW") {
        RE.insertVideoW(response.content)
        return
    }

    if (response.event == "RE.insertVideoWH") {
        RE.insertVideoWH(response.content)
        return
    }


    if (response.event == "RE.insertAudio") {
        RE.insertAudio(response.content)
        return
    }

    if (response.event == "RE.insertLink") {
        RE.insertLink(response.content)
        return
    }

    if (response.event == "RE.setTodo") {
        RE.setTodo()
        return
    }

    if (response.event == "RE.focus") {
        RE.focus()
        return
    }


    if (response.event == "RE.blurFocus") {
        RE.blurFocus()
        return
    }


    if (response.event == "RE.reFocus") {
        RE.reFocus()
        return
    }


    if (response.event == "RE.removeFormat") {
        RE.removeFormat()
        return
    }
})