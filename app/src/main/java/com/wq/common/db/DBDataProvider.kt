package com.wq.common.db

import com.wq.common.util.PageLimitDelegate
import io.realm.RealmObject

/**
 * Created by weiquan on 2017/8/7.
 */

class DBDataProvider<T :RealmObject> : PageLimitDelegate.DataProvider {
    override fun loadData(page: Int) {
//         realm.findAll()
    }
}
