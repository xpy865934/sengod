package com.sengod.sengod.utils;

import android.content.Context;
import android.util.Log;

import com.sengod.sengod.AnalyticalDataUtilsInterface;
import com.sengod.sengod.R;

import java.util.Arrays;

import static com.sengod.sengod.utils.CommonUtil.hexStringToString;
import static java.lang.Integer.parseInt;

/**
 * Created by Administrator on 2018/5/29.
 */

public class AnalyticalDataUtil {
    private static String[] tempResult = new String[19];
    private static String[] result = new String[]{};
    private static int len = 0;
    private static long startTime = System.currentTimeMillis();

    /**
     * 将str不足2个字节的补全
     *
     * @param str
     * @return
     */
    private static String completeToTwoByte(String str) {
        if (str.length() < 4) {
            str = "0000".substring(0, 4 - str.length()) + str;
        }
        str = changeToLittle(str);
        return str;
    }

    /**
     * 将str不足1个字节的补全
     *
     * @param str
     * @return
     */
    private static String completeToOneByte(String str) {
        if (str.length() < 2) {
            str = "00".substring(0, 2 - str.length()) + str;
        }
        return str;
    }

    /**
     * 转换为小端模式
     *
     * @param str
     * @return
     */
    private static String changeToLittle(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length / 2; i += 2) {
            //第一个字符
            char temp = chars[i];
            chars[i] = chars[chars.length - 2 - i];
            chars[chars.length - 2 - i] = temp;
            //第二个字符
            temp = chars[i + 1];
            chars[i + 1] = chars[chars.length - 1 - i];
            chars[chars.length - 1 - i] = temp;
        }
        return String.valueOf(chars);
    }

    public static void analyData(Context context, byte[] value, AnalyticalDataUtilsInterface analyticalDataUtilsInterface) {
        //判断是否是同一批数据
        if(System.currentTimeMillis() - startTime > 2000){
            tempResult = new String[19];
            result = new String[]{};
            len = 0;
        }
        startTime = System.currentTimeMillis();
        String[] listenerResult = new String[]{""};
        try {
            String temp = new String(CommonUtil.encodeHex(value));
            tempResult = CommonUtil.decode(temp);
            for (int i = 0; i < tempResult.length; i++) {
                tempResult[i] = Integer.toHexString(Integer.valueOf(tempResult[i]));
                LogUtils.i("TAG", tempResult[i] + "");
            }

            //判断剩余数据
            if (len == 0) {
                len = Integer.parseInt(tempResult[5] + tempResult[4], 16);
            }


            //判断长度
            String[] te = new String[result.length + tempResult.length];
            System.arraycopy(result, 0, te, 0, result.length);
            System.arraycopy(tempResult, 0, te, result.length, tempResult.length);
            result = Arrays.copyOf(te, result.length + tempResult.length);
            LogUtils.i("TAG", "result length:" + result.length);
            LogUtils.i("TAG", "len:" + len);

            if (len + 8 == result.length) {
                //长度达到
                LogUtils.i("TAG", "contact data:" + Arrays.toString(result));

                //CRC校验
                StringBuilder tempHeader = new StringBuilder();
                for (int i = 0; i < result.length - 2; i++) {
                    tempHeader.append(completeToOneByte(result[i]));
                }
                Log.i("TAG", "Copy data:" + tempHeader);
                int i = CRC16Util.calcCrc16(tempHeader.toString());
                String crc = String.format("%04x", i);
                String crcresult = completeToOneByte(result[result.length - 1]) + completeToOneByte(result[result.length - 2]);

                if (crc.equals(crcresult)) {
                    //crc校验成功
                    String.valueOf(parseInt(result[result.length - 1] + result[result.length - 2], 16));
                    switch (result[7]) {
                        //状态
                        case "81":
                        case "82":
                        case "83":
                        case "84":
                        case "85":
                        case "86":
                        case "87":
                        case "88":
                        case "89":
                        case "8A":
                        case "8B":
                        case "8C":
                        case "8D":
                        case "8E":
                        case "8F":
                        case "90":
                        case "91":
                        case "92":
                        case "93":
                            String state = String.valueOf(parseInt(result[8], 16));
                            switch (state) {
                                case "1":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg1));
                                    break;
                                case "3":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg2));
                                    break;
                                case "4":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg3));
                                    break;
                                case "5":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg4));
                                    break;
                                case "6":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg5));
                                    break;
                                case "12":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg6));
                                    break;
                                case "13":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg7));
                                    break;
                                case "14":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg8));
                                    break;
                                case "15":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg9));
                                    break;
                                case "10":
                                    analyticalDataUtilsInterface.fail(context.getString(R.string.errorMsg10));
                                    break;
                            }
                            break;

                        //读数据记录
                        case "1":
                            listenerResult = new String[7];
                            //出厂时间
                            listenerResult[0] = String.format(context.getResources().getString(R.string.activity_data_record_factory_time_data), String.valueOf(parseInt(result[9] + result[8], 16)), String.valueOf(parseInt(result[10], 16)), String.valueOf(parseInt(result[11], 16)));
                            //版本号
                            listenerResult[1] = String.format(context.getResources().getString(R.string.activity_data_record_factory_version_data), String.valueOf(parseInt(result[13] + result[12], 16)), String.valueOf(parseInt(result[15] + result[14], 16)));
                            //充电总次数
                            listenerResult[2] = String.format(context.getResources().getString(R.string.activity_data_record_total_charging_times_data), parseInt(result[35] + result[34], 16));
                            //使用时间
                            Integer tminutes = parseInt(result[19] + result[18] + result[17] + result[16], 16);
                            Integer hour = tminutes / 60;
                            Integer min = tminutes % 60;
                            listenerResult[3] = String.format(context.getResources().getString(R.string.activity_data_record_use_time_data), hour, min);
                            //行走时间
                            tminutes = parseInt(result[23] + result[22] + result[21] + result[20], 16);
                            hour = tminutes / 60;
                            min = tminutes % 60;
                            listenerResult[4] = String.format(context.getResources().getString(R.string.activity_data_record_walk_time_data), hour, min);
                            //载物时间
                            tminutes = parseInt(result[27] + result[26] + result[25] + result[24], 16);
                            hour = tminutes / 60;
                            min = tminutes % 60;
                            listenerResult[5] = String.format(context.getResources().getString(R.string.activity_data_record_carry_time_data), hour, min);
                            //顶升次数
                            listenerResult[6] = String.format(context.getResources().getString(R.string.activity_data_record_lift_time_data), parseInt(result[27] + result[26] + result[25] + result[24], 16));
                            break;
                        //读参数设置
                        case "3":
                            listenerResult = new String[11];
                            //空载加减速
                            listenerResult[0] = String.valueOf(parseInt(result[9] + result[8], 16));
                            listenerResult[1] = String.valueOf(parseInt(result[11] + result[10], 16));

                            //带载加减速
                            listenerResult[2] = String.valueOf(parseInt(result[13] + result[12], 16));
                            listenerResult[3] = String.valueOf(parseInt(result[15] + result[14], 16));

                            //顶升调度
                            listenerResult[4] = String.valueOf(parseInt(result[17] + result[16], 16));

                            //顶升速度
                            listenerResult[5] = String.valueOf(parseInt(result[19] + result[18], 16));

                            //减速距离
                            listenerResult[6] = String.valueOf(parseInt(result[21] + result[20], 16));

                            //减速加速度
                            listenerResult[7] = String.valueOf(parseInt(result[23] + result[22], 16));

                            //旋转速度
                            listenerResult[8] = String.valueOf(parseInt(result[25] + result[24], 16));

                            //左轮直径
                            listenerResult[9] = String.valueOf(parseInt(result[29] + result[28], 16));

                            //右轮直径
                            listenerResult[10] = String.valueOf(parseInt(result[31] + result[30], 16));
                            break;
                        //读动态测试
                        case "5":
                            listenerResult = new String[17];
                            //压力波开关38
                            listenerResult[0] = String.valueOf(parseInt(result[38], 16));
                            listenerResult[1] = String.valueOf(parseInt(result[39], 16));
                            //急停
                            listenerResult[2] = String.valueOf(parseInt(result[40], 16));
                            //充电插座
                            listenerResult[3] = String.valueOf(parseInt(result[43], 16));
                            listenerResult[4] = String.valueOf(parseInt(result[44], 16));
                            listenerResult[5] = String.valueOf(parseInt(result[45], 16));
                            //电池电压
                            listenerResult[6] = String.format(context.getResources().getString(R.string.activity_dynamic_test_battery_voltage_data), String.valueOf(parseInt(result[9] + result[8], 16)));
                            //电池温度
                            listenerResult[7] = String.format(context.getResources().getString(R.string.activity_dynamic_test_battery_temperature_data), parseInt(result[11] + result[10], 16));
                            //障碍物距离监测
                            listenerResult[8] = String.format(context.getResources().getString(R.string.activity_dynamic_test_obstacle_distance_data), parseInt(result[21] + result[20], 16));
                            //行走伺服
                            listenerResult[9] = String.valueOf(parseInt(result[22], 16));
                            listenerResult[10] = String.valueOf(parseInt(result[26], 16));
                            //行走伺服工作电流
                            listenerResult[11] = String.format(context.getResources().getString(R.string.activity_dynamic_test_walking_current_data), String.valueOf(parseInt(result[25] + result[24], 16)));
                            listenerResult[12] = String.format(context.getResources().getString(R.string.activity_dynamic_test_walking_current_data), String.valueOf(parseInt(result[29] + result[28], 16)));
                            //顶升伺服
                            listenerResult[13] = String.valueOf(parseInt(result[30], 16));
                            //顶升伺服工作电流
                            listenerResult[14] = String.format(context.getResources().getString(R.string.activity_dynamic_test_jacking_current_data), String.valueOf(parseInt(result[33] + result[32], 16)));
                            //旋转伺服
                            listenerResult[15] = String.valueOf(parseInt(result[34], 16));
                            //旋转伺服工作电流
                            listenerResult[16] = String.format(context.getResources().getString(R.string.activity_dynamic_test_rotate_current_data), String.valueOf(parseInt(result[37] + result[36], 16)));
                            break;
                        //读地址二维码
                        case "6":
                            //地址二维码数据
                            listenerResult = new String[5];
                            listenerResult[0] = String.valueOf(parseInt(result[15] + result[14], 16));
                            listenerResult[1] = String.valueOf(parseInt(result[17] + result[16], 16));

                            if(result[9].startsWith("F")||result[9].startsWith("f")){
                                listenerResult[2] = String.valueOf(parseInt(result[9] + result[8], 16)-65536);
                            }else{
                                listenerResult[2] = String.valueOf(parseInt(result[9] + result[8], 16));
                            }
                            if(result[11].startsWith("F")||result[11].startsWith("f")){
                                listenerResult[3] = String.valueOf(parseInt(result[11] + result[10], 16)-65536);
                            }else{
                                listenerResult[3] = String.valueOf(parseInt(result[11] + result[10], 16));
                            }

                            LogUtils.i("TAG","angle:"+String.format("%.2f",parseInt(result[13]+ result[12], 16)/1000.0*180.0/3.1415926));
                            if(result[13].startsWith("F")||result[13].startsWith("f")){
                                LogUtils.i("TAG","angle parseInt:"+(parseInt(result[13]+ result[12], 16)-65536));
                                listenerResult[4] = String.format("%.2f",(parseInt(result[13]+ result[12], 16)-65536)/1000.0*180.0/3.1415926);
                            }else{
                                listenerResult[4] = String.format("%.2f",parseInt(result[13]+ result[12], 16)/1000.0*180.0/3.1415926);
                            }
                            break;
                        //读货架二维码
                        case "7":
                            //货架二维码数据
                            listenerResult = new String[4];
                            String tempStr = "";
                            for (int j = 14; j < result.length - 2; j++) {
                                tempStr += completeToOneByte(result[j]);
                            }
                            listenerResult[0] = hexStringToString(tempStr);

                            if(result[9].startsWith("F")||result[9].startsWith("f")){
                                listenerResult[1] = String.valueOf(parseInt(result[9] + result[8], 16)-65536);
                            }else{
                                listenerResult[1] = String.valueOf(parseInt(result[9] + result[8], 16));
                            }

                            if(result[11].startsWith("F")||result[11].startsWith("f")){
                                listenerResult[2] = String.valueOf(parseInt(result[11] + result[10], 16)-65536);
                            }else{
                                listenerResult[2] = String.valueOf(parseInt(result[11] + result[10], 16));
                            }

                            if(result[13].startsWith("F")||result[13].startsWith("f")){
                                LogUtils.i("TAG","angle parseInt:"+(parseInt(result[13]+ result[12], 16)-65536));
                                listenerResult[3] = String.format("%.2f",(parseInt(result[13]+ result[12], 16)-65536)/1000.0*180.0/3.1415926);
                            }else{
                                listenerResult[3] = String.format("%.2f",parseInt(result[13]+ result[12], 16)/1000.0*180.0/3.1415926);
                            }
                            break;
                        //状态查询
                        case "13":
                            break;
                    }
                    analyticalDataUtilsInterface.success(listenerResult);
                } else {
                    //crc校验出错
                    analyticalDataUtilsInterface.fail(context.getResources().getString(R.string.errorMsg));
                }

                tempResult = new String[19];
                result = new String[]{};
                len = 0;
            }


        } catch (Exception e) {
            analyticalDataUtilsInterface.fail(context.getResources().getString(R.string.errorMsg11));
            e.printStackTrace();
        }
    }
}
