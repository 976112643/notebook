package com.wq.common.net

import com.wq.notebook.common.mode.UserBean
import okhttp3.*
import okio.Buffer
import java.io.IOException


/**
 * 用于放置全局通用參數
 * Created by weiquan on 2017/9/23.
 */
class addConfigInterceptor : Interceptor {
    val userInfo = UserBean()
    var queryParamsMap: Map<String, String> = HashMap()
    var paramsMap: Map<String, String> = HashMap()
    var headerParamsMap: Map<String, String> = HashMap()
    var headerLinesList: List<String> = ArrayList()


    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val requestBuilder = request.newBuilder()

        // process header params inject
        val headerBuilder = request.headers().newBuilder()
        for ((key, value) in headerParamsMap) {
            headerBuilder.add(key, value)
        }
        for (line in headerLinesList) {
            headerBuilder.add(line)
        }

        requestBuilder.headers(headerBuilder.build())
        // process header params end

        // process queryParams inject whatever it's GET or POST
        if (queryParamsMap.size > 0) {
            injectParamsIntoUrl(request, requestBuilder, queryParamsMap)
        }
        // process header params end
        // process post body inject
        if (request.method().equals("POST") && request.body()?.contentType()?.subtype().equals("x-www-form-urlencoded")) {
            val formBodyBuilder = FormBody.Builder()

            for ((key, value) in paramsMap) {
                formBodyBuilder.add(key, value)
            }
            formBodyBuilder.add("uid",userInfo.uid)
            val formBody = formBodyBuilder.build()
            var postBodyString = bodyToString(request.body())
            postBodyString += (if (postBodyString.length > 0) "&" else "") + bodyToString(formBody)
            requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString))
        } else {
            // can't inject into body, then inject into url
            injectParamsIntoUrl(request, requestBuilder, paramsMap)
        }

        request = requestBuilder.build()
        return chain.proceed(request)
    }

    // func to inject params into url
    private fun injectParamsIntoUrl(request: Request, requestBuilder: Request.Builder, paramsMap: Map<String, String>) {
        val httpUrlBuilder = request.url().newBuilder()
        for ((key, value) in paramsMap) {
            httpUrlBuilder.addQueryParameter(key, value)
        }
        httpUrlBuilder.addQueryParameter("uid",userInfo.uid)
        requestBuilder.url(httpUrlBuilder.build())
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }

    }
}