package com.doormaster.topkeeper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;

public class DBHelp extends SQLiteOpenHelper {

	private static DBHelp instance;
	/**
	 * @param context
	 *            上下文
	 */
	public DBHelp(Context context) {
		super(context, Constants.DB_NAME, null, Constants.DB_VISION);
		// TODO Auto-generated constructor stub
	}

	//单例模式 获取DatabaseHelper的实例
	public static DBHelp getInstance(Context context) {
		if (instance == null) {
			instance = new DBHelp(context.getApplicationContext());
		}
		return instance;
	}

	public static final String CREATE_ACCESS_DATA = "create table if not exists "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_TABLE_NAME + "("
			+ "_id integer primary key autoincrement, "
			+ AccessDevMetaData.AccessDev.ACCESS_USERNAME + " text, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_SN + " text, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_MAC + " text, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_NAME + " text, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_TYPE + " integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_PRIVILIGE + " integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_OPEN_TYPE + " integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_VERIFIED + " integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_START_DATE + " text, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_END_DATE + " text, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_USECOUNT + " integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_ENCRYPTION + " integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_EKEY + " text, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_NETWORKFUN + " integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_ENABLE +" integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_FUNCTION +" text, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_MANAGER_PWD +" TEXT, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_DBNAME_COMPANY +" TEXT, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_DOOR_NO +" integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_CARDNO +" TEXT, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_SHAKEOPEN +" integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_DEV_AUTOOPEN +" integer, "
			+ AccessDevMetaData.AccessDev.ACCESS_ORDER_NUMBER + " integer) ";

	//open_record_table
	private static final String CREATE_OPENRECORD = "create table if not exists "
			+ OpenRecordData.TABLE_NAME + "( "
			+ OpenRecordData.COLUMN_ROW_ID + " INTEGER PRIMARY KEY, "
			+ OpenRecordData.COLUMN_DEV_MAC +" TEXT, "
			+ OpenRecordData.COLUMN_DEV_NAME +" TEXT, "
			+ OpenRecordData.COLUMN_DEV_SN +" TEXT, "
			+ OpenRecordData.COLUMN_UPLOAD +" INTEGER, "
			+ OpenRecordData.COLUMN_EVENT_TIME +" TEXT, "
			+ OpenRecordData.COLUMN_ACTION_TIME +" INTEGER, "
			+ OpenRecordData.COLUMN_OP_TIME +" INETGER, "
			+ OpenRecordData.COLUMN_OP_RET +" INTEGER, "
			+ OpenRecordData.COLUMN_OP_USER + " TEXT );";

	private static final String CREATE_MESSAGEDATA = "create table if not exists "
			+ MessageData.TABLE_NAME + "( "
			+ MessageData.COLUMN_USERNAME +" TEXT, "
			+ MessageData.COLUMN_ID +" INTEGER, "
			+ MessageData.COLUMN_SENDER + " TEXT, "
			+ MessageData.COLUMN_MSG_TIME + " TEXT, "
			+ MessageData.COLUMN_MSG_IMAGE_TYPE + " INTEGER, "
			+ MessageData.COLUMN_MSG_IMAGE_CONTENT + " TEXT, "
			+ MessageData.COLUMN_MSG_CONTENT + " TEXT, "
			+ MessageData.COLUMN_MSG_DOOR_NO + " TEXT, "
			+ MessageData.COLUMN_MSG_READED + " INTEGER );";

	private static final String CREATE_DEV_LIST_DATA = "create table if not exists "
			+ DevKeyMetaData.DevKeyColums.TABLE_NAME + "("
			+ "_id integer primary key autoincrement, "
			+ DevKeyMetaData.DevKeyColums.USERNAME + " text, "
			+ DevKeyMetaData.DevKeyColums.COMMUNITY_CODE + " text, "
			+ DevKeyMetaData.DevKeyColums.DEV_NAME + " text, "
			+ DevKeyMetaData.DevKeyColums.DEV_SN + " text, "
			+ DevKeyMetaData.DevKeyColums.DEV_VOIP_ACCOUNT + " text, "
			+ DevKeyMetaData.DevKeyColums.DEV_TYPE + " integer, "
			+ DevKeyMetaData.DevKeyColums.ORDER_NUM + " integer) ";

