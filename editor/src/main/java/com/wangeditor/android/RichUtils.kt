/*
 * MIT License
 *
 * Copyright (c) 2022 SheTieJun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.wangeditor.android

import android.content.res.Resources
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.regex.Pattern

object RichUtils {
    /**
     * 截取富文本中的图片链接
     */
    @JvmStatic
    fun returnImageUrlsFromHtml(content: String?): ArrayList<String?> {
        val imageSrcList: MutableList<String?> = ArrayList()
        if (TextUtils.isEmpty(content)) {
            return imageSrcList as ArrayList<String?>
        }
        val p = Pattern.compile(
            "<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\u000c>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif" +
                    "|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut" +
                    "|\\.pic|\\b)\\b)[^>]*>",
            Pattern.CASE_INSENSITIVE
        )
        val m = p.matcher(content)
        var quote: String? = null
        var src: String? = null
        while (m.find()) {
            quote = m.group(1)
            src = if (quote == null || quote.trim { it <= ' ' }.isEmpty()) m.group(2).split("//s+".toRegex())
                .toTypedArray()[0] else m.group(2)
            imageSrcList.add(src)
        }
        return imageSrcList as ArrayList<String?>
    }

    /**
     * 截取富文本中的纯文本内容
     * 当有base64的时候时间太久了
     */
    @JvmStatic
    fun returnOnlyText(htmlStr: String): String {
        return if (TextUtils.isEmpty(htmlStr)) {
            ""
        } else {
            val regFormat = "\\s*|\t|\r|\n"
            val regTag = "<[^>]*>"
            var text = htmlStr.replace(regFormat.toRegex(), "").replace(regTag.toRegex(), "")
            text = text.replace("&nbsp;", "")
            text
        }
    }

    fun isEmpty(htmlStr: String?): Boolean {
        if (htmlStr.isNullOrEmpty()) return true
        val images = returnImageUrlsFromHtml(htmlStr)
        val text = returnOnlyText(htmlStr)
        return TextUtils.isEmpty(text) && images.size == 0
    }

    @JvmStatic
    fun initKeyboard(activity: FragmentActivity, onKeyboardHide: () -> Unit, onKeyboardShow: () -> Unit) {
        val mLayoutDelay = 0
        var mPreviousKeyboardHeight = 0
        var mKeyboardHeight = 0
        val window: Window = activity.window
        val rootView = window.decorView.findViewById<View>(android.R.id.content)

        val listener = object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (mLayoutDelay == 0) {
                    init()
                    return
                }
                rootView.postDelayed({ init() }, mLayoutDelay.toLong())
            }

            private fun init() {
                val r = Rect()
                val view = window.decorView
                view.getWindowVisibleDisplayFrame(r)
                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val keyboardHeight = screenHeight - r.bottom
                if (mPreviousKeyboardHeight != keyboardHeight) {
                    if (keyboardHeight > 100) {
                        mKeyboardHeight = keyboardHeight
                        onKeyboardShow()
                    } else {
                        onKeyboardHide()
                    }
                }
                mPreviousKeyboardHeight = keyboardHeight
            }
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        activity.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Event) {
                if (event == Event.ON_DESTROY) {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        })
    }

    /**
     * Return def text
     * 移除标签，只保留换行
     * @param htmlStr 富文本内容
     * @return
     */
    fun returnDefText(htmlStr: String): String {
        var defText = htmlStr
        if (TextUtils.isEmpty(defText)) {
            return ""
        }
        defText = defText.replace("<br>".toRegex(), "\n")
            .replace("&emsp;".toRegex(), "")
            .replace("&nbsp;".toRegex(), "")
            .replace("</p>".toRegex(), "\n")
            .replace("</h1>".toRegex(), "\n")
            .replace("</h2>".toRegex(), "\n")
            .replace("</h3>".toRegex(), "\n")
            .replace("</h4>".toRegex(), "\n")
            .replace("<br/>".toRegex(), "\n")
            .replace("</li>".toRegex(), "\n")
            .replace("<[^>]+>".toRegex(), "")
        return defText
    }
}
