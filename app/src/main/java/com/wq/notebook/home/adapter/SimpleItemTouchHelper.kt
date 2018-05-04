package com.wq.notebook.home.adapter

import android.graphics.Color
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Adapter

/**
 * 处理侧滑删除相关逻辑
 * Created by weiquan on 2017/10/6.
 */

class SimpleItemTouchHelper(var mAdapter: NoticeListAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = 0//ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.LEFT
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var position = viewHolder.layoutPosition
        val note = mAdapter.getItem(position)
        mAdapter.deleteItem(note)
        Snackbar.make(viewHolder.itemView, "删除成功!", Snackbar.LENGTH_SHORT).setActionTextColor(Color.WHITE)
                .setAction("撤销", {
                    mAdapter.deleteItemUndo(note)
                }).show()
    }
}
