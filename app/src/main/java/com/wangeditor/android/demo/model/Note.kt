package com.wangeditor.android.demo.model

import androidx.annotation.Keep
import me.shetj.base.ktx.saverCreate
import me.shetj.base.ktx.toJson
import me.shetj.base.saver.Saver

@Keep
data class Note(var saveId:Int = 0,var title:String,var content:String){


    var saver:Saver ?= null

    fun convertToSaver(): Saver {
      return  saverCreate(group = "Note","${System.currentTimeMillis()}","Note").also {
            it.id = saveId
            it.jsonInfo = this.toJson()
        }
    }
}