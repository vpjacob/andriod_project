package com.doormaster.topkeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.doormaster.topkeeper.bean.SendKeyRecordBean;
import com.doormaster.topkeeper.utils.LogUtils;

import java.util.ArrayList;

import static com.doormaster.topkeeper.db.SendKeyData.SendKeyColums.COLUMN_DESCRIPTION;
import static com.doormaster.topkeeper.db.SendKeyData.SendKeyColums.COLUMN_DEV_MAC;
import static com.doormaster.topkeeper.db.SendKeyData.SendKeyColums.COLUMN_LIMIT_TIME;
import static com.doormaster.topkeeper.db.SendKeyData.SendKeyColums.COLUMN_RECEIVER;
import static com.doormaster.topkeeper.db.SendKeyData.SendKeyColums.COLUMN_SENDER;
import static com.doormaster.topkeeper.db.SendKeyData.SendKeyColums.COLUMN_SEND_TIME;
import static com.doormaster.topkeeper.db.SendKeyData.SendKeyColums.TABLE_NAME;

public class SendKeyData {
	private SendKeyData(){}
	public static abstract class SendKeyColums implements BaseColumns {
		public static final String TABLE_NAME = "sendkey_record";
		public static final String COLUMN_SENDER = "sender";
		public static final String COLUMN_DEV_MAC = "dev_mac";
		public static final String COLUMN_RECEIVER = "receiver";
		public static final String COLUMN_LIMIT_TIME = "limit_time";
		public static final String COLUMN_SEND_TIME = "send_time";
		public static final String COLUMN_DESCRIPTION = "desription";
	}
	
	private static DBHelp doormasterHelper;

    public SendKeyData(Context context)
    {
    	doormasterHelper = DBHelp.getInstance(context);
    }
    
    //增加
    public void saveData(SendKeyRecordBean sendKeyRecord)
    {
    	if (sendKeyRecord.getSender() == null || sendKeyRecord.getDevMac() == null 
    			|| sendKeyRecord.getReceiver() == null) 
    	{
    		return;
    	}
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
    	String rawQuery = "select * from " + TABLE_NAME + " where "
    			+ COLUMN_SENDER + "=? and " + COLUMN_RECEIVER + "=? and " + COLUMN_DEV_MAC +"=?";
    	Cursor c = db.rawQuery(rawQuery, new String[]{sendKeyRecord.getSender(),
    			sendKeyRecord.getReceiver(), sendKeyRecord.getDevMac()});
		if ( db.isOpen()) 
		{
			ContentValues values = getContentValues(sendKeyRecord);
			if (c.getCount() == 0) 
			{				
				db.insert(TABLE_NAME, null, values);
			}
			else 
			{
				String updateClause = COLUMN_SENDER + "=? and "
						+ COLUMN_RECEIVER + "=? and " + COLUMN_DEV_MAC +"=?";
				db.update(TABLE_NAME, values, updateClause,
						new String[]{sendKeyRecord.getSender(), sendKeyRecord.getReceiver(),
							sendKeyRecord.getDevMac()});
			}
		}
		c.close();
    }
    
    //删除
    public void deleteData(SendKeyRecordBean record)
    {
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		if ( db.isOpen()) 
		{	
			String whereClause = COLUMN_SENDER + "=? and "
					+ COLUMN_RECEIVER + "=? and " + COLUMN_DEV_MAC +"=?";
			db.delete(TABLE_NAME, whereClause, new String[]{record.getSender(),
					record.getReceiver(), record.getDevMac()});
		}
    }
    
    //查询
    public ArrayList<SendKeyRecordBean> getSendKeyRecordList (String sender, String mac)
    {
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
    	ArrayList<SendKeyRecordBean> recordList = null;
		if ( db.isOpen()) 
		{	
			String queryClause = "select * from " + TABLE_NAME +" where "+ COLUMN_SENDER + "=? and "
					+ COLUMN_DEV_MAC +"=?";
			Cursor c = db.rawQuery(queryClause, new String[]{ sender, mac});
			if (c.getCount() == 0)
			{	
				LogUtils.d("null");
				c.close();
				return null;
			}
			
			if (c.moveToFirst()) 
			{
				recordList = new ArrayList<SendKeyRecordBean>();
				while (!c.isAfterLast()) 
				{
					SendKeyRecordBean record = getRecordByCursor(c);
					recordList.add(record);
					c.moveToNext();
				}
			}
			c.close();
		}
		LogUtils.d(recordList.toString());
		return recordList;
    }
    

	private SendKeyRecordBean getRecordByCursor(Cursor c)
	{
		SendKeyRecordBean record = new SendKeyRecordBean();
		record.setDevMac(c.getString(c.getColumnIndex(COLUMN_DEV_MAC)));
		record.setLimitTime(c.getString(c.getColumnIndex(COLUMN_LIMIT_TIME)));
		record.setSender(c.getString(c.getColumnIndex(COLUMN_SENDER)));
		record.setSendTime(c.getString(c.getColumnIndex(COLUMN_SEND_TIME)));
		record.setReceiver(c.getString(c.getColumnIndex(COLUMN_RECEIVER)));
		record.setDescription(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
		return record;
	}

	private ContentValues getContentValues(SendKeyRecordBean record)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_SENDER, record.getSender());
		values.put(COLUMN_RECEIVER, record.getReceiver());
		values.put(COLUMN_DEV_MAC, record.getDevMac());
		values.put(COLUMN_LIMIT_TIME, record.getLimitTime());
		values.put(COLUMN_SEND_TIME, record.getSendTime());
		values.put(COLUMN_DESCRIPTION, record.getDescription());
		return values;
	}
	
	public void clearAllData(String username, String dev_mac)
	{
		SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		if (username == null || dev_mac == null) 
		{
			return;
		}
		String whereClause = COLUMN_SENDER + "=? and " + COLUMN_DEV_MAC +"=?";
		db.delete(TABLE_NAME, whereClause, new String[]{username, dev_mac});
	}

}
