package com.wq.notebook.home

import android.os.Bundle
import com.lizhuo.kotlinlearning.R
import com.wq.common.base.BaseActivity
import com.wq.notebook.home.adapter.NoticeListAdapter
import kotlinx.android.synthetic.main.activity_notice_list.*

/**
 * 笔记列表
 */
class NoteListActivity : BaseActivity() {
    val adapter = NoticeListAdapter()
    override fun onViewCreated(savedInstanceState: Bundle?) {
        recyclerView.adapter = adapter
    }

    override fun getLayoutId(): Int =  R.layout.activity_notice_list


}
