package com.aion.axframe.common;

import android.app.Application;

import com.aion.axframe.utils.log.ALog;

/**
 * Created by Aion on 16/9/29.
 */
public class AXBaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ALog.init(true);
    }
}
