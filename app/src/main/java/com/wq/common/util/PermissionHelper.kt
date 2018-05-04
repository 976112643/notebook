package com.wq.common.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker

/**
 * 权限帮助类
 * Created by weiquan on 2018/5/3.
 */
class PermissionHelper {
    var callback: PermissionsResult? = null
    val REQUEST_CODE = 0x0011
    fun requestPermissions(activity: Activity,
                           permissions: Array<String>, callback: PermissionsResult?) {
        var isAllGrant = true
        for (permiss in permissions) {
            //检查权限是否都被允许
            isAllGrant = isAllGrant && (PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(activity, permiss))
        }
        this.callback = callback
        if(isAllGrant){
            //权限都被允许,直接执行回调
            callback?.onGrant()
        }else{
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        if(requestCode!=REQUEST_CODE)return;
        val isAllGrant = grantResults != null
        val findIndex = grantResults?.find { it != PERMISSION_GRANTED }
        if (isAllGrant && (findIndex == null || findIndex == -1)) {
            callback?.onGrant()
        } else {
            callback?.onDenied()
        }
    }

    interface PermissionsResult {
        fun onGrant()
        fun onDenied()
    }
}