package com.wq.editer;


import com.wq.editer.button.Button;

public interface Toolbar {
    /**
     * Get button by given button name.
     *
     * @param buttonName Button name.
     * @return
     */
    public Button getButton(String buttonName);

    public void popover(String buttonName, final String params, final String callbackName);

    public void setButtonEnabled(String buttonName, final boolean enabled);

    public void setButtonActivated(String buttonName, final boolean activated);

    public void resetButtonsStatus();
}

