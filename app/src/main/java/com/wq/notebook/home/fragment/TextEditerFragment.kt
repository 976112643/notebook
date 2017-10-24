package com.wq.notebook.home.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.wq.common.base.BaseFragment
import com.wq.common.util._CONTEXT
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
import kotlinx.android.synthetic.main.fragment_text_editer.*
import java.util.*

/**
 * Created by WQ on 2017/10/23.
 */

class TextEditerFragment : BaseFragment() {
    val iconfont by lazy {
        Typeface.createFromAsset(_CONTEXT.assets, "Simditor.ttf")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = TextViewToolbar()
        val options = Options {
            placeholder = "Input something..."
            addAllowedAttributes("img", Arrays.asList("data-type", "data-id", "class", "src", "alt", "width", "height", "data-non-image"))
            addAllowedAttributes("iframe", Arrays.asList("data-type", "data-id", "class", "src", "width", "height"))
            addAllowedAttributes("a", Arrays.asList("data-type", "data-id", "class", "href", "target", "title"))

        }

        val icarus = Icarus(toolbar, options, editor)
        prepareToolbar(toolbar, icarus)
        icarus.loadCSS("file:///android_asset/editor.css")
        icarus.loadJs("file:///android_asset/test.js")
        icarus.render()
    }

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
        findToolBtn(R.id.button_link).apply {
            val button = TextViewButton(this, Button.NAME_LINK, locIcarus, LinkPopoverImpl(this, locIcarus))
            toolbar.addButton(button)
        }
        findToolBtn(R.id.button_image).apply {
            val button = TextViewButton(this, Button.NAME_IMAGE, locIcarus, ImagePopoverImpl(this, locIcarus))
            toolbar.addButton(button)
        }
        findToolBtn(R.id.button_html5).apply {
            val button = TextViewButton(this, Button.NAME_HTML, locIcarus, HtmlPopoverImpl(this, locIcarus))
            toolbar.addButton(button)
        }
        findToolBtn(R.id.button_font_scale).apply {
            val button = FontScaleButton(this, Button.NAME_HTML, locIcarus, FontScalePopoverImpl(this, locIcarus))
            toolbar.addButton(button)
        }
        return toolbar
    }

    fun findToolBtn(id: Int): TextView {
        val toolTxt = layToolBar.findViewById(id) as TextView
        toolTxt.typeface = iconfont
        return toolTxt
    }

    override fun getLayoutId(): Int = R.layout.fragment_text_editer
}
