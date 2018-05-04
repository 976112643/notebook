package com.wq.common.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * ================================兼容扩展===================
 */

/**
 * 获取drawble对象
 */
fun _drawable(res: Int): Drawable = _CONTEXT.resources.getDrawable(res)

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
