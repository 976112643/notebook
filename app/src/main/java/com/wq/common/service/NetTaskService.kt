package com.wq.common.service

import android.app.IntentService
import android.content.Intent
import com.wq.common.db.executeTransaction
import com.wq.common.db.mode.Note
import com.wq.common.db.realm
import com.wq.common.net.BaseBean
import com.wq.common.util._Log
import com.wq.common.util.api


/**
 * Created by WQ on 2017/9/20.
 */
class NetTaskService : IntentService(NetTaskService::class.java.name + "" + NetTaskService.nameNo++) {

    override fun onHandleIntent(intent: Intent?) {
        try {
            var bean=api.getNotes("10",1).execute().body()
            executeTransaction {
                bean?.info?.apply {
                    forEach {
                        realm.copyToRealmOrUpdate(it)
                    }
                }
            }
            _Log(bean)
        }catch (e:Exception){
            _Log(e)
        }

    }

    companion object {
        private var nameNo = 0
    }
}
