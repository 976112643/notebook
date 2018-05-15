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
    fun updateAll(@Field("notes") notes:String): Call<BaseBean<String>>
    @FormUrlEncoded
    @POST("Login/Register")
    fun loginOrRegister(@Field("device")device:String,@Field("device_brand")phone_brand:String,
                        @Field("device_model")phone_model:String,
                        @Field("device_man")phone_man:String
                        ):Call<BaseBean<UserInfo>>
    @POST("Note/Index/del")
    @FormUrlEncoded
    fun delNote(@Field("ids[]") ids:Array<String>): Call<BaseBean<String>>

    @GET("Note/Index/get_diff_notes")
    fun getDiffNotes(@Query("ids[]") ids:Array<String>,@Query("versions[]") versions:Array<String>): Call<BaseBean<List<Note>>>
}