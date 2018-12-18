package com.wq.common.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.support.v4.content.ContextCompat.startActivity
import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.content.Intent
import android.support.annotation.DrawableRes
import android.support.v4.util.Pair
import android.widget.TextView
import com.github.florent37.expectanim.core.custom.CustomAnimExpectation


/**
 * 旋转动画
 */
fun roate(view: View, block: RotateAnimation.() -> Unit = {}): Animation {
    var anim = RotateAnimation(0f, 360f, (view.width / 2).toFloat(), (view.height / 2).toFloat())
    anim.duration = 1500
    anim.repeatMode = 0
    anim.fillBefore = true
    block(anim)
    view.startAnimation(anim)
    return anim
}

/**
 * 右侧进入动画
 */
fun rightIn(block: Animation.() -> Unit = {}): Animation {
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
fun rightOut(block: Animation.() -> Unit = {}): Animation {
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

fun Animation.listener(onStart: (p0: Animation?) -> Unit = {}, onRepeat: (p0: Animation?) -> Unit = {}, onEnd: (p0: Animation?) -> Unit = {}): Animation {
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

/**
 * 动画执行监听简化
 * Created by weiquan on 2018/5/4.
 */

fun Animator.listener(onStart: (Animator?) -> Unit = {}, onRepeat: (Animator?) -> Unit = {}, onEnd: (Animator?) -> Unit = {}, onCancel: (Animator?) -> Unit = {}): Animator {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            onRepeat(animation)
        }

        override fun onAnimationEnd(animation: Animator?) {
            onEnd(animation)
        }

        override fun onAnimationStart(animation: Animator?) {
            onStart(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
            onCancel(animation)
        }
    })
    return this
}

fun FadeInLeft(target: View) {
    var animatorSet = AnimatorSet()
//    animatorSet.duration
//    AnimatorBuilder {
//        duration = 700
//
//    }
    AnimatorSet().playTogether(
            ObjectAnimator.ofFloat(target, "alpha", 0f, 1f),
            ObjectAnimator.ofFloat(target, "translationX", (-target.getWidth() / 4).toFloat(), 0f)
    )
}

fun AnimBuilder(builder: AnimatorSet.() -> Unit): AnimatorSet {
    var animatorSet = AnimatorSet()
    builder(animatorSet)
    return animatorSet
}

val IMAGE_RES_ID = "image_red_id"
val TEXT = "text"
val TRANSIT_IMAGE = "transit_image"
val TRANSIT_TEXT = "transit_text"
fun startThis(context: Context, clazz: Class<*>, textView: View, text: CharSequence) {

    val intent = Intent(context, clazz)
    intent.putExtra(TEXT, text)
    // core

    try {
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                Pair.create(textView, TRANSIT_TEXT))
        ActivityCompat.startActivity(context as Activity, intent, optionsCompat.toBundle())
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        context.startActivity(intent)
    }

}

fun toWidthPx(aimWidth: Float): CustomAnimExpectation =
        object : CustomAnimExpectation() {
            override fun getAnimator(viewToMove: View?): Animator? {
                if(viewToMove==null)return null
                val objectAnimator = ObjectAnimator.ofFloat(viewToMove, "width", viewToMove.width.toFloat(), aimWidth)
                objectAnimator.setEvaluator(ArgbEvaluator())
                return objectAnimator
            }

        }

//class AnimatorBuilder {
//    val MODE_TOGETHER=1
//    val MODE_SEQUEN=2
//    val MODE_PLAY=3
//    var duration: Long = 500
//    val animSet by lazy { AnimatorSet() }
//    var playMode:Int=0;
//    constructor(init: AnimatorBuilder.() -> Unit) {
//        init()
//    }
//
//    fun together(vararg items: Animator) {
//        var animatorSet = AnimatorSet()
//        animatorSet.playTogether(items.asList())
//        animSet.duration=duration
//        when(playMode){
//            MODE_PLAY->
//                animSet.play(items.asList())
//        }
//
//    }
//
//}

//
//val anim= TranslateAnimation(
//        Animation.RELATIVE_TO_SELF,1f,
//        Animation.RELATIVE_TO_SELF,0f,
//        Animation.RELATIVE_TO_PARENT,0f,
//        Animation.RELATIVE_TO_PARENT,0f)
//anim.duration=200
//anim.interpolator= BounceInterpolator()