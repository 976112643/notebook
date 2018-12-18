package com.wq.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.telephony.TelephonyManager
import android.text.Html
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.wq.common.base.App
import com.wq.common.net.API
import com.wq.common.net.APIManager
import com.wq.common.net.BaseBean
import com.wq.common.util.FrameworkSetting.LOG_LEVEL
import com.wq.common.util.LEVEL.*
import org.jetbrains.anko.custom.onUiThread
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
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
    val logMessage = message ?: this.toString()
    when (level) {
        _D -> Log.d(javaClass.simpleName, "$logMessage")
        _E -> Log.e(javaClass.simpleName, "$logMessage")
        _I -> Log.i(javaClass.simpleName, "$logMessage")
        _V -> Log.v(javaClass.simpleName, "$logMessage")
        _W -> Log.w(javaClass.simpleName, "$logMessage")
        _A -> Log.wtf(javaClass.simpleName, "$logMessage")
    }
}

fun Any._L(message: Any? = null, level: LEVEL = _D) {
    _Log(message, level)
}

/**
 * 字符串转Bean
 */
inline fun <reified T> String.toBean(): T = Gson().fromJson(this, object : TypeToken<T>() {}.type)

/**
 * 对象转json
 */
fun Any.toJson(): String = GsonBuilder().create().toJson(this)

/**
 * 判断为空
 */
fun <T> T?.empty(callback: () -> Unit = {}): Boolean {
    when (true) {
        this == null,
        (this is TextView && this.text.toString().trim().isEmpty()),
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

/**
 * 判断非空
 */
fun <T> T?.notEmpty(callback: T.() -> Unit = {}): Boolean {
    if (!this.empty()) {
        callback.invoke(this as T)
        return true
    }
    return false
}

fun Long.date(): String {
    return try {
        SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(this))
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * String 转JSONObject
 */
fun String.json(): JSONObject {
    return try {
        JSONObject(this)
    } catch (e: JSONException) {
        e.printStackTrace()
        JSONObject()
    }
}

inline operator fun <reified T> Intent.get(key: String): T {
    var tClazz = T::class.java
    var any = when (tClazz) {
        String::class.java -> getStringExtra(key)
        Integer::class.java,
        Int::class.java -> getIntExtra(key, 0)
        Long::class.java -> getLongExtra(key, 0)
        Short::class.java -> getShortExtra(key, 0)
        Byte::class.java -> getByteExtra(key, 0)
        Boolean::class.java -> getBooleanExtra(key, false)
        else ->
            throw RuntimeException("该类型不支持使用Intent存取$tClazz ${Int::class.java}")
    }
    return any as T
}

inline fun <reified T> getRawType() = T::class.java

fun <T> T.ifrun(bool: Boolean = true, block: T.() -> Unit) {
    if (bool)
        block()
}

/**
 * 网络请求的成功回调
 */
fun <T : BaseBean<U>, U> Call<T>.isOK(callback: BaseBean<U>.() -> Unit) {
    val body = execute().body()
    if (body?.status == 1) {
        callback(body)
    } else {
        throw IOException(body?.msg)
    }
}

/**
 * 迭代集合,暴露item内部
 */
fun <T> Iterable<T>.innerforEach(callback: T.() -> Unit) {
    for (item in this) {
        callback(item)
    }
}

/**
 * 生成缩略信息
 */
fun generateSmallContene(content: String, limitLength: Int = 100): String {
    return if (content.notEmpty()) {
        val noHtmlContent = Html.fromHtml(content).toString()
        noHtmlContent.run {
            subSequence(0, Math.min(limitLength, length)).toString().replace("\n", " ")
        }
    } else ""
}

/**
 * 使用指定分隔符连接集合中所有元素
 * @param limitStr 分隔符 ,默认为","
 * @param callback 元素转字符串方法
 */
inline fun <T> Iterable<T>.toString(limitStr: String = ",", callback: (T) -> String): String {
    var tmpStr = ""
    forEach {
        tmpStr = tmpStr + callback.invoke(it) + limitStr
    }
    if (!tmpStr.isEmpty()) {
        tmpStr.substring(0, tmpStr.length - 1)
    }
    return tmpStr
}

fun <T, R> Iterable<T>.list2list(callback: (T) -> R): List<R> {
    var aimList = arrayListOf<R>()
    forEach { aimList.add(callback.invoke(it)) }
    return aimList;
}

//infix fun

/**
 * 伪三目运算
 */
fun <T> Boolean.ternary(t1: T, t2: T) = if (this) t1 else t2


/**
 * 睡一会
 */
fun Any.sleep(time:Long=100){
   try {
       Thread.sleep(time)
   } catch (e:InterruptedException){
       e.printStackTrace()
   }
}

/**
 * 为所有类扩展上下文字段，即全局上下文
 */
val _CONTEXT: App get() = App._CONTEXT

/**
 * 全局接口请求对象
 */
val Any.api: API get() = APIManager.request



