package com.wangeditor.android.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wangeditor.android.RichUtils
import com.wangeditor.android.demo.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import me.shetj.base.ktx.launch
import me.shetj.base.ktx.saverDB
import me.shetj.base.mvvm.viewbind.BaseViewModel
import me.shetj.base.saver.Saver


class PublishViewModel : BaseViewModel() {


    val enable = MutableLiveData<Boolean>()

    var currentNote: Note? = null


    fun save() {
        currentNote?.let {
            launch {
                currentNote?.convertToSaver()?.let {
                    currentNote!!.saveId = saverDB.insert(it).toInt()
                }
            }
        }
    }

    fun updateText(text: String?) {
        if (text.isNullOrEmpty()) return
        if(RichUtils.returnOnlyText(text).trim().isEmpty()) return
        checkAndInit()
        currentNote?.content = text
    }

    fun updateTitle(title: String?) {
        if (title.isNullOrEmpty()) return
        checkAndInit()
        currentNote?.title = title
    }

    private fun checkAndInit(): Note {
        return currentNote ?: Note(0, "", "").also {
            currentNote = it
        }
    }


    /**
     * Start auto save
     * 开始自动保存
     */
    fun startAutoSave(): Flow<Int> {
        return flow {
            while (true) {
                repeat(10) {
                    emit(10 - it)
                    delay(1000)
                }
                emit(100)
                save()
                delay(1000)
            }
        }.flowOn(Dispatchers.IO).catch { e ->
            e.printStackTrace()
        }
    }

    suspend fun getSaveInfo(saveKey: String?): Saver {
        return saverDB.findSaver("Note", saveKey.toString()).first()
    }


}

