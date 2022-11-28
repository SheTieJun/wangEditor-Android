package com.wangeditor.android.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wangeditor.android.Utils
import com.wangeditor.android.demo.databinding.ActivityMainBinding
import me.shetj.base.base.AbBindingActivity
import me.shetj.base.ktx.start

class MainActivity :   AbBindingActivity<ActivityMainBinding>() {

    override fun initView() {
        super.initView()
        Utils.isDebug(true)
        mViewBinding.buttonLook.setOnClickListener{
            start(Intent(this,ShowArtActivity::class.java))
        }
        mViewBinding.buttonPublish.setOnClickListener {
            start(Intent(this,PublishActivity::class.java))
        }
    }

    override fun initData() {
        super.initData()

    }
}