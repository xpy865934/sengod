package com.sengod.sengod.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.sengod.sengod.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoRectifyingOperationActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_rectifying_operation);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_back)
    public void imgBackClick(View view){
         this.finish();
    }
}
