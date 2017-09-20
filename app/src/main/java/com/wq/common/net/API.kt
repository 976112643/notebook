package com.wq.common.net

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET


/**
 * 接口
 * Created by WQ on 2017/9/20.
 */
interface API {
    @GET("openapi.do?keyfrom=Yanzhikai&key=2032414398&type=data&doctype=json&version=1.1&q=car")
    fun getCall(): Call<ResponseBody>
}