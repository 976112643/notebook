package com.wq.editer.button

import com.wq.editer.popover.Popover

/**
 * Created by WQ on 2017/10/24.
 */

class TestButtonInterface : Button {
    constructor(init:TestButtonInterface.()->Unit){
        init()
    }
    override var name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var isEnabled: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var isActivated: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun command() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetStatus() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var popover: Popover
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
}
