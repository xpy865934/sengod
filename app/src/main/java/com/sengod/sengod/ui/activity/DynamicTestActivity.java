package com.sengod.sengod.ui.activity;

import android.view.View;
import android.widget.ImageView;

import com.sengod.sengod.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicTestActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;

    @Override
    public void initView() {
        setContentView(R.layout.activity_dynamic_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_back)
    public void imgBackClick(View view){
        this.finish();
    }
}
