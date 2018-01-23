package com.sengod.sengod.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.sengod.sengod.MyApplication;
import com.sengod.sengod.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class ChooseRobotActivity extends AppCompatActivity {
    @BindView(R.id.sw_bluetooth)
    Switch swBluetooth;
    @BindView(R.id.lv_search_result)
    ListView lvSearchResult;

    private BluetoothClient mClient;
    private ArrayAdapter<Map<String,String>> adapter;
    private List<Map<String,String>> searchResult = new ArrayList<>();

    /**
     * 蓝牙开启状态监听器
     */
    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            swBluetooth.setChecked(openOrClosed);
            discover();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_robot);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, searchResult);
        lvSearchResult.setAdapter(adapter);

        mClient = MyApplication.mBluetoothClient;
        //注册蓝牙开启状态监听器
        mClient.registerBluetoothStateListener(mBluetoothStateListener);
        //蓝牙关闭就开启蓝牙
        if (mClient.isBluetoothOpened()) {
            swBluetooth.setChecked(true);
        } else {
            mClient.openBluetooth();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册蓝牙开启状态监听器
        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
    }

    @OnCheckedChanged(R.id.sw_bluetooth)
    public void swBluetoothCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            if (!mClient.isBluetoothOpened()) {
                mClient.openBluetooth();
            }
        } else {
            if (mClient.isBluetoothOpened()) {
                mClient.closeBluetooth();
            }
        }
    }

    private void discover() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Map<String,String> map = new HashMap<String, String>();
                map.put(device.getAddress(),device.getName());
                searchResult.add(map);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSearchStopped() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }

}
