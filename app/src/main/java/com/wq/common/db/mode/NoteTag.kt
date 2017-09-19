package com.wq.common.db.mode

import com.wq.common.db.generateId
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by WQ on 2017/8/3.
 */

open class NoteTag : RealmObject {
    @PrimaryKey
    var _id  = generateId()
    var addtime = System.currentTimeMillis()
    var updatetime = System.currentTimeMillis()
    var name: String = ""
    constructor() {}

    constructor(name: String) {
        this.name = name
        _id = name.hashCode().toString()
    }
}
