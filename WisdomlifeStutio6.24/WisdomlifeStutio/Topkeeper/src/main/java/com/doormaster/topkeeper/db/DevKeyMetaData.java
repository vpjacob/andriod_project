package com.doormaster.topkeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.doormaster.topkeeper.bean.DevKeyBean;
import com.doormaster.topkeeper.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频门禁
 * Created by Liukebing on 2017/4/6.
 */

public class DevKeyMetaData {
    private DevKeyMetaData(){}
    public static abstract class DevKeyColums implements BaseColumns {

        public static final String TABLE_NAME = "dev_list";

        public static final String USERNAME = "username";
        //    public static final String dev_id = "_id";
        public static final String DEV_NAME = "dev_name";
        //    public static final String COLUMN_DEV_MAC = "dev_mac";
        public static final String COMMUNITY_CODE = "community_code";
        //dbname_company，community_code，community，building，room_code，room_name，room_id，start_datetime，end_datetime
        public static final String DEV_SN = "dev_sn";
        //    public static final String COLUMN_EVENT_TIME = "event_time";
//    public static final String COLUMN_ACTION_TIME = "action_time";
//    public static final String COLUMN_OP_TIME = "op_time";
//    public static final String COLUMN_OP_RET = "op_ret";
//    public static final String COLUMN_OP_USER = "op_user";
        public static final String DEV_VOIP_ACCOUNT = "dev_voip_account";

        public static final String ORDER_NUM = "order_num";
        public static final String DEV_TYPE = "dev_type";

    }

    private static DBHelp dbHelp;

    public DevKeyMetaData(Context context){
        dbHelp = DBHelp.getInstance(context);
    }

    /**
     * 方法名： addRoomList <br>
     * 方法描述：TODO(添加门禁列表)<br>
     * @param dev 设备
     */
    public void addDevKeyList(DevKeyBean dev) {
        SQLiteDatabase database = dbHelp.getWritableDatabase();

        if (database.isOpen())
        {
            String queryClause = "select * from " + DevKeyColums.TABLE_NAME + " where " + DevKeyColums.USERNAME + "=? and " + DevKeyColums.DEV_SN + "=?";
            Cursor c = database.rawQuery(queryClause, new String[]{dev.getUsername(), dev.getDev_sn()});
            ContentValues values;
            if (c.getCount() == 0)
            {
                values = getDevKeyContentValues(dev);
                long ret = database.insert(DevKeyColums.TABLE_NAME, null, values);
                LogUtils.d("开始保存门禁数据:是否成功？ ret = " + ret + " 保存的设备是：" + dev.toString());
            }
            else
            {
                values = getDevKeyContentValues2(dev);
                String whereClause = DevKeyColums.USERNAME + "=? and " + DevKeyColums.DEV_SN + "=?";
                int ret = database.update(DevKeyColums.TABLE_NAME, values, whereClause, new String[]{dev.getUsername(), dev.getDev_sn()});
                LogUtils.d("开始保存门禁数据:更新是否成功？ ret = " + ret + " 更新的设备是：" + dev.toString());
            }
            c.close();
        }
    }

    //获取DevKeyBean的ContentValues
    private ContentValues getDevKeyContentValues(DevKeyBean device)
    {
        ContentValues values= new ContentValues();
        values.put(DevKeyColums.USERNAME, device.getUsername());
        values.put(DevKeyColums.COMMUNITY_CODE, device.getCommunity_code());
        values.put(DevKeyColums.DEV_NAME, device.getDev_name());
        values.put(DevKeyColums.DEV_SN, device.getDev_sn());
        values.put(DevKeyColums.DEV_VOIP_ACCOUNT, device.getDev_voip_account());
        values.put(DevKeyColums.DEV_TYPE, device.getDev_type());
        values.put(DevKeyColums.ORDER_NUM, device.getOrderNum());
        return values;
    }
    private ContentValues getDevKeyContentValues2(DevKeyBean device)
    {
        ContentValues values= new ContentValues();
//        values.put(DevKeyColums.USERNAME, device.getUsername());
        values.put(DevKeyColums.COMMUNITY_CODE, device.getCommunity_code());
        values.put(DevKeyColums.DEV_NAME, device.getDev_name());
//        values.put(DevKeyColums.DEV_SN, device.getDev_sn());
        values.put(DevKeyColums.DEV_VOIP_ACCOUNT, device.getDev_voip_account());
        values.put(DevKeyColums.DEV_TYPE, device.getDev_type());
        return values;
    }

