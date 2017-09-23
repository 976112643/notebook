package com.wq.common.db.mode

import com.wq.common.db.executeTransaction
import com.wq.common.db.generateId
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by WQ on 2017/8/3.
 */

open class Note : RealmObject() {

    @PrimaryKey
    var id = generateId()
    var content: String =""
    var addtime = System.currentTimeMillis()
    var updatetime = System.currentTimeMillis()
    var tags:RealmList<NoteTag>?=null
    var category_id: String? = null
    var type = "TYPE_TXT"
    var status=0
    var isUpload=0;
    companion object {
        val TYPE_TXT = 0
        val TYPE_IMG = 1
        val TYPE_MEDIA = 2
    }
}
