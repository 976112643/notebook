package com.wq.notebook.home.adapter

import android.support.design.widget.Snackbar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wq.common.base.SelectableAdapter
import com.wq.config.R
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.where
import com.wq.common.util.date
import com.wq.common.util.selectable.Selectable.Companion.MULTISELECT
import io.realm.Sort

/**
 * 回收站列表适配器
 * Created by weiquan on 2017/6/22.
 */
class RecycleBinListAdapter constructor() : SelectableAdapter<Note, BaseViewHolder>(R.layout.item_notice_simple_info) {
    var isEditMode = false
    set(value)  {//刷新适配器
        field =value
        notifyDataSetChanged()
    }
    override fun convert(helper: BaseViewHolder, item: Note) {

        val smallContent= item.small_content()
        helper.setText(R.id.item_time, item.updatetime.date())
        helper.setText(R.id.item_content, smallContent)
        val color33 = mContext.resources.getColor(R.color.text33)
        val color99 = mContext.resources.getColor(R.color.text99)
        helper.setTextColor(R.id.item_content,if(item.status==-1)color99 else color33)
        helper.setImageResource(R.id.item_cover, R.mipmap.test_img)
        helper.setChecked(R.id.itemCbox,isSelected(helper.layoutPosition))
        helper.setVisible(R.id.itemCbox,isEditMode)
        helper.addOnClickListener(R.id.itemCbox)
    }

    init {
//        .notEqualTo("status",-1)
        selectMode(MULTISELECT)
        where<Note>().equalTo("status",-1).findAllSorted("updatetime", Sort.DESCENDING).apply {
            setNewData(this)
            addChangeListener { t, changeSet ->
                notifyDataSetChanged()
            }
        }
    }



    fun deleteItem(position:Int){
        mData[position].modify {
            deleteFromRealm()
        }
    }
}