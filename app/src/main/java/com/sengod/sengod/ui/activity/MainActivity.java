package com.sengod.sengod.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sengod.sengod.ConfigApp;
import com.sengod.sengod.R;
import com.sengod.sengod.utils.AtyContainer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_no_rectify)
    Button btnNoRectify;
    @BindView(R.id.btn_rectify)
    Button btnRectify;
    @BindView(R.id.btn_dynamic_test)
    Button btnDynamicTest;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.tv_param_setting)
    TextView tvParamSetting;
    // 返回键监听点击时间
    private long clickTime = 0;

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //注册监听蓝牙连接状态
        registerConnectStatusListener(MainActivity.this);
    }



    //时间监听
    @OnClick(R.id.btn_no_rectify)
    public void btnNoRectifyClick(View view){
        Intent it = new Intent(MainActivity.this,NoRectifyingOperationActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.btn_rectify)
    public void btnRectifyClick(View view){
        Intent it = new Intent(MainActivity.this,RectifyingOperationAction.class);
        startActivity(it);
//        Toast.makeText(MainActivity.this,"正在完善中，请耐心等待!",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_dynamic_test)
    public void btnDynamicTestClick(View view){
        Intent it = new Intent(MainActivity.this,DynamicTestActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.btn_record)
    public void btnRecordClick(View view){
        Intent it = new Intent(MainActivity.this,DataRecordActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.tv_param_setting)
    public void tvParamSettingClick(View view){
        Intent it = new Intent(MainActivity.this,ParamSettingActivity.class);
        startActivity(it);
    }

    // 返回键点两次退出
    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(MainActivity.this, R.string.exit, Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
//            this.finish();
            AtyContainer.getInstance().finishAllActivity();
        }
    }

    // 返回键、音量键可以监测到
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            // 返回键
            case KeyEvent.KEYCODE_BACK:
                exit();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭监听
        unRegisterConnectStatusListener();
        mClient.disconnect(ConfigApp.current_connected_mac);
    }
}
