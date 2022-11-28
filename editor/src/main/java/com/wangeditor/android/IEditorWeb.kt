package com.wangeditor.android



/**
 *
 * <b>@author：</b> shetj<br>
 * <b>@createTime：</b> 2022/11/8<br>
 * <b>@email：</b> 375105540@qq.com<br>
 * <b>@describe</b>  <br>
 */
interface IEditorWeb {


    fun onContentChange(richText:String?)


    fun onStyleChange(styleJson:String?)

}