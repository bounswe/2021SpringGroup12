package com.group12.beabee;

import android.app.Application;
import android.content.Context;

import com.group12.beabee.network.BeABeeService;

public class BeABeeApplication extends Application {

    public static int currentGroupGoal;
    private static Context context;

    public static String AuthToken;
    public static int userId;
    public static int currentMainGoal;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BeABeeApplication.context = getApplicationContext();
        BeABeeApplication.userId = 0;
        AuthToken = null;
        BeABeeService.InitNetworking();
    }
}
