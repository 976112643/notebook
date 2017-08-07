package com.wq.common.db.mode

import android.text.TextUtils
import com.wq.common.db.executeTransaction
import com.wq.common.util.empty
import com.wq.common.util.notEmpty
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by WQ on 2017/8/3.
 */

class NoteTag : RealmObject {
    var addtime = System.currentTimeMillis()
    var updatetime = System.currentTimeMillis()
    @PrimaryKey
    var _id = generateId()
    var name: String = ""

    constructor() {}

    constructor(name: String) {
        this.name = name.toString()
        _id = name.hashCode().toString()
    }


    @Synchronized private fun generateId(): String {
        val id = (Realm.getDefaultInstance().where(NoteTag::class.java).count() + 1).toString()
        return id
    }

    companion object {

        fun addOrUpdate(notetag: NoteTag) {
            executeTransaction { realm -> realm.insertOrUpdate(notetag) }
        }

        fun addOrUpdate(tagName: String?) {
            tagName?.notEmpty {
                addOrUpdate(NoteTag(it))
            }
        }
    }
}
