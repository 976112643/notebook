package com.wq.common.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by WQ on 2017/9/20.
 */

public class NetTaskService extends IntentService {
    private static int nameNo=0;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NetTaskService() {
        super(NetTaskService.class.getName()+""+(nameNo++));
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
