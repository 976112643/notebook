package com.wq.common.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.wq.common.db.executeTransaction
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.realm
import com.wq.common.net.BaseBean
import com.wq.common.util.*
import com.wq.notebook.common.mode.UserBean
import io.realm.Sort


/**
 * Created by WQ on 2017/9/20.
 */
class NetTaskService : IntentService(NetTaskService::class.java.name + "" + NetTaskService.nameNo++) {

    override fun onHandleIntent(intent: Intent?) {
        try {
            checkLogin()
            tryUploadNotes()
            tryDownloadNotes()
        }catch (e:Exception){
            _Log(e)
        }

    }

    /**
     * 尝试下载新的数据
     */
    private fun tryDownloadNotes() {
        // tryUploadNote();
        var findAllSorted = realm.where(Note::class.java).findAllSorted("version", Sort.DESCENDING)
        var version=0
        if(findAllSorted.size>0){//查询当前最新版本,本地修改并不会
            version=   findAllSorted[0].version
        }
        var needBreak=false;
        for (i in 0..Int.MAX_VALUE ) {
            if(needBreak)break
            api.getNewNotes(i, version).isOK {
                if (info.empty()){
                    needBreak=true
                    return@isOK
                }
                executeTransaction {
                    //开启事务,存放请求到的数据
                    realm.copyFromRealm(info)
                }
            }
        }
        }

    private fun tryUploadNotes() {
        //查找所有需要上传的文件
        var result:List<Note> = realm.where(Note::class.java).equalTo("is_upload", 1).findAll()
//        val toJson = result.toJson()
        api.editNotes(result.toJson()).isOK {
           val newVersion= toString().toInt()
            executeTransaction {
                result.innerforEach {
                    is_upload=0
                    version=newVersion//更新版本信息
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


    private fun checkLogin(){
        val userInfo = UserBean()
        if(!userInfo.isVaild()){
            api.loginOrRegister(_DEVICE_NO).isOK {
                userInfo.uid=info.uid
            }
        }
    }

    companion object {
        private var nameNo = 0
        fun startNetTask(context: Context){
            context.startService(Intent(context,NetTaskService::class.java))
        }
    }
}
