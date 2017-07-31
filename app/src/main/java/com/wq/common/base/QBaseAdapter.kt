package com.wq.common.base

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by weiquan on 2017/5/26.
 */
class  QBaseAdapter(var viewIds: ((Int) -> Int) = { 0 }, var bindViews: ((BindViewHolder, Int) -> Unit)? = null) : RecyclerView.Adapter<QBaseAdapter.BindViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val itemView=View.inflate(parent.context, viewIds.invoke(viewType),null);
        val viewHolder=BindViewHolder(itemView);
        return viewHolder;
    }

    override fun getItemCount(): Int {
        return 10;
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        bindViews?.invoke(holder,position);
    }

    class BindViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}