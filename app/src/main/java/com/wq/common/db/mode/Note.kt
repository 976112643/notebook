package com.wq.common.db.mode

import com.wq.common.db.executeTransaction
import com.wq.common.db.generateId
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by WQ on 2017/8/3.
 */

class Note : RealmObject() {
    var content: String? = null
    var addtime = System.currentTimeMillis()
    var updatetime = System.currentTimeMillis()
    @PrimaryKey
    var _id = generateId()
    var tag: String? = null
    var tagIds: String? = null
    var category_id: String? = null
    var type = TYPE_TXT


    companion object {
        val TYPE_TXT = 0
        val TYPE_IMG = 1
        val TYPE_MEDIA = 2

        fun addOrUpdate(note: Note) {
            executeTransaction { realm ->
                realm.insertOrUpdate(note)
                NoteTag.addOrUpdate(note.tag)
            }
        }
    }
}
