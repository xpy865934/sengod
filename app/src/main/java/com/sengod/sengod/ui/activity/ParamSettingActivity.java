package com.sengod.sengod.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sengod.sengod.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParamSettingActivity extends AppCompatActivity {

    @BindView(R.id.btn_param_setting_next)
    Button btnParamSettingNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_param_setting_next)
    public void btnParamSettingNextClick(View view){
        this.finish();
    }
}
