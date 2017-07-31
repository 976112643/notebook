package com.wq.notebook.home

import android.os.Bundle
import com.lizhuo.kotlinlearning.R
import com.wq.common.base.BaseActivity
import com.wq.common.util.PageLimitDelegate
import com.wq.notebook.home.adapter.NoticeListAdapter
import com.wq.notebook.home.bean.NoteListItem
import kotlinx.android.synthetic.main.include_refresh_layout.*

/**
 * 笔记列表
 */
class NoteListActivity : BaseActivity(), PageLimitDelegate.DataProvider {
    val pageDelegate = PageLimitDelegate<NoteListItem>(this)
    val adapter = NoticeListAdapter()
    override fun onViewCreated(savedInstanceState: Bundle?) {
        pageDelegate.attach(swipeRefreshLayout, recyclerView, adapter)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_notice_list
    }

    override fun loadData(page: Int) {

    }

}
