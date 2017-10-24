package com.wq.editer

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View


import com.wq.editer.button.Button
import com.wq.editer.button.TextViewButton

import java.util.HashMap

class TextViewToolbar : Toolbar {
    protected var mainLopperHandler = Handler(Looper.getMainLooper())
    protected var buttons = mutableMapOf<String, TextViewButton>()


//    override fun getButton(buttonName: String): Button {
//        return null!!
//    }

    fun addButton(button: TextViewButton) {
        buttons.put(button.name, button)
        button.textView.setOnClickListener {
            if (button.isEnabled) {
                mainLopperHandler.post {
                    Log.d("native.command", button.name)
                    button.command()
                }
            }
        }
    }

    override fun setButtonActivated(buttonName: String, activated: Boolean) {
        val button = buttons[buttonName]
        if (button != null) {
            mainLopperHandler.post { button.isActivated = activated }
        }
    }

    override fun resetButtonsStatus() {
        for ((_,button) in buttons) {
            button.resetStatus()
        }
    }

    override fun setButtonEnabled(buttonName: String, enabled: Boolean) {
        val button = buttons[buttonName]
        if (button != null) {
            mainLopperHandler.post { button.isEnabled = enabled }
        }
    }

    override fun popover(buttonName: String, params: String, callbackName: String) {
        val button = buttons[buttonName]
        Log.d("@popover", buttonName)
        if (button != null && button.popover != null) {
            mainLopperHandler.post { button.popover?.show(params, callbackName) }
        }
    }
}