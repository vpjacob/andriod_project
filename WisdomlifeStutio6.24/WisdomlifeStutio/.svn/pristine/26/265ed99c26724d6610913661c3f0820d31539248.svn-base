package com.doormaster.topkeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.doormaster.topkeeper.bean.SystemInfoBean;

import static com.doormaster.topkeeper.db.SystemInfoData.SystemInfo.*;

public class SystemInfoData {
	public static abstract class SystemInfo implements BaseColumns {
		//表名和用户信息
		public static final String TABLE_NAME = "device_system_info";
		public static final String COLUMN_USERNAME = "username";
		//系统信息
		public static final String COLUMN_DEVICE_MAC = "dev_mac";
		public static final String COLUMN_SYSTEM_WIEGAND = "wiegand";
		public static final String COLUMN_SYSTEM_OPENDELY = "open_delay";
		public static final String COLUMN_SYSTEM_REG_CARD_NUM = "reg_card_num";
		public static final String COLUMN_SYSTEM_VERSION = "version";
		public static final String COLUMN_SYSTEM_CONTROL_WAY = "control_way";
		public static final String COLUMN_SYSTEM_MAX_CONTAINER = "max_container";
		public static final String COLUMN_SYSTEM_REG_PHONE_NUM = "reg_phone_num";
	}
	private static DBHelp doormasterHelper;
	
	public SystemInfoData(Context context){
		doormasterHelper = DBHelp.getInstance(context);
	}
	
	/**
	 * 保存设备系统信息
	 * */
	public void  saveSystemINfo (SystemInfoBean systemInfo )
	{
		SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		if (db.isOpen()) {
			String queryClause = "select * from " + TABLE_NAME + " where "
					+ COLUMN_USERNAME +"=? and "+ COLUMN_DEVICE_MAC +"=?";
			Cursor c = db.rawQuery(queryClause,
					new String[]{systemInfo.getUsername(),systemInfo.getDevMac()});
			ContentValues values = getContentValues(systemInfo);
			if (c.getCount() == 0) {
				db.insert(TABLE_NAME, null, values);
			} else {
				String whereClause = COLUMN_USERNAME +"=? and " + COLUMN_DEVICE_MAC +"=?";
				db.update(TABLE_NAME, values, whereClause,
						new String[]{systemInfo.getUsername(),systemInfo.getDevMac()});
			}
			c.close();
		}
	}
	/*
	 * 删除设备系统信息
	 * */
	public void deleteDeviceSystemInfo(SystemInfoBean systemInfo )
	{
		SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		if (db.isOpen()) {
			String whereClause = COLUMN_USERNAME + "=? and "+ COLUMN_DEVICE_MAC +"=?";
			db.delete(TABLE_NAME, whereClause, 
					new String[]{systemInfo.getUsername(), systemInfo.getDevMac()});
		}
	}
	/*
	 * 修改设备信息数据
	 * */
	public void updateDeviceSystemInfo(SystemInfoBean systemInfo)
	{
		SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = getContentValues(systemInfo);
			String whereClause = COLUMN_USERNAME + "=? and "+ COLUMN_DEVICE_MAC +"=?";
			db.update(TABLE_NAME, values, whereClause, 
					new String[]{systemInfo.getUsername(), systemInfo.getDevMac()});
		}
	}
	/*
	 * 查询设备信息
	 * */
	public SystemInfoBean getDeviceSystemInfo (String username, String dev_mac)
	{
		SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		SystemInfoBean infoDom = null;
		if (db.isOpen()) {
			String qureyClause = "select * from " + TABLE_NAME + " where "
					+ COLUMN_USERNAME + "=? and " +COLUMN_DEVICE_MAC + "=?";
			Cursor c = db.rawQuery(qureyClause, new String[]{username,dev_mac});
			if (c.getCount() == 0) {
				c.close();
				return null;
			}
			if (c.moveToFirst()) {
				while (!c.isAfterLast()) {
					infoDom = getSystemInfoByCursor(c);
					c.moveToNext();
				}
			}
			c.close();
		}
		return infoDom;
	}
	
	//通过Cursor获得系统信息类
	private SystemInfoBean getSystemInfoByCursor(Cursor c)
	{
		SystemInfoBean infoDom = new SystemInfoBean();
		infoDom.setUsername(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
		infoDom.setDevMac(c.getString(c.getColumnIndex(COLUMN_DEVICE_MAC)));
		infoDom.setWiegand(c.getInt(c.getColumnIndex(COLUMN_SYSTEM_WIEGAND)));
		infoDom.setRegCardNum(c.getInt(c.getColumnIndex(COLUMN_SYSTEM_REG_CARD_NUM)));
		infoDom.setOpenDelay(c.getInt(c.getColumnIndex(COLUMN_SYSTEM_OPENDELY)));
		infoDom.setRegPhoneNum(c.getInt(c.getColumnIndex(COLUMN_SYSTEM_REG_PHONE_NUM)));
		infoDom.setVersion(c.getInt(c.getColumnIndex(COLUMN_SYSTEM_VERSION)));
		infoDom.setControlWay(c.getInt(c.getColumnIndex(COLUMN_SYSTEM_CONTROL_WAY)));
		infoDom.setMaxContainer(c.getInt(c.getColumnIndex(COLUMN_SYSTEM_MAX_CONTAINER)));
		return infoDom;
	}

	private ContentValues getContentValues(SystemInfoBean systemInfo)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_USERNAME, systemInfo.getUsername());
		values.put(COLUMN_DEVICE_MAC, systemInfo.getDevMac());
		values.put(COLUMN_SYSTEM_WIEGAND, systemInfo.getWiegand());
		values.put(COLUMN_SYSTEM_OPENDELY, systemInfo.getOpenDelay());
		values.put(COLUMN_SYSTEM_REG_CARD_NUM, systemInfo.getRegCardNum());
		values.put(COLUMN_SYSTEM_REG_PHONE_NUM, systemInfo.getRegPhoneNum());
		values.put(COLUMN_SYSTEM_VERSION, systemInfo.getVersion());
		values.put(COLUMN_SYSTEM_CONTROL_WAY, systemInfo.getControlWay());
		values.put(COLUMN_SYSTEM_MAX_CONTAINER, systemInfo.getMaxContainer());
		return values;
	}
}
