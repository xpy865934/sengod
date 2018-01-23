package com.sengod.sengod;

import android.app.Application;

import com.inuker.bluetooth.library.BluetoothClient;

/**
 * Created by xpy on 2018/1/14.
 */

public class MyApplication extends Application {
    public static BluetoothClient mBluetoothClient = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mBluetoothClient == null)
            mBluetoothClient = new BluetoothClient(this);
    }
}
