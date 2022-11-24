package com.wangeditor.android.demo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.wangeditor.android.RichType
import com.wangeditor.android.WangRichEditor.OnTextChangeListener
import com.wangeditor.android.RichUtils
import com.wangeditor.android.WangRichEditor.OnDecorationStateListener
import com.wangeditor.android.demo.R.id
import com.wangeditor.android.demo.R.mipmap
import com.wangeditor.android.demo.databinding.ActivityPublishBinding
import com.wangeditor.android.toolbar.initFunStyle
import com.wangeditor.android.toolbar.initParagraphStyle
import com.wangeditor.android.toolbar.initTextStyle
import me.shetj.base.R.color
import me.shetj.base.ktx.selectFile

class PublishActivity : AppCompatActivity(), OnClickListener {
    var binding: ActivityPublishBinding? = null
    private var isFrom //0:表示正常编辑  1:表示是重新编辑
            = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublishBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        isFrom = intent.getIntExtra("isFrom", 0)
        initEditor()
        if (isFrom == 1) {
            val sharedPreferences = getSharedPreferences("art", MODE_PRIVATE)
            val title = sharedPreferences.getString("title", "title")
            val content = sharedPreferences.getString("content", "")
            binding!!.editName.setText(title)
            binding!!.richEditor.setHtml(content)
        }
    }

    private fun initEditor() {
        //输入框背景设置
        binding!!.richEditor.setEditorBackgroundColor(Color.WHITE)
        //输入框文本padding
        binding!!.richEditor.setPadding(20, 0, 20, 0)
        //输入提示文本
        binding!!.richEditor.setPlaceholder("请开始你的创作！~")
        //监听键盘，打开键盘的时候滚动选中位置
        binding!!.richEditor.initKeyboardChange(this)
        //文本输入框监听事件
        binding!!.editToolbar.setEditor(binding!!.richEditor)
        binding!!.editToolbar.initTextStyle()
        binding!!.editToolbar.initFunStyle()
        binding!!.editToolbar.initParagraphStyle()

        binding!!.richEditor.addOnTextChangeListener(object :OnTextChangeListener{
            override fun onTextChange(text: String?) {
                Log.e("富文本文字变动", text.toString())
                if (TextUtils.isEmpty(binding!!.editName.text.toString().trim { it <= ' ' })) {
                    binding!!.txtPublish.isSelected = false
                    binding!!.txtPublish.isEnabled = false
                    return
                }
                if (TextUtils.isEmpty(text)) {
                    binding!!.txtPublish.isSelected = false
                    binding!!.txtPublish.isEnabled = false
                } else {
                    if (TextUtils.isEmpty(Html.fromHtml(text))) {
                        binding!!.txtPublish.isSelected = false
                        binding!!.txtPublish.isEnabled = false
                    } else {
                        binding!!.txtPublish.isSelected = true
                        binding!!.txtPublish.isEnabled = true
                    }
                }
            }
        })

        binding!!.editName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val html = binding!!.richEditor.getHtml()
                if (TextUtils.isEmpty(html)) {
                    binding!!.txtPublish.isSelected = false
                    binding!!.txtPublish.isEnabled = false
                    return
                } else {
                    if (TextUtils.isEmpty(Html.fromHtml(html))) {
                        binding!!.txtPublish.isSelected = false
                        binding!!.txtPublish.isEnabled = false
                        return
                    } else {
                        binding!!.txtPublish.isSelected = true
                        binding!!.txtPublish.isEnabled = true
                    }
                }
                if (TextUtils.isEmpty(s.toString())) {
                    binding!!.txtPublish.isSelected = false
                    binding!!.txtPublish.isEnabled = false
                } else {
                    binding!!.txtPublish.isSelected = true
                    binding!!.txtPublish.isEnabled = true
                }
            }
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            id.txt_finish -> finish()
            id.txt_publish -> {
                val sharedPreferences = getSharedPreferences("art", MODE_PRIVATE)
                val edit = sharedPreferences.edit()
                edit.putString("content", binding!!.richEditor.getHtml())
                edit.putString("title", binding!!.editName.text.toString().trim { it <= ' ' })
                edit.commit()
                Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ShowArtActivity::class.java)
                startActivity(intent)
                finish()
            }
            id.button_rich_do ->                 //反撤销
                binding!!.richEditor.redo()
            id.button_rich_undo ->                 //撤销
                binding!!.richEditor.undo()
        }
    }

    private fun againEdit() {
        //如果第一次点击例如加粗，没有焦点时，获取焦点并弹出软键盘
        binding!!.richEditor.focusEditor()
    }

    companion object {
        private const val EXTRA_OUTPUT_URI = "extra_output_uri"
    }
}