package com.wq.editer.button;

import android.widget.TextView;

import com.wq.editer.Icarus;


/**
 * Created by decent on 16/5/30.
 */
public class FontScaleButton extends TextViewButton {
    public String getName() {
        return NAME_FONT_SCALE;
    }

    public FontScaleButton(TextView textView, Icarus icarus) {
        super(textView, icarus);
    }

    public void command() {
        if (getPopover() != null) {
            getPopover().show("", "");
        }
    }
}