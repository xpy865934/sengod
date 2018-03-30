package com.sengod.sengod.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.sengod.sengod.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends BaseActivity {
    @BindView(R.id.btn_start)
    Button btnStart;

    @Override
    public void initView() {
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start)
    public void btnStartClick(View view){
        Intent it = new Intent(StartActivity.this,ChooseRobotActivity.class);
        startActivity(it);
    }
}
