package com.wq.common.base

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wq.config.R
import com.wq.common.base.util.StatusBarUtil
import com.wq.common.base.util.StatusBarUtil.checkDeviceHasNavigationBarByKitkat
import com.wq.common.base.util.StatusBarUtil.checkDeviceHasNavigationBarByLollipop

/**
 * Activity 基类
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
         fixBug()
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





    /**
     * 修复状态栏沉浸和输入法模式冲突的问题
     * fixBug
     */
    private lateinit var mChildOfContent: View
    private var usableHeightPrevious: Int = 0
    private lateinit var frameLayoutParams: FrameLayout.LayoutParams
    private fun fixBug() {
        if(!isFixInputBug())return
        mChildOfContent = (window.decorView as ViewGroup).getChildAt(0)
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
                    if (checkDeviceHasNavigationBarByLollipop(this)) {
                        val navigationBarHeight = getNavigationBarHeight()
                        frameLayoutParams.height = usableHeightSansKeyboard - navigationBarHeight
                    } else {
                        frameLayoutParams.height = usableHeightSansKeyboard
                    }
                } else {
                    if (checkDeviceHasNavigationBarByKitkat(this)) {
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
        val resources = getResources()
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        return height
    }
}
