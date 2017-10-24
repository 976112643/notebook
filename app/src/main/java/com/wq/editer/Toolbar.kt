package com.wq.editer


import com.wq.editer.button.Button

interface Toolbar {
    /**
     * Get button by given button name.

     * @param buttonName Button name.
     * *
     * @return
     */
//    fun getButton(buttonName: String): Button

    fun popover(buttonName: String, params: String, callbackName: String)

    fun setButtonEnabled(buttonName: String, enabled: Boolean)

    fun setButtonActivated(buttonName: String, activated: Boolean)

    fun resetButtonsStatus()
}

