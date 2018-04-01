package com.sengod.sengod.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sengod.sengod.bean.BluetoothDevice;
import com.sengod.sengod.bean.BluetoothDeviceDao;
import com.sengod.sengod.bean.DaoMaster;
import com.sengod.sengod.bean.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by xpy on 2018/1/24.
 */

public class DbManager {
    private final static String dbName = "history_connect";
    private static DbManager mdbManager;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DbManager(Context context){
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context,dbName,null);
    }

    /**
     * 获取单例引用
     * @param context
     * @return
     */
    public static DbManager getInstance(Context context){
        if(mdbManager==null){
            synchronized (DbManager.class){
                if(mdbManager==null){
                    mdbManager = new DbManager(context);
                }
            }
        }
        return mdbManager;
    }

    /**
     * 获取可读数据库
     * @return
     */
    private SQLiteDatabase getReadableDatabase(){
        if(openHelper ==null){
            openHelper = new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     * @return
     */
    private SQLiteDatabase getWritableDatabase(){
        if(openHelper==null){
            openHelper = new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入连接历史记录
     * @param device
     */
    public void insertHistory(BluetoothDevice device){
        if(device == null){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BluetoothDeviceDao bluetoothDeviceDao = daoSession.getBluetoothDeviceDao();
        bluetoothDeviceDao.insert(device);
    }

    /**
     * 删除连接历史记录
     * @param device
     */
    public void deleteHistory(BluetoothDevice device){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BluetoothDeviceDao dao = daoSession.getBluetoothDeviceDao();
        dao.delete(device);
    }

    /**
     * 查询历史连接记录
     * @return
     */
    public List<BluetoothDevice> queryDeviceList(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BluetoothDeviceDao bluetoothDeviceDao = daoSession.getBluetoothDeviceDao();
        QueryBuilder<BluetoothDevice> qb = bluetoothDeviceDao.queryBuilder();
        List<BluetoothDevice> list = qb.list();
        return list;
    }

    /**
     * 判断蓝牙是否已经存在于数据库中
     * @param mac
     * @return
     */
    public boolean isExist(String mac){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BluetoothDeviceDao bluetoothDeviceDao = daoSession.getBluetoothDeviceDao();
        QueryBuilder<BluetoothDevice> qb = bluetoothDeviceDao.queryBuilder();
        long count = qb.where(BluetoothDeviceDao.Properties.Mac.eq(mac)).count();
        if(count==0){
            return false;
        }
        return true;
    }

    /**
     * 查询数据库表中的总记录数
     * @return
     */
    public long queryCount(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BluetoothDeviceDao bluetoothDeviceDao = daoSession.getBluetoothDeviceDao();
        return bluetoothDeviceDao.count();
    }

    /**
     * 删除数据库表中的最后一条记录
     */
    public void deleteLastIndex(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BluetoothDeviceDao bluetoothDeviceDao = daoSession.getBluetoothDeviceDao();
        daoSession.getDatabase().execSQL("delete from "+bluetoothDeviceDao.getTablename()+" where Id like (select top 1 Id from "+bluetoothDeviceDao.getTablename()+" order by Id desc)");
    }


}
