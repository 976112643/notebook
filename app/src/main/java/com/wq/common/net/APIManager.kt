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
                .baseUrl("http://fanyi.youdao.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(object: CallAdapter.Factory(){
                    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
                .build()
        // 创建 网络请求接口 的实例
        request = retrofit.create(API::class.java)
    }




}
