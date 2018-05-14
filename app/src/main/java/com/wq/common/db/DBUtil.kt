package com.wq.common.db

import com.wq.common.db.mode.NoteTag
import com.wq.common.util._CONTEXT
import io.realm.Realm
import io.realm.RealmObject
import org.jetbrains.anko.runOnUiThread

/**
 * 给所有对象扩展Realm属性
 */
val realm: Realm
    get() = Realm.getDefaultInstance()

/**
 * 给所有对象扩展数据库事务处理函数
 */
fun executeTransaction(transaction: (realm: Realm) -> Unit) {
    _CONTEXT.runOnUiThread{
        if (realm.isInTransaction) {
            transaction(realm)
        } else {
            realm.executeTransaction(transaction)
        }
    }
}
/**
 * 给所有对象扩展数据库事务处理函数
 */
fun TransactionThread(transaction: (realm: Realm) -> Unit) {
        if (realm.isInTransaction) {
            transaction(realm)
        } else {
            realm.executeTransaction(transaction)
        }
}
/**
 * 修改
 */
fun <T :RealmObject> T.modify(block:T.()->Unit){
    executeTransaction{
        block()
    }
}

fun RealmObject.addOrUpdate() {
    executeTransaction { realm -> realm.insertOrUpdate(this) }
}

fun String.toNoteTag() = NoteTag(this)

inline fun <reified T : RealmObject> where() = realm.where(T::class.java)

inline fun <reified T : RealmObject> findAll() = where<T>().findAll()

/**
 * 给数据库对象字段扩展id生成方法
 */
inline @Synchronized fun <reified T : RealmObject> T.generateId(): String = (System.currentTimeMillis()).toString()

