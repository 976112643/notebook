package com.wq.common.base

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

/**
 * Activity 基类
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        window.decorView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                window.decorView.removeOnLayoutChangeListener(this)
                onViewCreated(savedInstanceState)
            }
        })
    }
    /**
     * 视图创建中。。
     * 用来视图创建期间做些事情
     */
  open  fun  onViewCreating(savedInstanceState: Bundle?){}

    /**
     * 视图创建完毕，可以获取到控件宽高等信息
     */
    abstract fun  onViewCreated(savedInstanceState: Bundle?)

    abstract fun getLayoutId():Int
}
