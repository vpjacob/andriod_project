package com.doormaster.topkeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.SystemInfoBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;

import java.util.ArrayList;

import static com.doormaster.topkeeper.db.OpenRecordData.COLUMN_DEV_NAME;
import static com.doormaster.topkeeper.db.OpenRecordData.COLUMN_DEV_SN;


/**
 * Created by Liukebing on 2017/3/8.
 */

public class AccessDevMetaData {
    private AccessDevMetaData(){}
    public static abstract class AccessDev implements BaseColumns {
        public static final String ACCESS_DEV_TABLE_NAME = "access_dev_table";
        public static final String ACCESS_DEV_SN = "devSn";
        public static final String ACCESS_DEV_MAC = "devMac";
        public static final String ACCESS_DEV_NAME = "devName";
        public static final String ACCESS_DEV_TYPE = "devType";
        public static final String ACCESS_DEV_PRIVILIGE= "privilege";
        public static final String ACCESS_DEV_OPEN_TYPE = "openType";
        public static final String ACCESS_DEV_VERIFIED = "verified";
        public static final String ACCESS_DEV_START_DATE = "startDate";
        public static final String ACCESS_DEV_END_DATE = "endData";
        public static final String ACCESS_DEV_USECOUNT = "useCount";
        public static final String ACCESS_DEV_EKEY = "eKey";
        public static final String ACCESS_DEV_ENCRYPTION = "encryption";
        public static final String ACCESS_DEV_NETWORKFUN = "networkSupport";

        public static final String ACCESS_ORDER_NUMBER = "ordernumber";
        public static final String ACCESS_USERNAME = "username";
        public static final String ACCESS_ENABLE = "enable";
        public static final String ACCESS_FUNCTION = "function";
        public static final String ACCESS_DEV_MANAGER_PWD = "super_manager_pwd";

        public static final String ACCESS_DEV_DBNAME_COMPANY = "dbname_company";
        public static final String ACCESS_DEV_DOOR_NO = "door_no";
        public static final String ACCESS_DEV_CARDNO = "cardno";
        public static final String ACCESS_DEV_SHAKEOPEN = "shake_open";
        public static final String ACCESS_DEV_AUTOOPEN = "auto_open";
    }

    private static DBHelp dbHelp;

    public AccessDevMetaData(Context context){
        dbHelp = DBHelp.getInstance(context);
    }

    /**
     * @Title: saveAccessDev
     * @Description: TODO(保存一个设备)
     */
    public void saveAccessDev(AccessDevBean device)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db.isOpen())
        {
            String queryClause = "select * from " + AccessDev.ACCESS_DEV_TABLE_NAME + " where " + AccessDev.ACCESS_DEV_SN + "=? and " + AccessDev.ACCESS_USERNAME + "=?";
            Cursor c = db.rawQuery(queryClause, new String []{device.getDevSn(), device.getUsername()});
            ContentValues values;
            if (c.getCount() == 0)
            {
                values = getAccessDevContentValues(device);
                long ret = db.insert(AccessDev.ACCESS_DEV_TABLE_NAME, null, values);
                initSysInfo(device);
                LogUtils.d("开始保存门禁数据:是否成功？ ret = " + ret + " 保存的设备是：" + device.toString());
            }
            else
            {
                values = getAccessDevContentValues2(device);
                String whereClause = AccessDev.ACCESS_DEV_SN + "=? and " + AccessDev.ACCESS_USERNAME + "=?";
                int ret = db.update(AccessDev.ACCESS_DEV_TABLE_NAME, values, whereClause, new String[]{device.getDevSn(), device.getUsername()});
                LogUtils.d("开始保存门禁数据:更新是否成功？ ret = " + ret + " 更新的设备是：" + device.toString());
            }
            c.close();
        }
    }

    /**
     * 保存设备是否开启摇动开门
     * @param device 设备
     */
    public void saveDeviceShakeOpen(AccessDevBean device)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put(AccessDev.ACCESS_DEV_SHAKEOPEN, device.getShakeOpen());
