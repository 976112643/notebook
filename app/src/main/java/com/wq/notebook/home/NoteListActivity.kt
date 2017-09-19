package com.wq.notebook.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lizhuo.kotlinlearning.R
import com.wq.common.base.BaseActivity
import com.wq.notebook.home.adapter.NoticeListAdapter
import kotlinx.android.synthetic.main.activity_notice_list.*
import org.jetbrains.anko.startActivity

/**
 * 笔记列表
 */
class NoteListActivity : BaseActivity() {
    override fun onViewCreated(savedInstanceState: Bundle?) {

        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        window.decorView.postDelayed({
            val adapter = NoticeListAdapter()
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener{ _, _, position ->
                adapter.getItem(position)?.apply {
                    startActivity<AddNoteActivity>("id" to _id)
                }

            }
        },1000)

        fab.setOnClickListener {
            startActivity<AddNoteActivity>()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun getLayoutId(): Int =  R.layout.activity_notice_list


}
