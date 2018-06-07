package com.sengod.sengod.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.sengod.sengod.ConfigApp;
import com.sengod.sengod.MyApplication;
import com.sengod.sengod.R;
import com.sengod.sengod.bean.BluetoothDevice;
import com.sengod.sengod.ui.dialog.LoadingDialog;
import com.sengod.sengod.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.sengod.sengod.utils.CommonUtil.judgeDeviceRepet;

public class ChooseRobotActivity extends BaseActivity {
    @BindView(R.id.sw_bluetooth)
    Switch swBluetooth;
    @BindView(R.id.lv_search_result)
    ListView lvSearchResult;   //搜索结果ListView
    @BindView(R.id.lv_connect_history)
    SwipeMenuListView lvConnectHistory;  //连接历史ListView
    @BindView(R.id.img_back)
    ImageView imgBack;

    private BluetoothClient mClient;
    private SimpleAdapter adapter;
    private SimpleAdapter hisAdapter;
    private List<Map<String,String>> searchResult = new ArrayList<>();
    private List<Map<String,String>> hisResult = new ArrayList<>();
    private Dialog loadingDialog;

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

    public void initView() {
        setContentView(R.layout.activity_choose_robot);
        ButterKnife.bind(this);//注解

        loadingDialog = LoadingDialog.getDialog(ChooseRobotActivity.this,getString(R.string.first_connect));
        adapter = new SimpleAdapter(this,searchResult,R.layout.adapter__list,new String[]{"device_name","device_mac"},new int[]{R.id.tv_device_name,R.id.tv_device_mac} );
        lvSearchResult.setAdapter(adapter);//适配器

        List<BluetoothDevice> bluetoothDevicesList = dbManager.queryDeviceList();
        for (BluetoothDevice bluetoothDevice:bluetoothDevicesList
             ) {
            Map<String,String> map = new HashMap<>();
            map.put("id", String.valueOf(bluetoothDevice.getId()));
            map.put("device_name",bluetoothDevice.getName());
            map.put("device_mac",bluetoothDevice.getMac());
            hisResult.add(map);
        }

        hisAdapter = new SimpleAdapter(this,hisResult,R.layout.adapter__list,new String[]{"device_name","device_mac"},new int[]{R.id.tv_device_name,R.id.tv_device_mac} );
        lvConnectHistory.setAdapter(hisAdapter);

        mClient = MyApplication.getBluetoothClient();

        //注册蓝牙开启状态监听器
        mClient.registerBluetoothStateListener(mBluetoothStateListener);

        //蓝牙关闭就开启蓝牙
        if (mClient.isBluetoothOpened()) {
            swBluetooth.setChecked(true);
            discover();
        } else {
            mClient.openBluetooth();
        }

        //生成ListView侧滑菜单项
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建删除菜单项
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F,0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.delete);
                menu.addMenuItem(deleteItem);

            }
        };
        lvConnectHistory.setMenuCreator(creator);

        //连接历史设置点击监听器
        lvConnectHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String device_name = hisResult.get(i).get("device_name");
                String device_mac = hisResult.get(i).get("device_mac");
                connectDevice(device_name,device_mac);
            }
        });

        lvConnectHistory.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        BluetoothDevice bluetoothDevice = new BluetoothDevice();
                        bluetoothDevice.setId(Long.valueOf(hisResult.get(position).get("id")));
                        bluetoothDevice.setName(hisResult.get(position).get("device_name"));
                        bluetoothDevice.setMac(hisResult.get(position).get("device_mac"));
                        dbManager.deleteHistory(bluetoothDevice);
                        hisResult.remove(position);
                        hisAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });



        //ListView点击连接
        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mClient.stopSearch();
                String device_name = searchResult.get(i).get("device_name");
                String device_mac = searchResult.get(i).get("device_mac");
                connectDevice(device_name,device_mac);
            }
        });
    }

    /**
     * 连接设备
     * @param deviceName
     * @param deviceMac
     */
    public void connectDevice(final String deviceName, final String deviceMac){
        //配置连接参数
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                .build();

        loadingDialog.show();
        mClient.connect(deviceMac, options,new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                loadingDialog.dismiss();
                if(code == Constants.REQUEST_SUCCESS){
                    ConfigApp.current_connected_mac = deviceMac;
                    ConfigApp.current_connected_name = deviceName;

                    //判断数据库中是否已经有设备，没有的话，判断是否数据已经大于3条，如果是，删除最后一条
                    if(!dbManager.isExist(deviceMac)) {
                        if(dbManager.queryCount()>3){
                            dbManager.deleteLastIndex();
                        }
                        BluetoothDevice bluetoothDevice = new BluetoothDevice();
                        bluetoothDevice.setMac(deviceMac);
                        bluetoothDevice.setName(deviceName);
                        dbManager.insertHistory(bluetoothDevice);
                    }
                    //查看service和character UUID
                    LogUtils.i("TAG",data.toString());
                    Intent intent = new Intent(ChooseRobotActivity.this,MainActivity.class);
                    startActivity(intent);
                    ChooseRobotActivity.this.finish();
                }else if(code == Constants.REQUEST_FAILED){
                    Toast.makeText(ChooseRobotActivity.this,"连接失败，请重试！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册蓝牙开启状态监听器
        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
    }

    /**
     * 按钮点击开关蓝牙
     * @param compoundButton
     * @param b
     */
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

    /**
     * 搜索蓝牙
     */
    private void discover() {
        //配置扫描参数
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
                for (Map<String,String> map:hisResult
                     ) {
                    if(device.getAddress().equals(map.get("device_mac"))){
                        return;
                    }
                }
                
                //发现设备即将设备加入到适配器中
                Map<String,String> map = new HashMap();
                map.put("device_name",device.getName());
                map.put("device_mac",device.getAddress());
                if(judgeDeviceRepet(searchResult,map) != 1){
                    searchResult.add(map);
                    adapter.notifyDataSetChanged();
                }
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

    @OnClick(R.id.img_back)
    public void imgBackClick(View view){
        this.finish();
    }

}
