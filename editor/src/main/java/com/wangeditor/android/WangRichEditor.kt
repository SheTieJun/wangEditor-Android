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

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.net.http.SslError
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.wangeditor.android.RichUtils.initKeyboard
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.concurrent.CopyOnWriteArrayList
import org.json.JSONArray

open class WangRichEditor @SuppressLint("SetJavaScriptEnabled") constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : WebView(context, attrs, defStyleAttr), IEditorWeb {

    interface OnContentChangeListener {
        fun onContentChange(html: String?)
    }

    interface OnTextChangeListener {
        fun onTextChange(text: String?)
    }

    interface OnDecorationStateListener {
        fun onStateChangeListener(types: List<StyleItem>)
    }

    interface AfterInitialLoadListener {
        fun onAfterInitialLoad(isReady: Boolean)
    }

    private var isReady = false
    private var mContents: String? = null
    private var mContentChangeListenerList: CopyOnWriteArrayList<OnContentChangeListener>? = null
    private var mTextChangeListenerList: CopyOnWriteArrayList<OnTextChangeListener>? = null
    private var mDecorationStateListenerList: CopyOnWriteArrayList<OnDecorationStateListener>? = null
    private var mLoadListener: AfterInitialLoadListener? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, attr.webViewStyle) {
    }

    init {
        init()
        addOnLayoutChangeUpdateHeight()
    }

    private fun init() {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        settings.javaScriptEnabled = true
        webChromeClient = WebChromeClient()
        webViewClient = createWebviewClient()
        addJavascriptInterface(EditorJSBridge(this), "WreApp")
        loadUrl(SETUP_HTML)
        setWebContentsDebuggingEnabled(true)
    }

    private fun addOnLayoutChangeUpdateHeight() {
        addOnLayoutChangeListener { v: View?, left: Int, top: Int, right: Int,
            bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int ->
            val dp = Utils.px2dp(height.toFloat())
            if (dp == 0) return@addOnLayoutChangeListener
            setEditorHeight(dp)
        }
    }

    fun createWebviewClient(): EditorWebViewClient {
        return EditorWebViewClient()
    }

    fun addOnTextChangeListener(listener: OnTextChangeListener) {
        if (mTextChangeListenerList == null) {
            mTextChangeListenerList = CopyOnWriteArrayList()
        }
        if (!mTextChangeListenerList!!.contains(listener)) {
            mTextChangeListenerList?.add(listener)
        }
    }

    fun removeTextChangeListener(listener: OnTextChangeListener) {
        mTextChangeListenerList?.remove(listener)
    }


    fun addOnContentChangeListener(listener: OnContentChangeListener) {
        if (mContentChangeListenerList == null) {
            mContentChangeListenerList = CopyOnWriteArrayList()
        }
        if (!mContentChangeListenerList!!.contains(listener)) {
            mContentChangeListenerList?.add(listener)
            listener.onContentChange(mContents)
        }
    }

    fun removeContentChangeListener(listener: OnContentChangeListener) {
        mContentChangeListenerList?.remove(listener)
    }

    fun addOnDecorationChangeListener(listener: OnDecorationStateListener) {
        if (mDecorationStateListenerList == null) {
            mDecorationStateListenerList = CopyOnWriteArrayList()
        }
        if (!mDecorationStateListenerList!!.contains(listener)) {
            mDecorationStateListenerList!!.add(listener)
        }
    }

    fun removeDecorationChangeListener(listener: OnDecorationStateListener) {
        mDecorationStateListenerList?.remove(listener)
    }

    fun setOnInitialLoadListener(listener: AfterInitialLoadListener?) {
        mLoadListener = listener
    }

    /**
     *
     * @param contents
     */
    fun setHtml(contents: String?) {
        mContents = if (contents.isNullOrEmpty()) {
            "<p><br><p>"
        } else {
            contents
        }
        try {
            exec("javascript:RE.setHtml('" + URLEncoder.encode(mContents ?: "<p><br><p>", "UTF-8") + "');")
        } catch (e: UnsupportedEncodingException) {
            // No handling
        }
    }

    fun getHtml() = mContents

    fun disable() {
        exec("javascript:RE.disable();")
    }

    fun enable() {
        exec("javascript:RE.enable();")
    }

    /**
     * html px = androd dp
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
//        super.setPadding(left, top, right, bottom)
        exec(
            "javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom +
                "px');"
        )
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        setPadding(start, top, end, bottom)
    }

    fun setEditorBackgroundColor(color: Int) {
        setBackgroundColor(color)
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resid: Int) {
        val bitmap = Utils.decodeResource(context, resid)
        val base64 = Utils.toBase64(bitmap)
        bitmap.recycle()
        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64,$base64)');")
    }

    override fun setBackground(background: Drawable) {
        val bitmap = Utils.toBitmap(background)
        val base64 = Utils.toBase64(bitmap)
        bitmap.recycle()
        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64,$base64)');")
    }

    fun setBackground(url: String) {
        exec("javascript:RE.setBackgroundImage('url($url)');")
    }

    fun setEditorCursorColor(color: Int) {
        val hex = convertHexColorString(color)
        exec("javascript:RE.setCaretColor('$hex');")
    }

    fun setEditorWidth(px: Int) {
        exec("javascript:RE.setWidth('" + px + "px');")
    }

    fun setEditorHeight(px: Int) {
        exec("javascript:RE.setHeight('" + px + "px');")
    }

    /**
     * 设置高度，并且滚动到选中位置
     * @param needF 会自动滚到到选中位置
     */
    fun setEditorHeight(px: Int, needF: Boolean) {
        exec("javascript:RE.setHeight('" + px + "px'," + needF + ");")
    }

    fun setInputEnabled(inputEnabled: Boolean) {
        exec("javascript:RE.setInputEnabled($inputEnabled)")
    }

    fun undo() {
        exec("javascript:RE.undo();")
    }

    fun redo() {
        exec("javascript:RE.redo();")
    }

    fun setBold() {
        exec("javascript:RE.setBold();")
    }

    fun setItalic() {
        exec("javascript:RE.setItalic();")
    }

    fun setSubscript() {
        exec("javascript:RE.setSubscript();")
    }

    fun setSuperscript() {
        exec("javascript:RE.setSuperscript();")
    }

    fun setStrikeThrough() {
        exec("javascript:RE.setStrikeThrough();")
    }

    fun setUnderline() {
        exec("javascript:RE.setUnderline();")
    }

    fun setTextColor(color: Int) {
        val hex = convertHexColorString(color)
        exec("javascript:RE.setTextColor('$hex');")
    }

    fun setTextBackgroundColor(color: Int) {
        val hex = convertHexColorString(color)
        exec("javascript:RE.setTextBackgroundColor('$hex');")
    }

    /**
     * 字体大小px
     *
     * @param fontSize 默认18
     */
    fun setTextFontSize(fontSize: Int) {
        exec("javascript:RE.setTextFontSize('${fontSize}px');")
    }

    fun removeFormat() {
        exec("javascript:RE.removeFormat();")
    }

    fun setHeading(heading: Int) {
        exec("javascript:RE.setHeading('$heading');")
    }

    fun setParagraph() {
        exec("javascript:RE.setParagraph();")
    }

    fun setIndent() {
        exec("javascript:RE.setIndent();")
    }

    fun setOutdent() {
        exec("javascript:RE.setOutdent();")
    }

    fun setAlignLeft() {
        exec("javascript:RE.setJustifyLeft();")
    }

    fun setAlignCenter() {
        exec("javascript:RE.setJustifyCenter();")
    }

    fun setAlignRight() {
        exec("javascript:RE.setJustifyRight();")
    }

    fun setBlockquote() {
        exec("javascript:RE.setBlockquote();")
    }

    fun setBullets() {
        exec("javascript:RE.setBullets();")
    }

    fun setNumbers() {
        exec("javascript:RE.setNumbers();")
    }

    fun insertImage(url: String, alt: String) {
        exec("javascript:RE.insertImage('$url', '$alt');")
    }

    /**
     * tip :请不要使用这个图片插入大图，否则会非常的卡顿
     * @param url 本地图片
     */
    fun insertImageBase64(url: String) {
        kotlin.runCatching {
            if (!File(url).exists()){
                Log.e("WangEditor","insertImageBase64 only support local file")
                return
            }
            val base64 = Utils.toBase64(decodeFile(url))
            exec("javascript:RE.insertImageBase64('data:image/png;base64,$base64');")
        }.onFailure {
            it.printStackTrace()
        }
    }

    /**
     * the image according to the specific width of the image automatically
     *
     * @param url
     * @param alt
     * @param width
     */
    fun insertImage(url: String, alt: String, width: Int) {
        exec("javascript:RE.insertImageW('$url', '$alt',,${width}px');")
    }

    /**
     * [WangRichEditor.insertImage] will show the original size of the image.
     * So this method can manually process the image by adjusting specific width and height to fit into different mobile screens.
     *
     * @param url
     * @param alt
     * @param width
     * @param height
     */
    fun insertImage(url: String, alt: String, width: Int, height: Int) {
        exec("javascript:RE.insertImageWH('$url', '$alt',${width}px', '${height}px');")
    }

    /**
     * Insert video
     *
     * @param url
     * @param thumbURL 预览图片
     */
    fun insertVideo(url: String, thumbURL: String = "") {
        exec("javascript:RE.insertVideo('$url','$thumbURL');")
    }

    fun insertVideo(url: String, thumbURL: String = "", width: Int) {
        exec("javascript:RE.insertVideoW('$url','$thumbURL', '${width}px');")
    }

    fun insertVideo(url: String, thumbURL: String = "", width: Int, height: Int) {
        exec("javascript:RE.insertVideoWH('$url', '$thumbURL', '${width}px', '${height}px');")
    }

    fun insertAudio(url: String) {
        exec("javascript:RE.insertAudio('$url');")
    }

    fun insertLink(href: String, defText: String) {
        exec("javascript:RE.insertLink('$href', '$defText');")
    }

    fun insertTodo() {
        exec("javascript:RE.setTodo();")
    }

    fun insertDivider() {
        exec("javascript:RE.setDivider();")
    }

    fun insertCode() {
        exec("javascript:RE.setCode();")
    }

    fun focusEditor() {
        requestFocus()
        exec("javascript:RE.focus();")
    }

    fun clearFocusEditor() {
        exec("javascript:RE.blurFocus();")
    }

    private fun updateHeightByKeyboard() {
        if (isReady) {
            val dp = Utils.px2dp(height.toFloat())
            if (dp == 0) return
            setEditorHeight(dp, isFocused)
        } else {
            postDelayed({
                val dp = Utils.px2dp(height.toFloat())
                if (dp == 0) return@postDelayed
                setEditorHeight(dp, isFocused)
            }, 100)
        }
    }

    /**
     * 添加键盘监听
     * @param activity
     */
    open fun initKeyboardChange(
        activity: FragmentActivity,
        onKeyboardHide: (() -> Unit)? = null,
        onKeyboardShow: (() -> Unit)? = null
    ) {
        initKeyboard(activity, {
            clearFocusEditor()
            onKeyboardHide?.invoke()
        }) {
            postDelayed({ updateHeightByKeyboard() }, 50)
            onKeyboardShow?.invoke()
        }
    }

    private fun convertHexColorString(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    protected fun exec(trigger: String) {
        if (isReady) {
            evaluateJavascript(trigger, null)
        } else {
            postDelayed({ exec(trigger) }, 100)
        }
    }

    inner class EditorWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            isReady = url.equals(SETUP_HTML, ignoreCase = true)
            if (mLoadListener != null) {
                mLoadListener!!.onAfterInitialLoad(isReady)
            }
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed()
        }

        @Suppress("DEPRECATION")
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val decode = Uri.decode(url)
            Utils.logInfo(decode)
            if (url.startsWith("http")) {
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                intent.data = Uri.parse(url)
                startActivity(context, intent, null)
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url = request.url.toString()
            val decode = Uri.decode(url)
            Utils.logInfo(decode)
            if (url.startsWith("http")) {
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                intent.data = Uri.parse(url)
                startActivity(context, intent, null)
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }
    }

    private fun callback(richText: String) {
        mContents = richText
        if (mContentChangeListenerList != null) {
            post {
                mContentChangeListenerList!!.forEach {
                    it.onContentChange(mContents)
                }
            }
        }
    }

    private fun stateCheck(styleJson: String?) {
        Utils.logInfo(styleJson)
        if (styleJson.isNullOrEmpty()) {
            if (mDecorationStateListenerList != null) {
                mDecorationStateListenerList!!.forEach {
                    it.onStateChangeListener(emptyList())
                }
            }
            return
        }
        val jsonArray = JSONArray(styleJson)
        val length = jsonArray.length()
        val types: MutableList<StyleItem> = ArrayList()
        for (i in 0 until length) {
            val any = jsonArray.getJSONObject(i)
            val item = StyleItem(any.optString("type"), any.opt("value"))
            types.add(item)
        }
        if (mDecorationStateListenerList != null) {
            mDecorationStateListenerList!!.forEach {
                it.onStateChangeListener(types)
            }
        }
    }

    override fun destroy() {
        mDecorationStateListenerList?.clear()
        mContentChangeListenerList?.clear()
        super.destroy()
    }

    companion object {
        private const val SETUP_HTML = "file:///android_asset/wang_editor.html"
    }

    override fun onTextChange(onlyText: String) {
        if (mTextChangeListenerList != null) {
            post {
                mTextChangeListenerList!!.forEach {
                    it.onTextChange(onlyText)
                }
            }
        }
    }

    override fun onContentChange(richText: String?) {
        Utils.logInfo(richText)
        callback(richText ?: "")
    }

    override fun onStyleChange(styleJson: String?) {
        Utils.logInfo(styleJson)
        stateCheck(styleJson)
    }
}
