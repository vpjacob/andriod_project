package com.doormaster.topkeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.doormaster.topkeeper.bean.UsersCardDom;
import com.doormaster.topkeeper.utils.LogUtils;

import static com.doormaster.topkeeper.db.UsersCardData.UserCardColums.*;

public class UsersCardData {
	private UsersCardData(){}
	public static abstract class UserCardColums implements BaseColumns {
		//用户卡管理
		public static final String TABLE_NAME = "res_users_card";
		public static final String COLUMN_USERS_CARD_USERNAME = "username";
		public static final String COLUMN_USERS_CARD_DBNAME_COMPANY = "dbname_company";
		public static final String COLUMN_USERS_CARD_CARDNO = "cardno";
		public static final String COLUMN_USERS_CARD_SECTION = "section";
		public static final String COLUMN_USERS_CARD_SECTION_KEY = "section_key";
	}
    
    public static DBHelp doormasterHelper;

    public UsersCardData(Context context) {
		// TODO Auto-generated constructor stub
    	doormasterHelper = DBHelp.getInstance(context);
    }
    
    //保存用户卡表数据
    public synchronized Integer saveUsersCardData(UsersCardDom usersCard){
		int id = -1;
		if(usersCard == null || usersCard.getUsername() == null || usersCard.getDbname_company() == null)
		{
			return id;
		}
		LogUtils.d("msg :" + usersCard.toString());
		SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		
		if(db.isOpen()){
			Cursor c = db.rawQuery("select * from "
	  				+TABLE_NAME + " where " + COLUMN_USERS_CARD_USERNAME + "=? and " + COLUMN_USERS_CARD_DBNAME_COMPANY + "=?", new String[]{usersCard.getUsername(), usersCard.getDbname_company()});
			ContentValues values = new ContentValues();
			//有数据才更新
			if(usersCard.getUsername() != null && usersCard.getUsername().length() > 0)
			{
				values.put(COLUMN_USERS_CARD_USERNAME, usersCard.getUsername());
			}
			
			if(usersCard.getDbname_company() != null && usersCard.getDbname_company().length() > 0)
			{
				values.put(COLUMN_USERS_CARD_DBNAME_COMPANY,  usersCard.getDbname_company());
			}
			if(usersCard.getCardno() != null && usersCard.getCardno().length() > 0)
			{
				values.put(COLUMN_USERS_CARD_CARDNO, usersCard.getCardno());
			}
			
			if(usersCard.getSection() != -1)
			{
				values.put(COLUMN_USERS_CARD_SECTION, usersCard.getSection());
			}
			
			if(usersCard.getSection_key() != null && usersCard.getSection_key().length() > 0)
			{
				values.put(COLUMN_USERS_CARD_SECTION_KEY, usersCard.getSection_key());
			}
			if (c.getCount() == 0) 
	  		{
				db.insert(TABLE_NAME, null, values);
			}else
			{
				String whereClause = COLUMN_USERS_CARD_USERNAME +"=? and " + COLUMN_USERS_CARD_DBNAME_COMPANY + "=?";
				db.update(TABLE_NAME, values, whereClause, new String[]{ usersCard.getUsername(), usersCard.getDbname_company()} );
			}
			
			
			/*ContentValues values = new ContentValues();
			values.put(COLUMN_USERS_CARD_USERNAME, usersCard.getUsername());
			values.put(COLUMN_USERS_CARD_DBNAME_COMPANY, usersCard.getDbname_company());
			values.put(COLUMN_USERS_CARD_CARDNO, usersCard.getCardno());
			values.put(COLUMN_USERS_CARD_SECTION, usersCard.getSection());
			values.put(COLUMN_USERS_CARD_SECTION_KEY, usersCard.getSection_key());
			db.insert(TABLE_NAME, null, values);*/
			id = 1;
			c.close();
		}
		return id;
	}
    
    public UsersCardDom getUsersCardDom(String username, String dbname_company)
    {
    	UsersCardDom usersCardDom = null;
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		if(db.isOpen()){
			Cursor c = null;
			if(dbname_company .equals(""))
			{
				return null;
			}else
			{
				c = db.rawQuery("select * from " 
		  				+TABLE_NAME + " where " + COLUMN_USERS_CARD_USERNAME + "=? and " + COLUMN_USERS_CARD_DBNAME_COMPANY + "=?", new String[]{username, dbname_company});
			}
			if (c.getCount() == 0) 
	  		{
				c.close();
				return null;
			}
	  		if (c.moveToFirst()) {
				while (!c.isAfterLast()) {
					usersCardDom = getUserCardByCursor(c);
					c.moveToNext();
				}
			}
			c.close();
		}
    	return usersCardDom;
    }
    
    
    private UsersCardDom getUserCardByCursor(Cursor c)
  	{
    	UsersCardDom  usersCardDom = new UsersCardDom();
    	usersCardDom.setCardno(c.getString(c.getColumnIndex(COLUMN_USERS_CARD_CARDNO)));
    	usersCardDom.setDbname_company(c.getString(c.getColumnIndex(COLUMN_USERS_CARD_DBNAME_COMPANY)));
    	usersCardDom.setSection(c.getInt(c.getColumnIndex(COLUMN_USERS_CARD_SECTION)));
    	usersCardDom.setSection_key(c.getString(c.getColumnIndex(COLUMN_USERS_CARD_SECTION_KEY)));
    	usersCardDom.setUsername(c.getString(c.getColumnIndex(COLUMN_USERS_CARD_USERNAME)));
		return usersCardDom;
  	}
    
    //删除指定管理账户下的用户卡元组
    public synchronized void deleUsersCardData(String username, String dbname_company)
    {
    	SQLiteDatabase db = doormasterHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_USERS_CARD_USERNAME + "=?"+ " and "+ COLUMN_USERS_CARD_DBNAME_COMPANY +"=?",new String[]{username, dbname_company} );
		}
    }
    
}
