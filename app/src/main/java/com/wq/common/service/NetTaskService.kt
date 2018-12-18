package com.wq.common.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.wq.common.db.TransactionThread
import com.wq.common.db.executeTransaction
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.realm
import com.wq.common.net.BaseBean
import com.wq.common.service.RequestStatusEvent.Companion.EVNET_SYN
import com.wq.common.service.RequestStatusEvent.Companion.STATUS_ERROR
import com.wq.common.service.RequestStatusEvent.Companion.STATUS_FINIAH
import com.wq.common.service.RequestStatusEvent.Companion.STATUS_ING
import com.wq.common.service.RequestStatusEvent.Companion.STATUS_START
import com.wq.common.util.*
import com.wq.notebook.common.mode.UserBean
import io.realm.Sort
import org.jetbrains.anko.toast


/**
 * 网络同步任务服务
 * Created by WQ on 2017/9/20.
 */
class NetTaskService : IntentService(NetTaskService::class.java.name + "" + NetTaskService.nameNo++) {

    override fun onHandleIntent(intent: Intent?) {

        try {
            if (intent != null && intent.hasExtra("action")) {
                doOtherTask(intent)
            } else {
                checkLogin()
                RequestStatusEvent.sendEvent(EVNET_SYN, STATUS_START)
                tryUploadNotes()
                sleep(1000)
                RequestStatusEvent.sendEvent(EVNET_SYN, STATUS_ING)
                tryDownloadNotes()
                sleep(1000)
                RequestStatusEvent.sendEvent(EVNET_SYN, STATUS_FINIAH)
            }
        } catch (e: Exception) {
            _Log(e, LEVEL._E)
            RequestStatusEvent.sendEvent(EVNET_SYN, STATUS_ERROR)

        }

    }

    private fun doOtherTask(intent: Intent) {
        try {
            when (intent.getStringExtra("action")) {
                ACTION_DELETE -> {
                    var ids = intent.getStringArrayExtra("ids")
                    api.delNote(ids).isOK {
                        shortToast(msg)
                    }
                }

            }
        } catch (e: Exception) {
            _Log(e, LEVEL._E)
        }
    }


    /**
     * 尝试下载新的数据
     */
    private fun tryDownloadNotes() {
        "==============================================tryDownloadNotes 开始=============================================="._Log()
        //查找不是出于待上传状态的,版本跟服务器不一致的数据
        var alldata = realm.where(Note::class.java).notEqualTo("is_upload", 1).findAllSorted("id", Sort.DESCENDING)
        var idsStr: String
        var versionsStr: String
        idsStr = alldata.toString { it.id }
        versionsStr = alldata.toString { it.version.toString() }
        var ids = idsStr.split(",").toTypedArray()
        var versions = versionsStr.split(",").toTypedArray()
        api.getDiffNotes(ids, versions).isOK {
            TransactionThread {
                //开启事务,存放请求到的数据
                if (info.isNotEmpty())
                    realm.insertOrUpdate(info)
            }
        }
        "==============================================tryDownloadNotes 完成=============================================="._Log()
    }

    /**
     * 尝试上传本地改动
     */
    private fun tryUploadNotes() {

        "==============================================tryUploadNotes 开始=============================================="._Log()
        //查找所有需要上传的文件
        var result: List<Note> = realm.where(Note::class.java).equalTo("is_upload", 1).findAll()
        result = realm.copyFromRealm(result)
        val data =  result.list2list { it }
        api.updateAll(data.toJson()).isOK {
            val split = toString().split(",")
            executeTransaction {
                result.forEachIndexed { index, note ->
                    note.is_upload = 0
                    if (split[index] != "0") {
                        note.id = split[index]//设置服务器端的id
                    }
                    note.version++
                }
                realm.insertOrUpdate(result)
            }
        }
        "==============================================tryUploadNotes 完成=============================================="._Log()
    }

    /**
     * 检查登陆状态
     */
    private fun checkLogin() {
        val userInfo = UserBean()
        if (!userInfo.isVaild()) {
            api.loginOrRegister(_DEVICE_NO, getPhoneBrand(), getPhoneModel(), getPhoneMan()).isOK {
                userInfo.uid = info.uid
            }
        }
    }

    companion object {
        val ACTION_DELETE: String = "action_delete"
        val ACTION_DEF: String = "action_default"
        private var nameNo = 0
        fun startNetTask(context: Context) {
            context.startService(Intent(context, NetTaskService::class.java))
        }

        fun startDelTask(context: Context, ids: Array<String>) {
            var intent = Intent(context, NetTaskService::class.java)
            intent.putExtra("ids", ids)
            intent.putExtra("action", ACTION_DELETE)
            context.startService(intent)
        }
    }
}
