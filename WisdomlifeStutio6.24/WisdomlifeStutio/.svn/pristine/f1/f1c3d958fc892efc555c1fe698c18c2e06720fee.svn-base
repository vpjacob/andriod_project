package com.doormaster.topkeeper.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doormaster.topkeeper.bean.RoomBean;
import com.doormaster.topkeeper.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("UseSparseArrays")
public class DBBiz {
  private static DBBiz typeBiz;
  private DBHelp dbHelp;

  public DBBiz(Context context) {
    dbHelp = new DBHelp(context);
  }
  
  public static DBBiz getInstanter(Context context) {
    if (typeBiz == null) {
      typeBiz = new DBBiz(context);
    }
    return typeBiz;
  }

  /** -----------房间列表相关------------------- */
  /**
   * 方法名： addRoomList <br>
   * 方法描述：TODO(添加房间列表)<br>
   * 修改备注：<br>
   * 创建时间： 2016-3-15下午3:33:34<br>
   * 
   * @param room
   */
  
  public void addRoomList(RoomBean room) {
	  SQLiteDatabase db = dbHelp.getWritableDatabase();
	  if (db.isOpen())
	  {
		  String queryClause = "select * from " + RoomMetaData.Room.ROOM_TABLE_NAME + " where " + RoomMetaData.Room.COMMUNITY_CODE + "=? and " + RoomMetaData.Room.ROOM_ID + "=?";
		  Cursor c = db.rawQuery(queryClause, new String []{room.getCommunity_code(), String.valueOf(room.getRoom_id())});
		  ContentValues values= getRoomContentValues(room);
		  if (c.getCount() == 0)
		  {
			  long ret = db.insert(RoomMetaData.Room.ROOM_TABLE_NAME, null, values);
			  LogUtils.d("开始保存门禁数据:是否成功？ ret = " + ret + " 保存的设备是：" + room.toString());
		  }
		  else
		  {
			  String whereClause = RoomMetaData.Room.COMMUNITY_CODE + "=? and " + RoomMetaData.Room.ROOM_ID + "=?";
			  int ret = db.update(RoomMetaData.Room.ROOM_TABLE_NAME, values, whereClause, new String[]{room.getCommunity_code(), String.valueOf(room.getRoom_id())});
			  LogUtils.d("开始保存门禁数据:更新是否成功？ ret = " + ret + " 更新的设备是：" + room.toString());
		  }
		  c.close();
	  }
  }

	//获取RoomBean的ContentValues
	private ContentValues getRoomContentValues(RoomBean device)
	{
		ContentValues values= new ContentValues();
		values.put(RoomMetaData.Room.COMMUNITY_CODE, device.getCommunity_code());
		values.put(RoomMetaData.Room.COMMUNITY, device.getCommunity());
		values.put(RoomMetaData.Room.BUILDING, device.getBuilding());
		values.put(RoomMetaData.Room.ROOM_CODE, device.getRoom_code());
		values.put(RoomMetaData.Room.ROOM_NAME, device.getRoom_name());
		values.put(RoomMetaData.Room.ROOM_ID, device.getRoom_id());
		values.put(RoomMetaData.Room.START_DATETIME, device.getStart_datetime());
		values.put(RoomMetaData.Room.END_DATETIME, device.getEnd_datetime());
		return values;
	}

