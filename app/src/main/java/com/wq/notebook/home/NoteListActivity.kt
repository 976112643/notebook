package com.wq.notebook.home

import android.os.Bundle
import com.lizhuo.kotlinlearning.R
import com.wq.common.base.BaseActivity
import com.wq.common.db.executeTransaction
import com.wq.common.db.mode.Note
import com.wq.common.db.realm
import com.wq.common.util.PageLimitDelegate
import com.wq.notebook.home.adapter.NoticeListAdapter
import io.realm.Sort
import kotlinx.android.synthetic.main.include_refresh_layout.*

/**
 * 笔记列表
 */
class NoteListActivity : BaseActivity(), PageLimitDelegate.DataProvider {
    val pageDelegate = PageLimitDelegate<Note>(this)
    val adapter = NoticeListAdapter()
    override fun onViewCreated(savedInstanceState: Bundle?) {
        pageDelegate.attach(swipeRefreshLayout, recyclerView, adapter)
        adapter.setNewData(realm.where(Note::class.java).findAllSorted("updatetime", Sort.ASCENDING))
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_notice_list
    }

    override fun loadData(page: Int) {
        executeTransaction {
        }
    }

}
