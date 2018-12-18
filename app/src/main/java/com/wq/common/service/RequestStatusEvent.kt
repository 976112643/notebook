package com.wq.common.service

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.wq.common.util._CONTEXT

/**
 * Created by weiquan on 2018/5/21.
 */
class RequestStatusEvent {



    companion object {
        val EVNET_SYN="syn_data"
        val STATUS_START=1
        val STATUS_ING=2
        val STATUS_FINIAH=3
        val STATUS_ERROR=4

        fun sendEvent(event:String,status:Int){
            var broadcastManager = LocalBroadcastManager.getInstance(_CONTEXT)
            var intent = Intent(getAction())
            intent.putExtra("status",status)
            intent.putExtra("event",event)
            broadcastManager.sendBroadcast(intent)
        }

        private fun getAction():String{
            return "${_CONTEXT.packageName}.localBoardcast"
        }
    }

}