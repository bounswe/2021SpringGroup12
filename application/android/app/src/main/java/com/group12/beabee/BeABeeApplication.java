package com.group12.beabee;

import android.app.Application;
import android.content.Context;

import com.group12.beabee.network.BeABeeService;

public class BeABeeApplication extends Application {

    private static Context context;

    public static String AuthToken;
    public static int userId;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BeABeeApplication.context = getApplicationContext();
        BeABeeService.InitNetworking();
    }
}
