package com.wq.common.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

/**
 * 视图扩展工具
 * Created by weiquan on 2017/12/29.
 */

/**
 * ================================兼容扩展===================
 */

/**
 * 获取drawble对象
 */
fun _drawable(res: Int): Drawable = _CONTEXT.resources.getDrawable(res)
fun _color(res: Int): Int = _CONTEXT.resources.getColor(res)
/**
 * 打开输入法
 */
fun View?.openKeyboard() {
    this?.let {
        clearFocus()
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        postDelayed({
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
        }, 500)

    }
}

/**
 * 关闭输入法
 */
fun View?.closeKeyboard() {
    this?.let {
        clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        postDelayed({
            imm.hideSoftInputFromWindow(this.windowToken, 0)
        }, 500)

    }
}
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