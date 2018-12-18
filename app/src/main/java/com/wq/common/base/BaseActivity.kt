package com.wq.common.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.wq.common.base.util.StatusBarUtil
import com.wq.common.util.StatusBugfixHelper
import com.wq.common.util.get
import com.wq.config.R

/**
 * Activity 基类
 */
abstract class BaseActivity : AppCompatActivity() {
    val that by lazy { this }
    var eventCallback:((event:String,status:Int)->Unit)?=null
    val statusBugFix by lazy { StatusBugfixHelper().attatch(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        delegate.installViewFactory()
        delegate.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        registerLocalBoardcast()
        if (isTranslucent()) {
            StatusBarUtil.transparencyBar(this)
        }
        if (isStatusContentDark()) {
            val type = StatusBarUtil.StatusBarLightMode(this)
            if (type <= 0 && ifTitleBarIsWhiteThenIWillSetBlackStatusBarOnNotCompatDevice()) {
                StatusBarUtil.setStatusBarColor(this, android.R.color.black)
            }
        }
        setContentView(getLayoutId())

        window.decorView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                window.decorView.removeOnLayoutChangeListener(this)
                onViewCreated(savedInstanceState)
            }
        })
        statusBugFix. fixBug()
    }


    protected fun ifTitleBarIsWhiteThenIWillSetBlackStatusBarOnNotCompatDevice(): Boolean {
        return isStatusContentDark()
    }

    /**
     * 启用状态栏透明
     * @return
     */
    protected fun isTranslucent(): Boolean {
        //return getStatusColorInStyle() == Color.TRANSPARENT
        return true
    }

    private fun getStatusColorInStyle(): Int {
        val attrs = intArrayOf(R.attr.colorPrimaryDark)
        val typedArray = obtainStyledAttributes(attrs)
        val backgroundResource = typedArray.getColor(0, Color.BLACK)
        typedArray.recycle()
        return backgroundResource
    }

    protected fun isStatusContentDark(): Boolean {
        val colorPrimary = getStatusColorInStyle()
        return colorPrimary == Color.TRANSPARENT || colorPrimary == Color.WHITE
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

    /**
     * 界面视图布局
     */
    abstract fun getLayoutId():Int


    override fun onDestroy() {
        super.onDestroy()
        unregisterLocalBoardcast()
    }

    /**
     * 事件回调
     */
    fun registerLocalBoardcast(){
        LocalBroadcastManager.getInstance(that).registerReceiver(localBoardcast, IntentFilter(packageName+".localBoardcast"))
    }
    fun unregisterLocalBoardcast(){
        LocalBroadcastManager.getInstance(that)
                .unregisterReceiver(localBoardcast)

    }
    private val localBoardcast:BroadcastReceiver by lazy { object:BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            p1?.let { eventCallback?.invoke(it["event"],it["status"]) }

        }
    } }

}
