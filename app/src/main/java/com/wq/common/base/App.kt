package com.wq.common.base

import android.app.Application
import android.os.Handler
import com.wq.common.service.NetTaskService
import io.realm.Realm
import org.jetbrains.anko.startService
import android.os.Looper



/**
 * Created by WQ on 2017/5/24.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        _CONTEXT=this
        startService<NetTaskService>()
        Realm.init(this)
    }


    companion object {
      lateinit var _CONTEXT:App
    }
}
