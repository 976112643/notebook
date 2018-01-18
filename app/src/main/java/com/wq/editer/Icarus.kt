package com.wq.editer

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

import com.google.gson.Gson
import com.wq.editer.entity.Options

import java.util.HashMap
import java.util.UUID
import java.util.concurrent.LinkedBlockingQueue

open class Icarus(var toolbar: Toolbar, protected var options: Options, protected var webView: WebView) {
    protected var callbacks = HashMap<String, (String) -> Unit>()
    protected var gson = Gson()
    protected var content: String? = null
        set(value) {
            /**
             * Set HTML content to rich editor, method `render` must be called again after setContent.
             * @param content HTML string
             */
            runAfterReady {
                jsExec("editor.setValue(" + gson.toJson(value) + ");")
                toolbar.resetButtonsStatus()
            }
        }
    protected var initialized = false
    protected var onReadyCallbacks = LinkedBlockingQueue<() -> Unit>()
    protected var editorReady = false

    /**
     * Add native callback.

     * @param callback Native callback
     * *
     * @return Callback name, use for method `callback`.
     */
    fun addCallback(callback: (String) -> Unit): String {
        val callbackName = UUID.randomUUID().toString()
        callbacks.put(callbackName, callback)
        return callbackName
    }

    /**
     * Remove native callback.

     * @param callbackName Native callback.
     */
    fun removeCallback(callbackName: String) {
        if (callbacks.containsKey(callbackName)) {
            callbacks.remove(callbackName)
        }
    }

    /**
     * Natively execute callback function that defined in javascript runtime.

     * @param callbackName Callback name.
     * *
     * @param params       Params that passed to javascript runtime, will be converted to string by Gson.
     * *
     * @param typeOfParams The specific genericized type of `params` (typeOfSrc for gson).
     */
    @JvmOverloads fun jsCallback(callbackName: String, params: Any, typeOfParams: Class<*>? = null) {
        Log.d("jsCallbackName", callbackName)

        var paramsString = ""
        if (typeOfParams != null) {
            paramsString = gson.toJson(params, typeOfParams)
        } else {
            paramsString = gson.toJson(params)
        }
        val jsCode = "editor.callback(" + gson.toJson(callbackName) + "," + paramsString + ");"
        jsExec(jsCode)
    }

    /**
     * Natively remove callback function that defined in javascript runtime.

     * @param callbackName Callback function name.
     */
    fun jsRemoveCallback(callbackName: String) {
        jsExec("editor.removeCallback(" + gson.toJson(callbackName) + ");")
    }

    /**
     * Execute javascript codes under webView.

     * @param jsCode Javascript codes for running.
     */
    fun jsExec(jsCode: String) {
        var jsCode = jsCode
        jsCode = "javascript: " + jsCode
        Log.d("@execJS", jsCode)
        val finalJsCode = jsCode
        webView.post { webView.loadUrl(finalJsCode) }
    }

    /**
     * Initialize Icarus.
     */
    protected fun initialize() {
        if (initialized) {
            return
        }
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                jsExec(
                        " $(function () {\n" +
                                "        var options = $.extend(defaultOptions, " + gson.toJson(options) + ");\n" +
                                "        window.editor = new Simditor(options);\n" +
                                "    });" +
                                ""
                )
                editorReady = true
                if (onReadyCallbacks.size > 0) {
                    var runnable: (() -> Unit)? = onReadyCallbacks.poll()
                    while (runnable!=null) {
                        runnable()
                        runnable = onReadyCallbacks.poll()
                    }
                }
            }

        })

        webView.setWebChromeClient(WebChromeClient())
        webView.settings.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        webView.addJavascriptInterface(this, "IcarusBridge")
        initialized = true
    }


    /**
     * Get content in rich editor.

     * @param callback Callable will be called after content got.
     */
    fun getContent(callback: (String) -> Unit) {
        val callbackName = addCallback(callback)
        webView.post { jsExec("editor.getContentAsync(\"$callbackName\");") }
    }


    /**
     * Initialize and load WebView data. Must be called again after `setContent` method called.
     */
    fun render() {
        editorReady = false
        initialize()
        webView.loadUrl("file:///android_asset/icarus-editor/editor.html")
        //        webView.loadUrl("http://192.168.11.44:8080/editor.html");
        toolbar.resetButtonsStatus()
    }

    /**
     * Insert raw html code to current selection range of editor.

     * @param html
     */
    fun insertHtml(html: String) {
        jsExec("editor.toolbar.buttons['html'].insertHtml(" + gson.toJson(html) + ")")
    }

    /**
     * Call native `Callback` under javascript runtime.

     * @param callbackName You can get `callbackName` by `addCallback` calling.
     * *
     * @param params       Passed from javascript runtime as callback param, must be STRING.
     */
    @JavascriptInterface
    fun callback(callbackName: String, params: String) {
        val callback = callbacks[callbackName] ?: return
        callback(params)
        callbacks.remove(callbackName)
    }

    /**
     * Set activated status of given button name.

     * @param buttonName Button name.
     * *
     * @param activated  Activated status.
     */
    @JavascriptInterface
    fun setButtonActivated(buttonName: String, activated: Boolean) {
        toolbar.setButtonActivated(buttonName, activated)
    }

    /**
     * Set enabled status of given button name.

     * @param buttonName Button name.
     * *
     * @param enabled    Enabled status.
     */
    @JavascriptInterface
    fun setButtonEnabled(buttonName: String, enabled: Boolean) {
        toolbar.setButtonEnabled(buttonName, enabled)
    }

    /**
     * Show popover of given button name.

     * @param buttonName   Button name.
     * *
     * @param params       Passed from javascript runtime as callback param, must be STRING.
     * *
     * @param callbackName You can get `callbackName` by `editor.addCallback` calling under javascript runtime.
     */
    @JavascriptInterface
    fun popover(buttonName: String, params: String, callbackName: String) {
        toolbar.popover(buttonName, params, callbackName)
    }

    /**
     * Do something one time after the editor ready.

     * @param runnable Callback
     */
    fun runAfterReady(runnable: () -> Unit) {
        if (editorReady) {
            runnable()
        } else {
            try {
                onReadyCallbacks.put(runnable)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Load css file.

     * @param cssUrl Css file url that you want to load
     */
    fun loadCSS(cssUrl: String) {

        val js = String.format(
                "$(function() {" +
                        "$('head').append(" +
                        "$('<link></link>', " +
                        "{rel:'stylesheet', type:'text/css', href:'%s', media:'all'})" +
                        ")" +
                        "});",
                cssUrl
        )

        runAfterReady { jsExec(js) }
    }

    /**
     * Load javascript file

     * @param jsUrl Javascript file url that you want to load
     */
    fun loadJs(jsUrl: String) {

        val js = String.format(
                "        var body  = document.getElementsByTagName(\"body\")[0];\n" +
                        "        var script  = document.createElement(\"script\");\n" +
                        "        script.type = \"text/javascript\";\n" +
                        "        script.src = \"%s\";\n" +
                        "        body.appendChild(script);",
                jsUrl
        )

        runAfterReady { jsExec(js) }
    }
}
/**
 * Natively execute callback function that defined in javascript runtime.

 * @param callbackName Callback name.
 * *
 * @param params       Params pass to javascript runtime, will be converted to string by Gson.
 */
