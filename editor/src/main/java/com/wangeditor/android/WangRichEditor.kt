package com.wangeditor.android

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.net.http.SslError
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import androidx.annotation.Nullable
import com.wangeditor.android.RichUtils.initKeyboard
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.concurrent.CopyOnWriteArrayList
import org.json.JSONException
import org.json.JSONObject
import org.mozilla.geckoview.ContentBlocking
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSession.ProgressDelegate.SecurityInformation
import org.mozilla.geckoview.GeckoSession.SessionState
import org.mozilla.geckoview.GeckoView
import org.mozilla.geckoview.WebExtension
import org.mozilla.geckoview.WebExtension.MessageDelegate
import org.mozilla.geckoview.WebExtension.MessageSender
import org.mozilla.geckoview.WebExtension.Port
import org.mozilla.geckoview.WebExtension.PortDelegate


open class WangRichEditor(context: Context, attrs: AttributeSet?) : GeckoView(context, attrs) {


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
    private var runtime: GeckoRuntime? = null
    private var mPort: Port? = null
    private val contentMap = HashMap<Any, Any>()
    private val portDelegate: PortDelegate = object : PortDelegate {
        override fun onPortMessage(
            message: Any,
            port: Port
        ) {

            Log.d(
                "MessageDelegate", "Received message from WebExtension: "
                        + message
            )
        }

        override fun onDisconnect(port: Port) {
            Log.d("MessageDelegate", "onDisconnect")
            // After this method is called, this port is not usable anymore.
            if (port === mPort) {
                mPort = null
            }
        }
    }

    private val messageDelegate: MessageDelegate = object : MessageDelegate {
        override fun onMessage(
            nativeApp: String,
            message: Any,
            sender: MessageSender
        ): GeckoResult<Any>? {
            //处理数据
            Log.d("MessageDelegate", "onMessage:$message")
            val decode = Uri.decode(message.toString())
            if (TextUtils.indexOf(decode, CALLBACK_SCHEME) == 0) {
                callback(decode)
            } else if (TextUtils.indexOf(decode, STATE_SCHEME) == 0) {
                stateCheck(decode)
            }
            return null
        }

        @Nullable
        override fun onConnect(port: Port) {
            Log.d("MessageDelegate", "onConnect")
            mPort = port
            mPort?.setDelegate(portDelegate)
        }
    }


    init {
        init()
        addOnLayoutChangeUpdateHeight()
    }

    private fun init() {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false

        val session = GeckoSession()
        if (runtime == null) {
            runtime = GeckoRuntime.getDefault(context)
            session.open(runtime!!)
        }

        runtime!!
            .webExtensionController
            .ensureBuiltIn("resource://android/assets/", "messaging@wangEditor.com")
            .accept({ extension: WebExtension? ->
                post {
                    extension?.let {
                        session
                            .webExtensionController
                            .setMessageDelegate(it, messageDelegate, "browser")
                    }
                    extension?.setMessageDelegate(messageDelegate, "browser")
                }
            }
            ) { e: Throwable? ->
                Log.e(
                    "MessageDelegate",
                    "Error registering extension",
                    e
                )
            }
        setSession(session)


        session.loadUri(SETUP_HTML)
        session.progressDelegate = createProgressDelegate()
        session.contentDelegate
        setWebContentsDebuggingEnabled(true)
    }


    private fun createProgressDelegate(): GeckoSession.ProgressDelegate {
        return object : GeckoSession.ProgressDelegate {
            override fun onPageStop(session: GeckoSession, success: Boolean) = Unit
            override fun onSecurityChange(session: GeckoSession, securityInfo: SecurityInformation) = Unit
            override fun onPageStart(session: GeckoSession, url: String) = Unit

            override fun onProgressChange(session: GeckoSession, progress: Int) {


            }

            override fun onSessionStateChange(session: GeckoSession, sessionState: SessionState) {
                super.onSessionStateChange(session, sessionState)
                sessionState.forEach { ss ->
                    Log.i("MessageDelegate", ss.uri)
                }
            }

        }
    }

