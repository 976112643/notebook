package com.wq.common.base

import android.app.Application
import io.realm.Realm

/**
 * Created by WQ on 2017/5/24.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        _CONTEXT=this
        Realm.init(this);
    }


    companion object {
      lateinit var _CONTEXT:App
    }
}
