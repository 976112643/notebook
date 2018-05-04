package com.wq.common.util

import android.content.Context
import android.telephony.TelephonyManager

/**
 * 设备信息获取
 * Created by weiquan on 2018/5/3.
 */

/**
 * 设备号
 */

val _DEVICE_NO: String by lazy {
    val systemService = _CONTEXT.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    systemService.deviceId
}

/**
 * 获取手机品牌
 *
 * @return
 */
fun getPhoneBrand(): String {
    return android.os.Build.BRAND
}

/**
 * 获取手机型号
 *
 * @return
 */
fun getPhoneModel(): String {
    return android.os.Build.MODEL
}

/**
 * 手机厂商
 */
fun getPhoneMan(): String {
    return android.os.Build.MANUFACTURER
}