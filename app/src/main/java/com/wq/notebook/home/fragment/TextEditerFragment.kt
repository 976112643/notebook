package com.wq.notebook.home.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.webkit.WebView
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
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = TextViewToolbar()
        val options = Options {
            placeholder = "Input something..."
            addAllowedAttributes {
                this["img"] = Arrays.asList("data-type", "data-id", "class", "src", "alt", "width", "height", "data-non-image");
                 "iframe" to Arrays.asList("data-type", "data-id", "class", "src", "width", "height")
            }
        }
        //  img: ['src', 'alt', 'width', 'height', 'data-non-image']
        // a: ['href', 'target']
        options.addAllowedAttributes("img", Arrays.asList("data-type", "data-id", "class", "src", "alt", "width", "height", "data-non-image"))
        options.addAllowedAttributes("iframe", Arrays.asList("data-type", "data-id", "class", "src", "width", "height"))
        options.addAllowedAttributes("a", Arrays.asList("data-type", "data-id", "class", "href", "target", "title"))

        val icarus = Icarus(toolbar, options, editor)
        prepareToolbar(toolbar, icarus)
        icarus.loadCSS("file:///android_asset/editor.css")
        icarus.loadJs("file:///android_asset/test.js")
        icarus.render()
    }

    private fun prepareToolbar(toolbar: TextViewToolbar, icarus: Icarus): Toolbar {
        val iconfont = Typeface.createFromAsset(_CONTEXT.getAssets(), "Simditor.ttf")
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

        for (name in generalButtons.keys) {
            val textView = layToolBar.findViewById(generalButtons[name] as Int) as TextView ?: continue
            textView.typeface = iconfont
            val button = TextViewButton(textView, icarus)
            button.name = name
            toolbar.addButton(button)
        }
        val linkButtonTextView = layToolBar.findViewById(R.id.button_link) as TextView
        linkButtonTextView.typeface = iconfont
        val linkButton = TextViewButton(linkButtonTextView, icarus)
        linkButton.name = Button.NAME_LINK
        linkButton.popover = LinkPopoverImpl(linkButtonTextView, icarus)
        toolbar.addButton(linkButton)

        val imageButtonTextView = layToolBar.findViewById(R.id.button_image) as TextView
        imageButtonTextView.typeface = iconfont
        val imageButton = TextViewButton(imageButtonTextView, icarus)
        imageButton.name = Button.NAME_IMAGE
        imageButton.popover = ImagePopoverImpl(imageButtonTextView, icarus)
        toolbar.addButton(imageButton)

        val htmlButtonTextView = layToolBar.findViewById(R.id.button_html5) as TextView
        htmlButtonTextView.typeface = iconfont
        val htmlButton = TextViewButton(htmlButtonTextView, icarus)
        htmlButton.setName(Button.NAME_HTML)
        htmlButton.setPopover(HtmlPopoverImpl(htmlButtonTextView, icarus))
        toolbar.addButton(htmlButton)

        val fontScaleTextView = layToolBar.findViewById(R.id.button_font_scale) as TextView
        fontScaleTextView.typeface = iconfont
        val fontScaleButton = FontScaleButton(fontScaleTextView, icarus)
        fontScaleButton.setPopover(FontScalePopoverImpl(fontScaleTextView, icarus))
        toolbar.addButton(fontScaleButton)
        return toolbar
    }

    override fun getLayoutId(): Int = R.layout.fragment_text_editer
}