//                LogUtils.d("开启摇一摇开门失败，查询设备失败，请确认设备是否已经删除");
            String whereClause = AccessDev.ACCESS_DEV_SN + "=? and " + AccessDev.ACCESS_USERNAME + "=?";
            int ret = db.update(AccessDev.ACCESS_DEV_TABLE_NAME, values, whereClause, new String[]{device.getDevSn(), device.getUsername()});
            LogUtils.d("开启摇一摇开门是否成功？ ret = " + ret + " 更新的设备是：" + device.getDevName());
        }
    }

    /**
     * 保存设备是否开启自动开门
     * @param device 设备
     */
    public void saveDeviceAutoOpen(AccessDevBean device)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put(AccessDev.ACCESS_DEV_AUTOOPEN, device.getAutoOpen());
//                LogUtils.d("开启摇一摇开门失败，查询设备失败，请确认设备是否已经删除");
            String whereClause = AccessDev.ACCESS_DEV_SN + "=? and " + AccessDev.ACCESS_USERNAME + "=?";
            int ret = db.update(AccessDev.ACCESS_DEV_TABLE_NAME, values, whereClause, new String[]{device.getDevSn(), device.getUsername()});
            LogUtils.d("开启靠近开门是否成功？ ret = " + ret + " 更新的设备是：" + device.getDevName());
        }
    }

    //初始化新设备的设备信息
    private void initSysInfo(AccessDevBean device) {
        SystemInfoBean sysDom = new SystemInfoBean();
        sysDom.setUsername(SPUtils.getString(Constants.USERNAME));
        sysDom.setDevMac(device.getDevMac());
        SystemInfoData infoData = new SystemInfoData(BaseApplication.getContext());
        infoData.saveSystemINfo(sysDom);
    }

    /**
     * 删除指定设备，传入的参数必须包含用户名称、设备序列号dev_sn和设备mac地址
     * @param device 设备类
     * */
    public void deleteDevice (String username, AccessDevBean device)
    {
        if (username == null || device.getDevSn() == null
                || device.getDevMac() == null) {
            return;
        }
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db.isOpen()) {
            String deleteClause = AccessDev.ACCESS_USERNAME + "=? and " +
                    AccessDev.ACCESS_DEV_SN + "=? and " + AccessDev.ACCESS_DEV_MAC + "=?";
            db.delete(AccessDev.ACCESS_DEV_TABLE_NAME, deleteClause, new String[]{ username,
                    device.getDevSn(), device.getDevMac()});
        }
    }

    public ArrayList<AccessDevBean> getAllAccessDevList(String username)
    {
        SQLiteDatabase database = dbHelp.getReadableDatabase();
        Cursor c = database.rawQuery("select * from " + AccessDev.ACCESS_DEV_TABLE_NAME + " where " + AccessDev.ACCESS_USERNAME + "=? ", new String[]{username});
//        Cursor c = database.rawQuery("select * from " + ACCESS_DEV_TABLE_NAME, null);
        ArrayList<AccessDevBean> list = new ArrayList<>();
        if (c.getCount() == 0)
        {
            c.close();
            return null;
        }
        if (c.moveToFirst())
        {
            while(!c.isAfterLast())
            {
                AccessDevBean device = getAccessDevBeanByCursor(c);
                list.add(device);
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    /**
     * 获取当前用户开启摇一摇开门的列表
     * @param username 用户名
     * @return 设备集合
     */
    public ArrayList<AccessDevBean> getShakeOpenList(String username)
    {
        SQLiteDatabase database = dbHelp.getReadableDatabase();
//        String qureyClause = "select * from " + AccessDev.ACCESS_DEV_TABLE_NAME + " where " + AccessDev.ACCESS_USERNAME + "=? and " + AccessDev.ACCESS_DEV_SHAKEOPEN + "=?";
//        Cursor c = database.rawQuery(qureyClause, new String[]{username, "1"});
        Cursor c=database.query(AccessDev.ACCESS_DEV_TABLE_NAME, null
                , AccessDev.ACCESS_USERNAME + "=? and " + AccessDev.ACCESS_DEV_SHAKEOPEN + "=?"
                , new String[]{username, "1"}, null, null, null);
        ArrayList<AccessDevBean> list = new ArrayList<>();
        if (c.getCount() == 0)
        {
            c.close();
            return null;
        }
        if (c.moveToFirst())
        {
            while(!c.isAfterLast())
            {
                AccessDevBean device = getAccessDevBeanByCursor(c);
                list.add(device);
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    /**
     * 获取当前用户开启自动开门的列表
     * @param username 用户名
     * @return 设备集合
     */
    public ArrayList<AccessDevBean> getAutoOpenList(String username)
    {
        SQLiteDatabase database = dbHelp.getReadableDatabase();
//        Cursor c = database.rawQuery("select * from " + AccessDev.ACCESS_DEV_TABLE_NAME + " where " + AccessDev.ACCESS_USERNAME + "=? ", new String[]{username});
        Cursor c=database.query(AccessDev.ACCESS_DEV_TABLE_NAME, null
                , AccessDev.ACCESS_USERNAME + "=? and " + AccessDev.ACCESS_DEV_AUTOOPEN + "=?"
                , new String[]{username, "1"}, null, null, null);
        ArrayList<AccessDevBean> list = new ArrayList<>();
        if (c.getCount() == 0)
        {
            c.close();
            return null;
        }
        if (c.moveToFirst())
        {
            while(!c.isAfterLast())
            {
                AccessDevBean device = getAccessDevBeanByCursor(c);
                list.add(device);
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    /**
     * @Title: queryAccessDeviceByDevSn
     * @Description: TODO(查询指定视频设备)
     * @return AccessDevBean 设备
     */
    public AccessDevBean queryAccessDeviceByDevSn(String username,String devSn)
    {
        SQLiteDatabase database = dbHelp.getReadableDatabase();
        Cursor c = database.rawQuery("select * from " + AccessDev.ACCESS_DEV_TABLE_NAME
                + " where " + AccessDev.ACCESS_USERNAME + "=? and " + AccessDev.ACCESS_DEV_SN + "=?", new String[]{username, devSn});
        AccessDevBean device = null;
        if (c.getCount() == 0)
        {
            c.close();
            return null;
        }
        if (c.moveToFirst())
        {
            device = getAccessDevBeanByCursor(c);
        }
        c.close();
        return device;
    }

    /**
     * 根据mac获取设备
     * */
    public AccessDevBean getDeviceByMac (String username, String dev_mac){
        if (username == null  || dev_mac == null) {
            return null;
        }
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + AccessDev.ACCESS_DEV_TABLE_NAME + " where "
                        + AccessDev.ACCESS_USERNAME + "=? and " + AccessDev.ACCESS_DEV_MAC + "=?",
                new String[]{username, dev_mac});
        if (c.getCount() == 0) {
            c.close();
            return null;
        }
        AccessDevBean device = null;
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                device = getAccessDevBeanByCursor(c);
                c.moveToNext();
            }
        }
        c.close();
        return device;
    }

    /**
     * 判断设备是否在列表中
     * */
    public boolean isAccessDevDeviceExist (String username, String dev_mac) {
        if (username == null  || dev_mac == null) {
            return false;
        }
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + AccessDev.ACCESS_DEV_TABLE_NAME + " where "
                        + AccessDev.ACCESS_USERNAME + "=? and " + AccessDev.ACCESS_DEV_MAC + "=?",
                new String[]{username, dev_mac});
        int count = c.getCount();
        c.close();
        return count > 0;
    }

    /**
     * 方法名： deleteAccessDevData <br>
     * 方法描述：TODO(删除门禁列表数据)<br>
     * 修改备注：<br>
     * 创建时间： 2017-1-05下午17:33:51<br>
     */
    public void deleteAccessDevData() {
        SQLiteDatabase database = dbHelp.getReadableDatabase();
        database.delete(AccessDev.ACCESS_DEV_TABLE_NAME, null, null);
        database.close();
    }


    /**
     * 更新access的顺序
     * @param device
     */
    public void saveAccessOrderNum(AccessDevBean device)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db.isOpen())
        {
            String queryClause = "select * from " + AccessDev.ACCESS_DEV_TABLE_NAME + " where " + AccessDev.ACCESS_DEV_SN + "=? and " + AccessDev.ACCESS_USERNAME + "=?";
            Cursor c = db.rawQuery(queryClause, new String []{device.getDevSn(), device.getUsername()});
            ContentValues values= new ContentValues();
            values.put(AccessDev.ACCESS_ORDER_NUMBER, device.getOrderNum());
            if (c.getCount() == 0)
            {
//				long ret = db.insert(ACCESS_DEV_TABLE_NAME, null, values);
//				MyLog.debug("开始保存门禁数据:是否成功？ ret = " + ret + " 保存的设备是：" + device.toString());
            }
            else
            {
                String whereClause = AccessDev.ACCESS_DEV_SN + "=? and " + AccessDev.ACCESS_USERNAME + "=?";
                int ret = db.update(AccessDev.ACCESS_DEV_TABLE_NAME, values, whereClause, new String[]{device.getDevSn(), device.getUsername()});
//				MyLog.debug("开始保存门禁数据:更新是否成功？ ret = " + ret + " 更新的设备是：" + device.toString());
            }
            c.close();
        }
    }

    /**
     * 更新用户卡号
     * */
    public void updateUserCardno(String username, String cardno)
    {
        if (username == null || cardno == null) {
            return;
        }
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(AccessDev.ACCESS_DEV_CARDNO, cardno);
            String whereClause = AccessDev.ACCESS_USERNAME + "=?";
            db.update(AccessDev.ACCESS_DEV_TABLE_NAME, values, whereClause,
                    new String[]{ username });
        }
    }
    /**
     * 删除管理账户下指定用户的所有设备，传入的参数必须包含用户名称、所属管理账号名
     *
     * */
    public void deleteAllDeviceFromDbcompany (String username, String dbname_company)
    {
        if (username == null || dbname_company == null) {
            return;
        }
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db.isOpen()) {
            String deleteClause = AccessDev.ACCESS_USERNAME + "=? and " +
                    AccessDev.ACCESS_DEV_DBNAME_COMPANY + "=?";
            db.delete(AccessDev.ACCESS_DEV_TABLE_NAME, deleteClause, new String[]{ username,
                    dbname_company});
        }
    }

    //查询并更新设备的名称、编号、所属账号
    public void updateDevice(String dev_sn, String dev_name, String dbname_company, int door_no)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();

        if ( db.isOpen()) {
            //查找是否已有该设备，用户名、设备dev_sn,确定唯一设备
            String queryClause = "select * from " + AccessDev.ACCESS_DEV_TABLE_NAME + " where "
                    + AccessDev.ACCESS_USERNAME + "=? and " + AccessDev.ACCESS_DEV_SN +"=?";
            Cursor c = db.rawQuery( queryClause, new String[] { SPUtils.getString(Constants.USERNAME),
                    dev_sn});
            ContentValues values = new ContentValues();
            if(dev_name != null && dev_name.length() > 0)
            {
                values.put(COLUMN_DEV_NAME, dev_name);
            }
            if(dbname_company != null && dbname_company.length() > 0)
            {
                values.put(AccessDev.ACCESS_DEV_DBNAME_COMPANY, dbname_company);
            }
            if(door_no >= -1 && door_no <256)
            {
                values.put(AccessDev.ACCESS_DEV_DOOR_NO, door_no);
            }
            if (c.getCount()==0){
                db.insert(AccessDev.ACCESS_DEV_TABLE_NAME, null, values);
            } else {
                String whereClause = AccessDev.ACCESS_USERNAME + "=? and " + COLUMN_DEV_SN +"=?";
                db.update(AccessDev.ACCESS_DEV_TABLE_NAME, values, whereClause, new String[]{
                        SPUtils.getString(Constants.USERNAME),
                        dev_sn});
            }

        }
    }

    private AccessDevBean getAccessDevBeanByCursor(Cursor c) {
        AccessDevBean device = new AccessDevBean();
        device.setUsername(c.getString(c.getColumnIndex(AccessDev.ACCESS_USERNAME)));
        device.setDevSn(c.getString(c.getColumnIndex(AccessDev.ACCESS_DEV_SN)));
        device.setDevMac(c.getString(c.getColumnIndex(AccessDev.ACCESS_DEV_MAC)));
        device.setDevName(c.getString(c.getColumnIndex(AccessDev.ACCESS_DEV_NAME)));
        device.setStartDate(c.getString(c.getColumnIndex(AccessDev.ACCESS_DEV_START_DATE)));
        device.setEndDate(c.getString(c.getColumnIndex(AccessDev.ACCESS_DEV_END_DATE)));
        device.seteKey(c.getString(c.getColumnIndex(AccessDev.ACCESS_DEV_EKEY)));
        device.setDevType( c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_TYPE)) );
        device.setEncrytion(c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_ENCRYPTION)));
        device.setNetWorkSupport(c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_NETWORKFUN)));
        device.setOpenType(c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_OPEN_TYPE)));
        device.setPrivilege(c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_PRIVILIGE)));
        device.setVerified(c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_VERIFIED)));
        device.setUseCount(c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_USECOUNT)));
        device.setOrderNum(c.getInt(c.getColumnIndex(AccessDev.ACCESS_ORDER_NUMBER)));

        device.setFunction(c.getString(c.getColumnIndex(AccessDev.ACCESS_FUNCTION)));
        device.setEnable(c.getInt(c.getColumnIndex(AccessDev.ACCESS_ENABLE)));
        device.setDevManagerPassword(c.getString(c.getColumnIndex(AccessDev.ACCESS_DEV_MANAGER_PWD)));
        device.setDbname_company(c.getString(c.getColumnIndex(AccessDev.ACCESS_DEV_DBNAME_COMPANY)));
        device.setDoor_no(c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_DOOR_NO)));
        device.setCardno(c.getString(c.getColumnIndex(AccessDev.ACCESS_DEV_CARDNO)));
        device.setShakeOpen(c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_SHAKEOPEN)));
        device.setAutoOpen(c.getInt(c.getColumnIndex(AccessDev.ACCESS_DEV_AUTOOPEN)));
        return device;
    }

    //获取accessDev的ContentValues
    private ContentValues getAccessDevContentValues(AccessDevBean device)
    {
        ContentValues values= new ContentValues();
        values.put(AccessDev.ACCESS_DEV_SN, device.getDevSn());
        values.put(AccessDev.ACCESS_DEV_MAC, device.getDevMac());
        values.put(AccessDev.ACCESS_DEV_NAME, device.getDevName());
        values.put(AccessDev.ACCESS_DEV_TYPE, device.getDevType());
        values.put(AccessDev.ACCESS_DEV_PRIVILIGE, device.getPrivilege());
        values.put(AccessDev.ACCESS_DEV_OPEN_TYPE, device.getOpenType());
        values.put(AccessDev.ACCESS_DEV_VERIFIED, device.getVerified());
        values.put(AccessDev.ACCESS_DEV_START_DATE, device.getStartDate());
        values.put(AccessDev.ACCESS_DEV_END_DATE, device.getEndDate());
        values.put(AccessDev.ACCESS_DEV_USECOUNT, device.getUseCount());
        values.put(AccessDev.ACCESS_DEV_EKEY, device.geteKey());
        values.put(AccessDev.ACCESS_DEV_ENCRYPTION, device.getEncrytion());
        values.put(AccessDev.ACCESS_DEV_NETWORKFUN, device.getNetWorkSupport());
        values.put(AccessDev.ACCESS_ORDER_NUMBER,device.getOrderNum());
        values.put(AccessDev.ACCESS_USERNAME,device.getUsername());
        values.put(AccessDev.ACCESS_FUNCTION,device.getFunction());
        values.put(AccessDev.ACCESS_DEV_MANAGER_PWD,device.getDevManagerPassword());
        values.put(AccessDev.ACCESS_DEV_DBNAME_COMPANY,device.getDbname_company());
        values.put(AccessDev.ACCESS_DEV_DOOR_NO,device.getDoor_no());
        values.put(AccessDev.ACCESS_DEV_CARDNO,device.getCardno());
        String start_date = device.getStartDate();
        String end_date = device.getEndDate();
        if (start_date == null || end_date == null
                || start_date.isEmpty() || end_date.isEmpty())
        {
            values.put(AccessDev.ACCESS_ENABLE, AccessDevBean.USEFUL);
        }
        else
        {
            values.put(AccessDev.ACCESS_ENABLE, device.getEnable());
        }
        return values;
    }

    //获取accessDev的ContentValues
    private ContentValues getAccessDevContentValues2(AccessDevBean device)
    {
        ContentValues values= new ContentValues();
        values.put(AccessDev.ACCESS_DEV_SN, device.getDevSn());
        values.put(AccessDev.ACCESS_DEV_MAC, device.getDevMac());
        values.put(AccessDev.ACCESS_DEV_NAME, device.getDevName());
        values.put(AccessDev.ACCESS_DEV_TYPE, device.getDevType());
        values.put(AccessDev.ACCESS_DEV_PRIVILIGE, device.getPrivilege());
        values.put(AccessDev.ACCESS_DEV_OPEN_TYPE, device.getOpenType());
        values.put(AccessDev.ACCESS_DEV_VERIFIED, device.getVerified());
        values.put(AccessDev.ACCESS_DEV_START_DATE, device.getStartDate());
        values.put(AccessDev.ACCESS_DEV_END_DATE, device.getEndDate());
        values.put(AccessDev.ACCESS_DEV_USECOUNT, device.getUseCount());
        values.put(AccessDev.ACCESS_DEV_EKEY, device.geteKey());
        values.put(AccessDev.ACCESS_DEV_ENCRYPTION, device.getEncrytion());
        values.put(AccessDev.ACCESS_DEV_NETWORKFUN, device.getNetWorkSupport());
        values.put(AccessDev.ACCESS_USERNAME,device.getUsername());
        values.put(AccessDev.ACCESS_FUNCTION,device.getFunction());
        values.put(AccessDev.ACCESS_DEV_MANAGER_PWD,device.getDevManagerPassword());
        values.put(AccessDev.ACCESS_DEV_DBNAME_COMPANY,device.getDbname_company());
        values.put(AccessDev.ACCESS_DEV_DOOR_NO,device.getDoor_no());
        values.put(AccessDev.ACCESS_DEV_CARDNO,device.getCardno());
        String start_date = device.getStartDate();
        String end_date = device.getEndDate();
        if (start_date == null || end_date == null
                || start_date.isEmpty() || end_date.isEmpty())
        {
            values.put(AccessDev.ACCESS_ENABLE, AccessDevBean.USEFUL);
        }
        else
        {
            values.put(AccessDev.ACCESS_ENABLE, device.getEnable());
        }
        return values;
    }

}
