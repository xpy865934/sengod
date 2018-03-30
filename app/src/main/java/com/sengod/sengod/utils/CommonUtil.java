package com.sengod.sengod.utils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/8.
 */

/**
 * 工具类
 */
public class CommonUtil {
    private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  //用于控制输出的char[]
    private static String hexString = "0123456789ABCDEF";

    /**
     * 判断设备列表是否重复
     * @param list
     * @param data
     * @return
     */
    public static int judgeDeviceRepet(List<Map<String,String>> list,Map<String,String> data){
        for (Map<String,String> map:list
             ) {
            if(map.get("device_mac").equals(data.get("device_mac"))){
                return 1;
            }
        }
        return 0;
    }

    ///蓝牙转换字符相关 start
    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param data
     *            十六进制char[]
     * @return byte[]
     * @throws RuntimeException
     *             如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHex(char[] data) {
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }
        byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch
     *            十六进制char
     * @param index
     *            十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException
     *             当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch
                    + " at index " + index);
        }
        return digit;
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data
     *            byte[]
     *
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_UPPER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_UPPER[0x0F & data[i]];
        }
        return out;
    }

    /*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
    public static String[] decode(String str) {
        String[] temp = new String[str.length() / 2];
        int j = 0;
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < str.length(); i += 2) {
            String temp1 = new String(
                    (hexString.indexOf(str.charAt(i)) << 4 | hexString
                            .indexOf(str.charAt(i + 1))) + "");
            temp[j] = temp1;
            j++;
        }
        return temp;
    }
    ///蓝牙转换字符相关 end



}
