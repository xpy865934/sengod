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
    public static final String PROTOCOLID = "0000";

    //节点号
    public static final String NODEID="00";

    //前进和后退默认距离
    public static Integer distance=20;

    //升降默认距离
    public static Integer upDistance=20;  //十进制

    //速度
    public static Integer speed=20;

    //功能码
    public static final Map<String,String> FUCTIONID=new HashMap<String,String>(){
        {
            put("moveForward","08");
            put("moveBack","09");
            put("turn","0A");
            put("relativeTurn","0B");
            put("upOrDown","0C");
            put("readAddressQrCode","06");
            put("readShelfQrCode","07");
            put("readData","01");
            put("writeData","02");
            put("readParam","03");
            put("writeParam","04");
            put("readDynamicData","05");
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
