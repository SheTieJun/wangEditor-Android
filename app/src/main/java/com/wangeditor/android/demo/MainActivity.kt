package com.wangeditor.android.demo

import android.graphics.Color
import android.view.View
import android.view.View.OnClickListener
import com.wangeditor.android.Utils
import com.wangeditor.android.demo.activity.PublishActivity
import com.wangeditor.android.demo.adapter.NoteAdapter
import com.wangeditor.android.demo.databinding.ActivityMainBinding
import com.wangeditor.android.demo.model.Note
import com.wangeditor.android.demo.viewmodel.MainViewModel
import me.shetj.base.ktx.launch
import me.shetj.base.ktx.setAppearance
import me.shetj.base.mvvm.viewbind.BaseBindingActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding, MainViewModel>(), OnClickListener {

    private val mAdapter = NoteAdapter(null,lifecycle)
    override fun initView() {
        super.initView()
        setAppearance(true, Color.WHITE)
        Utils.isDebug(true)
        mBinding.create.setOnClickListener(this)
        mBinding.recycleView.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            PublishActivity.start(this, mAdapter.getItem(position).saver?.keyName,false)
        }

    }

    override fun initData() {
        super.initData()
        launch {
            mViewModel.getNoteList().collect {
                it.mapNotNull {
                    it.getInfoByJson<Note>()?.also { note ->
                        note.saveId = it.id
                        note.saver = it
                    }
                }.let { noteList ->
                    mAdapter.setNewInstance(noteList.toMutableList())
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.create -> {
                PublishActivity.start(this, null,isEdit = true)
            }
            else -> {}
        }
    }
}