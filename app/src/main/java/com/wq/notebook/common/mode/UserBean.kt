package com.wq.notebook.common.mode
import com.wq.common.util.Pref
import com.wq.common.util.notEmpty

/**
 * Created by weiquan on 2017/9/23.
 */
class UserBean {
    var uid:String by Pref.prefString()

    /**
     * 登陆信息是否有效
     */
    fun isVaild():Boolean{
        return uid.notEmpty ()
    }
}