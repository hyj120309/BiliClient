package com.RobinNotBad.BiliClient;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class BiliClient extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        ErrorCatch errorCatch = ErrorCatch.getInstance();
        errorCatch.init(getApplicationContext());
        if(context==null) context = getApplicationContext();
    }
}