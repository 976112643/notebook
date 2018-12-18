package com.wq.notebook.home

import com.wq.common.service.RequestStatusEvent.Companion.STATUS_ERROR
import com.wq.common.service.RequestStatusEvent.Companion.STATUS_FINIAH
import com.wq.common.service.RequestStatusEvent.Companion.STATUS_ING
import com.wq.common.service.RequestStatusEvent.Companion.STATUS_START
import com.wq.common.util.empty
import com.wq.common.widget.MultipleStatusView
import com.wq.notebook.home.adapter.NoticeListAdapter

/**
 * 页面状态
 * Created by weiquan on 2018/5/21.
 */

/**
 * 列表页面状态
 */
fun notelist(mltView: MultipleStatusView, adapter: NoticeListAdapter, callback: (Boolean) -> Unit, targetEvent: String): ((String, Int) -> Unit) = { event, status ->
    if (targetEvent == event) {
        when (status) {
            STATUS_START, STATUS_ING -> {
                if (adapter.itemCount == 0)
                    mltView.showLoading()
                callback.invoke(true)
            }
            STATUS_FINIAH -> {
                mltView.showContent()
                callback.invoke(false)
            }
            STATUS_ERROR ->{
                if (adapter.itemCount == 0)
                    mltView.showError()
            }
            else ->
                callback.invoke(false)
        }

    }
}