package com.wq.common.util.selectable

import android.util.SparseBooleanArray
import com.wq.common.util.selectable.Selectable.Companion.MULTISELECT
import com.wq.common.util.selectable.Selectable.Companion.RADIO

/**
 * 选择帮助类,选择接口的实现
 * Created by WQ on 2017/4/27.
 */

class SelectableHelper<T : Selectable.SelectableEntity> : Selectable {
    protected var selectMode = RADIO
    protected var selectList: SparseBooleanArray? = null
    protected var selectIndex = -1
    override var selectCount = 0
        protected set(value: Int) {
            field = value
        }
    protected var mData:List<T> = arrayListOf()

    override var selectPosition: Int
        get() = selectIndex
        set(position) = if (selectMode == RADIO) {
            selectIndex = position
            selectCount = 1
        } else {
            val isSelected = !isSelected(position)
            selectCount = if (isSelected) selectCount + 1 else selectCount - 1
            selectList!!.put(itemId(position), isSelected)
        }

    override val selectItem: T?
        get() = if (isVaildIndex(selectIndex)) {
            mData[selectIndex]
        } else null

    private val count: Int
        get() = if (isVaildIndex(0)) mData.size else 0

    constructor(mData: List<T>) {
        setData(mData)
        selectMode(RADIO)
    }

    constructor() {}
    constructor(mData: List<T>, selectMode: Int) {
        setData(mData)
        selectMode(selectMode)
    }

    fun setData(mData: List<T>?) {
        this.mData = mData ?: arrayListOf()
    }


    override fun isSelected(position: Int): Boolean {
        return if (selectMode == RADIO)
            selectIndex == position
        else
            selectList!!
                    .get(itemId(position))
    }

    override fun <T : Selectable.SelectableEntity> isSelected(item: T): Boolean {
        return selectList!!.get(item.uniqueCode)
    }

    fun isSelected(item: Any): Boolean {
        return selectList!!
                .get(item.hashCode())
    }

    override fun selectAll(isSelect: Boolean) {
        // TODO Auto-generated method stub
        if (selectMode == MULTISELECT) {
            for (i in 0 until mData.size)
                selectList!!.put(itemId(i), isSelect)
            selectCount = if (isSelect) count else 0
        } else if (!isSelect) {
            selectIndex = -1
            selectCount = 0
        }
    }

    override fun <T> getAllCheckData(): List<T> {
        val tmpData = ArrayList<T>()
        if (mData != null) {
            for (i in 0 until mData.size) {
                if (isSelected(i)) {
                    tmpData.add(mData[i] as T)
                }
            }
        }
        return tmpData
    }

    /**
     * 选择模式
     */

    override fun selectMode(mode: Int) {
        if (mode == RADIO || mode == MULTISELECT) {
            this.selectMode = mode
            selectCount = 0
            if (selectMode == MULTISELECT)
                selectList = SparseBooleanArray(mData.size)
        }

    }

    /**
     * 获得数据源hashCode
     *
     * @param position
     * @return
     */
    private fun itemId(position: Int): Int {
        return if (isVaildIndex(position) && mData[position] != null) {
            mData[position].uniqueCode
        } else 0
    }

    private fun isVaildIndex(index: Int): Boolean {
        return !(mData == null || index < 0 || index >= mData.size)
    }
}