  /**
   * 方法名： queryRoomId <br>
   * 方法描述：TODO(查询RoomId，单元门口机)<br>
   * 修改备注：<br>
   * 创建时间： 2016-3-15上午11:43:36<br>
   * 
   * @return 返回 -1 则：查询失败
   */
  public int queryRoomId(String name) {
    int roomId = -1;
    Cursor cursor;
    try {
      SQLiteDatabase database = dbHelp.getReadableDatabase();
      cursor = database.rawQuery("select * from room_list where name=" + name, null);
      while (cursor.moveToNext()) {
        roomId = cursor.getInt(cursor.getColumnIndex("room_id"));
      }
      cursor.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return roomId;
  }
  /**
   * 方法名： queryRoomAddress <br>
   * 方法描述：TODO(查询RoomAddress)<br>
   * 修改备注：<br>
   * 创建时间： 2016-6-28上午11:43:36<br>
   * 
   * @return 返回 -1 则：查询失败
   */
  public String queryRoomAddr(int roomId) {
    Cursor cursor;
    String community_code = null;
    String community = null;
    String building = null; 
    String room_code=null;
    String room_name=null;
    int room_id=0;
    String start_datetime=null;
    String end_datetime=null;
    try {
      SQLiteDatabase database = dbHelp.getReadableDatabase();
      cursor = database.rawQuery("select * from room_list where room_id=" + roomId, null);
      while (cursor.moveToNext()) {
    	  community_code = cursor.getString(cursor.getColumnIndex("community_code"));
    	  community = cursor.getString(cursor.getColumnIndex("community"));
    	  building = cursor.getString(cursor.getColumnIndex("building"));
    	  room_code = cursor.getString(cursor.getColumnIndex("room_code"));
    	  room_name = cursor.getString(cursor.getColumnIndex("room_name"));
    	 // room_id = cursor.getString(cursor.getColumnIndex("room_id"));
    	  start_datetime = cursor.getString(cursor.getColumnIndex("start_datetime"));
    	  end_datetime = cursor.getString(cursor.getColumnIndex("end_datetime"));
      }
      cursor.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "小区编号:"+community_code+"，小区名称:"+community+"，楼栋单元:"+building+"，房间名称:"+room_name;
  }
   

  
  /**
   * 方法名： deleteRoomData <br>
   * 方法描述：TODO(删除房间数据)<br>
   * 修改备注：<br>
   * 创建时间： 2016-3-15上午11:43:51<br>
   */
  public void deleteRoomData() {
    SQLiteDatabase database = dbHelp.getReadableDatabase();
    database.delete("room_list", null, null);
    database.close();
  }
  
  /** 
* @Title: queryAllRow 
* @Description: TODO(这里用一句话描述这个方法的作用) 
* @return void    返回类型
* @author （作者） 
* @date 2016年10月17日 上午10:19:26
* @version V1.0   
*/
public long queryAllRow () {  
	String doorName = null;
	  long count =0;
  Cursor cursor;
  try {
    SQLiteDatabase database = dbHelp.getReadableDatabase();
    cursor = database.rawQuery(" select count(*) from room_list", null); 
    cursor.moveToFirst();          // 将光标移动到第一行。
    count =  cursor.getLong(0); //获取到总行数.返回请求的列的长的值。
 
   
/*    */
    LogUtils.i("测试查询room_list","1、房间列表记录条数count="+count);
    
    while (cursor.moveToNext()) {
  	//  doorName = cursor.getString(cursor.getColumnIndex("dev_name"));
  	 String room_name = cursor.getString(cursor.getColumnIndex("room_name"));
  	 
  	  String s = cursor.getString(cursor.getColumnIndex("path"));                     //关键来了，cur.getColumnIndex("path")返回的是什么，s又是什么？？
  	  
  	LogUtils.i("测试啊", "获取到:"+room_name+s);
    }
    cursor.close();
    database.close();
  } catch (Exception e) {
    e.printStackTrace();
  }
  return count;
  }

/** 
* @Title: queryAllRow 
* @Description: TODO(这里用一句话描述这个方法的作用) 
* @param @param Dev    入参
* @return void    返回类型
* @author （作者） 
* @throws
* @date 2016年10月17日 上午10:19:26 
* @version V1.0   
*/
public List<String> queryAllAddress() {  
	String doorName = null;
	  long count =0;
  Cursor cursor;
  List<String> mylist =new ArrayList<String>();
  try {
    SQLiteDatabase database = dbHelp.getReadableDatabase();
 //   cursor = database.rawQuery(" select count(*) from room_list", null); 
   // cursor = database.rawQuery(sql, selectionArgs);
    cursor = database.rawQuery("select * from room_list", null);     		 
    int i=0;
    while (cursor.moveToNext()) {
  	//  doorName = cursor.getString(cursor.getColumnIndex("dev_name"));
  	 String room_name = cursor.getString(cursor.getColumnIndex("room_name"));
  	mylist.add(room_name);
  	 i++;
  	//  String s = cursor.getString(cursor.getColumnIndex("path"));                     //关键来了，cur.getColumnIndex("path")返回的是什么，s又是什么？？
  	  
  	LogUtils.i("测试啊", "获取到:"+i+"----:"+room_name);
    }
   // L.i("测试", "列表:"+"--"+mylist.toString());
    cursor.close();
  } catch (Exception e) {
    e.printStackTrace();
  }
  return mylist;
}
public List<String> queryAllCommunity() {   
  Cursor cursor;
  List<String> mylist =new ArrayList<String>();
  try {
    SQLiteDatabase database = dbHelp.getReadableDatabase(); 
    cursor = database.rawQuery("select * from room_list", null);     		 
    int i=0;
    while (cursor.moveToNext()) {
  	//  doorName = cursor.getString(cursor.getColumnIndex("dev_name"));
  	 String community = cursor.getString(cursor.getColumnIndex("community"));
  	mylist.add(community);
  	 i++;
  	//  String s = cursor.getString(cursor.getColumnIndex("path"));                     //关键来了，cur.getColumnIndex("path")返回的是什么，s又是什么？？
  	  
  	LogUtils.i("测试啊", "获取到:"+i+"----:"+community);
    }
   // L.i("测试", "列表:"+"--"+mylist.toString());
    cursor.close();
  } catch (Exception e) {
    e.printStackTrace();
  }
return mylist;
  }
public List<String> queryAllbuilding() {   
	  Cursor cursor;
	  List<String> mylist =new ArrayList<String>();
	  try {
	    SQLiteDatabase database = dbHelp.getReadableDatabase(); 
	    cursor = database.rawQuery("select * from room_list", null);     		 
	    int i=0;
	    while (cursor.moveToNext()) {
	  	//  doorName = cursor.getString(cursor.getColumnIndex("dev_name"));
	  	 String building = cursor.getString(cursor.getColumnIndex("building"));
	  	mylist.add(building);
	  	 i++;
	  	//  String s = cursor.getString(cursor.getColumnIndex("path"));                     //关键来了，cur.getColumnIndex("path")返回的是什么，s又是什么？？
	  	  
	  	LogUtils.i("测试啊", "获取到:"+i+"----:"+building);
	    }
	   // L.i("测试", "列表:"+"--"+mylist.toString());
	    cursor.close();
	  } catch (Exception e) {
	    e.printStackTrace();
	  }
	return mylist;
	  }
/**
 * 更新修改的房间号
 */
public void updateRoomName(String roomId ,String room_name){
	  SQLiteDatabase db=dbHelp.getReadableDatabase();
	  ContentValues values=new ContentValues();
	  values.put("room_name",  (room_name));
	  db.update("room_list", values, "room_id=?", new String[]{roomId});
	  db.close();
}

  /**
   * 方法名： queryDoor <br>
   * 方法描述：TODO(查询门口机信息)<br>
   * 修改备注：<br>
   * 创建时间： 2016-3-15上午11:43:36<br>
   * 
   * @return 返回null 则：查询失败
   */
  public String queryDoor(String sipId) {
    String doorName = null;
    Cursor cursor;
    try {
      SQLiteDatabase database = dbHelp.getReadableDatabase();
      cursor = database.rawQuery("select * from dev_list where dev_voip_account=" + sipId, null);//通过sipid获取到对端信息
      while (cursor.moveToNext()) {
    	  doorName = cursor.getString(cursor.getColumnIndex("dev_name"));
      }
      cursor.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return doorName;
  }

/** 
* @Title: queryAllRow 
* @Description: TODO(这里用一句话描述这个方法的作用) 
* @param @param Dev    入参
* @return void    返回类型
* @author （作者） 
* @throws
* @date 2016年10月17日 上午10:19:26 
* @version V1.0   
*/
public List<String> queryAllDevSipid() {    
  Cursor cursor;
  List<String> devlist =new ArrayList<String>();
  try {
    SQLiteDatabase database = dbHelp.getReadableDatabase();
    cursor = database.rawQuery("select * from dev_list", null);     		 
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
	 * @Title: queryAllRooms
	 * @Description: TODO(查询所有视频设备列表)
	 * @return ArrayList<DevKeyBean> 返回类型
	 * @author Cola Zhang
	 * @date 2016年10月17日 上午10:19:26
	 * @version V1.0
	 */
	public ArrayList<RoomBean> queryAllRooms()
	{
		SQLiteDatabase database = dbHelp.getReadableDatabase();
		Cursor c = database.rawQuery("select * from room_list", null);
		ArrayList<RoomBean> roomList = new ArrayList<RoomBean>();
		if (c.getCount() == 0)
		{
			return null;
		}
		if (c.moveToFirst()) 
		{
			while(!c.isAfterLast()) 
			{
				RoomBean room = new RoomBean();
				room.community_code = c.getString(c.getColumnIndex("community_code"));
				room.community = c.getString(c.getColumnIndex("community"));
				room.building = c.getString(c.getColumnIndex("building"));
				room.room_code = c.getString(c.getColumnIndex("room_code"));
				room.room_name = c.getString(c.getColumnIndex("room_name"));
				room.room_id = c.getInt(c.getColumnIndex("room_id"));
				room.start_datetime = c.getString(c.getColumnIndex("start_datetime"));
				room.end_datetime = c.getString(c.getColumnIndex("end_datetime"));
				roomList.add(room);
				c.moveToNext();
			}
		}
		c.close();
		return roomList;
	}

	/**
	 * 方法名： deleteRoomData <br>
	 * 方法描述：TODO(删除房间数据)<br>
	 * 修改备注：<br>
	 * 创建时间： 2016-3-15上午11:43:51<br>
	 */
	public void deleteDevData() {
		SQLiteDatabase database = dbHelp.getReadableDatabase();
		database.delete("dev_list", null, null);
		database.close();
	}
}
