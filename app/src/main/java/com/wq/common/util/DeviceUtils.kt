package com.wq.common.util

import android.content.Context
import android.telephony.TelephonyManager
import android.os.Build



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

var m_szDevIDShort = "35" + //we make this look like a valid IMEI
        Build.BOARD.length % 10 + Build.BRAND.length % 10 +
        Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
        Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
        Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
        Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
        Build.TAGS.length % 10 + Build.TYPE.length % 10 +
        Build.USER.length % 10

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