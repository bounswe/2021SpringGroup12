package com.group12.beabee;

import android.app.Application;

import com.group12.beabee.network.BeABeeService;

public class BeABeeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BeABeeService.InitNetworking();
    }


}
