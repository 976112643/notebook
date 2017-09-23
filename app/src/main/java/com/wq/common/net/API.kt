package com.wq.common.net

import com.wq.common.db.mode.Note
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * 接口
 * Created by WQ on 2017/9/20.
 */
interface API {
    @GET("Note/Index")
    fun getNotes(@Query("uid")uid:String,@Query("p")p:Int): Call<BaseBean<List<Note>>>
}