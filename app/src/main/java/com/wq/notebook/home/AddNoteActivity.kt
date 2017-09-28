package com.wq.notebook.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ScrollView
import com.wq.config.R
import com.wq.common.base.BaseActivity
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.realm
import com.wq.common.service.NetTaskService
import com.wq.common.util.ifrun
import kotlinx.android.synthetic.main.activity_add_note.*

/**
 * 添加/修改笔记
 */
class AddNoteActivity : BaseActivity() {

    var isTop = true
    var note: Note = Note()
    var hasChange = false
    override fun onViewCreated(savedInstanceState: Bundle?) {
        val note_id = intent.getStringExtra("note_id")
        realm.where(Note::class.java).
                equalTo("note_id", note_id).
                findFirst()?.
                apply {
                    note = this
                }
        titleBar.ifrun(note_id != null) {
            setTitle("编辑笔记")
            setRightVisible(View.VISIBLE)
            setRightAction {
                finish()
            }
        }
        titleBar.setOnClickListener {

            if (isTop) scrollView.fullScroll(ScrollView.FOCUS_DOWN); else scrollView.smoothScrollTo(0, 0)
            isTop = !isTop
        }
        editContent.run {
            setText(note.content)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    hasChange = true
                }

            })

        }
    }

    override fun onPause() {
        saveModify()
        super.onPause()
    }

    private fun saveModify() {
        if (editContent.text.trim().isEmpty() || !hasChange) return
        note.modify {
            is_upload = 1
            content = editContent.text.toString()
            updatetime = System.currentTimeMillis()
            realm.insertOrUpdate(note)
        }
        NetTaskService.startNetTask(this)//
    }

    override fun getLayoutId(): Int = R.layout.activity_add_note

}
