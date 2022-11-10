package com.wangeditor.android

import android.app.Activity
import android.content.res.Resources
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import java.util.regex.Pattern

/**
 * Created by leo
 * on 2020/9/18.
 */
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
            "<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\u000c>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic|\\b)\\b)[^>]*>",
            Pattern.CASE_INSENSITIVE
        )
        val m = p.matcher(content)
        var quote: String? = null
        var src: String? = null
        while (m.find()) {
            quote = m.group(1)
            src = if (quote == null || quote.trim { it <= ' ' }.length == 0) m.group(2).split("//s+".toRegex())
                .toTypedArray()[0] else m.group(2)
            imageSrcList.add(src)
        }
        return imageSrcList as ArrayList<String?>
    }

    /**
     * 截取富文本中的纯文本内容
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

    fun isEmpty(htmlStr: String): Boolean {
        val images = returnImageUrlsFromHtml(htmlStr)
        val text = returnOnlyText(htmlStr)
        return TextUtils.isEmpty(text) && images.size == 0
    }


    @JvmStatic
    fun initKeyboard(activity: Activity,onKeyboardHide:() ->Unit,onKeyboardShow:()->Unit) {
        val mLayoutDelay = 0
        var mPreviousKeyboardHeight = 0
        var mKeyboardHeight = 0
        val window: Window = activity.window
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
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
            })
    }


}