package com.wq.notebook.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

/**
 * 添加/修改笔记
 */
class AddNoteActivity : BaseActivity() {

    var note: Note = Note()
    var hasChange=false
    override fun onViewCreated(savedInstanceState: Bundle?) {
        val id = intent.getStringExtra("id")
        realm.where(Note::class.java).
                equalTo("_id", id).
                findFirst()?.
                apply {
                    note = this
                    titleBar.setTitle("编辑笔记")
                }
        editContent.run {
            setText(note.content)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    hasChange=true
                }

            })

        }
    }

    override fun onPause() {
        saveModify()
        super.onPause()
    }

    private fun saveModify() {
        if (editContent.text.trim().isEmpty() ||  !hasChange) return
        note.modify {
            content = editContent.text.toString()
            updatetime = System.currentTimeMillis()
            realm.insertOrUpdate(note)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_add_note

}
