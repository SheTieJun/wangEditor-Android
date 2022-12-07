package com.wangeditor.android.demo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.wangeditor.android.RichType
import com.wangeditor.android.RichUtils
import com.wangeditor.android.WangRichEditor.OnContentChangeListener
import com.wangeditor.android.WangRichEditor.OnTextChangeListener
import com.wangeditor.android.demo.R.id
import com.wangeditor.android.demo.databinding.ActivityPublishBinding
import com.wangeditor.android.demo.viewmodel.PublishViewModel
import com.wangeditor.android.toolbar.impl.media.AbRichItem_Media
import com.wangeditor.android.toolbar.impl.media.MediaStrategy
import com.wangeditor.android.toolbar.initFunStyle
import com.wangeditor.android.toolbar.initMedia
import com.wangeditor.android.toolbar.initParagraphStyle
import com.wangeditor.android.toolbar.initTextStyle
import me.shetj.base.ktx.launch
import me.shetj.base.ktx.selectFile
import me.shetj.base.ktx.setAppearance
import me.shetj.base.ktx.showToast
import me.shetj.base.ktx.start
import me.shetj.base.mvvm.BaseBindingActivity
import me.shetj.base.tip.TipKit
import me.shetj.base.tools.app.KeyboardUtil

class PublishActivity : BaseBindingActivity<ActivityPublishBinding, PublishViewModel>(), OnClickListener {


    companion object {
        fun start(context: Context, saveKey: String? = null, isEdit: Boolean = true) {
            val intent = Intent(context, PublishActivity::class.java).apply {
                putExtra("saveKey", saveKey)
                putExtra("isEdit", isEdit)
            }
            context.start(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppearance(true, Color.WHITE)
        val saveKey = intent.getStringExtra("saveKey")
        val isEnable = intent.getBooleanExtra("isEdit", false)
        mViewModel.enable.postValue(isEnable)
        initEditor()
        if (!saveKey.isNullOrEmpty()) {
            launch {
                mViewModel.getSaveInfo(saveKey).let {
                    mViewModel.currentNote = it.getInfoByJson()
                    mViewBinding.editName.setText(mViewModel.currentNote?.title)
                    mViewBinding.richEditor.setHtml(mViewModel.currentNote?.content)
                    mViewBinding.contentSize.text =
                        "  |  ${RichUtils.returnOnlyText(mViewModel.currentNote?.content ?: "").length} 字"
                }
            }
        }
        startAutoSave()
        mViewBinding.richEditor.setInputEnabled(isEnable)
        lifecycleScope.launchWhenCreated {
            KeyboardUtil.hideSoftKeyboard(this@PublishActivity)
            mViewModel.enable.observe(this@PublishActivity) {
                mViewBinding.richEditor.setInputEnabled(it)
                TransitionManager.endTransitions(mViewBinding.root)
                TransitionManager.beginDelayedTransition(mViewBinding.root, ChangeBounds())
                mViewBinding.leoBar.isVisible = it
                mViewBinding.editName.isFocusable = it
                mViewBinding.editName.isFocusableInTouchMode = it
                mViewBinding.editEnable.isVisible = !it
            }
        }
        mViewBinding.editEnable.setOnClickListener(this@PublishActivity)
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

        mViewBinding.richEditor.addOnContentChangeListener(object : OnContentChangeListener {
            override fun onContentChange(html: String?) {
                mViewModel.updateText(html)
            }
        })

        mViewBinding.richEditor.addOnTextChangeListener(object : OnTextChangeListener {
            override fun onTextChange(text: String?) {
                launch {
                    mViewBinding.contentSize.text = "  |  ${text?.length ?: 0} 字"
                    val titleIsEmpty = TextUtils.isEmpty(mViewBinding.editName.text.toString().trim { it <= ' ' })
                    val empty = text.isNullOrEmpty()
                    mViewBinding.txtPublish.isSelected = !empty && !titleIsEmpty
                    mViewBinding.txtPublish.isEnabled = !empty && !titleIsEmpty
                }
            }
        })

        mViewBinding.editName.addTextChangedListener { ed ->
            val title = ed.toString()
            mViewModel.updateTitle(title)
            val html = mViewBinding.richEditor.getHtml().toString()
            val empty = RichUtils.isEmpty(html)
            mViewBinding.txtPublish.isSelected = !empty && title.isNotEmpty()
            mViewBinding.txtPublish.isEnabled = !empty && title.isNotEmpty()
            if (title.length > 40) {
                mViewBinding.editName.setText(title.substring(0, 40))
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
                    RichType.Video.name -> {
                        val url =
                            "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4"
                        iRichItem.insertMedia(url)
                    }
                    RichType.Image.name -> {
                        selectFile(type = iRichItem.getMimeType()) {
                            it?.let {
                                //我这里只是测试，如果是真实环境请不要这样使用，请使用上传后的文件
                                //content的地址只是临时的
                                //base64 大图会非常的卡顿
                                iRichItem.insertMedia(it.toString())
                                "这里的资源访问地址是临时地址".showToast()
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
            id.txt_finish -> {
                mViewModel.save()
                mViewModel.enable.postValue(false)
            }
            id.txt_publish -> {
                mViewModel.save()
                mViewModel.enable.postValue(false)
            }
            id.button_rich_do ->                 //反撤销
                mViewBinding.richEditor.redo()
            id.button_rich_undo ->                 //撤销
                mViewBinding.richEditor.undo()
            id.edit_enable -> {
                mViewModel.enable.postValue(true)
            }
            else -> {

            }
        }
    }

    override fun onBackPressed() {
        mViewModel.save()
        super.onBackPressed()
    }

}