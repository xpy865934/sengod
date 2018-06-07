package com.sengod.sengod.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.sengod.sengod.ConfigApp;
import com.sengod.sengod.MyApplication;
import com.sengod.sengod.R;
import com.sengod.sengod.db.DbManager;
import com.sengod.sengod.ui.customerview.CustomerDialog;
import com.sengod.sengod.ui.dialog.LoadingDialog;
import com.sengod.sengod.ui.dialog.MyDialog;
import com.sengod.sengod.utils.AtyContainer;
import com.sengod.sengod.utils.CRC16Util;
import com.sengod.sengod.utils.CommonUtil;
import com.sengod.sengod.utils.LogUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by xpy on 2018/3/7.
 */

public abstract class BaseActivity extends Activity{
    public BluetoothClient mClient;
    public DbManager dbManager;
    private int msgLen=0;
    private int currLen=0;
    private List<byte[]> list;
    public CustomerDialog mcustomerDialog;
    private Dialog loadingDialog;
    private BleNotifyResponse bleNotifyResponse=null;

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            LogUtils.i("TAG","connectStatus:"+status);
            loadingDialog.dismiss();
            if(status == Constants.STATUS_DISCONNECTED){
                mcustomerDialog.show();
            }else if(status == Constants.STATUS_CONNECTED){
                mcustomerDialog.dismiss();
                //打开notify使能
                if(bleNotifyResponse!=null){
                    BaseActivity.this.notify(bleNotifyResponse);
                }
            }
        }
    };

    public void setBleNotifyResponse(BleNotifyResponse bleNotifyResponse){
        this.bleNotifyResponse = bleNotifyResponse;
    }

    /**
     * 注册状态监听变换
     */
    public void registerConnectStatusListener(Context context){
        loadingDialog = LoadingDialog.getDialog(context,context.getString(R.string.bluetooth_reconnected));
        mcustomerDialog = MyDialog.getDisConnectedDialog(context, new CustomerDialog.OnDialogButtonClickListener() {
            @Override
            public void okButtonClick() {
                loadingDialog.show();
                mClient.connect(ConfigApp.current_connected_mac, new BleConnectResponse() {
                    @Override
                    public void onResponse(int code, BleGattProfile data) {

                    }
                });
            }

            @Override
            public void exitButtonClick() {
                AtyContainer.getInstance().finishAllActivity();
            }
        });
        mClient.registerConnectStatusListener(ConfigApp.current_connected_mac,mBleConnectStatusListener);
    }

    /**
     * 取消注册监听
     */
    public void unRegisterConnectStatusListener(){
        mClient.unregisterConnectStatusListener(ConfigApp.current_connected_mac,mBleConnectStatusListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加Activity到栈中
        AtyContainer.getInstance().addActivity(this);

        mClient = MyApplication.getBluetoothClient();
        dbManager = DbManager.getInstance(getApplicationContext());

        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除栈中的Activity
        LogUtils.i("TAG","调用父类onDestory");
        AtyContainer.getInstance().removeActivity(this);
    }

    public abstract void initView();

    /**
     * 写数据（不超过20字节）
     * @param msg
     * @param response
     */
    public void write(String msg, BleWriteResponse response){
        UUID serviceuuid = UUID.fromString(ConfigApp.write_characteristic_serviceuuid);
        UUID characteruuid = UUID.fromString(ConfigApp.write_characteristic_characteruuid);
        //注意这边写的时候需要进行转换之后才可以
        byte[] resultMsg=CommonUtil.decodeHex(msg.toCharArray());
        if(resultMsg.length>20){
            list = splitPacketFor16Byte(resultMsg);
            msgLen = list.size();
            writeMore(list.get(currLen),writeResponse);
            currLen++;
        }else{
            mClient.write(ConfigApp.current_connected_mac,serviceuuid,characteruuid,resultMsg,response);
        }
    }

    /**
     * 写数据（超过20字节）
     * @param msg
     * @param response
     */
    public void writeMore(byte[] msg, BleWriteResponse response){
        UUID serviceuuid = UUID.fromString(ConfigApp.write_characteristic_serviceuuid);
        UUID characteruuid = UUID.fromString(ConfigApp.write_characteristic_characteruuid);
        LogUtils.i("TAG","writeMoreData:"+ Arrays.toString(msg));
        mClient.write(ConfigApp.current_connected_mac,serviceuuid,characteruuid,msg,response);
    }

    private BleWriteResponse writeResponse = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            LogUtils.i("TAG", "write response code " + code);
            if(currLen<msgLen){
                write(list.get(currLen),writeResponse);
                currLen++;
            }else{
                list = new LinkedList<>();
                msgLen=0;
                currLen=0;
            }
        }
    };

    /**
     * 写数据（不超过20字节）
     * @param msg
     * @param response
     */
    public void write(byte[] msg, BleWriteResponse response){
        UUID serviceuuid = UUID.fromString(ConfigApp.write_characteristic_serviceuuid);
        UUID characteruuid = UUID.fromString(ConfigApp.write_characteristic_characteruuid);

        mClient.write(ConfigApp.current_connected_mac,serviceuuid,characteruuid,msg,response);
    }

    /**
     * 写数据（速度快，用于固件升级）
     * @param msg
     * @param response
     */
    public void writeNoRsp(byte[] msg, BleWriteResponse response){
        UUID serviceuuid = UUID.fromString(ConfigApp.write_characteristic_serviceuuid);
        UUID characteruuid = UUID.fromString(ConfigApp.write_characteristic_characteruuid);

        mClient.writeNoRsp(ConfigApp.current_connected_mac,serviceuuid,characteruuid,msg,response);
    }

    /**
     * 写数据（速度快，用于固件升级）
     * @param msg
     * @param response
     */
    public void writeNoRsp(String msg, BleWriteResponse response){
        UUID serviceuuid = UUID.fromString(ConfigApp.write_characteristic_serviceuuid);
        UUID characteruuid = UUID.fromString(ConfigApp.write_characteristic_characteruuid);
        //注意这边写的时候需要进行转换之后才可以
        byte[] resultMsg=CommonUtil.decodeHex(msg.toCharArray());
        mClient.writeNoRsp(ConfigApp.current_connected_mac,serviceuuid,characteruuid,resultMsg,response);
    }

    /**
     * read characteristic
     * @param response
     */
    public void read(BleReadResponse response){
        UUID serviceuuid = UUID.fromString(ConfigApp.read_characteristic_serviceuuid);
        UUID characteruuid = UUID.fromString(ConfigApp.read_characteristic_characteruuid);
        mClient.read(ConfigApp.current_connected_mac,serviceuuid,characteruuid,response);
    }

    /**
     * 接收蓝牙通知消息
     * @param response
     */
    public void notify(BleNotifyResponse response){
        UUID serviceuuid = UUID.fromString(ConfigApp.read_characteristic_serviceuuid);
        UUID characteruuid = UUID.fromString(ConfigApp.read_characteristic_characteruuid);
        mClient.notify(ConfigApp.current_connected_mac, serviceuuid, characteruuid,response);
    }

    /**
     * 关闭接收蓝牙消息
     * @param response
     */
    public void unNotify(BleUnnotifyResponse response){
        UUID serviceuuid = UUID.fromString(ConfigApp.read_characteristic_serviceuuid);
        UUID characteruuid = UUID.fromString(ConfigApp.read_characteristic_characteruuid);
        mClient.unnotify(ConfigApp.current_connected_mac,serviceuuid,characteruuid,response);
    }

    /**
     * 获取事务ID
     * @return
     */
    public String getTrId(){
        String trid = Integer.toHexString(ConfigApp.TRID);
        if(trid.length()<4){
            trid = "0000".substring(0,4-trid.length())+trid;
        }
        trid = changeToLittle(trid);
        return trid;
    }

    /**
     * 将长度转换成2个字节的16进制字符串
     * @param i
     * @return
     */
    public String lenToHexString(int i){
        String len = Integer.toHexString(i);
        if(len.length()<4){
            len = "0000".substring(0,4-len.length())+len;
        }
        len = changeToLittle(len);
        return len;
    }

    /**
     * 将str不足2个字节的补全
     * @param str
     * @return
     */
    public String completeToTwoByte(String str){
        if(str.length()<4){
            str = "0000".substring(0,4-str.length())+str;
        }
        str = changeToLittle(str);
        return str;
    }

    /**
     * 将str不足4个字节的补全
     * @param str
     * @return
     */
    public String completeToFourByte(String str){
        if(str.length()<8){
            str = "00000000".substring(0,8-str.length())+str;
        }
        str = changeToLittle(str);
        return str;
    }

    /**
     * 转换为小端模式
     * @param str
     * @return
     */
    public String changeToLittle(String str){
        char[] chars = str.toCharArray();
        for (int i = 0; i <chars.length/2 ; i+=2) {
            //第一个字符
            char temp = chars[i];
            chars[i]=chars[chars.length-2-i];
            chars[chars.length-2-i] = temp;
            //第二个字符
            temp = chars[i+1];
            chars[i+1]=chars[chars.length-1-i];
            chars[chars.length-1-i] = temp;
        }
        return String.valueOf(chars);
    }

    /**
     * 构造发送的数据
     * @param msg
     * @return
     */
    public String generateMsg(String fnCode,String msg){
        //事务id
        String trid = getTrId();

        //节点号之后的内容
        String node = ConfigApp.NODEID+ConfigApp.FUCTIONID.get(fnCode)+msg;

        //长度
        String len = lenToHexString(node.length()/2);

        //发送的内容
        String send = trid+changeToLittle(ConfigApp.PROTOCOLID)+len+node;

        //crc
        int i = CRC16Util.calcCrc16(send);
        String crc = completeToTwoByte(Integer.toHexString(i));

        //send+crc
        send +=crc;
        LogUtils.i("TAG","send :"+send);
        return send;
    }

    // 将dp转换为px
    public int dp2px(int value) {
        // 第一个参数为我们待转的数据的单位，此处为 dp（dip）
        // 第二个参数为我们待转的数据的值的大小
        // 第三个参数为此次转换使用的显示量度（Metrics），它提供屏幕显示密度（density）和缩放信息
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }
    //另一种将dp转换为px的方法
    public int dp2px(float value){
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(value*scale + 0.5f);
    }

    public List<byte[]> splitPacketFor16Byte(byte[] data) {
        List<byte[]> dataInfoList = new LinkedList<>();
        if (data != null) {
            int index = 0;
            do {
                byte[] surplusData = new byte[data.length - index];
                byte[] currentData;
                System.arraycopy(data, index, surplusData, 0, data.length - index);
                if (surplusData.length <= 16) {
                    currentData = new byte[surplusData.length];
                    System.arraycopy(surplusData, 0, currentData, 0, surplusData.length);
                    index += surplusData.length;
                } else {
                    currentData = new byte[16];
                    System.arraycopy(data, index, currentData, 0, 16);
                    index += 16;
                }
                dataInfoList.add(currentData);
            } while (index < data.length);
        }
        return dataInfoList;
    }
}
