package com.wq.common.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

/**
 * 视图扩展工具
 * Created by weiquan on 2017/12/29.
 */

/**
 * 监听改变前
 */
fun TextView.wathchAfter(wathchAfter:(p0: Editable?)->Unit){
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) =wathchAfter(p0)
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}
/**
 * 监听改变后
 */
fun TextView.wathchBefore(wathchBefore:(p0: CharSequence?, p1: Int, p2: Int, p3: Int)->Unit){
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?){}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =wathchBefore(p0,p1,p2,p3)
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}

/**
 * 监听改变
 */
fun TextView.wathchChange(wathchChange:(p0: CharSequence?, p1: Int, p2: Int, p3: Int)->Unit){
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?){}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =wathchChange(p0,p1,p2,p3)
    })
}

/**
 * 监听改变,简单参数
 */
fun TextView.wathch(wathchChange:(p0: CharSequence?)->Unit){
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?){}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =wathchChange(p0)
    })
}