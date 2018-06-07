package com.sengod.sengod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xpy on 2018/1/15.
 */

public class ConfigApp {
    public static String current_connected_mac=""; //当前连接的mac地址
    public static String current_connected_name="";//当前连接的name

    //读
    public static String read_characteristic_serviceuuid="0000fff0-0000-1000-8000-00805f9b34fb";
    public static String read_characteristic_characteruuid="0000fff6-0000-1000-8000-00805f9b34fb";

    //写
    public static String write_characteristic_serviceuuid="0000fff0-0000-1000-8000-00805f9b34fb";
    public static String write_characteristic_characteruuid="0000fff6-0000-1000-8000-00805f9b34fb";

    //事务ID
    public static final Integer TRID=1;

    //协议ID
    public static final String PROTOCOLID = "0001";

    //节点号
    public static final String NODEID="00";

    //前进和后退默认距离
    public static Integer distance=20;

    //升降默认距离
    public static Integer upDistance=20;  //十进制

    //速度
    public static Integer speed=20;

    //空载加速度默认值
    public static Integer noLoadSpeedUp = 20;

    //空载减速度默认值
    public static Integer noLoadSpeedDown = 20;

    //带载加速度默认值
    public static Integer loadSpeedUp = 20;

    //带载减速度默认值
    public static Integer loadSpeedDown = 20;

    //顶升调度默认值
    public static Integer liftDispatch = 5;

    //顶升速度默认值
    public static Integer liftSpeed = 10;

    //减速距离默认值
    public static Integer speedDownDistance = 20;

    //减速加速度默认值
    public static Integer speedDownAcc = 10;

    //旋转速度默认值
    public static Integer rotateSpeed = 10;

    //左轮直径默认值
    public static Integer wheelLeft = 10;
    //右轮直径默认值
    public static Integer wheelRight = 10;



    //功能码
    public static final Map<String,String> FUCTIONID=new HashMap<String,String>(){
        {
            put("readData","01");
            put("writeData","02");
            put("readParam","03");
            put("writeParam","04");
            put("readDynamicData","05");
            put("readAddressQrCode","06");
            put("readShelfQrCode","07");
            put("moveForward","08");
            put("moveBack","09");
            put("turn","0A");
            put("relativeTurn","0B");
            put("upOrDown","0C");
            put("absoluteRotate","0D");
            put("absoluteRotateExcursion","0E");
            put("rectifyMoveForward","0F");
            put("rectifyMoveBack","10");
            put("relativeShelfRotate","11");
            put("absoluteShelfRotate","12");
            put("queryState","13");
        }
    };
}
