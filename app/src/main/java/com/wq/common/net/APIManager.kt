package com.wq.common.net

import com.wq.common.util.ternary
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 接口管理，用于创建请求实例
 * Created by WQ on 2017/9/20.
 */

object APIManager {
    internal var request: API
    val DEBUG=true
    init {
        /**
         * 配置OkHtttpClient一些东西
         */
        val client = OkHttpClient.Builder()
                .addInterceptor(addConfigInterceptor())
//                .addNetworkInterceptor(
//                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .addNetworkInterceptor(
                           HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .hostnameVerifier { s, sslSession -> true}
                .build()
        /**
         * 创建请求实例
         */

        var baseUrl = DEBUG.ternary(
//                "http://192.168.2.137/Api/",
                "http://192.168.2.125/manager/Api/",
                "https://manager.quanwe.top/Api/")
        val retrofit = Retrofit.Builder()
               .baseUrl(baseUrl) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .client(client)
                .build()
        // 创建 网络请求接口 的实例
        request = retrofit.create(API::class.java)
    }


}
