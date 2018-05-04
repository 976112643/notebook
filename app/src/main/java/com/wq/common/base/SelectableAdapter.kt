package com.wq.common.base

import android.support.annotation.LayoutRes

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wq.common.util.selectable.Selectable
import com.wq.common.util.selectable.SelectableHelper


/**
 * 提供选择操作的适配器
 * Created by WQ on 2017/5/27.
 */

abstract class SelectableAdapter<T : Selectable.SelectableEntity, K : BaseViewHolder> : BaseQuickAdapter<T, K>, Selectable {
    var selectableHelper = SelectableHelper<T>()

    //多选事务逻辑
    override var selectPosition: Int
        get() = selectableHelper.selectPosition
        set(position) {
            if (position == -1) {
                selectAll(false)
                return
            }
            selectableHelper.selectPosition = position
            notifyDataSetChanged()
        }

    override val selectCount: Int
        get() = selectableHelper.selectCount

    override val selectItem: T?
        get() = selectableHelper.selectItem

    constructor(@LayoutRes layoutResId: Int, data: List<T>?) : super(layoutResId, data) {

    }

    constructor(data: List<T>?) : super(data) {}

    constructor(@LayoutRes layoutResId: Int) : super(layoutResId) {}

    override fun getItemCount(): Int {
        selectableHelper.setData(data)
        return super.getItemCount()
    }

    override fun isSelected(position: Int): Boolean {
        return selectableHelper.isSelected(position)
    }

    override fun <T : Selectable.SelectableEntity> isSelected(item: T): Boolean {
        return selectableHelper.isSelected<Selectable.SelectableEntity>(item)
    }

    override fun selectAll(isSelect: Boolean) {
        selectableHelper.selectAll(isSelect)
        notifyDataSetChanged()
    }

    override fun selectMode(mode: Int) {
        selectableHelper.selectMode(mode)
    }

    override fun <T > getAllCheckData(): List<T> {
        return selectableHelper.getAllCheckData()
    }

}
