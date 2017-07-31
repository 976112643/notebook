package com.wq.common.base

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Fragment 基类
 * Created by weiquan on 2017/5/26.
 */

abstract class BaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(getLayoutId(),container)
    }
    abstract fun  getLayoutId():Int
}
