package com.wq.editer.entity

import java.util.HashMap

class Link {
    var text = ""
    private var attributes: MutableMap<String, String> = mutableMapOf()
    var url: String?
        get() = getAttribute("href")
        set(url) = setAttribute("href", url)

    var target: String?
        get() = getAttribute("target")
        set(target) = setAttribute("target", target)

    fun getAttributes(): Map<String, String> {
        return attributes
    }

    fun setAttributes(attributes: MutableMap<String, String>) {
        this.attributes = attributes
    }

    fun getAttribute(name: String): String? {
        return attributes!![name]
    }

    fun setAttribute(name: String, value: String?) {
        if(value==null)return
        attributes.put(name, value)
    }

    fun removeAttribute(name: String, value: String) {
        attributes.remove(name)
    }
}
