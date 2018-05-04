package com.wq.notebook.home

import android.Manifest
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import com.wq.common.base.BaseActivity
import com.wq.common.service.NetTaskService
import com.wq.common.util.*
import com.wq.config.R
import com.wq.notebook.home.adapter.NoticeListAdapter
import com.wq.notebook.home.adapter.SimpleItemTouchHelper
import kotlinx.android.synthetic.main.activity_notice_list.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 笔记列表
 */
class NoteListActivity : BaseActivity() {
    val permissHelper = PermissionHelper()
    var adapter = NoticeListAdapter()
    override fun onViewCreated(savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.emptyView = layoutInflater.inflate(R.layout.lay_empty_view, null)
        var dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(_drawable(R.drawable.shape_list_divier))
        recyclerView.addItemDecoration(dividerItemDecoration)
        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        initListener()
        //拿下设备码的权限
        permissHelper.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), object : PermissionHelper.PermissionsResult {
            override fun onDenied() {
                toast("需要相关权限才能正常工作")
            }
            override fun onGrant() {
                NetTaskService.startNetTask(this@NoteListActivity)//
            }

        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        adapter.setOnItemClickListener { _, _, position ->
            adapter.getItem(position)?.apply {
                startActivity<AddRichNoteActivity>("note_id" to note_id)
            }
        }
        titleBar.apply {
            setLeftAction {
                startActivity<RecycleBinListActivity>()
            }
            setLeftIcon(-1)
            setRightAction {
                rightIn {
                    laySearch.startAnimation(this)
                    laySearch.visibility = View.VISIBLE
                    editSearch.openKeyboard()
                }
            }
        }
        imgClose.setOnClickListener {
            rightOut {
                listener {
                    laySearch.visibility = View.GONE
                }
                laySearch.startAnimation(this)
                editSearch.closeKeyboard()
            }
        }
        editSearch.wathch {
            adapter.loadPageData(editSearch.text.toString())
        }
        fab.setOnClickListener {
            startActivity<AddRichNoteActivity>()
        }
    }


    override fun getLayoutId(): Int = R.layout.activity_notice_list


    override fun onBackPressed() {
        if (laySearch.isShown) {
            laySearch.visibility = View.GONE
            return
        }
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
