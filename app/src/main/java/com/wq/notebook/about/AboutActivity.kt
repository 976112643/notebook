package com.wq.notebook.about

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.wq.common.base.BaseActivity
import com.wq.common.util.*
import com.wq.config.R
import kotlinx.android.synthetic.main.activity_notice_about.*
import android.view.Gravity
import android.widget.SeekBar
import com.github.florent37.expectanim.ExpectAnim
import com.github.florent37.expectanim.core.Expectations.*


class AboutActivity : BaseActivity() {
    override fun onViewCreated(savedInstanceState: Bundle?) {

        YoYo.with(Techniques.FadeInLeft)
                .duration(500)
                .playOn(text2)
        YoYo.with(Techniques.FadeInLeft)
                .duration(700)
                .playOn(text1)
        YoYo.with(Techniques.FadeInRight)
                .duration(700)
                .playOn(text4)
        YoYo.with(Techniques.FadeInRight)
                .duration(500)
                .playOn(text3)

//        AnimatorSet().playTogether(Techniques.FadeInLeft.animator.animatorAgent)

        AnimBuilder {
            duration = 250
            startDelay = 2000
            listener(onEnd = {
               titleBar.visibleSeize(false)
            })
            playTogether(
                    ObjectAnimator.ofFloat(titleBar, "alpha", 1f, 0f),
                    ObjectAnimator.ofFloat(titleBar, "translationY", titleBar.translationY, (-titleBar.getHeight()).toFloat())
            )
        }.start()

        layContent.click {
            if(titleBar.isShown)return@click
            titleBar.visibleSeize(true)
            AnimBuilder {
                duration = 250
                playTogether(
                        ObjectAnimator.ofFloat(titleBar, "alpha", 0f, 1f),
                        ObjectAnimator.ofFloat(titleBar, "translationY", (-titleBar.getHeight()).toFloat(), 0f)
                )
            }.start()
        }

        click(this::onTextClick, text1, text2, text3, text4)
        click({
            startThis(that,SharedAnimActivity::class.java,it,(it as TextView).text)
        },shareText,shareText2)

        descriptAnim()
        descriptLeftBtnAnim()
    }

    private fun descriptLeftBtnAnim() {
        click({view->
            arrayListOf(btn1, btn2, btn3, btn4).filter { it != view }
                    .forEach {
                        ExpectAnim()
                                .expect(it)
                                .toBe(width(80).toDp()).toAnimation().start()
                    }
            ExpectAnim()
                    .expect(view)
                    .toBe(width(100, Gravity.CENTER, Gravity.CENTER).toDp()).toAnimation().start()

        },btn1,btn2,btn3,btn4)
    }


    private fun descriptAnim() {
        var defWidth=animText1.width
        val expectAnimMove = ExpectAnim()
                .expect(animText1)//Anim1缩小一半
                .toBe(width(animText1.width/2).keepRatio(), leftOfParent().withMarginDp(5f), topOfParent().withMarginDp(5f) )
                .expect(animText3)//Anim2移动到Anim1右边
                .toBe(toRightOf(animText1).withMarginDp(5f),
                        width(animText1.width/2) .keepRatio(), topOfParent().withMarginDp(5f))
                .expect(animText2)//Anim3缩小一半-到Anim1下面
                .toBe(belowOf(animText1).withMarginDp(5f),
                        width(animText1.width/2).keepRatio(),leftOfParent().withMarginDp(5f)
                )
                .expect(animText4)//Anim4缩小一半-到Anim3右边
                .toBe(belowOf(animText1).withMarginDp(5f),
                        toRightOf(animText2).withMarginDp(5f),
                        width(animText1.width/2).keepRatio()
                )
                .toAnimation()

        click({
            expectAnimMove.setDuration(250)
            if(animText1.scaleX!=1f||animText1.scaleY!=1f){
                seekBar.progress=0
                expectAnimMove.reset()
            }else{
                seekBar.progress=seekBar.max
                expectAnimMove.start()
            }

        },animText1,animText2,animText3,animText4)
        seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    expectAnimMove.setPercent((1f * progress / seekBar.max).toFloat())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {


                ValueAnimator.ofFloat(seekBar.progress.toFloat()/seekBar.max.toFloat(),(seekBar.progress>seekBar.max/2).ternary(1f,0f)).apply {
                    duration=250
                    addUpdateListener {
                        var percent = it.animatedValue as Float
                        expectAnimMove.setPercent(percent)
                        seekBar.progress=(percent*seekBar.max).toInt()
                    }
                    start()
                }

            }
        })
    }

    fun onTextClick(view: View) {
        AnimBuilder {
            listener (onEnd = {
                startThis(that,SharedAnimActivity::class.java,view,(view as TextView).text)
            })
            play(AnimBuilder {
                //先播放被点击按钮的动画
                duration = 250
                playTogether(
                        ObjectAnimator.ofFloat(view, "alpha", 1f, 0f),
                        ObjectAnimator.ofFloat(view, "translationY", view.translationY, (-view.getHeight()).toFloat())
                )
            }).before(AnimBuilder {
                //然后同时播放其余按钮的动画
                playTogether(
                        arrayOf(text1, text2, text3, text4)
                                .filter { it != view }
                                .list2list {
                                    AnimBuilder {
                                        //其余按钮的动画效果如下
                                        duration = 500
                                        playTogether(
                                                ObjectAnimator.ofFloat(it, "alpha", 1f, 0f),
                                                ObjectAnimator.ofFloat(it, "translationY", view.translationY, (-it.getHeight() / 4).toFloat())
                                        )
                                    }
                                }
                )
            })
        }.start()
    }


    override fun getLayoutId(): Int = R.layout.activity_notice_about


}