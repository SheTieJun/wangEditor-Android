package com.wangeditor.android.toolbar

import android.graphics.Color


fun getColorInt(colorString: String, mDefValue: Any): Int {
    return runCatching {
        if (colorString.startsWith("#")) {
            Color.parseColor(colorString)
        } else if (colorString.startsWith("rgb")) {
            val colorList = colorString.subSequence(4, colorString.length - 1).split(",")
            Color.rgb(
                colorList[0].toInt(),
                colorList[1].toInt(),
                colorList[2].toInt()
            )
        } else if (colorString.startsWith("rgba")) {
            val colorList = colorString.subSequence(5, colorString.length - 1).split(",")
            Color.argb(
                colorList[3].toInt(),
                colorList[0].toInt(),
                colorList[1].toInt(),
                colorList[2].toInt()
            )
        } else if (colorString.startsWith("argb")) {
            val colorList = colorString.subSequence(5, colorString.length - 1).split(",")
            Color.argb(
                colorList[0].toInt(),
                colorList[1].toInt(),
                colorList[2].toInt(),
                colorList[3].toInt()
            )
        } else {
            Color.parseColor(mDefValue.toString())
        }

    }.onFailure {
        it.printStackTrace()
    }.getOrNull() ?: 0
}