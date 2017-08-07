package com.wq.common.db

import io.realm.Realm
import io.realm.RealmObject

/**
 * 给所有对象扩展Realm属性
 */
val realm: Realm
    get() = Realm.getDefaultInstance()

/**
 * 给所有对象扩展数据库事务处理函数
 */
fun executeTransaction(transaction: (realm: Realm) -> Unit) {
    if (realm.isInTransaction) {
        transaction.invoke(realm)
    } else {
        realm.executeTransaction(transaction)
    }
}

inline fun <reified T : RealmObject> where() = realm.where(T::class.java)

inline fun <reified T : RealmObject> findAll() = where<T>().findAll()

/**
 * 给数据库对象字段扩展id生成方法
 */
@Synchronized fun RealmObject.generateId(): String = (realm.where(this.javaClass).count() + 1).toString()

