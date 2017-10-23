package com.wq.editer.popover;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.wq.editer.Icarus;
import com.wq.editer.entity.Image;


/**
 * Created by WQ on 2017/10/23.
 */

public abstract class BaseImagePopover implements Popover {
    protected Icarus icarus;
    protected Handler mainLopperHandler;
    protected Context context;
    protected String callbackName;

    public BaseImagePopover(Context context, Icarus icarus) {
        this.context = context;
        this.icarus = icarus;
        mainLopperHandler = new Handler(Looper.getMainLooper());
    }

    public void setImage(final Image image) {
        mainLopperHandler.post(new Runnable() {
            @Override
            public void run() {
                icarus.jsCallback(callbackName, image, Image.class);
            }
        });
    }

    @Override
    final public void show(String params, String callbackName) {
        this.callbackName = callbackName;
    }

    abstract public void showed(String params, String callbackName);

    @Override
    public void hide() {

    }
}
