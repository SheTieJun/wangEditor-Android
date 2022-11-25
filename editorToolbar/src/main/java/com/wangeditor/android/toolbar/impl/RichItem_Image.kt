package com.wangeditor.android.toolbar.impl

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import android.widget.LinearLayout.LayoutParams
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import com.wangeditor.android.RichType
import com.wangeditor.android.toolbar.AbRichItem_Media
import com.wangeditor.android.toolbar.IRichItem
import com.wangeditor.android.toolbar.R
import java.io.File


class RichItem_Image : AbRichItem_Media() {
    override fun getType(): String {
        return RichType.Image.name
    }

    override fun onClick() {
        kotlin.runCatching {
            (mWangEditor!!.context as FragmentActivity)
        }
    }

    override fun insertMedia(url: String) {
        if (url.startsWith("http")||url.startsWith("file")) {
            mWangEditor!!.insertImage(url, "")
            return
        }
        if (File(url).exists()){
            mWangEditor!!.insertImage("file://$url", "")
            return
        }
        Log.e("WangRichEditor","insertImage is error,url is not standard")
    }


    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.note_icon_pic)
        }
    }
}