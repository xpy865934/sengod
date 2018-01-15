package com.sengod.sengod;

import android.app.Application;

import com.inuker.bluetooth.library.BluetoothClient;

/**
 * Created by xpy on 2018/1/14.
 */

public class MyApplication extends Application{
    public static BluetoothClient mBluetoothClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothClient = new BluetoothClient(this);
    }
}
