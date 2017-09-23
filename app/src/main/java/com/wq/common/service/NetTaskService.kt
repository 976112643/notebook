package com.wq.common.service

import android.app.IntentService
import android.content.Intent
import com.wq.common.db.executeTransaction
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.realm
import com.wq.common.net.BaseBean
import com.wq.common.util.*


/**
 * Created by WQ on 2017/9/20.
 */
class NetTaskService : IntentService(NetTaskService::class.java.name + "" + NetTaskService.nameNo++) {

    override fun onHandleIntent(intent: Intent?) {
        try {

           // tryUploadNote();
            api.getNotes(1).isOK {
                executeTransaction {//开启事务,存放请求到的数据
                    realm.copyFromRealm(info)
                }
                _Log(info)
            }
        }catch (e:Exception){
            _Log(e)
        }

    }

    fun addOrUpdate( note: Note){

    }

    private fun tryUploadNotes() {
        //查找所有需要上传的文件
        var result = realm.where(Note::class.java).equalTo("is_upload", 1).findAll()
//        val toJson = result.toJson()
        api.editNotes(result).isOK {
            executeTransaction {
                result.innerforEach {
                    is_upload=0;
                }
            }
        }

    }

    private fun tryUploadNote( note:Note) {
        api.editNote(note).isOK{//请求成功
            note.modify {
                //更新上传状态
                note.is_upload=0
            }
        }

    }

    companion object {
        private var nameNo = 0
    }
}
