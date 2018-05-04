package com.wq.common.util.selectable

/**
 * 可选择接口
 * Created by WQ on 2017/4/27.
 */

interface Selectable {

    /**
     * 获取当前选中的位置
     * @return
     */
    /**
     * 设置指定索引选中状态 ,
     * @param position
     */
    var selectPosition: Int

    /**
     * 获取选中的数量
     * @return
     */
    val selectCount: Int

    /**
     * 获取选中的条目
     * @return
     */
    val selectItem: Any?

    /**
     * 指定索引是否选中
     * @param position
     * @return
     */
    fun isSelected(position: Int): Boolean

    /**
     * 指定条目是否选中
     * @param item
     * @param <T>
     * @return
    </T> */
    fun <T : SelectableEntity> isSelected(item: T): Boolean

    /**
     * 选中/取消所有
     * @param isSelect
     */
    fun selectAll(isSelect: Boolean)

    /**
     * 选择模式
     * @param mode
     */
    fun selectMode(mode: Int)

    /**
     * 获取所有选中数据
     * @param <T>
     * @return
    </T> */
    fun <T  > getAllCheckData(): List<T >

    /**
     * 参与选择的数据源接口,需返回唯一识别码供区分
     */
    interface SelectableEntity {
        val uniqueCode: Int
    }

    companion object {
        /**
         * 选择模式 ,单选 or 多选
         */
        val RADIO = 0x2100
        val MULTISELECT = 0x2101
    }
}