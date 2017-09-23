package com.wq.notebook.common.mode
import com.wq.common.util.Pref

/**
 * Created by weiquan on 2017/9/23.
 */
class UserBean {
    var uid:String by Pref.prefString()
}