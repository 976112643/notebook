package com.wq.notebook.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lizhuo.kotlinlearning.R
import com.wq.common.db.mode.Note
import com.wq.common.util.date
import com.wq.notebook.home.bean.NoteListItem

/**
 * 笔记列表适配器
 * Created by weiquan on 2017/6/22.
 */
class NoticeListAdapter :BaseQuickAdapter<Note,BaseViewHolder>(R.layout.item_notice_simple_info){
    override fun convert(helper: BaseViewHolder, item: Note) {
        helper.setText(R.id.item_time, item.updatetime.date())
        helper.setText(R.id.item_content,item.content)
        helper.setImageResource(R.id.item_cover,R.mipmap.test_img)
    }

}