package com.wq.notebook.home.adapter

import android.support.design.widget.Snackbar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wq.config.R
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.where
import com.wq.common.util.date
import com.wq.common.util.generateSmallContene
import io.realm.Sort

/**
 * 笔记列表适配器
 * Created by weiquan on 2017/6/22.
 */
class NoticeListAdapter constructor() : BaseQuickAdapter<Note, BaseViewHolder>(R.layout.item_notice_simple_info) {
    var keyword:String=""
    override fun convert(helper: BaseViewHolder, item: Note) {

        var smallContent= item.small_content()
        helper.setText(R.id.item_time, item.updatetime.date())
        helper.setText(R.id.item_content, smallContent)
        val color33 = mContext.resources.getColor(R.color.text33)
        val color99 = mContext.resources.getColor(R.color.text99)
        helper.setTextColor(R.id.item_content,if(item.status==-1)color99 else color33)
        helper.setImageResource(R.id.item_cover, R.mipmap.test_img)
    }

    init {
      loadPageData("")
    }

    fun loadPageData(keyword:String){
        var realmQuery = where<Note>()
        if(!keyword.isEmpty()){
            realmQuery=realmQuery.like("content","*$keyword*")
        }
        this.keyword=keyword
        realmQuery .notEqualTo("status",-1).findAllSorted("updatetime", Sort.DESCENDING).apply {
            setNewData(this)
            addChangeListener { t, changeSet ->
                notifyDataSetChanged()
            }
        }
    }

    fun deleteItemUndo(note:Note?){
        note?.modify {
            status=0
            is_upload=0
        }
    }

    fun deleteItem(note:Note?){
        note?.modify {
            status=-1
            is_upload=1
        }
    }
}