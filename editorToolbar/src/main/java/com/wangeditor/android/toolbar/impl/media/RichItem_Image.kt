package com.wangeditor.android.toolbar.impl.media

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import com.wangeditor.android.RichType
import com.wangeditor.android.Utils
import com.wangeditor.android.toolbar.R
import java.io.File


class RichItem_Image : AbRichItem_Media() {
    override fun getType(): String {
        return RichType.Image.name
    }



    override fun insertMedia(url: String) {
        Utils.logInfo("insertMedia:$url")
        if (url.startsWith("https://")
            || url.startsWith("http://")
            || url.startsWith("file://")
            || url.startsWith("raw://")
            || url.startsWith("content://")
        ) {
            mWangEditor!!.insertImage(url, "")
            return
        }
        if (File(url).exists()){
            mWangEditor!!.insertImage("file://$url", "")
            return
        }
        Log.e("WangRichEditor","insertImage is error,url is not standard")
    }

    override fun getMimeType(): String {
        return "image/*"
    }

    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.note_icon_pic)
        }
    }
}