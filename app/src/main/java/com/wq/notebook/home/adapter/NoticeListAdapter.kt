package com.wq.notebook.home.adapter

import android.support.design.widget.Snackbar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wq.config.R
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.where
import com.wq.common.util.date
import io.realm.Sort

/**
 * 笔记列表适配器
 * Created by weiquan on 2017/6/22.
 */
class NoticeListAdapter constructor() : BaseQuickAdapter<Note, BaseViewHolder>(R.layout.item_notice_simple_info) {
    override fun convert(helper: BaseViewHolder, item: Note) {

        var smallContent= item.smallContent
        helper.setText(R.id.item_time, item.updatetime.date())
        helper.setText(R.id.item_content, smallContent)
        val color33 = mContext.resources.getColor(R.color.text33)
        val color99 = mContext.resources.getColor(R.color.text99)
        helper.setTextColor(R.id.item_content,if(item.status==-1)color99 else color33)
        helper.setImageResource(R.id.item_cover, R.mipmap.test_img)
    }

    init {
//        .notEqualTo("status",-1)
        where<Note>().notEqualTo("status",-1).findAllSorted("updatetime", Sort.DESCENDING).apply {
            setNewData(this)
            addChangeListener { t, changeSet ->
                notifyDataSetChanged()
            }
        }

    }

    fun deleteItem(position:Int){
        mData[position].modify {
            status=-1
            is_upload=1
        }
    }
}