    /**
     * 更新DevKeyBean的顺序
     * @param device 设备
     */
    public void saveDevKeyOrderNum(DevKeyBean device)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db.isOpen())
        {
            String queryClause = "select * from " + DevKeyColums.TABLE_NAME + " where " + DevKeyColums.USERNAME + "=? and " + DevKeyColums.DEV_SN + "=?";
            Cursor c = db.rawQuery(queryClause, new String []{device.getUsername(), device.getDev_sn()});
            ContentValues values= new ContentValues();
            values.put(DevKeyColums.ORDER_NUM, device.getOrderNum());
            if (c.getCount() != 0)
            {
                String whereClause = DevKeyColums.USERNAME + "=? and " + DevKeyColums.DEV_SN + "=?";
                int ret = db.update(DevKeyColums.TABLE_NAME, values, whereClause, new String[]{device.getUsername(), device.getDev_sn()});
                LogUtils.d("开始保存门禁数据:更新是否成功？ ret = " + ret + " 更新的设备是：" + device.toString());
            }
            c.close();
        }
    }

    /**
     * @Title: 查询此用户所有的视频对讲帐号列表
     * @param username 用户名
     * @return 返回sip集合
     */
    public List<String> queryAllDevSipid(String username) {
        Cursor cursor;
        List<String> devlist =new ArrayList<>();
        try {
            SQLiteDatabase database = dbHelp.getReadableDatabase();
            cursor = database.rawQuery("select * from dev_list" + " where " + DevKeyColums.USERNAME + "=? ", new String[]{username});
            int i=0;
            while (cursor.moveToNext()) {
                String dev_sipid = cursor.getString(cursor.getColumnIndex("dev_voip_account"));
                devlist.add(dev_sipid);
                i++;
                LogUtils.i("测试啊", "获取到:"+i+"----:"+dev_sipid+"->"+devlist.size());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devlist;
    }

    /**
     * @Title: queryAllDevices
     * @Description: TODO(查询所有视频设备列表)
     * @return ArrayList<DevKeyBean> 返回类型
     */
    public ArrayList<DevKeyBean> queryAllDevices(String username)
    {
        SQLiteDatabase database = dbHelp.getReadableDatabase();

        Cursor c = database.rawQuery("select * from " + DevKeyColums.TABLE_NAME + " where " + DevKeyColums.USERNAME + "=? ", new String[]{username});
        ArrayList<DevKeyBean> doorList = new ArrayList<>();
        if (c.getCount() == 0)
        {
            c.close();
            return null;
        }
        if (c.moveToFirst())
        {
            while(!c.isAfterLast())
            {
                DevKeyBean door = new DevKeyBean();
                door.setUsername(c.getString(c.getColumnIndex(DevKeyColums.USERNAME)));
                door.setCommunity_code(c.getString(c.getColumnIndex(DevKeyColums.COMMUNITY_CODE)));
                door.setDev_name(c.getString(c.getColumnIndex(DevKeyColums.DEV_NAME)));
                door.setDev_sn(c.getString(c.getColumnIndex(DevKeyColums.DEV_SN)));
                door.setDev_voip_account(c.getString(c.getColumnIndex(DevKeyColums.DEV_VOIP_ACCOUNT)));
                door.setDev_type(c.getInt(c.getColumnIndex(DevKeyColums.DEV_TYPE)));
                door.setOrderNum(c.getInt(c.getColumnIndex(DevKeyColums.ORDER_NUM)));
                doorList.add(door);
                c.moveToNext();
            }
        }
        c.close();
        return doorList;
    }

    /**
     * @Title: queryDevice
     * @Description: TODO(查询视频设备)
     * @return DevKeyBean 返回类型
     */
    public DevKeyBean queryDeviceBySn(String username,String devSn)
    {
        SQLiteDatabase database = dbHelp.getReadableDatabase();

        String queryClause = "select * from " + DevKeyColums.TABLE_NAME + " where " + DevKeyColums.USERNAME + "=? and " + DevKeyColums.DEV_SN + "=?";
        Cursor c = database.rawQuery(queryClause, new String[]{username,devSn});
        DevKeyBean door = null;
        if (c.getCount() == 0)
        {
            c.close();
            return null;
        }
        if (c.moveToFirst())
        {
            door = new DevKeyBean();
            door.setUsername(c.getString(c.getColumnIndex(DevKeyColums.USERNAME)));
            door.setCommunity_code(c.getString(c.getColumnIndex(DevKeyColums.COMMUNITY_CODE)));
            door.setDev_name(c.getString(c.getColumnIndex(DevKeyColums.DEV_NAME)));
            door.setDev_sn(c.getString(c.getColumnIndex(DevKeyColums.DEV_SN)));
            door.setDev_voip_account(c.getString(c.getColumnIndex(DevKeyColums.DEV_VOIP_ACCOUNT)));
            door.setDev_type(c.getInt(c.getColumnIndex(DevKeyColums.DEV_TYPE)));
            door.setOrderNum(c.getInt(c.getColumnIndex(DevKeyColums.ORDER_NUM)));
            c.moveToNext();
        }
        c.close();
        return door;
    }

    /**
     * @Title: queryAllDevName
     * @Description: TODO(获取数据库的设备名称)
     * @param username 用户名
     */
    public List<String> queryAllDevName(String username) {
        Cursor cursor;
        List<String> dev_name_list =new ArrayList<>();
        try {
            SQLiteDatabase database = dbHelp.getReadableDatabase();
            cursor = database.rawQuery("select * from "+DevKeyColums.TABLE_NAME + " where " + DevKeyColums.USERNAME + "=? ", new String[]{username});
            int i=0;
            if (cursor.getCount() == 0) {
                cursor.close();
                return null;
            } else {
                while (cursor.moveToNext()) {
                    String dev_name = cursor.getString(cursor.getColumnIndex(DevKeyColums.DEV_NAME));
                    dev_name_list.add(dev_name);
                    i++;
                    LogUtils.i("测试啊", "获取到:"+i+"----:"+dev_name+"->"+dev_name_list.size());
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dev_name_list;
    }


}
