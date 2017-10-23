package com.wq.notebook.common.mode.logic

import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator

/**
 * 控件拖动逻辑处理
 * Created by WQ on 2017/10/23.
 */

class ViewDragMode(private val targetView: View) : View.OnTouchListener {
    init {
        this.targetView.setOnTouchListener(this)
    }

    private var parentHeight: Int = 0
    private var parentWidth: Int = 0
    private var lastX: Int = 0
    private var lastY: Int = 0
    /**
     * 是否拖拽中
     * @return
     */
    private var isDraging: Boolean = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val rawX = event.rawX.toInt()
        val rawY = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                targetView.isPressed = true
                isDraging = false
                val viewParent = targetView.parent
                viewParent.requestDisallowInterceptTouchEvent(true)
                lastX = rawX
                lastY = rawY
                val parent: ViewGroup
                parent = viewParent as ViewGroup
                parentHeight = parent.height
                parentWidth = parent.width
            }
            MotionEvent.ACTION_MOVE -> {
                if (parentHeight <= 0 || parentWidth == 0) {
                    isDraging = false
                } else {
                    isDraging = true
                    val dx = rawX - lastX
                    val dy = rawY - lastY
                    //这里修复一些华为手机无法触发点击事件
                    val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
                    if (distance == 0) {
                        isDraging = false
                    } else {
                        var x = targetView.x + dx
                        var y = targetView.y + dy
                        //检测是否到达边缘 左上右下
                        x = if (x < 0)
                            0f
                        else if (x > parentWidth - targetViewWidth)
                            (parentWidth - targetViewWidth).toFloat()
                        else x
                        y = if (targetView.y < 0)
                            0f
                        else if (targetView.y + targetViewHeight > parentHeight)
                            (parentHeight - targetViewHeight).toFloat()
                        else y
                        targetView.x = x
                        targetView.y = y
                        lastX = rawX
                        lastY = rawY
                    }
                }

            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP ->
                if (!isNotDrag) {
                    //恢复按压效果
                    targetView.isPressed = false
                    if (rawX >= parentWidth / 2) {
                        //靠右吸附
                        targetView.animate().setInterpolator(DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(parentWidth.toFloat() - targetViewWidth.toFloat() - targetView.x)
                                .start()
                    } else {
                        //靠左吸附
                        val oa = ObjectAnimator.ofFloat(targetView, "x", targetView.x, 0f)
                        oa.interpolator = DecelerateInterpolator()
                        oa.duration = 500
                        oa.start()
                    }
                }
        }
        //如果是拖拽则消s耗事件，否则正常传递即可。
        return !isNotDrag || targetView.onTouchEvent(event)
    }

    private val targetViewWidth: Int get() {
        var margin = 0
        val layoutParams = targetView.layoutParams;
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            margin = layoutParams.leftMargin + layoutParams.rightMargin
        }
        return targetView.width + margin
    }
    private val targetViewHeight: Int get() {
        var margin = 0
        val layoutParams = targetView.layoutParams;
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            margin = layoutParams.topMargin + layoutParams.bottomMargin
        }
        return targetView.height + margin
    }

    private val isNotDrag: Boolean
            = !isDraging && (targetView.x == 0f || targetView.x == (parentWidth - targetView.width).toFloat())

}
