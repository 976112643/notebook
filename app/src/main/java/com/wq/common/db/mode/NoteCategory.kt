package com.wq.common.db.mode

import com.wq.common.db.generateId
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by WQ on 2017/8/3.
 */

open class NoteCategory : RealmObject() {
    var addtime: Long = 0
    var updatetime: Long = 0
    @PrimaryKey
    var _id = generateId()
    var name: String? = null


}
