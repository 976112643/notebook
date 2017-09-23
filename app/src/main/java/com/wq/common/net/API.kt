package com.wq.common.net

import com.wq.common.db.mode.Note
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 * 接口
 * Created by WQ on 2017/9/20.
 */
interface API {
    @GET("Note/Index")
    fun getNotes(@Query("p")p:Int): Call<BaseBean<List<Note>>>
    @FormUrlEncoded
    @POST("Note/Index/edit")
    fun editNote(note:Note): Call<BaseBean<Any>>
    @FormUrlEncoded
    @POST("Note/Index/update_all")
    fun editNotes(notes:List<Note>): Call<BaseBean<Any>>
}