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
import com.sengod.sengod.R;
import com.sengod.sengod.ui.customerview.CircleController;
import com.sengod.sengod.ui.customerview.CircleControllerNoCenter;
import com.sengod.sengod.utils.AnalyticalDataUtil;
import com.sengod.sengod.utils.LogUtils;

import java.util.Arrays;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RectifyingOperationAction extends BaseActivity {
    @BindView(R.id.cc_top_view)
    CircleController cc_top_view;
    @BindView(R.id.ccnc_bottom)
    CircleControllerNoCenter ccnc_bottom;
    @BindView(R.id.img_back)
    ImageView imgBack;
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
    @BindView(R.id.btn_shelf_qrcode)
    Button btnShelfQrcode;
    @BindView(R.id.edi_shelf_id)
    EditText ediShelfId;
    @BindView(R.id.edi_shelf_c_x)
    EditText ediShelfCX;
    @BindView(R.id.edi_shelf_c_y)
    EditText ediShelfCY;
    @BindView(R.id.edi_shelf_angle)
    EditText ediShelfAngle;

    @BindView(R.id.btn_turn_left)
    Button btnTurnLeft;
    @BindView(R.id.btn_turn_right)
    Button btnTurnRight;
    @BindView(R.id.edi_distance)
    EditText ediDistance;
    @BindView(R.id.edi_speed)
    EditText ediSpeed;
    @BindView(R.id.edi_c_p)
    EditText ediCP;
    @BindView(R.id.edi_c_l)
    EditText ediCL;

    BleNotifyResponse bleNotifyResponse = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            LogUtils.i("TAG","getData:"+ Arrays.toString(value));
            AnalyticalDataUtil.analyData(RectifyingOperationAction.this, value, new AnalyticalDataUtilsInterface() {
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
                    Toast.makeText(RectifyingOperationAction.this,errorMsg,Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResponse(int code) {

        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_rectifying_operation_action);
        ButterKnife.bind(this);

        setBleNotifyResponse(bleNotifyResponse);

        //注册监听蓝牙连接状态
        registerConnectStatusListener(RectifyingOperationAction.this);

        notify(bleNotifyResponse);

        cc_top_view.setListener(new CircleController.RegionViewClickListener() {
            @Override
            public void clickLeft() {
                //左进定位
                String mangle = changeToLittle("0C46");
                String msg = generateMsg("absoluteRotate", mangle);

                //发送数据
                write(msg, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        LogUtils.i("TAG", "write response code " + code);
                    }
                });
            }

            @Override
            public void clickTop() {
                //前进定位
                String mangle = changeToLittle("0623");
                String msg = generateMsg("absoluteRotate", mangle);

                //发送数据
                write(msg, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        LogUtils.i("TAG", "write response code " + code);
                    }
                });
            }

            @Override
            public void clickRight() {
                //右进定位
                String mangle = changeToLittle("0000");
                String msg = generateMsg("absoluteRotate", mangle);

                //发送数据
                write(msg, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        LogUtils.i("TAG", "write response code " + code);
                    }
                });
            }

            @Override
            public void clickBottom() {
                //后退定位
                String mangle = changeToLittle("1268");
                String msg = generateMsg("absoluteRotate", mangle);

                //发送数据
                write(msg, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        LogUtils.i("TAG", "write response code " + code);
                    }
                });
            }

            @Override
            public void clickCenter() {

            }
        });

        ccnc_bottom.setListener(new CircleControllerNoCenter.RegionViewClickListener() {
            @Override
            public void clickLeft() {
                //左进定位
                String mangle = changeToLittle("0C46");
                String msg = generateMsg("absoluteRotate", mangle);

                //发送数据
                write(msg, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        LogUtils.i("TAG", "write response code " + code);
                    }
                });
            }

            @Override
            public void clickTop() {
                //前进定位
                String mangle = changeToLittle("0623");
                String msg = generateMsg("absoluteRotate", mangle);

                //发送数据
                write(msg, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        LogUtils.i("TAG", "write response code " + code);
                    }
                });
            }

            @Override
            public void clickRight() {
                //右进定位
                String mangle = changeToLittle("0000");
                String msg = generateMsg("absoluteRotate", mangle);

                //发送数据
                write(msg, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        LogUtils.i("TAG", "write response code " + code);
                    }
                });
            }

            @Override
            public void clickBottom() {
                //后退定位
                String mangle = changeToLittle("1268");
                String msg = generateMsg("absoluteRotate", mangle);

                //发送数据
                write(msg, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        LogUtils.i("TAG", "write response code " + code);
                    }
                });
            }
        });
    }
    @OnClick(R.id.img_back)
    public void imgBackClick(View view) {
        this.finish();
    }

    //地址二维码
    @OnClick(R.id.btn_address_qrcode)
    public void btnAddressQrcodeClick(View view){
        String msg = generateMsg("readAddressQrCode", "");

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });

    }

    //货架二维码
    @OnClick(R.id.btn_shelf_qrcode)
    public void btnShelfQrcodeClick(View view){
        String msg = generateMsg("readShelfQrCode", "");

        //发送数据
        write(msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                LogUtils.i("TAG", "write response code " + code);
            }
        });

    }

    //左转90
    @OnClick(R.id.btn_turn_left)
    public void btnTurnLeftClick(View view){
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

    //右转90
    @OnClick(R.id.btn_turn_right)
    public void btnTurnRightClick(View view){
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
