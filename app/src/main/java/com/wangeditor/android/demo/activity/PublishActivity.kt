package com.wangeditor.android.demo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.wangeditor.android.RichType
import com.wangeditor.android.RichUtils
import com.wangeditor.android.WangRichEditor.OnTextChangeListener
import com.wangeditor.android.demo.R.id
import com.wangeditor.android.demo.databinding.ActivityPublishBinding
import com.wangeditor.android.demo.model.Note
import com.wangeditor.android.demo.viewmodel.PublishViewModel
import com.wangeditor.android.toolbar.impl.media.AbRichItem_Media
import com.wangeditor.android.toolbar.initFunStyle
import com.wangeditor.android.toolbar.initMedia
import com.wangeditor.android.toolbar.initParagraphStyle
import com.wangeditor.android.toolbar.initTextStyle
import com.wangeditor.android.toolbar.impl.media.MediaStrategy
import kotlinx.coroutines.flow.collect
import me.shetj.base.ktx.launch
import me.shetj.base.ktx.logI
import me.shetj.base.ktx.selectFile
import me.shetj.base.ktx.setAppearance
import me.shetj.base.ktx.showToast
import me.shetj.base.ktx.start
import me.shetj.base.ktx.toBean
import me.shetj.base.ktx.toJson
import me.shetj.base.mvvm.BaseBindingActivity
import me.shetj.base.tip.TipKit

class PublishActivity : BaseBindingActivity<ActivityPublishBinding, PublishViewModel>(), OnClickListener {


    companion object {
        fun start(context: Context, saveKey:String?=null) {
            val intent = Intent(context, PublishActivity::class.java).apply {
                putExtra("saveKey",saveKey)
            }
            context.start(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppearance(true, Color.WHITE)
        val saveKey = intent.getStringExtra("saveKey")
        initEditor()
        if (!saveKey.isNullOrEmpty()) {
            launch {
                mViewModel.getSaveInfo(saveKey).let {
                    mViewModel.currentNote = it.getInfoByJson()
                    mViewBinding.editName.setText(mViewModel.currentNote?.title)
                    mViewBinding.richEditor.setHtml(mViewModel.currentNote?.content)
                    mViewBinding.contentSize.text = "  |  ${RichUtils.returnOnlyText(mViewModel.currentNote?.content?:"").length} 字"
                }
            }
        }
        startAutoSave()
    }

    private fun initEditor() {
        //输入框背景设置
        mViewBinding.richEditor.setEditorBackgroundColor(Color.WHITE)
        //输入框文本padding
        mViewBinding.richEditor.setPadding(20, 0, 20, 0)
        //监听键盘，打开键盘的时候滚动选中位置
        mViewBinding.richEditor.initKeyboardChange(this, onKeyboardHide = {
            mViewBinding.bottomBar.isVisible = false
            mViewBinding.editToolbar.onKeyboardHide()
        }, onKeyboardShow = {
            mViewBinding.bottomBar.isVisible = true
            mViewBinding.editToolbar.onKeyboardShow()
        })
        mViewBinding.contentSize.text = "  |  0 字"
        initEditorToolbar()

        mViewBinding.richEditor.addOnTextChangeListener(object : OnTextChangeListener {
            override fun onTextChange(text: String?) {
                mViewModel.updateText(text)
                text?.let {
                    mViewBinding.contentSize.text = "  |  ${RichUtils.returnOnlyText(text).length} 字"
                }
                text.toString().logI("WangNote")
                if (TextUtils.isEmpty(mViewBinding.editName.text.toString().trim { it <= ' ' })) {
                    mViewBinding.txtPublish.isSelected = false
                    mViewBinding.txtPublish.isEnabled = false
                    return
                }
                if (TextUtils.isEmpty(text)) {
                    mViewBinding.txtPublish.isSelected = false
                    mViewBinding.txtPublish.isEnabled = false
                } else {
                    if (TextUtils.isEmpty(Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))) {
                        mViewBinding.txtPublish.isSelected = false
                        mViewBinding.txtPublish.isEnabled = false
                    } else {
                        mViewBinding.txtPublish.isSelected = true
                        mViewBinding.txtPublish.isEnabled = true
                    }
                }
            }
        })

        mViewBinding.editName.addTextChangedListener { ed ->
            mViewModel.updateTitle(ed.toString())
            val html = mViewBinding.richEditor.getHtml()
            if (TextUtils.isEmpty(html)) {
                mViewBinding.txtPublish.isSelected = false
                mViewBinding.txtPublish.isEnabled = false
                return@addTextChangedListener
            } else {
                if (TextUtils.isEmpty(Html.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY))) {
                    mViewBinding.txtPublish.isSelected = false
                    mViewBinding.txtPublish.isEnabled = false
                    return@addTextChangedListener
                } else {
                    mViewBinding.txtPublish.isSelected = true
                    mViewBinding.txtPublish.isEnabled = true
                }
            }
            if (TextUtils.isEmpty(ed.toString())) {
                mViewBinding.txtPublish.isSelected = false
                mViewBinding.txtPublish.isEnabled = false
            } else {
                mViewBinding.txtPublish.isSelected = true
                mViewBinding.txtPublish.isEnabled = true
            }
            if (ed.toString().length > 40) {
                mViewBinding.editName.setText(ed.toString().substring(0, 40))
                mViewBinding.editName.setSelection(mViewBinding.editName.text.length)
                TipKit.normal(this, "标题不能超过40个字")
            }
        }



    }

    private fun startAutoSave() {
        launch {
            mViewModel.startAutoSave().collect {
                if (it < 100) {
                    mViewBinding.autoSaveTime.text = "${it}s 后自动保存"
                } else {
                    mViewBinding.autoSaveTime.text = "正在保存..."
                }
            }
        }
    }

    private fun initEditorToolbar() {
        mViewBinding.editToolbar.setEditor(mViewBinding.richEditor)
        mViewBinding.editToolbar.initTextStyle()
        mViewBinding.editToolbar.initFunStyle()
        mViewBinding.editToolbar.initMedia()
        mViewBinding.editToolbar.initParagraphStyle()
        mViewBinding.editToolbar.setMediaStrategy(object : MediaStrategy {
            override fun startSelectMedia(iRichItem: AbRichItem_Media) {
                when (iRichItem.getType()) {
                    RichType.Video.name, RichType.Image.name -> {
                        selectFile(type = iRichItem.getMimeType()) {
                            it?.let {
                                //我这里只是测试，如果是真实环境请不要
                                iRichItem.insertMedia(it.toString())
                            }
                        }
                    }
                    else -> {
                        "暂不支持${iRichItem.getType()}类型".showToast()
                    }
                }
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            id.txt_finish -> finish()
            id.txt_publish -> {
                mViewModel.save()
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
                finish()
            }
            id.button_rich_do ->                 //反撤销
                mViewBinding.richEditor.redo()
            id.button_rich_undo ->                 //撤销
                mViewBinding.richEditor.undo()
        }
    }

    override fun onBackPressed() {
        mViewModel.save()
        super.onBackPressed()
    }

}