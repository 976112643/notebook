package com.wq.common.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.Display
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager

import java.lang.reflect.Method

/**
 * Created by wq on 2017-07-12.
 *
 *
 * 监听输入法导致的界面可见区域变化,解决背景挤压的问题.  (只能在全屏沉浸时有效, 其他情况依然会挤压到根布局)
 */

class ConversationHeightFix
/**
 * @param activity
 * @param onInputChange 高度变化监听
 */
(activity: Activity, private val onInputChange: OnInputChange) {
    private val context: Context

    //根布局
    private val mRootView: View
    private var usableHeightPrevious: Int = 0

    private val navigationBarHeight: Int
        get() {
            val resources = context.resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }

    //NavigationBar状态是否是显示
    val isNavigationBarShow: Boolean
        get() {
            val mContext = context as Activity
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val display = mContext.windowManager.defaultDisplay
                val size = Point()
                val realSize = Point()
                display.getSize(size)
                display.getRealSize(realSize)
                return realSize.y != size.y
            } else {
                val menu = ViewConfiguration.get(context).hasPermanentMenuKey()
                val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
                return if (menu || back) {
                    false
                } else {
                    true
                }
            }
        }

    init {
        this.context = activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        mRootView = activity.window.decorView
        mRootView.viewTreeObserver.addOnGlobalLayoutListener { possiblyResizeChildOfContent() }
    }

    /**
     * @param activity
     * @param contentView  内容区域
     */
    constructor(activity: Activity, contentView: View) : this(activity, object : OnInputChange {
        override fun onInputChange(height: Int) {
            setContentHeight(height, contentView)
        }
    }) {
    }

    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()//计算可用高度, (从屏幕最上面开始到输入法的高度)
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mRootView.height//获取跟布局高度,
            val heightDifference = usableHeightSansKeyboard - usableHeightNow//计算差值,判断键盘是否展示中
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // 软键盘弹出
                onInputChange.onInputChange(usableHeightNow)
            } else {
                // 软键盘隐藏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (checkDeviceHasNavigationBarByLollipop(context)) {
                        onInputChange.onInputChange(usableHeightNow)
                    } else {
                        onInputChange.onInputChange(usableHeightNow)
                    }
                } else {
                    if (checkDeviceHasNavigationBarByKitkat(context)) {
                        var navigationBarHeight = navigationBarHeight
                        if (!isNavigationBarShow) navigationBarHeight = 0
                        val height = usableHeightSansKeyboard - navigationBarHeight
                        onInputChange.onInputChange(height)
                    } else {
                        onInputChange.onInputChange(usableHeightSansKeyboard)
                    }
                }
            }
            mRootView.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun printView(viewName: String, view: View) {
        var pW = 0
        var pH = 0
        if (view.layoutParams != null) {
            pW = view.layoutParams.width
            pH = view.layoutParams.height
        }
        Log.d("ConversationHeightFix:", viewName + "-- W:" + view.width + " H:" + view.height + " pW:" + pW + " pH:" + pH)
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mRootView.getWindowVisibleDisplayFrame(r)
        return r.bottom
    }


    interface OnInputChange {
        fun onInputChange(height: Int)
    }

    companion object {
        fun setListener(activity: Activity, onInputChange: OnInputChange) {
            ConversationHeightFix(activity, onInputChange)
        }

        fun setAutoSizeContent(activity: Activity, contentView: View) {
            contentView.post {
                ConversationHeightFix(activity, contentView)
            }

        }

        fun setContentHeight(height: Int, layContent: View) {
            val layoutParams = layContent.layoutParams
            val r = Rect()
            layContent.getHitRect(r)
            layoutParams.height = height - r.top
            layContent.requestLayout()
        }


        fun checkDeviceHasNavigationBarByLollipop(context: Context): Boolean {
            var hasNavigationBar = false
            val rs = context.resources
            val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id)
            }
            try {
                val systemPropertiesClass = Class.forName("android.os.SystemProperties")
                val m = systemPropertiesClass.getMethod("get", String::class.java)
                val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
                if ("1" == navBarOverride) {
                    hasNavigationBar = false
                } else if ("0" == navBarOverride) {
                    hasNavigationBar = true
                }
            } catch (e: Exception) {

            }

            return hasNavigationBar

        }

        @SuppressLint("NewApi")
        fun checkDeviceHasNavigationBarByKitkat(activity: Context): Boolean {
            //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
            val hasMenuKey = ViewConfiguration.get(activity)
                    .hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap
                    .deviceHasKey(KeyEvent.KEYCODE_BACK)

            return if (!hasMenuKey && !hasBackKey) {
                // 做任何你需要做的,这个设备有一个导航栏
                true
            } else false
        }
    }
}