	private static final String CREATE_ROOM_LIST = "create table if not exists "
			+ RoomMetaData.Room.ROOM_TABLE_NAME + "("
			+ "_id integer primary key autoincrement, "
			+ RoomMetaData.Room.COMMUNITY_CODE + " text, "
			+ RoomMetaData.Room.COMMUNITY + " text, "
			+ RoomMetaData.Room.BUILDING + " text, "
			+ RoomMetaData.Room.ROOM_CODE + " text, "
			+ RoomMetaData.Room.ROOM_NAME + " text, "
			+ RoomMetaData.Room.ROOM_ID + " text, "
			+ RoomMetaData.Room.START_DATETIME + " text, "
			+ RoomMetaData.Room.END_DATETIME + " integer) ";

	//user_table
	private static final String USER_INFO_TABLE = "create table if not exists "
			+ UserData.UserBase.TABLE_NAME + "( "
			+ UserData.UserBase.COLUMN_USER_CLIENT_ID+" TEXT, "
			+ UserData.UserBase.COLUMN_USER_NAME +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_NICKNAME +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_PWD +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_IDENTITY +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_MD5 +" INTEGER, "
			+ UserData.UserBase.COLUMN_USER_SHAKE_OPEN +" INTEGER, "
			+ UserData.UserBase.COLUMN_USER_SHAKE_OPEN_PROGRESS +" INTEGER, "
			+ UserData.UserBase.COLUMN_USER_AUTO_OPEN +" INTEGER, "
			+ UserData.UserBase.COLUMN_USER_AUTO_OPEN_PROGRESS +" INTEGER, "
			+ UserData.UserBase.COLUMN_USER_ENABLE_AUTO_OPEN_SPACE +" INTEGER, "
			+ UserData.UserBase.COLUMN_USER_AUTO_OPEN_SPACE_TIME +" INTEGER, "
			+ UserData.UserBase.COLUMN_USER_SERVER_IP +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_SERVER_PORT +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_WIFI_NAME +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_WIFI_PSWD +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_TOKEN_PWD +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_PIN +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_AUTO_UPLOAD +" TEXT, "
			+ UserData.UserBase.COLUMN_USER_CARDNO + " TEXT );";

	private static final String DEVICE_SYSTEM_INFO = "create table if not exists "
			+ SystemInfoData.SystemInfo.TABLE_NAME + "( "
			+ SystemInfoData.SystemInfo.COLUMN_USERNAME +" TEXT, "
			+ SystemInfoData.SystemInfo.COLUMN_DEVICE_MAC +" TEXT, "
			+ SystemInfoData.SystemInfo.COLUMN_SYSTEM_WIEGAND + " INTEGER, "
			+ SystemInfoData.SystemInfo.COLUMN_SYSTEM_OPENDELY + " INTEGER, "
			+ SystemInfoData.SystemInfo.COLUMN_SYSTEM_REG_CARD_NUM + " INTEGER, "
			+ SystemInfoData.SystemInfo.COLUMN_SYSTEM_REG_PHONE_NUM + " INTEGER, "
			+ SystemInfoData.SystemInfo.COLUMN_SYSTEM_CONTROL_WAY + " INTEGER, "
			+ SystemInfoData.SystemInfo.COLUMN_SYSTEM_MAX_CONTAINER + " INTEGER, "
			+ SystemInfoData.SystemInfo.COLUMN_SYSTEM_VERSION + " INTEGER );";

	//用户卡列表
	private static final String RES_USERS_CARD = "create table if not exists "
			+ UsersCardData.UserCardColums.TABLE_NAME + "( "
			+ UsersCardData.UserCardColums.COLUMN_USERS_CARD_USERNAME +" TEXT, "
			+ UsersCardData.UserCardColums.COLUMN_USERS_CARD_DBNAME_COMPANY +" TEXT, "
			+ UsersCardData.UserCardColums.COLUMN_USERS_CARD_SECTION_KEY +" TEXT, "
			+ UsersCardData.UserCardColums.COLUMN_USERS_CARD_SECTION +" INTEGER, "
			+ UsersCardData.UserCardColums.COLUMN_USERS_CARD_CARDNO +" TEXT );";

