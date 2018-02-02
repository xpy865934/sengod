package com.sengod.sengod.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sengod.sengod.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstConnectActivity extends AppCompatActivity {

    @BindView(R.id.btn_first_connect_next)
    Button btnFirstConnectNext;
    @BindView(R.id.img_back)
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_connect);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_first_connect_next)
    public void btnFirstConnectNextClick(View view){
        Intent it = new Intent(FirstConnectActivity.this,MainActivity.class);
        startActivity(it);
        this.finish();
    }

    @OnClick(R.id.img_back)
    public void imgBackClick(View view){
        Intent it = new Intent(FirstConnectActivity.this,StartActivity.class);
        startActivity(it);
        this.finish();
    }
}
