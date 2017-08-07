package com.wq.common.util

import android.text.format.DateUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wq.common.base.App
import com.wq.common.util.FrameworkSetting.LOG_LEVEL
import com.wq.common.util.LEVEL.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 扩展方法集
 * Created by weiquan on 2017/6/22.
 */


/**
 * 打印日志
 * @param message 消息內容
 * @param level 消息級別
 * 兩種使用方法
 * _Log(日誌信息,日誌等級)
 * 日誌信息._Log(level=D)
 */
fun Any._Log(message: Any? = null, level: LEVEL = _D) {
    if (LOG_LEVEL == _NONE) return
    val isAll = LOG_LEVEL == _ALL
    val logMessage = message ?: this.toString()
    when (true) {
        (level == _D || isAll) -> Log.d(javaClass.simpleName, "$logMessage")
        (level == _E || isAll) -> Log.e(javaClass.simpleName, "$logMessage")
        (level == _I || isAll) -> Log.i(javaClass.simpleName, "$logMessage")
        (level == _V || isAll) -> Log.v(javaClass.simpleName, "$logMessage")
        (level == _W || isAll) -> Log.w(javaClass.simpleName, "$logMessage")
        (level == _A || isAll) -> Log.wtf(javaClass.simpleName, "$logMessage")
    }
}

/**
 * 字符串转Bean
 */
inline fun <reified T> String.toBean(): T = Gson().fromJson(this, object : TypeToken<T>() {}.type)

fun <T> T?.empty(callback: () -> Unit = {}): Boolean {
    when (true) {
        this == null,
        (this is String && this.length == 0),
        (this is List<*> && this.size == 0),
        (this is Map<*, *> && this.size == 0),
        (this is MutableMap<*, *> && this.size == 0),
        (this is MutableList<*> && this.size == 0),
        (this is Array<*> && this.size == 0) -> {
            callback.invoke()
            return true
        }

    }
    return false
}

fun <T> T?.notEmpty(callback: (T) -> Unit) {
    if (!this.empty()) callback.invoke(this as T)
}

fun Long.date():String {
    try {
        return SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(this))
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}
/**
 * 为所有类扩展上下文字段，即全局上下文
 */
val _CONTEXT: App get() = App._CONTEXT