    private fun addOnLayoutChangeUpdateHeight() {
        addOnLayoutChangeListener { v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int ->
            val dp = px2dp(height.toFloat())
            if (dp == 0) return@addOnLayoutChangeListener
            setEditorHeight(dp)
        }
    }

    fun addOnTextChangeListener(listener: OnTextChangeListener) {
        if (mTextChangeListenerList == null) {
            mTextChangeListenerList = CopyOnWriteArrayList()
        }
        if (!mTextChangeListenerList!!.contains(listener)) {
            mTextChangeListenerList?.add(listener)
            listener.onTextChange(mContents)
        }
    }

    fun removeTextChangeListener(listener: OnTextChangeListener) {
        mTextChangeListenerList?.remove(listener)
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
                contentMap.clear()
                contentMap["content"] = URLEncoder.encode(contents ?: "", "UTF-8")
                exec("RE.setHtml", contentMap)
            } catch (e: UnsupportedEncodingException) {
                // No handling
            }
            mContents = contents ?: ""
        }

    fun disable() {
        contentMap.clear()
        exec("RE.disable", contentMap)
    }

    fun enable() {
        contentMap.clear()
        exec("RE.enable", contentMap)
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
        contentMap.clear()
        contentMap["content"] = "'${left}px','${top}px', '${right}px', '${bottom}px'"
        exec("RE.setPadding", contentMap)
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
        contentMap.clear()
        contentMap["content"] = "'url(data:image/png;base64,$base64)'"
        exec("RE.setBackgroundImage", contentMap)
    }

    override fun setBackground(background: Drawable) {
        val bitmap = Utils.toBitmap(background)
        val base64 = Utils.toBase64(bitmap)
        bitmap.recycle()
        contentMap.clear()
        contentMap["content"] = "'url(data:image/png;base64,$base64)'"
        exec("RE.setBackgroundImage", contentMap)
    }

    fun setBackground(url: String) {
        contentMap.clear()
        contentMap["content"] = "'url($url)'"
        exec("RE.setBackgroundImage", contentMap)
    }

    fun setEditorCursorColor(color: Int) {
        val hex = convertHexColorString(color)
        contentMap.clear()
        contentMap["content"] = "'$hex'"
        exec("RE.setCaretColor", contentMap)
    }

    fun setEditorWidth(px: Int) {
        contentMap.clear()
        contentMap["content"] = "'${px}px'"
        exec("RE.setWidth")
    }

    fun setEditorHeight(px: Int) {
        contentMap.clear()
        contentMap["content"] = "'${px}px'"
        exec("RE.setHeight")
    }

    /**
     * 设置高度，并且滚动到选中位置
     * @param needF 会自动滚到到选中位置
     */
    fun setEditorHeight(px: Int, needF: Boolean) {
        contentMap.clear()
        contentMap["content"] = "'${px}px', $needF"
        exec("RE.setHeight", contentMap)
    }

    fun setPlaceholder(placeholder: String) {
        contentMap.clear()
        contentMap["content"] = "'$placeholder'"
        exec("RE.setPlaceholder")
    }

    fun setInputEnabled(inputEnabled: Boolean) {
        contentMap.clear()
        contentMap["content"] = "$inputEnabled"
        exec("RE.setInputEnabled()")
    }


    fun undo() {
        contentMap.clear()
        exec("RE.undo", contentMap)
    }

    fun redo() {
        contentMap.clear()
        exec("RE.redo", contentMap)
    }

    fun setBold() {
        contentMap.clear()
        exec("RE.setBold", contentMap)
    }

    fun setItalic() {
        contentMap.clear()
        exec("RE.setItalic", contentMap)
    }

    fun setSubscript() {
        contentMap.clear()
        exec("RE.setSubscript", contentMap)
    }

    fun setSuperscript() {
        contentMap.clear()
        exec("RE.setSuperscript", contentMap)
    }

    fun setStrikeThrough() {
        contentMap.clear()
        exec("RE.setStrikeThrough", contentMap)
    }

    fun setUnderline() {
        contentMap.clear()
        exec("RE.setUnderline", contentMap)
    }

    fun setTextColor(color: Int) {
        val hex = convertHexColorString(color)
        contentMap.clear()
        contentMap["content"] = "'$hex'"
        exec("RE.setTextColor")
    }

    fun setTextBackgroundColor(color: Int) {
        val hex = convertHexColorString(color)
        contentMap.clear()
        contentMap["content"] = "'$hex'"
        exec("RE.setTextBackgroundColor")
    }

    /**
     * 字体大小px
     *
     * @param fontSize 默认18
     */
    fun setFontSize(fontSize: Int) {
        contentMap.clear()
        contentMap["content"] = "${fontSize}px"
        exec("RE.setFontSize")
    }

    fun removeFormat() {
        contentMap.clear()
        exec("RE.removeFormat")
    }

    fun setHeading(heading: Int) {
        contentMap.clear()
        contentMap["content"] = "'$heading'"
        exec("RE.setHeading")
    }

    fun setParagraph() {
        contentMap.clear()
        exec("RE.setParagraph")
    }

    fun setIndent() {
        contentMap.clear()
        exec("RE.setIndent")
    }

    fun setOutdent() {
        contentMap.clear()
        exec("RE.setOutdent")
    }

    fun setAlignLeft() {
        contentMap.clear()
        exec("RE.setJustifyLeft")
    }

    fun setAlignCenter() {
        contentMap.clear()
        exec("RE.setJustifyCenter")
    }

    fun setAlignRight() {
        contentMap.clear()
        exec("RE.setJustifyRight")
    }

    fun setBlockquote() {
        contentMap.clear()
        exec("RE.setBlockquote")
    }

    fun setBullets() {
        contentMap.clear()
        exec("RE.setBullets")
    }

    fun setNumbers() {
        contentMap.clear()
        exec("RE.setNumbers")
    }

    fun insertImage(url: String, alt: String) {
        contentMap.clear()
        contentMap["content"] = "'$url', '$alt'"
        exec("RE.insertImage", contentMap)
    }

    /**
     * the image according to the specific width of the image automatically
     *
     * @param url
     * @param alt
     * @param width
     */
    fun insertImage(url: String, alt: String, width: Int) {
        contentMap.clear()
        contentMap["content"] = "'$url', '$alt','$width'"
        exec("RE.insertImageW", contentMap)
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
        contentMap.clear()
        contentMap["content"] = "'$url', '$alt','$width', '$height'"
        exec("RE.insertImageWH", contentMap)
    }

    fun insertVideo(url: String) {
        contentMap.clear()
        contentMap["content"] = "'$url'"
        exec("RE.insertVideo", contentMap)
    }

    fun insertVideo(url: String, width: Int) {
        contentMap.clear()
        contentMap["content"] = "'$url', '$width'"
        exec("RE.insertVideoW", contentMap)
    }

    fun insertVideo(url: String, width: Int, height: Int) {
        contentMap.clear()
        contentMap["content"] = "'$url', '$width', '$height'"
        exec("RE.insertVideoWH", contentMap)
    }

    fun insertAudio(url: String) {
        contentMap.clear()
        contentMap["content"] = "'$url'"
        exec("RE.insertAudio", contentMap)
    }

    fun insertLink(href: String, defText: String) {
        contentMap.clear()
        contentMap["content"] = "'$href', '$defText'"
        exec("RE.insertLink")
    }

    fun insertTodo() {
        contentMap.clear()
        exec("RE.setTodo", contentMap)
    }


    fun insertCode() {
        contentMap.clear()
        exec("RE.setCode", contentMap)
    }

    fun focusEditor() {
        requestFocus()
        contentMap.clear()
        exec("RE.focus", contentMap)
    }

    fun clearFocusEditor() {
        contentMap.clear()
        exec("RE.blurFocus", contentMap)
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

    protected fun exec(event: String, map: HashMap<*, *> = contentMap) {
        val message = JSONObject(map)
        try {
            message.put("event", event)
        } catch (ex: JSONException) {
            throw RuntimeException(ex)
        }
        mPort?.postMessage(message)
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

    companion object {
        //wang_editor.html
        private const val TAG = "RichEditor"
        private const val SETUP_HTML = "resource://android/assets/wang_editor.html"
        private const val CALLBACK_SCHEME = "re-callback://"
        private const val STATE_SCHEME = "re-state://"
    }
}