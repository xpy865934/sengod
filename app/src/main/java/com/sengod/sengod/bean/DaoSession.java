package com.sengod.sengod.bean;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.sengod.sengod.bean.BluetoothDevice;

import com.sengod.sengod.bean.BluetoothDeviceDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bluetoothDeviceDaoConfig;

    private final BluetoothDeviceDao bluetoothDeviceDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bluetoothDeviceDaoConfig = daoConfigMap.get(BluetoothDeviceDao.class).clone();
        bluetoothDeviceDaoConfig.initIdentityScope(type);

        bluetoothDeviceDao = new BluetoothDeviceDao(bluetoothDeviceDaoConfig, this);

        registerDao(BluetoothDevice.class, bluetoothDeviceDao);
    }
    
    public void clear() {
        bluetoothDeviceDaoConfig.clearIdentityScope();
    }

    public BluetoothDeviceDao getBluetoothDeviceDao() {
        return bluetoothDeviceDao;
    }

}