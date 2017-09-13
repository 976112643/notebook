package com.wq.common.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.wq.common.util._CONTEXT
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {
    //上下文，数据库名，数据库工厂，版本号
    companion object {
        private val instance: MyDatabaseOpenHelper by lazy {
            MyDatabaseOpenHelper(_CONTEXT)
        }

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            return instance
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable("Customer", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "name" to TEXT,
                "photo" to BLOB)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//数据库升级示例代码
        if (newVersion == 2) {
            if (oldVersion < newVersion) {
                db.execSQL("  ALTER TABLE Person RENAME TO temp_person")
                db?.createTable("Person", true, "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                        "name" to TEXT,
                        "age" to INTEGER,
                        "address" to TEXT,
                        "sex" to INTEGER
                )
                db.execSQL("INSERT INTO Person SELECT id, name, age,address,'' FROM temp_person")
                db.dropTable("temp_person")
            }
        }
    }
}