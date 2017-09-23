package com.wq.common.net

import com.wq.notebook.common.mode.UserBean
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.HttpUrl


/**
 * 用于放置全局通用样式
 * Created by weiquan on 2017/9/23.
 */
class addConfigInterceptor : Interceptor {
    val userInfo = UserBean()
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val modifiedUrl = request.url().
                newBuilder().
                addQueryParameter("uid", userInfo.uid).
                build()
        request = request.newBuilder().url(modifiedUrl).build();
        return chain.proceed(request)
    }

}