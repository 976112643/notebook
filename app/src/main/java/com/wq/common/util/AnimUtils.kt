package com.wq.common.util

import android.view.animation.Animation
import android.view.animation.TranslateAnimation


/**
 * 右侧进入动画
 */
fun rightIn(block:Animation.()->Unit={}): Animation {
    val anim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f)
    anim.duration = 200
    block(anim)
    return anim
}

/**
 * 右侧退出动画
 */
fun rightOut(block:Animation.()->Unit={}): Animation {
    val anim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f)
    anim.duration = 200
    block(anim)
    return anim
}

/**
 * 动画执行监听简化
 * Created by weiquan on 2018/5/4.
 */

fun Animation.listener(onStart: (p0: Animation?) -> Unit = {}, onRepeat: (p0: Animation?) -> Unit = {}, onEnd: (p0: Animation?) -> Unit = {}):Animation {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
            onRepeat(p0)
        }

        override fun onAnimationEnd(p0: Animation?) {
            onEnd(p0)
        }

        override fun onAnimationStart(p0: Animation?) {
            onStart(p0)
        }
    })
    return this
}
//
//val anim= TranslateAnimation(
//        Animation.RELATIVE_TO_SELF,1f,
//        Animation.RELATIVE_TO_SELF,0f,
//        Animation.RELATIVE_TO_PARENT,0f,
//        Animation.RELATIVE_TO_PARENT,0f)
//anim.duration=200
//anim.interpolator= BounceInterpolator()