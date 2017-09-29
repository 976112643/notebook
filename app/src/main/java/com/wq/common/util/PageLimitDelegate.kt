package com.wq.common.util

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import java.util.concurrent.atomic.AtomicBoolean


/**
 * 分页代理类
 * Created by WQ on 2017/5/26.
 */

class PageLimitDelegate<T>( var provider: PageLimitDelegate.DataProvider//数据提供者,主要实现loadData 方法即可
) {
    var page = 1//当前页
    lateinit var quickAdapter: BaseQuickAdapter<T, *>//适配器
    lateinit var refreshLayout: SwipeRefreshLayout//下拉刷新控件
    var isLoadMore = AtomicBoolean(false)//是否处于加载状态
    var maxPageSize = 10//每页最大值

    /**
     * 附加列表信息,设置加载刷新信息
     * @param refreshLayout
     * *
     * @param recyclerView
     * *
     * @param quickAdapter
     */
    fun attach(refreshLayout: SwipeRefreshLayout, recyclerView: RecyclerView, quickAdapter: BaseQuickAdapter<T, *>) {
        this.quickAdapter = quickAdapter
        this.refreshLayout = refreshLayout
        refreshLayout.isEnabled = true
        refreshLayout.setOnRefreshListener { refreshPage() }
        quickAdapter.setEnableLoadMore(true)
        quickAdapter.disableLoadMoreIfNotFullPage(recyclerView)
        quickAdapter.setOnLoadMoreListener({
            isLoadMore.set(true)
            page++
            provider.loadData(page)
        }, recyclerView)
        provider.loadData(page)
    }

    /**
     * 刷新
     */
    fun refreshPage() {
        page = 1
        isLoadMore.set(false)
        provider.loadData(page)
    }

    /**
     * 设置数据,
     * @param data
     */
    fun setData(list: List<T>?) {
        var data =list?: arrayListOf();
        maxPageSize = Math.max(maxPageSize, data.size)
        if (isLoadMore.get() || page != 1) {
            if (data.isEmpty()) {
                page--
                quickAdapter.setEnableLoadMore(false)
                quickAdapter.loadMoreEnd()
            } else {
                quickAdapter.addData(data)
            }
            loadComplete()
        } else {
            loadComplete()
            quickAdapter.setNewData(data)
            quickAdapter.setEnableLoadMore(data.size >= maxPageSize)
        }
    }

    /**
     * 加载/刷新完成
     */
    fun loadComplete() {
        isLoadMore.set(false)
        refreshLayout.isRefreshing = false
        quickAdapter.loadMoreComplete()

    }

    /**
     * 数据提供者接口,用于进行加载动作
     */
    interface DataProvider {
        fun loadData(page: Int)
    }


}
