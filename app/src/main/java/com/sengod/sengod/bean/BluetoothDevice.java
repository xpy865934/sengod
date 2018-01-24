package com.sengod.sengod.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Lenovo on 2018/1/24.
 */

@Entity
public class BluetoothDevice {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String mac;

    private String name;

    @Generated(hash = 33228802)
    public BluetoothDevice(Long id, String mac, String name) {
        this.id = id;
        this.mac = mac;
        this.name = name;
    }

    @Generated(hash = 484399680)
    public BluetoothDevice() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
