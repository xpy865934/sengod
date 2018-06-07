package com.sengod.sengod;

import android.app.Application;
import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;
import com.sengod.sengod.db.DbManager;

/**
 * Created by xpy on 2018/1/14.
 */

public class MyApplication extends Application {
    private static BluetoothClient mBluetoothClient = null;
    private static DbManager dbManager=null;
    private static Context mcontext;

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext = MyApplication.this;

        //初始化BluetoothClient,全局单例使用
        if(mBluetoothClient == null){
            mBluetoothClient = new BluetoothClient(this);
        }
    }

    public static BluetoothClient getBluetoothClient(){
        return mBluetoothClient;
    }

    public static Context getMContext(){
        return mcontext;
    }
}
