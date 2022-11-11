package com.wangeditor.android

import android.R.attr
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.net.http.SslError
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wangeditor.android.RichUtils.initKeyboard
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.concurrent.CopyOnWriteArrayList

open class WangRichEditor @SuppressLint("SetJavaScriptEnabled") constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : WebView(context, attrs, defStyleAttr) {




    interface OnTextChangeListener {
        fun onTextChange(text: String?)
    }

    interface OnDecorationStateListener {
        fun onStateChangeListener(text: String, types: List<RichType>)
    }

    interface AfterInitialLoadListener {
        fun onAfterInitialLoad(isReady: Boolean)
    }

    private var isReady = false
    private var mContents: String? = null
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
        loadUrl(SETUP_HTML)
        setWebContentsDebuggingEnabled(true)
    }

    private fun addOnLayoutChangeUpdateHeight() {
        addOnLayoutChangeListener { v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int ->
            val dp = px2dp(height.toFloat())
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
        if (!mTextChangeListenerList!!.contains(listener)){
            mTextChangeListenerList?.add(listener)
            listener.onTextChange(mContents)
        }
    }

    fun removeTextChangeListener(listener: OnTextChangeListener){
        mTextChangeListenerList?.remove(listener)
    }

    fun addOnDecorationChangeListener(listener: OnDecorationStateListener) {
        if (mDecorationStateListenerList == null){
            mDecorationStateListenerList = CopyOnWriteArrayList()
        }
        if (!mDecorationStateListenerList!!.contains(listener)){
            mDecorationStateListenerList!!.add(listener)
        }
    }


    fun removeDecorationChangeListener(listener: OnDecorationStateListener){
        mDecorationStateListenerList?.remove(listener)
    }

    fun setOnInitialLoadListener(listener: AfterInitialLoadListener?) {
        mLoadListener = listener
    }

    private fun callback(text: String) {
        mContents = text.replaceFirst(CALLBACK_SCHEME.toRegex(), "")
        if (mTextChangeListenerList != null) {
            mTextChangeListenerList!!.forEach {
                it.onTextChange(mContents)
            }
        }
    }

    private fun stateCheck(text: String) {
        val state = text.replaceFirst(STATE_SCHEME.toRegex(), "").uppercase()
        val types: MutableList<RichType> = ArrayList()
        for (type in RichType.values()) {
            if (TextUtils.indexOf(state, type.name) != -1) {
                types.add(type)
            }
        }
        if (mDecorationStateListenerList != null) {
            mDecorationStateListenerList!!.forEach {
                it.onStateChangeListener(state, types)
            }
        }
    }


    private var density = -1f
        get() {
            if (field <= 0f) {
                field = Resources.getSystem().displayMetrics.density
            }
            return field
        }


    private fun px2dp(pxValue: Float): Int {
        return (pxValue / density + 0.5f).toInt()
    }

    // No handling
    var html: String?
        get() = mContents
        set(contents) {
            try {
                exec("javascript:RE.setHtml('" + URLEncoder.encode(contents?:"", "UTF-8") + "');")
            } catch (e: UnsupportedEncodingException) {
                // No handling
            }
            mContents = contents?:""
        }

    fun disable() {
        exec("javascript:RE.disable();")
    }

    fun enable() {
        exec("javascript:RE.enable();")
    }

    fun setEditorFontColor(color: Int) {
        val hex = convertHexColorString(color)
        exec("javascript:RE.setBaseTextColor('$hex');")
    }

    fun setEditorFontSize(px: Int) {
        exec("javascript:RE.setBaseFontSize('" + px + "px');")
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
            "javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
                    + "px');"
        )
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        // still not support RTL.
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

    fun setEditorCursorColor(color: Int){
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

    fun setPlaceholder(placeholder: String) {
        exec("javascript:RE.setPlaceholder('$placeholder');")
    }

    fun setInputEnabled(inputEnabled: Boolean) {
        exec("javascript:RE.setInputEnabled($inputEnabled)")
    }

    /**
     * 动态添加css
     * @param cssFile
     */
    fun loadCSS(cssFile: String) {
        val jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +
                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +
                "}) ();"
        exec("javascript:$jsCSSImport")
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
    fun setFontSize(fontSize: Int) {
        exec("javascript:RE.setFontSize('$fontSize'px);")
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
     * the image according to the specific width of the image automatically
     *
     * @param url
     * @param alt
     * @param width
     */
    fun insertImage(url: String, alt: String, width: Int) {
        exec("javascript:RE.insertImageW('$url', '$alt','$width');")
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
        exec("javascript:RE.insertImageWH('$url', '$alt','$width', '$height');")
    }

    fun insertVideo(url: String) {
        exec("javascript:RE.insertVideo('$url');")
    }

    fun insertVideo(url: String, width: Int) {
        exec("javascript:RE.insertVideoW('$url', '$width');")
    }

    fun insertVideo(url: String, width: Int, height: Int) {
        exec("javascript:RE.insertVideoWH('$url', '$width', '$height');")
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


    fun insertCode(){
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
            val dp = px2dp(height.toFloat())
            if (dp == 0) return
            setEditorHeight(dp, isFocused)
        } else {
            postDelayed({
                val dp = px2dp(height.toFloat())
                if (dp == 0) return@postDelayed
                setEditorHeight(dp, isFocused)
            }, 100)
        }
    }

    /**
     * 添加键盘监听
     * @param activity
     */
    open fun initKeyboardChange(activity: Activity?) {
        initKeyboard(activity!!, {
            clearFocusEditor()
        }) {
            postDelayed({ updateHeightByKeyboard() }, 50)
        }
    }

    private fun convertHexColorString(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    protected fun exec(trigger: String) {
        if (isReady) {
            load(trigger)
        } else {
            postDelayed({ exec(trigger) }, 100)
        }
    }

    private fun load(trigger: String) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null)
        } else {
            loadUrl(trigger)
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
            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode)
                return true
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode)
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url = request.url.toString()
            val decode = Uri.decode(url)
            println(decode)
            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode)
                return true
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode)
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    private var mHeight = 0

    //实例化WebViwe后，调用此方法可滚动到底部
    fun scrollToBottom() {
        val temp = computeVerticalScrollRange()
        val valueAnimator = ValueAnimator.ofInt(height, temp)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener { animation: ValueAnimator ->
            val nowHeight = animation.animatedValue as Int
            mHeight = nowHeight
            scrollTo(0, height)
            if (height == temp) {
                //再调用一次，解决不能滑倒底部
                scrollTo(0, computeVerticalScrollRange())
            }
        }
        valueAnimator.start()
    }

    override fun destroy() {
        mDecorationStateListenerList?.clear()
        mTextChangeListenerList?.clear()
        super.destroy()
    }

    companion object {
        //wang_editor.html
        private const val TAG = "RichEditor"
        private const val SETUP_HTML = "file:///android_asset/wang_editor.html"
        private const val CALLBACK_SCHEME = "re-callback://"
        private const val STATE_SCHEME = "re-state://"
    }
}