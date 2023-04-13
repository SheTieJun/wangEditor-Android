package com.wangeditor.android.demo.viewmodel

import kotlinx.coroutines.flow.Flow
import me.shetj.base.ktx.saverDB
import me.shetj.base.mvvm.viewbind.BaseViewModel
import me.shetj.base.saver.Saver


class MainViewModel: BaseViewModel() {


    fun getNoteList(): Flow<List<Saver>> {
       return saverDB.getAll("Note",false)
    }
}