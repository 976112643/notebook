package com.wq.common.util

import android.os.Handler
import android.os.Looper
import android.widget.Toast


/**
 * toast 可在子线程中使用
 * Created by weiquan on 2018/5/4.
 */

private val shortToast = Toast.makeText(_CONTEXT, "", Toast.LENGTH_SHORT)
private val handler by lazy { Handler(_CONTEXT.mainLooper) }

/**
 * 显示短Toast
 *
 * @param msg
 */
fun shortToast(msg: Any) {
    showToast(msg, Toast.LENGTH_SHORT)
}

/**
 * 显示长Toast
 *
 * @param msg
 */
fun longToast(msg: Any) {
    showToast(msg, Toast.LENGTH_LONG)
}


fun showToast(msg: Any, duration: Int) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        showToastImpl(msg, duration)
    } else {
        handler.post { showToastImpl(msg, duration) }
    }
}

private fun showToastImpl(msg: Any, duration: Int) {
    if (msg is Int) {
        shortToast.setText(msg)
    } else {
        shortToast.setText(msg.toString())
    }
    shortToast.duration = duration
    shortToast.show()
}