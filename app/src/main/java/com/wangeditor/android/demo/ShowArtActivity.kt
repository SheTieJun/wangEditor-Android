package com.wangeditor.android.demo

import android.R.id
import android.os.Bundle
import android.content.SharedPreferences
import android.webkit.WebSettings
import android.webkit.WebChromeClient
import com.wangeditor.android.RichUtils
import com.wangeditor.android.demo.R
import android.content.Intent
import com.wangeditor.android.demo.PublishActivity
import android.webkit.WebViewClient
import android.webkit.SslErrorHandler
import android.net.http.SslError
import android.annotation.TargetApi
import android.os.Build
import android.os.Build.VERSION_CODES
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN
import android.webkit.WebSettings.TextSize.NORMAL
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.wangeditor.android.demo.databinding.ActivityShowArtBinding

/**
 * Created by leo
 * on 2020/9/21.
 */
class ShowArtActivity : AppCompatActivity() {
    var binding: ActivityShowArtBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowArtBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val sharedPreferences = getSharedPreferences("art", MODE_PRIVATE)
        val title = sharedPreferences.getString("title", "title")
        val content = sharedPreferences.getString("content", "")

//        content = "上海专业用户上海专业用户上海专业用户上海专业用户上海专业用户<p></p><p><img src=\"https://greenvalley.oss-cn-shanghai.aliyuncs.com/patient/270f8cbc044b4400bdb098d67b72a859_160_160.png\" style=\"max-width:100%;\"></p>";
        initWebView(content)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = title
    }

    fun initWebView(data: String?) {
        var data = data
        val settings = binding!!.webView.settings

        settings.loadWithOverviewMode = true //设置WebView是否使用预览模式加载界面。
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        binding!!.webView.isVerticalScrollBarEnabled = false //不能垂直滑动
        binding!!.webView.isHorizontalScrollBarEnabled = false //不能水平滑动
        settings.textSize = NORMAL //通过设置WebSettings，改变HTML中文字的大小
        settings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        //设置WebView属性，能够执行Javascript脚本
        binding!!.webView.settings.javaScriptEnabled = true //设置js可用
        settings.layoutAlgorithm = SINGLE_COLUMN //支持内容重新布局
        binding!!.webView.webViewClient = webViewClient
        binding!!.webView.webChromeClient = WebChromeClient()
        data = "</Div><head><style>body{font-size:18px}</style>" +
                "<style>img{ width:100% !important;margin-top:0.4em;margin-bottom:0.4em}</style>" +
                "<style>ul{ padding-left: 1em;margin-top:0em}</style>" +
                "<style>ol{ padding-left: 1.2em;margin-top:0em}</style>" +
                "</head>" + data
        val arrayList = RichUtils.returnImageUrlsFromHtml(data)
        if (arrayList.size > 0) {
            for (i in arrayList.indices) {
                if (!arrayList[i]!!.contains("http")) {
                    //如果不是http,那么就是本地绝对路径，要加上file
                    data = data!!.replace(arrayList[i]!!, "file://" + arrayList[i])
                }
            }
        }
        binding!!.webView.loadDataWithBaseURL(null, data!!, "text/html", "utf-8", null)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            id.home -> {
                finish()
                true
            }
            R.id.repet_editor -> {
                val intent = Intent(this@ShowArtActivity, PublishActivity::class.java)
                intent.putExtra("isFrom", 1)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    var webViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }

        @TargetApi(VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url = request.url.toString()
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}