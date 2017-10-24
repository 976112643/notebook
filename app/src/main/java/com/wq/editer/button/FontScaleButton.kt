package com.wq.editer.button

import android.widget.TextView

import com.wq.editer.Icarus
import com.wq.editer.popover.Popover


/**
 * Created by decent on 16/5/30.
 */
class FontScaleButton(textView: TextView, name: String, icarus: Icarus, popover: Popover) : TextViewButton(textView, name, icarus, popover) {
    override var name: String
        get() = Button.Companion.NAME_FONT_SCALE
        set(value: String) {
            super.name = value
        }

    override fun command() {
        popover?.show("", "")
    }
}