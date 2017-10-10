package com.wq.notebook.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.wq.config.R
import com.wq.common.base.BaseActivity
import com.wq.common.service.NetTaskService
import com.wq.notebook.home.adapter.NoticeListAdapter
import com.wq.notebook.home.adapter.RecycleBinListAdapter
import com.wq.notebook.home.adapter.SimpleItemTouchHelper
import kotlinx.android.synthetic.main.activity_recyclebin_list.*
import org.jetbrains.anko.startActivity

/**
 * 已删除笔记列表
 */
class RecycleBinListActivity : BaseActivity() {
    override fun onViewCreated(savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = RecycleBinListAdapter()
        recyclerView.adapter = adapter
//        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelper(adapter))
//        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.setOnItemClickListener { _, _, position ->
            adapter.getItem(position)?.apply {
                startActivity<AddNoteActivity>("note_id" to note_id)
            }
        }
        titleBar.apply {
            setLeftAction{

            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun getLayoutId(): Int = R.layout.activity_recyclebin_list


}
