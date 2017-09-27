package com.wq.common.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by WQ on 2017/9/20.
 */

object APIManager {
    internal var request: API

    init {
        var client = OkHttpClient.Builder()
                .addInterceptor(addConfigInterceptor()).build()
        val retrofit = Retrofit.Builder()
               .baseUrl("https://manager.quanwe.top/Api/") //设置网络请求的Url地址
//              .baseUrl("http://192.168.1.130/Api/") //设置网络请求的Url地址
//                .baseUrl("http://192.168.31.36/Api/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .client(client)
                .build()
        // 创建 网络请求接口 的实例
        request = retrofit.create(API::class.java)
    }


}
