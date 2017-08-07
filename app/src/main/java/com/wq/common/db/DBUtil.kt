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


/**
 * 给数据库对象字段扩展id生成方法
 */
@Synchronized fun RealmObject.generateId():String=(realm.where(this.javaClass).count() + 1).toString()



//fun <T> Any.useDB(call: SQLiteDatabase.() -> T): T = MyDatabaseOpenHelper.getInstance(_CONTEXT).use(call)

/**
 * Created by weiquan on 2017/8/1.
 */
//inline fun <reified T : Any> Any.findAll(page: Int = 0, selectMap: Map<String, Any> = mapOf<String, Any>()): List<T> = useDB {
//    select(T::class.java.simpleName).whereArgs(linkWhereArgs(selectMap)).limit(page * 10, 10).parseList(classParser<T>())
//}

fun linkWhereArgs(selectMap: Map<String, Any>): String {
    val sBuilder = StringBuilder()
    for ((key, value) in selectMap) {
        sBuilder.append(key).append(" = ").append("'$value'").append(" AND ")
    }
    if (sBuilder.isNotEmpty()) sBuilder.delete(sBuilder.length - 5, sBuilder.length)
    return sBuilder.toString()
}
