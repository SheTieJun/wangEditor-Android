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
package com.wangeditor.android.toolbar.impl

import android.app.Activity
import android.content.DialogInterface
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.core.view.setPadding
import com.google.android.material.textfield.TextInputLayout
import com.wangeditor.android.RichType
import com.wangeditor.android.toolbar.IRichItem
import com.wangeditor.android.toolbar.R

class RichItem_Link : IRichItem() {

    private var dialog: AlertDialog?= null

    override fun getType(): String {
        return RichType.Link.name
    }

    override fun onClick() {
        openLinkDialog()
    }

    override fun buildView(): View {
        return ImageView(mWangEditor!!.context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(15)
            setImageResource(R.drawable.note_icon_link)
        }
    }

    private fun openLinkDialog() {
        if (dialog == null){
            val activity = getItemView().context as Activity
            val builder = Builder(activity)
            val layoutInflater = activity.layoutInflater
            val areInsertLinkView: View = layoutInflater.inflate(R.layout.editor_link_insert, null)
            builder.setView(areInsertLinkView)
                .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, _ ->
                    val editText = areInsertLinkView.findViewById<View>(R.id.insert_link_edit) as TextInputLayout
                    val url = editText.editText?.text.toString()
                    if (TextUtils.isEmpty(url)) {
                        dialog.dismiss()
                        return@OnClickListener
                    }
                    mWangEditor?.insertLink(url, "链接地址")
                    editText.editText?.setText("")
                })
            builder.setNegativeButton(
                R. string.cancel
            ) { dialog, _ -> dialog.dismiss() }
            dialog = builder.create()
        }
        dialog?.show()
    }

}
