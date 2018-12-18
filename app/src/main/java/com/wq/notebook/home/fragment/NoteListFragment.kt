package com.wq.notebook.home.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import com.wq.common.base.BaseFragment
import com.wq.common.service.NetTaskService
import com.wq.common.util.*
import com.wq.config.R
import com.wq.notebook.home.AddRichNoteActivity
import com.wq.notebook.home.adapter.NoticeListAdapter
import com.wq.notebook.home.adapter.SimpleItemTouchHelper
import com.wq.notebook.home.notelist
import kotlinx.android.synthetic.main.fragment_notice_list.*
import org.jetbrains.anko.startActivity

/**
 * 笔记列表
 * Created by weiquan on 2018/5/15.
 */

class NoteListFragment : BaseFragment() {
    var adapter = NoticeListAdapter()
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.emptyView = LayoutInflater.from(activity).inflate(R.layout.lay_empty_view, recyclerView,false)
        var dividerItemDecoration = DividerItemDecoration(that, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(_drawable(R.drawable.shape_list_divier))
        recyclerView.addItemDecoration(dividerItemDecoration)
        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        initListener()
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
       " $_DEVICE_NO, ${getPhoneBrand()}, ${getPhoneModel()}, ${getPhoneMan()}"._Log()
        adapter.setOnItemClickListener { _, _, position ->
            adapter.getItem(position)?.apply {
                startActivity<AddRichNoteActivity>("note_id" to note_id)
            }
        }
        fab.setOnClickListener {
            startActivity<AddRichNoteActivity>()
        }
        fab.setOnLongClickListener {
            NetTaskService.startNetTask(that)
            true
        }
//        multiStatusView.showLoading()
        eventCallback =  notelist(multiStatusView,adapter,this::changeFabStatus,"syn_data")
    }

    private fun changeFabStatus(isSyning:Boolean){
        if(isSyning){
            roate(fab){
                fab.setImageDrawable(_drawable(R.mipmap.ic_cloud_syn))
            }
        }else{
            fab.clearAnimation()
            fab.setImageDrawable(_drawable(R.drawable.ic_add_black_24dp))
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_notice_list
}
