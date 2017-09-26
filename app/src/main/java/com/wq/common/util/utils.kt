package com.wq.common.util

import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.wq.common.base.App
import com.wq.common.db.mode.Note
import com.wq.common.net.API
import com.wq.common.net.APIManager
import com.wq.common.net.BaseBean
import com.wq.common.util.FrameworkSetting.LOG_LEVEL
import com.wq.common.util.LEVEL.*
import io.realm.RealmObject
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
val array=arrayOf(Note::class.java)
/**
 * 字符串转Bean
 */
inline fun <reified T> String.toBean(): T = Gson().fromJson(this, object : TypeToken<T>() {}.type)
fun Any.toJson(): String =  GsonBuilder()
        .setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return !array.contains(clazz)
            }

            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return false
            }
        }).create().toJson(this)
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

fun <T> T?.notEmpty(callback: (T) -> Unit={}):Boolean {
    if (!this.empty()){
        callback.invoke(this as T)
        return true
    }
    return false
}

fun Long.date(): String {
    try {
        return SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(this))
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

inline operator fun <reified T> Intent.get(key: String): T {
    when (T::class.java) {
        String::class.java -> getStringExtra(key)
        Int::class.java -> getIntExtra(key, 0)
        Long::class.java -> getLongExtra(key, 0)
        Short::class.java -> getShortExtra(key, 0)
        Byte::class.java -> getByteExtra(key, 0)
        Boolean::class.java -> getBooleanExtra(key, false)
    }
    throw RuntimeException("该类型不支持使用Intent存取")
}

inline  fun <reified T>getRawType()=T::class.java

fun <T> T.ifrun(bool: Boolean = true, block: T.() -> Unit) {
    if (bool)
        block()
}

/**
 * 网络请求的成功回调
 */
fun <T:BaseBean<U>,U> Call<T>.isOK(callback: BaseBean<U>.() -> Unit){
    val body = execute().body()
    if (body?.status==1){
        callback(body)
    }else{
        throw IOException(body?.msg)
    }
}

/**
 * 迭代集合,暴露item内部
 */
fun <T> Iterable<T>.innerforEach(callback: T.() -> Unit){
    for (item in this){
        callback(item)
    }
}

/**
 * 设备号
 */
val _DEVICE_NO:String by lazy {
    val systemService = _CONTEXT.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    systemService.deviceId
}


/**
 * 为所有类扩展上下文字段，即全局上下文
 */
val _CONTEXT: App get() = App._CONTEXT

/**
 * 全局接口请求对象
 */
val Any.api:API get() = APIManager.request



