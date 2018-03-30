package com.sengod.sengod.ui.activity;

import android.view.View;
import android.widget.Button;

import com.sengod.sengod.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParamSettingActivity extends BaseActivity {

    @BindView(R.id.btn_param_setting_next)
    Button btnParamSettingNext;

    @Override
    public void initView() {
        setContentView(R.layout.activity_param_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_param_setting_next)
    public void btnParamSettingNextClick(View view){
        this.finish();
    }
}
