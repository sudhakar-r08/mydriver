package com.techgimmicks.mydriver;

import android.app.Application;
import android.content.Context;

public class GlobalData extends Application {
    static GlobalData globalData;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static GlobalData getInstance() {
        if (globalData == null)
            new GlobalData();
        return globalData;
    }

    public Context getContext() {
        return this.getApplicationContext();
    }
}
