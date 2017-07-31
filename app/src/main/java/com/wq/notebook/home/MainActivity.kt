package com.wq.notebook.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lizhuo.kotlinlearning.R
import com.wq.common.base.BaseActivity
import com.wq.common.base.QBaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

/**
 * 主页
 */
class MainActivity : BaseActivity() {
    override fun onViewCreated(savedInstanceState: Bundle?) {
        toast("主页面加载")
        list_notices.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        list_notices.adapter= QBaseAdapter().apply { NoteAdapter(this)}

    }

    override fun getLayoutId(): Int {
      return  R.layout.activity_main
    }

}
