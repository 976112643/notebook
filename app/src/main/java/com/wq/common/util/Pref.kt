package com.wq.common.util
import android.content.Context
import com.wq.common.base.App.Companion._CONTEXT
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public class Pref<T>( val default: T, val prefName:String="app_prref") : ReadWriteProperty<Any?, T> {
    val prefs by lazy { _CONTEXT.getSharedPreferences(prefName, Context.MODE_PRIVATE) }
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(property.name, default)
    }
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(property.name, value)
    }
    private fun <U> findPreference(name: String, default: U?): U = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        res as U
    }
//    private fun notNullDef
    private fun <U> putPreference(name: String, value: U) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply()
    }

    companion object {
        fun prefLong()=Pref(0L)
        fun prefString()=Pref("")
        fun prefInt()=Pref("")
        fun prefBool()=Pref(false)
        fun prefFloat()=Pref(0f)
        fun prefDouble()=Pref(0.0)
    }
}