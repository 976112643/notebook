package com.wq.notebook.home

import android.os.Bundle
import com.lizhuo.kotlinlearning.R
import com.wq.common.base.BaseActivity
import com.wq.common.db.addOrUpdate
import com.wq.common.db.executeTransaction
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.realm
import com.wq.common.net.APIManager
import com.wq.common.util.get
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNoteActivity : BaseActivity() {

    var note:Note=Note()
    override fun onViewCreated(savedInstanceState: Bundle?) {
        val id=intent.getStringExtra("id")
        realm.where(Note::class.java).
                equalTo("_id", id).
                findFirst()?.
                apply { note=this }
        editContent.setText(note.content)
        APIManager.request.getCall()
    }

    override fun onPause() {
        note.modify {
            content=editContent.text.toString()
            updatetime=System.currentTimeMillis();
            realm.insertOrUpdate(note)
        }
        super.onPause()
    }

    override fun getLayoutId(): Int =R.layout.activity_add_note

}
