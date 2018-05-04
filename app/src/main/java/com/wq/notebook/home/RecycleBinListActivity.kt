package com.wq.notebook.home

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import com.wq.common.base.BaseActivity
import com.wq.common.db.executeTransaction
import com.wq.common.db.mode.Note
import com.wq.common.db.realm
import com.wq.common.service.NetTaskService
import com.wq.common.util._drawable
import com.wq.common.util.ternary
import com.wq.common.util.toString
import com.wq.config.R
import com.wq.notebook.home.adapter.RecycleBinListAdapter
import kotlinx.android.synthetic.main.activity_recyclebin_list.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 已删除笔记列表
 */
class RecycleBinListActivity : BaseActivity() {
    lateinit var adapter: RecycleBinListAdapter
    override fun onViewCreated(savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = RecycleBinListAdapter()
        recyclerView.adapter = adapter
        adapter.emptyView = layoutInflater.inflate(R.layout.lay_empty_view,null)
        var dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(_drawable(R.drawable.shape_list_divier))

//        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelper(adapter))
//        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.setOnItemClickListener { _, _, position ->
            if (adapter.isEditMode) {
                adapter.selectPosition = position

            } else {
                adapter.getItem(position)?.apply {
                    startActivity<AddRichNoteActivity>("note_id" to note_id)
                }
            }
        }
        adapter.setOnItemLongClickListener { _, view, position ->
            adapter.selectPosition = position
            adapter.isEditMode = !adapter.isEditMode;
            titleBar.setRightVisible(adapter.isEditMode.ternary(View.VISIBLE,View.GONE))
            true


        }
        titleBar.setRightAction {
            showOptMenu(it)
        }
    }

    private fun showOptMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        menuInflater.inflate(R.menu.menu_main, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.action_recover -> {
                    recoverNote()
                    toast(it.title)
                }
                R.id.action_delete -> {
                    deleteNotes()
                    toast(it.title)

                }
            }
            super.onOptionsItemSelected(it)
        }
        popupMenu.show()
    }

    private fun recoverNote() {
        var allCheckData = adapter.getAllCheckData<Note>()
        executeTransaction {
            allCheckData.forEachIndexed { index, note ->
                note.status=0
                note.is_upload=1
                realm.insertOrUpdate(note)
            }
        }
        NetTaskService.startNetTask(this)
    }
    private fun deleteNotes() {
        var allCheckData = adapter.getAllCheckData<Note>()
        var idsStr = allCheckData.toString { it.id }
        executeTransaction {
            allCheckData.forEachIndexed { index, note ->
               note.deleteFromRealm()
            }
        }
        var ids = idsStr.split(",").toTypedArray()
        NetTaskService.startDelTask(this, ids)
    }

    override fun getLayoutId(): Int = R.layout.activity_recyclebin_list

    override fun onBackPressed() {
        if(adapter.isEditMode){
            adapter.isEditMode=false
        }else {
            super.onBackPressed()
        }

    }

}
