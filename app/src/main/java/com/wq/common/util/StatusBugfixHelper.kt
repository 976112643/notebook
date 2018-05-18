package com.wq.common.util

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wq.common.base.util.StatusBarUtil

/**
 * Created by weiquan on 2018/5/18.
 */
class StatusBugfixHelper {

    lateinit var act:Activity
    fun attatch(act:Activity ):StatusBugfixHelper{
        this.act=act
        return this
    }
    /**
     * 修复状态栏沉浸和输入法模式冲突的问题
     * fixBug
     */
    private lateinit var mChildOfContent: View
    private var usableHeightPrevious: Int = 0
    private lateinit var frameLayoutParams: FrameLayout.LayoutParams
     fun fixBug() {
        if(!isFixInputBug())return
        mChildOfContent = (act.window.decorView as ViewGroup).getChildAt(0)
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener { possiblyResizeChildOfContent() }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }

    /**
     * 是否修复,出现冲突时,开启该选项
     */
    open protected fun  isFixInputBug(): Boolean =false

    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mChildOfContent.rootView.height
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // 软键盘弹出
                val navigationBarHeight = getNavigationBarHeight()
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + navigationBarHeight / 2

            } else {
                // 软键盘隐藏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (StatusBarUtil.checkDeviceHasNavigationBarByLollipop(act)) {
                        val navigationBarHeight = getNavigationBarHeight()
                        frameLayoutParams.height = usableHeightSansKeyboard - navigationBarHeight
                    } else {
                        frameLayoutParams.height = usableHeightSansKeyboard
                    }
                } else {
                    if (StatusBarUtil.checkDeviceHasNavigationBarByKitkat(act)) {
                        val navigationBarHeight = getNavigationBarHeight()
                        frameLayoutParams.height = usableHeightSansKeyboard - navigationBarHeight
                    } else {
                        frameLayoutParams.height = usableHeightSansKeyboard
                    }
                }
            }
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top// 全屏模式下： return r.bottom
    }

    private fun getNavigationBarHeight(): Int {
        val resources = act.getResources()
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        return height
    }
}