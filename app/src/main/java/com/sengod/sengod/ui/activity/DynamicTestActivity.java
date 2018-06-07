package com.sengod.sengod.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.sengod.sengod.AnalyticalDataUtilsInterface;
import com.sengod.sengod.R;
import com.sengod.sengod.utils.AnalyticalDataUtil;
import com.sengod.sengod.utils.LogUtils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicTestActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;

    //压力波开关
    @BindView(R.id.img_pressure_switch1)
    ImageView imgPressureSwitch1;
    @BindView(R.id.img_pressure_switch2)
    ImageView imgPressureSwitch2;

    //急停
    @BindView(R.id.img_scram1)
    ImageView imgScram1;
    @BindView(R.id.img_scram2)
    ImageView imgScram2;

    //充电插座
    @BindView(R.id.img_power_socket1)
    ImageView imgPowerSocket1;
    @BindView(R.id.img_power_socket2)
    ImageView imgPowerSocket2;
    @BindView(R.id.img_power_socket3)
    ImageView imgPowerSocket3;

    //电池电压
    @BindView(R.id.tv_battery_voltage)
    TextView tvBatteryVoltage;

    //电池温度
    @BindView(R.id.tv_battery_temperature)
    TextView tvBatteryTemperature;

    //障碍物距离监测
    @BindView(R.id.tv_barrier_distance)
    TextView tvBarrierDistance;

    //行走伺服
    @BindView(R.id.tv_walking_servo1_state)
    TextView tvWalkingServo1State;
    @BindView(R.id.tv_walking_servo2_state)
    TextView tvWalkingServo2State;
    @BindView(R.id.tv_walking_current1)
    TextView tvWalkingCurrent1;
    @BindView(R.id.tv_walking_current2)
    TextView tvWalkingCurrent2;

    //顶升伺服
    @BindView(R.id.tv_jacking_state)
    TextView tvJackingState;
    @BindView(R.id.tv_jacking_current)
    TextView tvJackingCurrent;

    //旋转伺服
    @BindView(R.id.tv_rotate_state)
    TextView tvRotateState;
    @BindView(R.id.tv_rotate_current)
    TextView tvRotateCurrent;

    BleNotifyResponse bleNotifyResponse = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            AnalyticalDataUtil.analyData(DynamicTestActivity.this, value, new AnalyticalDataUtilsInterface() {
                @Override
                public void success(String[] data) {
                    //压力波开关38
                    if ("1".equals(data[0])) {
                        imgPressureSwitch1.setBackgroundResource(R.drawable.online);
                    } else {
                        imgPressureSwitch1.setBackgroundResource(R.drawable.offline);
                    }
                    //压力波开关39
                    if ("1".equals(data[1])) {
                        imgPressureSwitch2.setBackgroundResource(R.drawable.online);
                    } else {
                        imgPressureSwitch2.setBackgroundResource(R.drawable.offline);
                    }

                    //急停
                    if ("1".equals(data[2])) {
                        imgScram1.setBackgroundResource(R.drawable.online);
                        imgScram2.setBackgroundResource(R.drawable.online);
                    } else {
                        imgScram1.setBackgroundResource(R.drawable.offline);
                        imgScram2.setBackgroundResource(R.drawable.offline);
                    }

                    //充电插座
                    if ("1".equals(data[3])) {
                        imgPowerSocket1.setBackgroundResource(R.drawable.online);
                    } else {
                        imgPowerSocket1.setBackgroundResource(R.drawable.offline);
                    }
                    if ("1".equals(data[4])) {
                        imgPowerSocket2.setBackgroundResource(R.drawable.online);
                    } else {
                        imgPowerSocket2.setBackgroundResource(R.drawable.offline);
                    }
                    if ("1".equals(data[5])) {
                        imgPowerSocket3.setBackgroundResource(R.drawable.online);
                    } else {
                        imgPowerSocket3.setBackgroundResource(R.drawable.offline);
                    }
                    //电池电压
                    tvBatteryVoltage.setText(data[6]);
                    //电池温度
                    tvBatteryTemperature.setText(data[7]);
                    //障碍物距离监测
                    tvBarrierDistance.setText(data[8]);
                    //行走伺服
                    switch (data[9]) {
                        case "0":
                            tvWalkingServo1State.setText(getString(R.string.activity_common_init));
                            break;
                        case "1":
                            tvWalkingServo1State.setText(getString(R.string.activity_common_disconnect));
                            break;
                        case "2":
                            tvWalkingServo1State.setText(getString(R.string.activity_common_connecting));
                            break;
                        case "4":
                            tvWalkingServo1State.setText(getString(R.string.activity_common_stop));
                            break;
                        case "5":
                            tvWalkingServo1State.setText(getString(R.string.activity_common_run));
                            break;
                        case "127":
                            tvWalkingServo1State.setText(getString(R.string.activity_common_prerun));
                            break;
                        case "15":
                            tvWalkingServo1State.setText(getString(R.string.activity_common_unknown));
                            break;
                    }
                    switch (data[10]) {
                        case "0":
                            tvWalkingServo2State.setText(getString(R.string.activity_common_init));
                            break;
                        case "1":
                            tvWalkingServo2State.setText(getString(R.string.activity_common_disconnect));
                            break;
                        case "2":
                            tvWalkingServo2State.setText(getString(R.string.activity_common_connecting));
                            break;
                        case "4":
                            tvWalkingServo2State.setText(getString(R.string.activity_common_stop));
                            break;
                        case "5":
                            tvWalkingServo2State.setText(getString(R.string.activity_common_run));
                            break;
                        case "127":
                            tvWalkingServo2State.setText(getString(R.string.activity_common_prerun));
                            break;
                        case "15":
                            tvWalkingServo2State.setText(getString(R.string.activity_common_unknown));
                            break;
                    }

                    //行走伺服工作电流
                    tvWalkingCurrent1.setText(data[11]);
                    tvWalkingCurrent2.setText(data[12]);
                    //顶升伺服
                    switch (data[13]) {
                        case "0":
                            tvJackingState.setText(getString(R.string.activity_common_init));
                            break;
                        case "1":
                            tvJackingState.setText(getString(R.string.activity_common_disconnect));
                            break;
                        case "2":
                            tvJackingState.setText(getString(R.string.activity_common_connecting));
                            break;
                        case "4":
                            tvJackingState.setText(getString(R.string.activity_common_stop));
                            break;
                        case "5":
                            tvJackingState.setText(getString(R.string.activity_common_run));
                            break;
                        case "127":
                            tvJackingState.setText(getString(R.string.activity_common_prerun));
                            break;
                        case "15":
                            tvJackingState.setText(getString(R.string.activity_common_unknown));
                            break;
                    }
                    //顶升伺服工作电流
                    tvJackingCurrent.setText(data[14]);

                    //旋转伺服
                    switch (data[15]) {
                        case "0":
                            tvRotateState.setText(getString(R.string.activity_common_init));
                            break;
                        case "1":
                            tvRotateState.setText(getString(R.string.activity_common_disconnect));
                            break;
                        case "2":
                            tvRotateState.setText(getString(R.string.activity_common_connecting));
                            break;
                        case "4":
                            tvRotateState.setText(getString(R.string.activity_common_stop));
                            break;
                        case "5":
                            tvRotateState.setText(getString(R.string.activity_common_run));
                            break;
                        case "127":
                            tvRotateState.setText(getString(R.string.activity_common_prerun));
                            break;
                        case "15":
                            tvRotateState.setText(getString(R.string.activity_common_unknown));
                            break;
                    }

                    //旋转伺服工作电流
                    tvRotateCurrent.setText(data[16]);
                }

                @Override
                public void fail(String errorMsg) {

                }
            });
        }

        @Override
        public void onResponse(int code) {

        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_dynamic_test);
        ButterKnife.bind(this);

        setBleNotifyResponse(bleNotifyResponse);

        //注册监听蓝牙连接状态
        registerConnectStatusListener(DynamicTestActivity.this);

        notify(bleNotifyResponse);

        //读取动态数据
        String msg = generateMsg("readDynamicData", "");

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    @OnClick(R.id.img_back)
    public void imgBackClick(View view){
        this.finish();
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
