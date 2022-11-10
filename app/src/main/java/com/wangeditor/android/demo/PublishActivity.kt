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
import com.wangeditor.android.RichWangEditor.OnTextChangeListener
import com.wangeditor.android.RichUtils
import com.wangeditor.android.demo.R.id
import com.wangeditor.android.demo.R.mipmap
import com.wangeditor.android.demo.databinding.ActivityPublishBinding
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
            binding!!.richEditor.html = content
        }
    }

    private fun initEditor() {
        //输入框显示字体的大小
        binding!!.richEditor.setEditorFontSize(18)
        //输入框显示字体的颜色
        binding!!.richEditor.setEditorFontColor(ContextCompat.getColor(this,color.blackText))
        //输入框背景设置
        binding!!.richEditor.setEditorBackgroundColor(Color.WHITE)
        //输入框文本padding
        binding!!.richEditor.setPadding(10, 0, 10, 0)
        //输入提示文本
        binding!!.richEditor.setPlaceholder("请开始你的创作！~")
        //监听键盘，打开键盘的时候滚动选中位置
        binding!!.richEditor.initKeyboardChange(this)
        //文本输入框监听事件
        binding!!.richEditor.setOnTextChangeListener(OnTextChangeListener { text ->
            Log.e("富文本文字变动", text)
            if (TextUtils.isEmpty(binding!!.editName.text.toString().trim { it <= ' ' })) {
                binding!!.txtPublish.isSelected = false
                binding!!.txtPublish.isEnabled = false
                return@OnTextChangeListener
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
        })
        binding!!.editName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val html = binding!!.richEditor.html
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
        binding!!.richEditor.setOnDecorationChangeListener { text, types ->
            val flagArr = ArrayList<String>()
            for (i in types.indices) {
                flagArr.add(types[i].name)
            }
            if (flagArr.contains("BOLD")) {
                binding!!.buttonBold.setImageResource(mipmap.bold_)
            } else {
                binding!!.buttonBold.setImageResource(mipmap.bold)
            }
            if (flagArr.contains("UNDERLINE")) {
                binding!!.buttonUnderline.setImageResource(mipmap.underline_)
            } else {
                binding!!.buttonUnderline.setImageResource(mipmap.underline)
            }
            if (flagArr.contains("ORDEREDLIST")) {
                binding!!.buttonListUl.setImageResource(mipmap.list_ul)
                binding!!.buttonListOl.setImageResource(mipmap.list_ol_)
            } else {
                binding!!.buttonListOl.setImageResource(mipmap.list_ol)
            }
            if (flagArr.contains("UNORDEREDLIST")) {
                binding!!.buttonListOl.setImageResource(mipmap.list_ol)
                binding!!.buttonListUl.setImageResource(mipmap.list_ul_)
            } else {
                binding!!.buttonListUl.setImageResource(mipmap.list_ul)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            id.txt_finish -> finish()
            id.txt_publish -> {
                val sharedPreferences = getSharedPreferences("art", MODE_PRIVATE)
                val edit = sharedPreferences.edit()
                edit.putString("content", binding!!.richEditor.html)
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
            id.button_bold -> {
                //加粗
                againEdit()
                binding!!.richEditor.setBold()
            }
            id.button_underline -> {
                //加下划线
                againEdit()
                binding!!.richEditor.setUnderline()
            }
            id.button_list_ul -> {
                //加带点的序列号
                againEdit()
                binding!!.richEditor.setBullets()
            }
            id.button_list_ol -> {
                //加带数字的序列号
                againEdit()
                binding!!.richEditor.setNumbers()
            }
            id.button_image -> {
                if (!TextUtils.isEmpty(binding!!.richEditor.html)) {
                    val arrayList = RichUtils.returnImageUrlsFromHtml(
                        binding!!.richEditor.html
                    )
                    if (arrayList.size >= 9) {
                        Toast.makeText(this@PublishActivity, "最多添加9张照片~", Toast.LENGTH_SHORT).show()
                        return
                    }
                    selectFile {
                        it?.let {
                            binding!!.richEditor.insertImage(it.toString(),"图片${arrayList.size}")
                        }
                    }
                }
            }
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