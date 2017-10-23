package com.wq.editer.entity

import java.util.*

class Options {
    /**
     * Placeholder of Editor. Use the placeholder attribute value of the textarea by default.
     */
    var placeholder = "Icarus editor."
    /**
     * Default image placeholder. Used when inserting pictures in Edtior.
     */
    var defaultImage = "images/image.png"
    /**
     * Remove all styles in paste content automatically.
     */
    var isCleanPaste = false

    /**
     * Tags that are allowed in Editor.
     */
    private var allowedTags: MutableList<String> = Arrays.asList("br", "span", "a", "img", "b", "strong", "i", "strike", "u", "font", "p", "ul", "ol", "li", "blockquote", "pre", "code", "h1", "h2", "h3", "h4", "hr")
    private var allowedAttributes: MutableMap<String, List<String>> = HashMap()

    constructor(build:Options.()->Unit){
        build()
    }


    fun getAllowedTags(): List<String> {
        return allowedTags
    }

    fun setAllowedTags(allowedTags: MutableList<String>) {
        this.allowedTags = allowedTags
    }

    fun addAllowedTag(tagName: String) {
        allowedTags.add(tagName)
    }

    fun getAllowedAttributes(): Map<String, List<String>> {
        return allowedAttributes
    }

    fun setAllowedAttributes(allowedAttributes: MutableMap<String, List<String>>) {
        this.allowedAttributes = allowedAttributes
    }

    fun addAllowedAttributes(tag: String, attributes: List<String>) {
        allowedAttributes.put(tag, attributes)
    }
    fun addAllowedAttributes(attrsBuilder:MutableMap<String,List<String>>.()->Unit) {
        val attrs= mutableMapOf<String,List<String>>()
        attrsBuilder.invoke(attrs)
        for((tag,attributes) in attrs){
            allowedAttributes.put(tag, attributes)
        }
    }
}
