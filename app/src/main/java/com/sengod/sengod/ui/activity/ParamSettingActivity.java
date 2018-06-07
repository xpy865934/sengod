package com.sengod.sengod.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.sengod.sengod.AnalyticalDataUtilsInterface;
import com.sengod.sengod.ConfigApp;
import com.sengod.sengod.R;
import com.sengod.sengod.utils.AnalyticalDataUtil;
import com.sengod.sengod.utils.LogUtils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParamSettingActivity extends BaseActivity {

    @BindView(R.id.btn_param_setting_confirm)
    Button btnParamSettingConfirm;
    @BindView(R.id.edi_noload_speedup)
    EditText ediNoloadSpeedup;
    @BindView(R.id.edi_noload_speeddown)
    EditText ediNoloadSpeeddown;
    @BindView(R.id.edi_load_speedup)
    EditText ediLoadSpeedup;
    @BindView(R.id.edi_load_speeddown)
    EditText ediLoadSpeeddown;
    @BindView(R.id.edi_lift_dispatch)
    EditText ediLiftDispatch;
    @BindView(R.id.edi_lift_speed)
    EditText ediLiftSpeed;
    @BindView(R.id.edi_speed_down_distance)
    EditText ediSpeedDownDistance;
    @BindView(R.id.edi_speed_down_acc)
    EditText ediSpeedDownAcc;
    @BindView(R.id.edi_rotate_speed)
    EditText ediRotateSpeed;
    @BindView(R.id.edi_wheel_left)
    EditText ediWheelLeft;
    @BindView(R.id.edi_wheel_right)
    EditText ediWheelRight;

    BleNotifyResponse bleNotifyResponse = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            AnalyticalDataUtil.analyData(ParamSettingActivity.this, value, new AnalyticalDataUtilsInterface() {
                @Override
                public void success(String[] data) {
                    //空载加减速
                    ediNoloadSpeedup.setText(data[0]);
                    ediNoloadSpeeddown.setText(data[1]);

                    //带载加减速
                    ediLoadSpeedup.setText(data[2]);
                    ediLoadSpeeddown.setText(data[3]);

                    //顶升调度
                    ediLiftDispatch.setText(data[4]);

                    //顶升速度
                    ediLiftSpeed.setText(data[5]);

                    //减速距离
                    ediSpeedDownDistance.setText(data[6]);

                    //减速加速度
                    ediSpeedDownAcc.setText(data[7]);

                    //旋转速度
                    ediRotateSpeed.setText(data[8]);

                    //左轮直径
                    ediWheelLeft.setText(data[9]);

                    //右轮直径
                    ediWheelRight.setText(data[10]);
                }
                @Override
                public void fail(String errorMsg) {
                    Toast.makeText(ParamSettingActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResponse(int code) {

        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_param_setting);
        ButterKnife.bind(this);

        setBleNotifyResponse(bleNotifyResponse);

        //注册监听蓝牙连接状态
        registerConnectStatusListener(ParamSettingActivity.this);

        notify(bleNotifyResponse);

        //读取参数设置
        String msg = generateMsg("readParam", "");

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    @OnClick(R.id.btn_param_setting_confirm)
    public void btnParamSettingConfirmClick(View view){

        String noLoadSpeedUp = ediNoloadSpeedup.getText().toString();
        String noLoadSpeedDown = ediNoloadSpeeddown.getText().toString();
        String loadSpeedUp = ediLoadSpeedup.getText().toString();
        String loadSpeedDown = ediLoadSpeeddown.getText().toString();
        String liftDispatch = ediLiftDispatch.getText().toString();
        String liftSpeed = ediLiftSpeed.getText().toString();
        String speedDownDistance = ediSpeedDownDistance.getText().toString();
        String speedDownAcc = ediSpeedDownAcc.getText().toString();
        String rotateSpeed = ediRotateSpeed.getText().toString();
        String wheelLeft = ediWheelLeft.getText().toString();
        String wheelRight = ediWheelRight.getText().toString();

        //空载加速度
        if(noLoadSpeedUp==null||"".equals(noLoadSpeedUp)){
            noLoadSpeedUp = completeToTwoByte(Integer.toHexString(ConfigApp.noLoadSpeedUp));
        }else{
            noLoadSpeedUp = completeToTwoByte(Integer.toHexString(Integer.valueOf(noLoadSpeedUp)));
        }

        //空载减速度
        if(noLoadSpeedDown==null||"".equals(noLoadSpeedDown)){
            noLoadSpeedDown = completeToTwoByte(Integer.toHexString(ConfigApp.noLoadSpeedDown));
        }else{
            noLoadSpeedDown = completeToTwoByte(Integer.toHexString(Integer.valueOf(noLoadSpeedDown)));
        }

        //带载加速度
        if(loadSpeedUp==null||"".equals(loadSpeedUp)){
            loadSpeedUp = completeToTwoByte(Integer.toHexString(ConfigApp.loadSpeedUp));
        }else{
            loadSpeedUp = completeToTwoByte(Integer.toHexString(Integer.valueOf(loadSpeedUp)));
        }

        //带载减速度
        if(loadSpeedDown==null||"".equals(loadSpeedDown)){
            loadSpeedDown = completeToTwoByte(Integer.toHexString(ConfigApp.loadSpeedDown));
        }else{
            loadSpeedDown = completeToTwoByte(Integer.toHexString(Integer.valueOf(loadSpeedDown)));
        }

        //顶升调度
        if(liftDispatch==null||"".equals(liftDispatch)){
            liftDispatch = completeToTwoByte(Integer.toHexString(ConfigApp.liftDispatch));
        }else{
            liftDispatch = completeToTwoByte(Integer.toHexString(Integer.valueOf(liftDispatch)));
        }

        //顶升速度
        if(liftSpeed==null||"".equals(liftSpeed)){
            liftSpeed = completeToTwoByte(Integer.toHexString(ConfigApp.liftSpeed));
        }else{
            liftSpeed = completeToTwoByte(Integer.toHexString(Integer.valueOf(liftSpeed)));
        }
        //减速距离
        if(speedDownDistance==null||"".equals(speedDownDistance)){
            speedDownDistance = completeToTwoByte(Integer.toHexString(ConfigApp.speedDownDistance));
        }else{
            speedDownDistance = completeToTwoByte(Integer.toHexString(Integer.valueOf(speedDownDistance)));
        }
        //减速加速度
        if(speedDownAcc==null||"".equals(speedDownAcc)){
            speedDownAcc = completeToTwoByte(Integer.toHexString(ConfigApp.speedDownAcc));
        }else{
            speedDownAcc = completeToTwoByte(Integer.toHexString(Integer.valueOf(speedDownAcc)));
        }
        //旋转速度
        if(rotateSpeed==null||"".equals(rotateSpeed)){
            rotateSpeed = completeToTwoByte(Integer.toHexString(ConfigApp.rotateSpeed));
        }else{
            rotateSpeed = completeToTwoByte(Integer.toHexString(Integer.valueOf(rotateSpeed)));
        }

        //旋转加速度
        String rotateSpeedAcc="0";
        if(rotateSpeedAcc==null||"".equals(rotateSpeedAcc)){
            rotateSpeedAcc = completeToTwoByte(Integer.toHexString(ConfigApp.rotateSpeed));
        }else{
            rotateSpeedAcc = completeToTwoByte(Integer.toHexString(Integer.valueOf(rotateSpeedAcc)));
        }
        //左轮直径
        if(wheelLeft==null||"".equals(wheelLeft)){
            wheelLeft = completeToTwoByte(Integer.toHexString(ConfigApp.wheelLeft));
        }else{
            wheelLeft = completeToTwoByte(Integer.toHexString(Integer.valueOf(wheelLeft)));
        }
        //右轮直径
        if(wheelRight==null||"".equals(wheelRight)){
            wheelRight = completeToTwoByte(Integer.toHexString(ConfigApp.wheelRight));
        }else{
            wheelRight = completeToTwoByte(Integer.toHexString(Integer.valueOf(wheelRight)));
        }

        //左右轮间距
        String wheelSpace="10";
        if(wheelSpace==null||"".equals(wheelSpace)){
            wheelSpace = completeToTwoByte(Integer.toHexString(ConfigApp.wheelRight));
        }else{
            wheelSpace = completeToTwoByte(Integer.toHexString(Integer.valueOf(wheelSpace)));
        }

        final String msg = generateMsg("writeParam", noLoadSpeedUp+noLoadSpeedDown+loadSpeedUp+loadSpeedDown+liftDispatch+liftSpeed+speedDownDistance+speedDownAcc+rotateSpeed+rotateSpeedAcc+wheelLeft+wheelRight+wheelSpace);

        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        });

//        Handler mHandler = new Handler();
//        mHandler.post(new Runnable(){
//            @Override
//            public void run() {
//                //发送数据
//                Integer len = msg.length()/2;
//                String temp="";
//                for (int i=0;i<len;i+=20){
//                    if(i+20>=len){
//                        temp = msg.substring(i,len);
//                    }else {
//                        temp = msg.substring(i,i+20);
//                    }
//                    LogUtils.i("TAG","SUBSend:"+temp);
//                    writeNoRsp(temp, new BleWriteResponse() {
//                        @Override
//                        public void onResponse(int code) {
//                            LogUtils.i("TAG", "write response code " + code);
//                        }
//                    });
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

//        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关闭监听
        unRegisterConnectStatusListener();
        //关闭通知
        unNotify(new BleUnnotifyResponse() {
            @Override
            public void onResponse(int code) {

            }
        });
    }
}