	private static final String SENDKEY_DATA = "create table if not exists "
			+ SendKeyData.SendKeyColums.TABLE_NAME + "( "
			+ SendKeyData.SendKeyColums.COLUMN_SENDER +" TEXT, "
			+ SendKeyData.SendKeyColums.COLUMN_DEV_MAC +" TEXT, "
			+ SendKeyData.SendKeyColums.COLUMN_RECEIVER +" TEXT, "
			+ SendKeyData.SendKeyColums.COLUMN_LIMIT_TIME +" TEXT, "
			+ SendKeyData.SendKeyColums.COLUMN_DESCRIPTION +" TEXT, "
			+ SendKeyData.SendKeyColums.COLUMN_SEND_TIME + " TEXT );";

	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtils.e("测试啊", "创建数据库");
		db.execSQL(CREATE_DEV_LIST_DATA);
		db.execSQL(CREATE_ROOM_LIST);
		db.execSQL(CREATE_ACCESS_DATA);

		db.execSQL(CREATE_OPENRECORD);
		db.execSQL(CREATE_MESSAGEDATA);

		db.execSQL(USER_INFO_TABLE);
		db.execSQL(DEVICE_SYSTEM_INFO);
		db.execSQL(RES_USERS_CARD);
		db.execSQL(SENDKEY_DATA);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		for (int j = oldVersion; j < newVersion; j++) {

			switch (j) {
				case 7:
				case 6:
					BaseApplication.getInstance().setClientId(null);
					SPUtils.put(Constants.IS_AUTOLOGIN, false,BaseApplication.getContext());
					SPUtils.put(Constants.PSW, null,BaseApplication.getContext());
					db.execSQL("drop table if exists '" + UserData.UserBase.TABLE_NAME +"'");
					db.execSQL(USER_INFO_TABLE);
					j = 7;
					break;
				case 5:
				case 4:
				case 3:
				case 2:
				case 1:
					BaseApplication.getInstance().setClientId(null);
					SPUtils.put(Constants.IS_AUTOLOGIN, false,BaseApplication.getContext());
					SPUtils.put(Constants.PSW, null,BaseApplication.getContext());
					SPUtils.put(Constants.USERNAME, null,BaseApplication.getContext());
					db.execSQL("drop table if exists '" + AccessDevMetaData.AccessDev.ACCESS_DEV_TABLE_NAME +"'");
					db.execSQL("drop table if exists '" + DevKeyMetaData.DevKeyColums.TABLE_NAME +"'");
					db.execSQL("drop table if exists '" + RoomMetaData.Room.ROOM_TABLE_NAME +"'");
					db.execSQL("drop table if exists '" + OpenRecordData.TABLE_NAME +"'");
					db.execSQL("drop table if exists '" + MessageData.TABLE_NAME +"'");
					db.execSQL("drop table if exists '" + UserData.UserBase.TABLE_NAME +"'");
					db.execSQL("drop table if exists '" + SystemInfoData.SystemInfo.TABLE_NAME +"'");
					db.execSQL("drop table if exists '" + UsersCardData.UserCardColums.TABLE_NAME +"'");
					db.execSQL("drop table if exists '" + UsersCardData.UserCardColums.TABLE_NAME +"'");

					db.execSQL(CREATE_DEV_LIST_DATA);
					db.execSQL(CREATE_ROOM_LIST);
					db.execSQL(CREATE_ACCESS_DATA);
					db.execSQL(CREATE_OPENRECORD);
					db.execSQL(CREATE_MESSAGEDATA);
					db.execSQL(USER_INFO_TABLE);
					db.execSQL(DEVICE_SYSTEM_INFO);
					db.execSQL(RES_USERS_CARD);
					db.execSQL(SENDKEY_DATA);
					j = 5;
					break;

				default:

					break;

			}

		}
	}

}
