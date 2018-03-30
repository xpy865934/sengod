package com.sengod.sengod.utils;

import com.orhanobut.logger.Logger;
/**
 * Created by Administrator on 2018/3/8.
 */
/**
 * 日志工具类
 */
public class LogUtils {
    /**
     * 是否开启debug
     * 注意：使用Eclipse打包的时候记得取消Build Automatically，否则一直是true
     */
    public static boolean isDebug= true;

    /**
     * 错误
     */
    public static void e(String tag,String msg){
        if(isDebug){
            Logger.t(tag).e(msg+"");
        }
    }
    /**
     * 调试
     */
    public static void d(String tag,String msg){
        if(isDebug){
            Logger.t(tag).d( msg+"");
        }
    }
    /**
     * 信息
     */
    public static void i(String tag,String msg){
        if(isDebug){
            Logger.t(tag).i( msg+"");
        }
    }
}
