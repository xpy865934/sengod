package com.sengod.sengod.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class DataRecordActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_factory_time)
    TextView tvFactoryTime;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_charging_times)
    TextView tvChargingTimes;
    @BindView(R.id.tv_use_time)
    TextView tvUseTime;
    @BindView(R.id.tv_walk_time)
    TextView tvWalkTime;
    @BindView(R.id.tv_carry_time)
    TextView tvCarryTime;
    @BindView(R.id.tv_lift_time)
    TextView tvLiftTime;

    BleNotifyResponse bleNotifyResponse = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            AnalyticalDataUtil.analyData(DataRecordActivity.this, value, new AnalyticalDataUtilsInterface() {
                @Override
                public void success(String[] data) {
                    //出厂时间
                    tvFactoryTime.setText(data[0]);
                    //版本号
                    tvVersion.setText(data[1]);
                    //充电总次数
                    tvChargingTimes.setText(data[2]);
                    //使用时间
                    tvUseTime.setText(data[3]);
                    //行走时间
                    tvWalkTime.setText(data[4]);
                    //载物时间
                    tvCarryTime.setText(data[5]);
                    //顶升次数
                    tvLiftTime.setText(data[6]);
                }

                @Override
                public void fail(String errorMsg) {
                    Toast.makeText(DataRecordActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResponse(int code) {

        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_data_record);
        ButterKnife.bind(this);

        setBleNotifyResponse(bleNotifyResponse);

        //注册监听蓝牙连接状态
        registerConnectStatusListener(DataRecordActivity.this);

        notify(bleNotifyResponse);

        //读取动态数据
        String msg = generateMsg("readData", "");

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
