package com.wq.common.net

import com.wq.common.db.mode.Note
import com.wq.notebook.common.mode.UserBean
import com.wq.notebook.common.mode.UserInfo
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
    @GET("Note/Index/get_new_notes")
    fun getNewNotes(@Query("p")p:Int,@Query("version")version:Int): Call<BaseBean<List<Note>>>

    @POST("Note/Index/edit")
    fun editNote(@Body note:Note): Call<BaseBean<Any>>

    @POST("Note/Index/update_all")
    @FormUrlEncoded
    fun editNotes(@Field("notes") notes:String): Call<BaseBean<String>>
    @FormUrlEncoded
    @POST("Login/Register")
    fun loginOrRegister(@Field("device")device:String):Call<BaseBean<UserInfo>>
}