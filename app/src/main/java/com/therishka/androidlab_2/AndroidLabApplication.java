package com.therishka.androidlab_2;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * @author Rishad Mustafaev
 */

public class AndroidLabApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
