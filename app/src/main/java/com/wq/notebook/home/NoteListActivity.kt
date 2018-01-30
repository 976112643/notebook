package com.wq.notebook.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.wq.config.R
import com.wq.common.base.BaseActivity
import com.wq.common.service.NetTaskService
import com.wq.notebook.common.mode.logic.ViewDragMode
import com.wq.notebook.home.adapter.NoticeListAdapter
import com.wq.notebook.home.adapter.SimpleItemTouchHelper
import kotlinx.android.synthetic.main.activity_notice_list.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 笔记列表
 */
class NoteListActivity : BaseActivity() {
    override fun onViewCreated(savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = NoticeListAdapter()
        recyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.setOnItemClickListener { _, _, position ->
            adapter.getItem(position)?.apply {
                startActivity<AddRichNoteActivity>("note_id" to note_id)
            }
        }
        titleBar.apply {
            setLeftAction{
                startActivity<RecycleBinListActivity>()
            }
            setLeftIcon(-1)
            setRightAction {
                toast("搜索")
            }
        }
        fab.setOnClickListener {
//            startActivity<AddNoteActivity>()
            startActivity<AddRichNoteActivity>()
        }
        NetTaskService.startNetTask(this)//
//        val viewDragMode= ViewDragMode(fab)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun getLayoutId(): Int = R.layout.activity_notice_list


}
