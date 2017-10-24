package com.wq.editer.button


import com.wq.editer.popover.Popover

interface Button {

    var name: String

    var isEnabled: Boolean

    var isActivated: Boolean

    fun command()

    fun resetStatus()

    var popover: Popover?

    companion object {
        val NAME_TITLE = "title"
        val NAME_BOLD = "bold"
        val NAME_ITALIC = "italic"
        val NAME_UNDERLINE = "underline"
        val NAME_STRIKETHROUGH = "strikethrough"
        val NAME_FONT_SCALE = "fontScale"
        val NAME_COLOR = "color"
        val NAME_OL = "ol"
        val NAME_UL = "ul"
        val NAME_BLOCKQUOTE = "blockquote"
        val NAME_CODE = "code"
        val NAME_TABLE = "table"
        val NAME_LINK = "link"
        val NAME_IMAGE = "image"
        val NAME_HR = "hr"
        val NAME_INDENT = "indent"
        val NAME_OUTDENT = "outdent"
        val NAME_ALIGN_LEFT = "alignLeft"
        val NAME_ALIGN_CENTER = "alignCenter"
        val NAME_ALIGN_RIGHT = "alignRight"
        val NAME_HTML = "html"
    }
}