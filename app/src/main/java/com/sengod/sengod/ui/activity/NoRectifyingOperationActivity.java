package com.sengod.sengod.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.sengod.sengod.AnalyticalDataUtilsInterface;
import com.sengod.sengod.ConfigApp;
import com.sengod.sengod.R;
import com.sengod.sengod.utils.AnalyticalDataUtil;
import com.sengod.sengod.utils.LogUtils;

import java.util.Arrays;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoRectifyingOperationActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.btn_forward)
    Button btnForward;
    @BindView(R.id.edi_distance)
    EditText ediDistance;
    @BindView(R.id.edi_speed)
    EditText ediSpeed;

    //地址二维码
    @BindView(R.id.btn_address_qrcode)
    Button btnAddressQrcode;
    @BindView(R.id.edi_address_x)
    EditText ediAddressX;
    @BindView(R.id.edi_address_y)
    EditText ediAddressY;
    @BindView(R.id.edi_address_c_x)
    EditText ediAddressCX;
    @BindView(R.id.edi_address_c_y)
    EditText ediAddressCY;
    @BindView(R.id.edi_address_angle)
    EditText ediAddressAngle;

    //货架二维码
    @BindView(R.id.edi_shelf_id)
    EditText ediShelfId;
    @BindView(R.id.edi_shelf_c_x)
    EditText ediShelfCX;
    @BindView(R.id.edi_shelf_c_y)
    EditText ediShelfCY;
    @BindView(R.id.edi_shelf_angle)
    EditText ediShelfAngle;

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_turn_left)
    Button btnTurnLeft;
    @BindView(R.id.btn_turn_right)
    Button btnTurnRight;
    @BindView(R.id.btn_sync_inverse)
    Button btnSyncInverse;
    @BindView(R.id.btn_sync_up)
    Button btnSyncUp;
    @BindView(R.id.btn_up)
    Button btnUp;
    @BindView(R.id.btn_down)
    Button btnDown;

    @BindView(R.id.btn_shelf_qrcode)
    Button btnShelfQrcode;

    BleNotifyResponse bleNotifyResponse = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            LogUtils.i("TAG","getData:"+ Arrays.toString(value));
            AnalyticalDataUtil.analyData(NoRectifyingOperationActivity.this, value, new AnalyticalDataUtilsInterface() {
                @Override
                public void success(String[] data) {
                    if(data.length==5){
                        //地址二维码数据
                        ediAddressX.setText(data[0]);
                        ediAddressY.setText(data[1]);
                        ediAddressCX.setText(data[2]);
                        ediAddressCY.setText(data[3]);
                        ediAddressAngle.setText(data[4]);
                    }else if(data.length==4){
                        ediShelfId.setText(data[0]);
                        ediShelfCX.setText(data[1]);
                        ediShelfCY.setText(data[2]);
                        ediShelfAngle.setText(data[3]);
                    }
                }

                @Override
                public void fail(String errorMsg) {
                    Toast.makeText(NoRectifyingOperationActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResponse(int code) {

        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_no_rectifying_operation);
        ButterKnife.bind(this);

        setBleNotifyResponse(bleNotifyResponse);

        //注册监听蓝牙连接状态
        registerConnectStatusListener(NoRectifyingOperationActivity.this);

        notify(bleNotifyResponse);
    }

    @OnClick(R.id.img_back)
    public void imgBackClick(View view) {
        this.finish();
    }

    /**
     * 前进
     *
     * @param view
     */
    @OnClick(R.id.btn_forward)
    public void btnForwardClick(View view) {

        String distance = ediDistance.getText().toString();

        String speed = ediSpeed.getText().toString();

        //距离
        if(distance==null||"".equals(distance)){
            distance = completeToFourByte(Integer.toHexString(ConfigApp.distance));
        }else{
            distance = completeToFourByte(Integer.toHexString(Integer.valueOf(distance)));
        }
        //速度
        if(speed==null||"".equals(speed)){
            speed = completeToTwoByte(Integer.toHexString(ConfigApp.speed));
        }else{
            speed = completeToTwoByte(Integer.toHexString(Integer.valueOf(speed)));
        }
        String msg = generateMsg("moveForward", distance + speed);
        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    /**
     * 后退
     *
     * @param view
     */
    @OnClick(R.id.btn_back)
    public void btnBackClick(View view) {

        String distance = ediDistance.getText().toString();

        String speed = ediSpeed.getText().toString();

        //距离
        if(distance==null||"".equals(distance)){
            distance = completeToFourByte(Integer.toHexString(ConfigApp.distance));
        }else{
            distance = completeToFourByte(Integer.toHexString(Integer.valueOf(distance)));
        }
        //速度
        if(speed==null||"".equals(speed)){
            speed = completeToTwoByte(Integer.toHexString(ConfigApp.speed));
        }else{
            speed = completeToTwoByte(Integer.toHexString(Integer.valueOf(speed)));
        }
        String msg = generateMsg("moveBack", distance + speed);
        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    /**
     * 距离输入框
     *
     * @param view
     */
    @OnClick(R.id.edi_distance)
    public void ediDistanceClick(View view) {

    }

    /**
     * 左转90
     * 1571  两个字节 0623
     * @param view
     */
    @OnClick(R.id.btn_turn_left)
    public void btnTurnLeftClick(View view) {
        //旋转角度
        String mangle = changeToLittle("0623");
        String msg = generateMsg("turn", mangle);

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    /**
     * 右转90
     *
     * @param view
     */
    @OnClick(R.id.btn_turn_right)
    public void btnTurnRightClick(View view) {
        //旋转角度
        String mangle = changeToLittle("F9DD");
        String msg = generateMsg("turn", mangle);

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    /**
     * 同步逆时90
     * @param view
     */
    @OnClick(R.id.btn_sync_inverse)
    public void btnSyncInverseClick(View view) {
        //旋转角度
        String mangle = changeToLittle("0623");
        String msg = generateMsg("relativeTurn", mangle);

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    /**
     * 同步顺时90
     * @param view
     */
    @OnClick(R.id.btn_sync_up)
    public void btnSyncUpClick(View view){
        //旋转角度
        String mangle = changeToLittle("F9DD");
        String msg = generateMsg("relativeTurn", mangle);

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    /**
     * 上升
     * @param view
     */
    @OnClick(R.id.btn_up)
    public void btnUpClick(View view){
        //上升距离
        String upDistance = completeToTwoByte(Integer.toHexString(ConfigApp.upDistance));

        String msg = generateMsg("upOrDown", upDistance);

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    /**
     * 下降
     * @param view
     */
    @OnClick(R.id.btn_down)
    public void btnDownClick(View view){
        //下降距离
        String temp =Integer.toHexString(Integer.valueOf("-"+ConfigApp.upDistance));
        temp =temp.substring(temp.length()-4);
        String upDistance = completeToTwoByte(temp);

        String msg = generateMsg("upOrDown", upDistance);

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    /**
     * 读取地址二维码
     * @param view
     */
    @OnClick(R.id.btn_address_qrcode)
    public void btnAddressQrcode(View view){
        String msg = generateMsg("readAddressQrCode", "");

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    /**
     * 读取货架二维码
     * @param view
     */
    @OnClick(R.id.btn_shelf_qrcode)
    public void btnShelfQrcode(View view){
        String msg = generateMsg("readShelfQrCode", "");

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关闭监听
        unRegisterConnectStatusListener();

        unNotify(new BleUnnotifyResponse() {
            @Override
            public void onResponse(int code) {

            }
        });
    }
}
