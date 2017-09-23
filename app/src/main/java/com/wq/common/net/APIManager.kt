package com.wq.common.net

import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/**
 * Created by WQ on 2017/9/20.
 */

object APIManager {
    internal var request: API
    init {
        val retrofit = Retrofit.Builder()
//                .baseUrl("https://manager.quanwe.top/Api/") //设置网络请求的Url地址
                .baseUrl("http://192.168.1.130/Api/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build()
        // 创建 网络请求接口 的实例
        request = retrofit.create(API::class.java)
    }




}
