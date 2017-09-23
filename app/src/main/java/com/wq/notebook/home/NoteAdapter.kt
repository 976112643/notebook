package com.wq.notebook.home

import com.wq.config.R
import com.wq.common.base.QBaseAdapter
import com.wq.common.base.QBaseAdapter.BindViewHolder

/**
 * Created by weiquan on 2017/5/27.
 */
class NoteAdapter(val adapter: QBaseAdapter) {
    val layoutIds:((Int)->Int)={ R.layout.item_notice}
    //    val bindViews=((BindViewHolder,Int)->{ viewHolder, position->})
    val bindViews:((BindViewHolder ,Int)->Unit)={viewHolder, position->};
    init {
        adapter.viewIds=layoutIds;
        adapter.bindViews=bindViews;
    }

}