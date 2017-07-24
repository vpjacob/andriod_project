package com.doormaster.topkeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doormaster.topkeeper.bean.RecordBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class OpenRecordData {
	public static final String TABLE_NAME = "record";
	
	public static final String COLUMN_ROW_ID = "id";
    public static final String COLUMN_DEV_NAME = "dev_name";
    public static final String COLUMN_DEV_MAC = "dev_mac";
    public static final String COLUMN_UPLOAD = "upload_state";
    public static final String COLUMN_DEV_SN = "dev_sn";
    public static final String COLUMN_EVENT_TIME = "event_time";
    public static final String COLUMN_ACTION_TIME = "action_time";
    public static final String COLUMN_OP_TIME = "op_time";
    public static final String COLUMN_OP_RET = "op_ret";
    public static final String COLUMN_OP_USER = "op_user";
	
    public static DBHelp doormasterHelper;

    public OpenRecordData(Context context) {
		// TODO Auto-generated constructor stub
    	doormasterHelper = DBHelp.getInstance(context);
    }
    
    //增加存储数据
    public void saveOpenRecord(RecordBean record)
    {
//    	MyLog.Assert(record != null);
    	LogUtils.d("save record :" + record.toString());
    	SQLiteDatabase doormaster = doormasterHelper.getWritableDatabase();
    	if (doormaster.isOpen()) {
    		//保存开锁记录
            ContentValues values = new ContentValues();
            values.put(COLUMN_DEV_MAC, record.getDevMac());
            values.put(COLUMN_DEV_NAME, record.getDevName());
            values.put(COLUMN_DEV_SN, record.getDevSn());
            values.put(COLUMN_UPLOAD, record.getUpload());
            values.put(COLUMN_EVENT_TIME, record.getEventTime());
            values.put(COLUMN_ACTION_TIME, record.getActionTime());
            values.put(COLUMN_OP_TIME,record.getOptime());
            values.put(COLUMN_OP_RET, record.getOpRet());
            values.put(COLUMN_OP_USER, record.getOpUser());
			long ret = doormaster.insert(TABLE_NAME, null, values);
			LogUtils.d("开始保存开门记录:是否成功？ ret = " + ret + " 保存的设备是：" + record.toString());
		}
    }
    /**
     * 获取最大ID
     * */
    public int getMaxID () {
    	String op_user = SPUtils.getString(Constants.USERNAME);
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
    	int comm_id = 0x00;
		Cursor c = db.rawQuery("select " + COLUMN_ROW_ID + " from " + TABLE_NAME + " where "
				+ COLUMN_OP_USER +"=? order by " + COLUMN_ROW_ID + " ASC", new String[]{op_user});
		if (c.getCount() == 0) {
			LogUtils.d("Count" + 0);
		}
		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {
				comm_id = c.getInt(c.getColumnIndex(COLUMN_ROW_ID));
				LogUtils.d("" + comm_id);
				c.moveToNext();
			}
		}
		return comm_id;
    }
    /**
     * 获取下载的记录
     * */
    public ArrayList<RecordBean> getUpLoadRecord()
    {
		String op_user = SPUtils.getString(Constants.USERNAME);
    	ArrayList<RecordBean> uploadData = null;
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
    	Cursor c = db.rawQuery("select * from " + TABLE_NAME
    			+ " where " + COLUMN_OP_USER +"=? and " + COLUMN_UPLOAD + "=?",
    			new String[]{op_user, Integer.toString(RecordBean.UPLOAD_FAILED)});
    	if (c.getCount() == 0) {
			return null;
		}
		if (c.moveToFirst()) {
			uploadData = new ArrayList<RecordBean>();
			while (!c.isAfterLast()) {
				RecordBean record = getRecordByCursor(c);
				uploadData.add(record);
				c.moveToNext();
			}
		}
    	return uploadData;
    }
    /**
     * 通过Cursor获取开锁记录
     * */
    private RecordBean getRecordByCursor(Cursor c) {
		RecordBean record = new RecordBean();
		record.setId(c.getInt(c.getColumnIndex(COLUMN_ROW_ID)));
		record.setOpUser(c.getString(c.getColumnIndex(COLUMN_OP_USER)));
		record.setDevName(c.getString(c.getColumnIndex(COLUMN_DEV_NAME)));
		record.setDevMac(c.getString(c.getColumnIndex(COLUMN_DEV_MAC)));
		record.setDevSn(c.getString(c.getColumnIndex(COLUMN_DEV_SN)));
		record.setUpload(c.getInt(c.getColumnIndex(COLUMN_UPLOAD)));
		record.setEventTime(c.getString(c.getColumnIndex(COLUMN_EVENT_TIME)));
		record.setActionTime(c.getInt(c.getColumnIndex(COLUMN_ACTION_TIME)));
		record.setOptime(c.getInt(c.getColumnIndex(COLUMN_OP_TIME)));
		record.setOpRet(c.getInt(c.getColumnIndex(COLUMN_OP_RET)));
		return record;
    }
    
    //更新上传状态
    public void  updateCommId(ArrayList<RecordBean> recordList) {
//    	MyLog.Assert(recordList != null);
    	if (recordList == null || recordList.size() <=0) 
    	{
    		return;
    	}
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		if (db.isOpen()) {
			for (RecordBean record : recordList) {
				ContentValues c = new ContentValues();
				c.put(COLUMN_UPLOAD, RecordBean.UPLOAD_SUCCESS);
				String where = COLUMN_OP_USER +"=? and " +COLUMN_DEV_MAC +"=?";
				db.update(TABLE_NAME,c,where , new String[]{record.getOpUser(), record.getDevMac()});
			}
		}
    }
    
  /**获取开锁成功的记录
   * 
   * @param username
   * @return
   */
    public List<RecordBean> getRecordSuccessList(String username)
    {
    	List<RecordBean> uploadData = null;
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
    	Cursor c = db.rawQuery("select * from " + TABLE_NAME
    			+ " where " + COLUMN_OP_USER + "=? and " + COLUMN_OP_RET + "=? order by "+ COLUMN_EVENT_TIME + " DESC ",
    			new String[]{username, Integer.toString(0)});
    	if (c.getCount() == 0) {
			c.close();
			return null;
		}

		if (c.moveToFirst()) {
			uploadData = new ArrayList<>();
			while (!c.isAfterLast()) {
				RecordBean record = getRecordByCursor(c);
				uploadData.add(record);
				c.moveToNext();
			}
		}
		c.close();
    	return uploadData;
    }

	/**获取开锁所有的记录
	 *
	 * @param username
	 * @return
	 */
	public List<RecordBean> getOpenRecordList(String username)
	{
		List<RecordBean> uploadData = null;
		SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		Cursor c = db.rawQuery("select * from " + TABLE_NAME
						+ " where " + COLUMN_OP_USER + "=? order by "+ COLUMN_EVENT_TIME + " DESC ",
				new String[]{username});
		if (c.getCount() == 0) {
			c.close();
			return null;
		}

		if (c.moveToFirst()) {
			uploadData = new ArrayList<>();
			while (!c.isAfterLast()) {
				RecordBean record = getRecordByCursor(c);
				uploadData.add(record);
				c.moveToNext();
			}
		}
		c.close();
		return uploadData;
	}
}
