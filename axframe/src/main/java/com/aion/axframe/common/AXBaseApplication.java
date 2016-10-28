package com.aion.axframe.common;

import android.app.Application;

import com.aion.axframe.utils.log.ALog;

/**
 * Created by Aion on 16/9/29.
 */
public abstract class AXBaseApplication extends Application {

    protected static AXBaseApplication instance;

    public static AXBaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ALog.init(true);
        instance = this;
        ALog.d("AXBaseApplication");
    }
}
