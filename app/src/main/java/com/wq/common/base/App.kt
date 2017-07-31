package com.wq.common.base

import android.app.Application

/**
 * Created by WQ on 2017/5/24.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        _CONTEXT=this
    }


    companion object {
      lateinit var _CONTEXT:App
    }
}
