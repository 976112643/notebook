package com.wq.notebook.home

import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import com.wq.common.base.BaseActivity
import com.wq.common.db.mode.Note
import com.wq.common.db.modify
import com.wq.common.db.realm
import com.wq.common.service.NetTaskService
import com.wq.common.util.ConversationHeightFix
import com.wq.common.util._CONTEXT
import com.wq.common.util.empty
import com.wq.config.R
import com.wq.editer.Icarus
import com.wq.editer.TextViewToolbar
import com.wq.editer.Toolbar
import com.wq.editer.button.Button
import com.wq.editer.button.FontScaleButton
import com.wq.editer.button.TextViewButton
import com.wq.editer.entity.Options
import com.wq.editer.popover.FontScalePopoverImpl
import com.wq.editer.popover.HtmlPopoverImpl
import com.wq.editer.popover.ImagePopoverImpl
import com.wq.editer.popover.LinkPopoverImpl
import kotlinx.android.synthetic.main.activity_add_rich_note.*
import java.util.*


/**
 * Created by WQ on 2018/1/11.
 */

class AddRichNoteActivity : BaseActivity() {
    private var isTop = true
    private lateinit var note: Note
    private var hasChange = false
    lateinit var  icarus:Icarus

    val iconfont by lazy {
        Typeface.createFromAsset(_CONTEXT.assets, "Simditor.ttf")
    }
    override fun onViewCreated(savedInstanceState: Bundle?) {
        val toolbar = TextViewToolbar()
        val options = Options {
            placeholder = "Input something..."
            addAllowedAttributes("img", arrayListOf("data-type", "data-id", "class", "src", "alt", "width", "height", "data-non-image"))
            addAllowedAttributes("iframe", arrayListOf("data-type", "data-id", "class", "src", "width", "height"))
            addAllowedAttributes("a", arrayListOf("data-type", "data-id", "class", "href", "target", "title"))
        }
        icarus = Icarus(toolbar, options, editor)
        prepareToolbar(toolbar, icarus)
        icarus.loadCSS("file:///android_asset/editor.css")
       // icarus.loadJs("file:///android_asset/test.js")
        icarus.render()

        initData()
        ConversationHeightFix.setAutoSizeContent(this,layContent)
    }

    private fun initData() {
        val note_id = intent.getStringExtra("note_id")
        note = realm.where(Note::class.java).equalTo("note_id", note_id).findFirst()?: Note()
        titleBar.apply {
            if (note_id != null)
                setTitle("编辑笔记")
            setRightAction {
                finish()
            }
            setOnClickListener {
                if (isTop) editor.flingScroll(0,1000)
                else editor.flingScroll(0,0)
                isTop = !isTop
            }
        }
    }

    /**
     * 初始化工具条
     */
    private fun prepareToolbar(toolbar: TextViewToolbar, locIcarus: Icarus): Toolbar {
        val generalButtons = HashMap<String, Int>()
        generalButtons.put(Button.NAME_BOLD, R.id.button_bold)
        generalButtons.put(Button.NAME_OL, R.id.button_list_ol)
        generalButtons.put(Button.NAME_BLOCKQUOTE, R.id.button_blockquote)
        generalButtons.put(Button.NAME_HR, R.id.button_hr)
        generalButtons.put(Button.NAME_UL, R.id.button_list_ul)
        generalButtons.put(Button.NAME_ALIGN_LEFT, R.id.button_align_left)
        generalButtons.put(Button.NAME_ALIGN_CENTER, R.id.button_align_center)
        generalButtons.put(Button.NAME_ALIGN_RIGHT, R.id.button_align_right)
        generalButtons.put(Button.NAME_ITALIC, R.id.button_italic)
        generalButtons.put(Button.NAME_INDENT, R.id.button_indent)
        generalButtons.put(Button.NAME_OUTDENT, R.id.button_outdent)
        generalButtons.put(Button.NAME_CODE, R.id.button_math)
        generalButtons.put(Button.NAME_UNDERLINE, R.id.button_underline)
        generalButtons.put(Button.NAME_STRIKETHROUGH, R.id.button_strike_through)
        for ((nameTag, btnId) in generalButtons) {
            toolbar.addButton(TextViewButton(findToolBtn(btnId), nameTag, locIcarus))
        }
        findToolBtn(R.id.button_link) {
            val button = TextViewButton(this, Button.NAME_LINK, locIcarus, LinkPopoverImpl(this, locIcarus))
            toolbar.addButton(button)
        }
        findToolBtn(R.id.button_image){
            val button = TextViewButton(this, Button.NAME_IMAGE, locIcarus, ImagePopoverImpl(this, locIcarus))
            toolbar.addButton(button)
        }
        findToolBtn(R.id.button_html5){
            val button = TextViewButton(this, Button.NAME_HTML, locIcarus, HtmlPopoverImpl(this, locIcarus))
            toolbar.addButton(button)
        }
        findToolBtn(R.id.button_font_scale){
            val button = FontScaleButton(this, Button.NAME_HTML, locIcarus, FontScalePopoverImpl(this, locIcarus))
            toolbar.addButton(button)
        }
        return toolbar
    }

    private fun findToolBtn(id: Int,call:TextView.()->Unit={}): TextView {
        val toolTxt = layToolBar.findViewById(id) as TextView
        toolTxt.typeface = iconfont
        call(toolTxt)
        return toolTxt
    }

    override fun onPause() {
        saveModify()
        super.onPause()
    }
    private fun saveModify() {
        icarus.getContent {

            if (it.isEmpty() || !hasChange){
                return@getContent
            }

            note.modify {
                is_upload = 1
                content = it
                updatetime = System.currentTimeMillis()
                realm.insertOrUpdate(note)
            }
            NetTaskService.startNetTask(this)//启动任务进行同步操作
        }

    }
    override fun getLayoutId(): Int= R.layout.activity_add_rich_note
}
