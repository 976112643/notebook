package com.wq.notebook.common.mode

import kotlin.properties.Delegates

/**
 * Created by weiquan on 2017/7/14.
 */

data class TestBean(val map: MutableMap<String, Any?>) {
    var _id: Long by map
    var name: String by map
    var address: String by map
    var test :String by Delegates.notNull<String>()

    constructor() : this(HashMap()) {}
    constructor(id: Long, name: String, address: String) : this(HashMap()) {
        this._id = id
        this.name = name
        this.address = address


    }
}
