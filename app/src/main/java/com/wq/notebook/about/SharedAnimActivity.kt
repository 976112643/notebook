package com.wq.notebook.about

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.wq.common.base.BaseActivity
import com.wq.config.R
import android.support.v4.view.ViewCompat
import android.widget.TextView
import com.wq.common.util.*
import kotlinx.android.synthetic.main.activity_notice_shared_anim.*


class SharedAnimActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val imageResId = intent.getIntExtra(IMAGE_RES_ID, 0)
//        if (imageResId != 0) {
//            ViewCompat.setTransitionName(mIv, TRANSIT_IMAGE)
//            mIv.setImageResource(imageResId)
//        }

        val title = intent.getStringExtra(TEXT)
        if (title != null) {
            ViewCompat.setTransitionName(shareText, TRANSIT_TEXT)
            shareText.setText(title)
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {



    }



    override fun getLayoutId(): Int = R.layout.activity_notice_shared_anim


}