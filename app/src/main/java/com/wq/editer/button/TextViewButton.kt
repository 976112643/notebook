package com.wq.editer.button

import android.os.Handler
import android.os.Looper
import android.widget.TextView

import com.google.gson.Gson
import com.wq.config.R
import com.wq.editer.Icarus
import com.wq.editer.popover.Popover

open class TextViewButton() : Button {
    override var name: String=""
    protected var gson = Gson()
    protected var mainLopperHandler: Handler
    var enabledColor: Int = 0
    var disabledColor: Int = 0
    var activatedColor: Int = 0
    var deactivatedColor: Int = 0
    lateinit var textView: TextView
    lateinit var icarus: Icarus
    override var popover: Popover?=null

    init {
        mainLopperHandler = Handler(Looper.getMainLooper())

    }//        this.icarus = icarus;

    constructor(init:TextViewButton.()->Unit) : this() {
        init()
    }
    constructor(textView: TextView,name:String,icarus: Icarus,popover: Popover?=null) : this() {
        this.textView=textView
        this.name=name
        this.icarus=icarus
        this.popover=popover
        enabledColor = textView.resources.getColor(R.color.button_enabled)
        disabledColor = textView.resources.getColor(R.color.button_disabled)
        activatedColor = textView.resources.getColor(R.color.button_activated)
        deactivatedColor = textView.resources.getColor(R.color.button_deactivated)
    }
    override var isEnabled: Boolean=true
        get() = field
        set(enabled) {
            field = enabled
            if (enabled) {
                textView.setTextColor(enabledColor)
            } else {
                textView.setTextColor(disabledColor)
            }
        }

    override var isActivated: Boolean =false
        get() = field
        set(activated) {
            field = activated
            if (activated) {
                textView.setTextColor(activatedColor)
            } else {
                textView.setTextColor(deactivatedColor)
            }
        }

    override fun command() {
        icarus.jsExec("javascript: editor.toolbar.execCommand('$name')")
    }

    override fun resetStatus() {
        isActivated = false
        isEnabled = true
    }
}
