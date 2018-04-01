package com.sengod.sengod;

import android.app.Application;

import com.inuker.bluetooth.library.BluetoothClient;
import com.sengod.sengod.db.DbManager;

/**
 * Created by xpy on 2018/1/14.
 */

public class MyApplication extends Application {
    private static BluetoothClient mBluetoothClient = null;
    private static DbManager dbManager=null;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化BluetoothClient,全局单例使用
        if(mBluetoothClient == null){
            mBluetoothClient = new BluetoothClient(this);
        }
    }

    public static BluetoothClient getBluetoothClient(){
        return mBluetoothClient;
    }
}
