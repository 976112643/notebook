package com.wq.common.base

import android.app.Activity
import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wq.common.util.get

/**
 * Fragment 基类
 * Created by weiquan on 2017/5/26.
 */

abstract class BaseFragment : Fragment() {
    val that by lazy { activity }
    var eventCallback:((event:String,status:Int)->Unit)?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(getLayoutId(),container,false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerLocalBoardcast()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterLocalBoardcast()
    }
    abstract fun  getLayoutId():Int

    /**
     * 事件回调
     */
    fun registerLocalBoardcast(){
//        com.wq.common.service.localBoardcast
        LocalBroadcastManager.getInstance(that).registerReceiver(localBoardcast, IntentFilter("${that.packageName}.localBoardcast"))
    }
    fun unregisterLocalBoardcast(){
        LocalBroadcastManager.getInstance(that)
                .unregisterReceiver(localBoardcast)

    }
    private val localBoardcast: BroadcastReceiver by lazy { object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            p1?.let { eventCallback?.invoke(it["event"],it["status"]) }

        }
    } }
}
