package com.doormaster.topkeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doormaster.topkeeper.bean.MessageBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;

import java.util.ArrayList;

public class MessageData {
	
	//msg数据表结构
	public static final String TABLE_NAME = "message";
	
	public static final String COLUMN_USERNAME = "USERNAME";
	
	public static final String COLUMN_ID = "id";
    public static final String COLUMN_SENDER = "sender";
    public static final String COLUMN_MSG_TIME = "sender_time";
    public static final String COLUMN_MSG_IMAGE_TYPE = "msg_image_type";
    public static final String COLUMN_MSG_IMAGE_CONTENT = "msg_image_content";
    public static final String COLUMN_MSG_CONTENT = "content";
    public static final String COLUMN_MSG_DOOR_NO = "door_no";
    public static final String COLUMN_MSG_READED = "read";
    
    public static DBHelp doormasterHelper;

    public MessageData(Context context) {
    	doormasterHelper = DBHelp.getInstance(context);
    }
    
	public synchronized Integer saveMessageData(MessageBean message){
		LogUtils.d("msg :" + message.toString());
		SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		int id = -1;
        if (db.isOpen())
        {
            String queryClause = "select * from " + TABLE_NAME + " where " + COLUMN_ID + "=? ";
            Cursor c = db.rawQuery(queryClause, new String []{String.valueOf(message.getId())});
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, message.getUsername());

            values.put(COLUMN_ID, message.getId());
            values.put(COLUMN_SENDER, message.getSender());
            values.put(COLUMN_MSG_TIME, message.getSendTime());
            values.put(COLUMN_MSG_CONTENT, message.getContent());
            values.put(COLUMN_MSG_READED, message.getRead());
            values.put(COLUMN_MSG_IMAGE_TYPE, message.getImageType());
            values.put(COLUMN_MSG_IMAGE_CONTENT, message.getImageContent());
            if (c.getCount() == 0)
            {
                long ret = db.insert(TABLE_NAME, null, values);
                LogUtils.d("开始保存门禁数据:是否成功？ ret = " + ret + " 保存的设备是：" + message.toString());
            }
            else
            {
                String whereClause = COLUMN_ID + "=? ";
                int ret = db.update(TABLE_NAME, values, whereClause, new String[]{String.valueOf(message.getId())});
                LogUtils.d("开始保存门禁数据:更新是否成功？ ret = " + ret + " 更新的设备是：" + message.toString());
            }
            c.close();
        }
        return id;
	}
    
    //查询数据，指定username,返回senderList，用于显示多少项
    public ArrayList<String> getSenderList(String username)
    {
    	SQLiteDatabase db = doormasterHelper.getReadableDatabase();
    	ArrayList<String> senderList = new ArrayList<String>();
    	Cursor c = db.rawQuery("select distinct "+ COLUMN_SENDER +" from " + TABLE_NAME + " where " + COLUMN_USERNAME +"=?" +" order by "+ COLUMN_MSG_TIME + " DESC ", new String[]{username});
    	if(c.moveToFirst()){
    		while(!c.isAfterLast()){
    			senderList.add(c.getString(c.getColumnIndex(COLUMN_SENDER)));
    			c.moveToNext();
    		}
    	}
    	c.close();
    	return senderList;
    }

    //返回指定发送者的消息列表
    public ArrayList<MessageBean> getMsgContent(String username, String sender)
    {
    	SQLiteDatabase db = doormasterHelper.getReadableDatabase();
    	ArrayList<MessageBean> contentList = new ArrayList<MessageBean>();
    	Cursor c = db.rawQuery("select * from " + TABLE_NAME +" where USERNAME=? and sender =?"
    						+ " order by "+ COLUMN_MSG_TIME + " DESC ", new String[]{username,sender});
    	if(c.moveToFirst()){
    		while(!c.isAfterLast()){
    			MessageBean message = new MessageBean();
    			int id = c.getInt(c.getColumnIndex(COLUMN_ID));
    			String content = c.getString(c.getColumnIndex(COLUMN_MSG_CONTENT));
    			String time = c.getString(c.getColumnIndex(COLUMN_MSG_TIME));
    			int readed = c.getInt(c.getColumnIndex(COLUMN_MSG_READED));
    			int imageType = c.getInt(c.getColumnIndex(COLUMN_MSG_IMAGE_TYPE));
    			String imageContent = c.getString(c.getColumnIndex(COLUMN_MSG_IMAGE_CONTENT));
//    			String door_no = c.getString(c.getColumnIndex(COLUMN_MSG_DOOR_NO));
    			message.setId(id);
    			message.setContent(content);
    			message.setSendTime(time);
    			message.setRead(readed);
    			message.setImageType(imageType);
    			message.setImageContent(imageContent);
//    			message.setDoorNo(door_no);
    			contentList.add(message);
    			c.moveToNext();
    		}
    	}
    	c.close();
    	return contentList;
    }

	//返回所有的消息列表
	public ArrayList<MessageBean> getAllMsg(String username)
	{
		SQLiteDatabase db = doormasterHelper.getReadableDatabase();
		ArrayList<MessageBean> contentList = new ArrayList<MessageBean>();
		Cursor c = db.rawQuery("select * from " + TABLE_NAME +" where USERNAME=?"
				+ " order by "+ COLUMN_MSG_TIME + " DESC ", new String[]{username});
		if(c.moveToFirst()){
			while(!c.isAfterLast()){
				MessageBean message = new MessageBean();
				int id = c.getInt(c.getColumnIndex(COLUMN_ID));
				String sender = c.getString(c.getColumnIndex(COLUMN_SENDER));
				String content = c.getString(c.getColumnIndex(COLUMN_MSG_CONTENT));
				String time = c.getString(c.getColumnIndex(COLUMN_MSG_TIME));
				int readed = c.getInt(c.getColumnIndex(COLUMN_MSG_READED));
				int imageType = c.getInt(c.getColumnIndex(COLUMN_MSG_IMAGE_TYPE));
				String imageContent = c.getString(c.getColumnIndex(COLUMN_MSG_IMAGE_CONTENT));
//    			String door_no = c.getString(c.getColumnIndex(COLUMN_MSG_DOOR_NO));
				message.setId(id);
				message.setUsername(username);
				message.setSender(sender);
				message.setContent(content);
				message.setSendTime(time);
				message.setRead(readed);
				message.setImageType(imageType);
				message.setImageContent(imageContent);
//    			message.setDoorNo(door_no);
				contentList.add(message);
				c.moveToNext();
			}
		}
		c.close();
		return contentList;
	}

	//获取消息发送者未读消息条数
    public int getNotReadCount (String sender)
    {
		String username = SPUtils.getString(Constants.USERNAME);
		SQLiteDatabase db = doormasterHelper.getReadableDatabase();
    	Cursor c = db.rawQuery("select " + COLUMN_MSG_READED + " from " +TABLE_NAME + " where "
    			+ COLUMN_USERNAME + "=?" +" and " + COLUMN_SENDER + "=?"
    			+ " and " + COLUMN_MSG_READED + "=?", new String[]{username, sender, Integer.toString(MessageBean.NOT_READ)});
    	return c.getCount();
    }
    
    
    public void setMsgHasRead (String sender)
    {

		String username = SPUtils.getString(Constants.USERNAME);
    	SQLiteDatabase db = doormasterHelper.getReadableDatabase();
		ContentValues c = new ContentValues();
		c.put(COLUMN_MSG_READED, MessageBean.HAS_READ);
		String where = COLUMN_USERNAME + "=?" +" and " + COLUMN_SENDER + "=?"
    			+ " and " + COLUMN_MSG_READED + "=?";
		db.update(TABLE_NAME,c,where , new String[]{username, sender, Integer.toString(MessageBean.NOT_READ)});
    }
    
    //删除单条消息
    public void deleteSingleContact(int id)
    {
		SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		
		if(db.isOpen()){
			String delete_sql = "delete from "+ TABLE_NAME +" where id=" + id;
			db.execSQL(delete_sql);
		}
	}
    
    //删除指定发送者的所有信息
    public void deleteSenderMsg(String username, String sender)
    {
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_USERNAME + "=?"+ " and "+ COLUMN_SENDER +"=?",new String[]{username,sender} );
		}
    }
    
    //清空消息列表
    public void deleteAllMsg(String username)
    {
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
    	if(db.isOpen()){
    		db.delete(TABLE_NAME, COLUMN_USERNAME, new String[]{username});
    	}
    }
/***************************************************************************************************************************/
//    /**
//     * 添加消息进数据库
//     * @param 
//     * */
//    public void saveMessage(MessageDom message) {
//    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
//    	if (db.isOpen()) {
//    		ContentValues values = getMessageValues(message);
//    		db.insert(TABLE_NAME, null, values);
//    	}
//    }
//    /**
//     * 删除所有消息
//     * */
//
//    /**
//     * 获取message ContentValues
//     * */
//	private ContentValues getMessageValues(MessageDom message) {
//		ContentValues values = new ContentValues();
//		values.put(COLUMN_USERNAME, message.getUsername());
//		values.put(COLUMN_ID, message.getId());
//		values.put(COLUMN_SENDER, message.getSender());
//		values.put(COLUMN_MSG_TIME, message.getSendTime());
//		values.put(COLUMN_MSG_CONTENT, message.getContent());
//		values.put(COLUMN_MSG_READED, message.getRead());
//		return values;
//	}
    
}
