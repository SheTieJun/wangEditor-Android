package com.wangeditor.android.demo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.wangeditor.android.demo.R
import com.wangeditor.android.demo.databinding.ActivityShowArtBinding
import com.wangeditor.android.demo.viewmodel.PublishViewModel
import me.shetj.base.ktx.launch
import me.shetj.base.ktx.setAppearance
import me.shetj.base.ktx.start
import me.shetj.base.mvvm.BaseBindingActivity


class ShowArtActivity : BaseBindingActivity<ActivityShowArtBinding, PublishViewModel>(), OnClickListener {

    companion object {
        fun start(context: Context, saveKey: String?) {
            val intent = Intent(context, ShowArtActivity::class.java).apply {
                putExtra("saveKey", saveKey)
            }
            context.start(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppearance(true,Color.WHITE)
        mViewBinding.initView()
    }

    private fun ActivityShowArtBinding.initView() {
        richEditor.setEditorBackgroundColor(Color.WHITE)
        richEditor.setPadding(20, 0, 20, 0)
        richEditor.disable()
        val saveKey = intent.getStringExtra("saveKey")
        if (!saveKey.isNullOrEmpty()) {
            launch {
                mViewModel.getSaveInfo(saveKey).let {
                    mViewModel.currentNote = it.getInfoByJson()
                    mViewBinding.title.text = (mViewModel.currentNote?.title)
                    mViewBinding.richEditor.setHtml(mViewModel.currentNote?.content)
                }
            }
        }
        mViewBinding.edit.setOnClickListener(this@ShowArtActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edit -> {
                PublishActivity.start(this, intent.getStringExtra("saveKey"))
                finish()
            }
            else ->{}
        }
    }

}