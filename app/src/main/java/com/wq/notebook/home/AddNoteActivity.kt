package com.wq.notebook.home

import android.os.Bundle
import android.widget.ScrollView
import com.wq.common.base.BaseActivity
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.realm
import com.wq.common.service.NetTaskService
import com.wq.common.util.empty
import com.wq.common.util.wathch
import com.wq.config.R
import kotlinx.android.synthetic.main.activity_add_note.*

/**
 * 添加/修改笔记
 */
class AddNoteActivity : BaseActivity() {
    private var isTop = true
    private lateinit var note: Note
    private var hasChange = false
    override fun onViewCreated(savedInstanceState: Bundle?) {
        val note_id = intent.getStringExtra("note_id")
        val firstNote = realm.where(Note::class.java).equalTo("note_id", note_id).findFirst()
        note = firstNote ?: Note()
        titleBar.apply {
            note_id?.apply {
                setTitle("编辑笔记")
            }
            setRightAction {
                finish()
            }
            setOnClickListener {
                if (isTop) scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                else scrollView.smoothScrollTo(0, 0)
                isTop = !isTop
            }
        }
        editContent.apply {
            setText(note.content)
            wathch {
                hasChange = true
            }
        }
    }

    override fun onPause() {
        saveModify()
        super.onPause()
    }

    private fun saveModify() {
        if (editContent.empty() || !hasChange)
            return
        note.modify {
            is_upload = 1
            content = editContent.text.toString()
            updatetime = System.currentTimeMillis()
            realm.insertOrUpdate(note)
        }
        NetTaskService.startNetTask(this)//启动任务进行同步操作
    }

    //override fun isFixInputBug(): Boolean =true
    override fun getLayoutId(): Int = R.layout.activity_add_note

}
