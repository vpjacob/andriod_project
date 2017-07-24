package com.doormaster.topkeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.doormaster.topkeeper.bean.UserBean;
import com.doormaster.topkeeper.constant.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import static com.doormaster.topkeeper.db.UserData.UserBase.*;

/**
 * @author Ivan Cola
 * 该类用于存储用户信息，在登录界面进行插入操作
 * */

public class UserData {
    private UserData(){}
    public static abstract class UserBase implements BaseColumns {
        public static final String TABLE_NAME = "user_info";
        public static final String COLUMN_USER_CLIENT_ID = "client_id";
        public static final String COLUMN_USER_NAME = "username";
        public static final String COLUMN_USER_NICKNAME = "nickname";
        public static final String COLUMN_USER_PWD = "password";
        public static final String COLUMN_USER_CARDNO = "cardno";
        public static final String COLUMN_USER_MD5 = "MD5";


        //设置摇一摇开门和自动开门参数
        public static final String COLUMN_USER_SHAKE_OPEN = "shake_open";
        public static final String COLUMN_USER_SHAKE_OPEN_PROGRESS = "shake_open_progress";
        public static final String COLUMN_USER_AUTO_OPEN = "auto_open";
        public static final String COLUMN_USER_AUTO_OPEN_PROGRESS = "auto_open_progress";
        public static final String COLUMN_USER_ENABLE_AUTO_OPEN_SPACE = "enable_auto_open_space";
        public static final String COLUMN_USER_AUTO_OPEN_SPACE_TIME = "auto_open_space_time";

        public static final String COLUMN_USER_IDENTITY = "identity";
        public static final String COLUMN_USER_TOKEN_PWD = "token_pwd";
        public static final String COLUMN_USER_PIN = "pin";
        public static final String COLUMN_USER_AUTO_UPLOAD = "auto_upload_event";

        //配置wifi字段
        public static final String COLUMN_USER_SERVER_IP = "server_ip";
        public static final String COLUMN_USER_SERVER_PORT = "server_port";
        public static final String COLUMN_USER_WIFI_NAME = "wifi_name";
        public static final String COLUMN_USER_WIFI_PSWD = "wifi_pswd";

        //设置摇一摇开门和自动开门距离
    }

    public static DBHelp doormasterHelper;

    public UserData(Context context) {
        // TODO Auto-generated constructor stub
        doormasterHelper = DBHelp.getInstance(context);
    }

    public void saveUserData(UserBean user)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME +" where " + COLUMN_USER_NAME +" =?", new String[]{user.getUserName()});
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_NICKNAME, user.getNickName());
        values.put(COLUMN_USER_CLIENT_ID, user.getClientID());
        values.put(COLUMN_USER_PWD, encodePwd(user.getPassWord()));
        values.put(COLUMN_USER_MD5, UserBean.MD5_NO);
        values.put(COLUMN_USER_CARDNO, user.getCardno());
        values.put(COLUMN_USER_IDENTITY,user.getIdentity());
        values.put(COLUMN_USER_TOKEN_PWD,user.getToken_pwd());
        values.put(COLUMN_USER_PIN,user.getPin());
        values.put(COLUMN_USER_AUTO_UPLOAD,user.getAuto_upload_event());

        if (c.getCount() == 0) {
            values.put(COLUMN_USER_SHAKE_OPEN, UserBean.SHAKE_OFF);
            values.put(COLUMN_USER_SHAKE_OPEN_PROGRESS, user.getShake_open_progress());
            values.put(COLUMN_USER_AUTO_OPEN, UserBean.AUTO_OPEN_OFF);
            values.put(COLUMN_USER_AUTO_OPEN_PROGRESS, user.getAuto_open_progress());
            values.put(COLUMN_USER_ENABLE_AUTO_OPEN_SPACE, user.getEnable_auto_open_space());
            values.put(COLUMN_USER_AUTO_OPEN_SPACE_TIME, user.getAuto_open_space_time());
            db.insert(TABLE_NAME, null, values);
        } else {
            String whereClause = COLUMN_USER_NAME +"=?";
            db.update(TABLE_NAME, values, whereClause, new String[]{ user.getUserName()} );
        }
    }



    //判断是否传输MD5密码
    public boolean isMDpwd (String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select " + COLUMN_USER_MD5 + " from " +
                TABLE_NAME + " where " + COLUMN_USER_NAME + "=?", new String[]{username});
        if (c.getCount() == 0) {
            c.close();
            return true;
        }

        if (c.moveToFirst()) {

            int isMD = c.getInt(c.getColumnIndex(COLUMN_USER_MD5));

            if (isMD == UserBean.MD5_NO) {
                c.close();
                return false;
            } else {
                c.close();
                return true;
            }
        }
        c.close();
        return false;
    }

    //加密密码为MD5
    public String encodePwd(String password)
    {
        //处理数据
        byte[] pwd_bytes = password.getBytes();
        int head_len = ( 4- ( pwd_bytes.length % 4 ) );
        byte[] pwd_en = new byte[head_len+pwd_bytes.length];
        System.arraycopy(pwd_bytes, 0, pwd_en, 0, pwd_bytes.length);
        if(head_len!=0){
            byte[] tail = {(byte)0xFF};
            for(int i=0;i<head_len;i++){
                System.arraycopy(tail, 0, pwd_en, pwd_bytes.length+i, tail.length);
            }
        }
        String pwd_str = getMD5(pwd_en);
        return pwd_str;

    }

//md5处理
    public String getMD5(byte[] info)
    {
        try
        {
          MessageDigest md5 = MessageDigest.getInstance("MD5");
          md5.update(info);
          byte[] encryption = md5.digest();

          StringBuffer strBuf = new StringBuffer();
          for (int i = 0; i < encryption.length; i++)
          {
              if (Integer.toHexString(0xff & encryption[i]).length() == 1)
              {
                  strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
              }
              else
              {
                  strBuf.append(Integer.toHexString(0xff & encryption[i]));
              }
          }
          return strBuf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
          return "";
        }
    }

    public boolean getShakeOpen(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select "+ COLUMN_USER_SHAKE_OPEN + " from "
                +TABLE_NAME + " where " + COLUMN_USER_NAME + "=?", new String[]{username});
        if (c.getCount() == 0) {
            c.close();
            return false;
        } else {
            if (c.moveToFirst()) {

                int state = c.getInt(c.getColumnIndex(COLUMN_USER_SHAKE_OPEN));
                if ( state == UserBean.SHAKE_ON) {
                    c.close();
                    return true;
                }
            }
            c.close();
            return false;
        }

    }

    //获取摇一摇距离
    public int getShakeDistance(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select "+ COLUMN_USER_SHAKE_OPEN_PROGRESS + " from "
              +TABLE_NAME + " where " + COLUMN_USER_NAME + "=?", new String[]{username});
        if (c.getCount() == 0) {
            c.close();
          return Constants.SHAKE_DEFAUT_PROGRESS;
        } else {
          if (c.moveToFirst()) {

              int progress = c.getInt(c.getColumnIndex(COLUMN_USER_SHAKE_OPEN_PROGRESS));
              c.close();
              return progress;
          }
          c.close();
          return Constants.SHAKE_DEFAUT_PROGRESS;
        }
    }

    public boolean getAutoOpen(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select "+ COLUMN_USER_AUTO_OPEN + " from "
                +TABLE_NAME + " where " + COLUMN_USER_NAME + "=?", new String[]{username});
        if (c.getCount() == 0) {
            c.close();
            return false;
        } else {
            if (c.moveToFirst()) {
                int state = c.getInt(c.getColumnIndex(COLUMN_USER_AUTO_OPEN));
                if ( state == UserBean.AUTO_OPEN_ON) {
                    c.close();
                    return true;
                }
            }
            c.close();
            return false;
        }

    }

    //获取自动开门距离
    public int getAutoDistance(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select "+ COLUMN_USER_AUTO_OPEN_PROGRESS + " from "
              +TABLE_NAME + " where " + COLUMN_USER_NAME + "=?", new String[]{username});
        if (c.getCount() == 0) {
            c.close();
          return Constants.AUTO_DEFAUT_PROGRESS;
        } else {
          if (c.moveToFirst()) {

              int progress = c.getInt(c.getColumnIndex(COLUMN_USER_AUTO_OPEN_PROGRESS));
              c.close();
              return progress;
          }
            c.close();
          return Constants.AUTO_DEFAUT_PROGRESS;
        }
    }

    public boolean isOpenSpaceEnable(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select "+ COLUMN_USER_ENABLE_AUTO_OPEN_SPACE + " from "
                +TABLE_NAME + " where " + COLUMN_USER_NAME + "=?", new String[]{username});
        if (c.getCount() == 0) {
            c.close();
            return false;
        } else {
            if (c.moveToFirst()) {
                int state = c.getInt(c.getColumnIndex(COLUMN_USER_ENABLE_AUTO_OPEN_SPACE));
                if ( state == UserBean.AUTO_OPEN_ON) {
                    c.close();
                    return true;
                }
            }
            c.close();
            return false;
        }

    }

    //获取自动开门距离
    public int getAutoOpenSpaceTime(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select "+ COLUMN_USER_AUTO_OPEN_SPACE_TIME + " from "
              +TABLE_NAME + " where " + COLUMN_USER_NAME + "=?", new String[]{username});
        if (c.getCount() == 0) {
            c.close();
          return Constants.AUTO_OPEN_SPACE_SECONDS_DEFAUT;
        } else {
          if (c.moveToFirst()) {

              int progress = c.getInt(c.getColumnIndex(COLUMN_USER_AUTO_OPEN_SPACE_TIME));
              c.close();
              return progress;
          }
            c.close();
          return Constants.AUTO_OPEN_SPACE_SECONDS_DEFAUT;
        }
    }

//    得到唯一的用户
    public UserBean getUser(String username)
    {
        if (username == null) {
            return null;
        }
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where "
            + COLUMN_USER_NAME + "=? ", new String[]{username});
        if (c.getCount() == 0) {
            return null;
        }
        UserBean user = null ;
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                user = getUserByCursor(c);
                c.moveToNext();
            }
        }
        c.close();
        return user;

    }

    private UserBean getUserByCursor(Cursor c)
    {
        UserBean  user = new UserBean();
        user.setClientID(c.getString(c.getColumnIndex(COLUMN_USER_CLIENT_ID)));
        //  		user.setCardNo(c.getString(c.getColumnIndex(COLUMN_USER_CARDNO)));
        user.setUserName(c.getString(c.getColumnIndex(COLUMN_USER_NAME)));
        user.setNickName(c.getString(c.getColumnIndex(COLUMN_USER_NICKNAME)));
        user.setPassWord(c.getString(c.getColumnIndex(COLUMN_USER_PWD)));
        user.setIdentity(c.getString(c.getColumnIndex(COLUMN_USER_IDENTITY)));
        user.setToken_pwd(c.getString(c.getColumnIndex(COLUMN_USER_TOKEN_PWD)));
        user.setPin(c.getString(c.getColumnIndex(COLUMN_USER_PIN)));
        user.setCardno(c.getString(c.getColumnIndex(COLUMN_USER_CARDNO)));
        return user;

    }

    public void setShakeOpen(String username, boolean state)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //设置
        if (state) {
            cv.put(COLUMN_USER_SHAKE_OPEN, UserBean.SHAKE_ON);
        } else {
            cv.put(COLUMN_USER_SHAKE_OPEN, UserBean.SHAKE_OFF);
        }
        //修改密码账户的名称
        String whereClause = COLUMN_USER_NAME + "=?";

        db.update(TABLE_NAME, cv, whereClause, new String[] {username});
    }

  //设置摇一摇距离
    public int setShakeDistance(String username, int progress)
    {
        int ret = -1;

        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //设置
        cv.put(COLUMN_USER_SHAKE_OPEN_PROGRESS, progress);
        String whereClause = COLUMN_USER_NAME + "=?";
        ret = db.update(TABLE_NAME, cv, whereClause, new String[] {username});
        return ret;
    }

    public void setAutoOpen(String username, boolean state)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //设置
        if (state) {
            cv.put(COLUMN_USER_AUTO_OPEN, UserBean.AUTO_OPEN_ON);
        } else {
            cv.put(COLUMN_USER_AUTO_OPEN, UserBean.AUTO_OPEN_OFF);
        }
        //修改密码账户的名称
        String whereClause = COLUMN_USER_NAME + "=?";
        db.update(TABLE_NAME, cv, whereClause, new String[] {username});
    }

    //设置摇一摇距离
    public int setAutoDistance(String username, int progress)
    {
        int ret = -1;

        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //设置距离
        cv.put(COLUMN_USER_AUTO_OPEN_PROGRESS, progress);
        String whereClause = COLUMN_USER_NAME + "=?";

        ret = db.update(TABLE_NAME, cv, whereClause, new String[] {username});
        return ret;
    }

    /**
     * 打开自动开门间隔时间开关
     * @param username
     * @param state
     */
    public void enableOpenSpace(String username, boolean state)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //设置开关
        if (state) {
            cv.put(COLUMN_USER_ENABLE_AUTO_OPEN_SPACE, UserBean.AUTO_OPEN_ON);
        } else {
            cv.put(COLUMN_USER_ENABLE_AUTO_OPEN_SPACE, UserBean.AUTO_OPEN_OFF);
        }
        //修改密码账户的名称
        String whereClause = COLUMN_USER_NAME + "=?";
        db.update(TABLE_NAME, cv, whereClause, new String[] {username});
    }

    /**
     * 设置自动开门的时间间隔
     * @param username 用户名
     * @param seconds 秒
     * @return
     */
    public int setAutoOpenSpaceTime(String username, int seconds)
    {
        int ret = -1;

        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //设置时间
        cv.put(COLUMN_USER_AUTO_OPEN_SPACE_TIME, seconds);
        String whereClause = COLUMN_USER_NAME + "=?";

        ret = db.update(TABLE_NAME, cv, whereClause, new String[] {username});
        return ret;
    }

  //保存wifi信息
    public int setWiFiInfo(String username, String server_ip, String server_port, String wifi_name, String wifi_pswd)
    {
        int ret = -1;
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_USER_NAME +"=?", new String[]{username});
        if (c.getCount() != 0) {
            ContentValues cv = new ContentValues();
            //设置
            cv.put(COLUMN_USER_SERVER_IP, server_ip);
            cv.put(COLUMN_USER_SERVER_PORT, server_port);
            cv.put(COLUMN_USER_WIFI_NAME, wifi_name);
            cv.put(COLUMN_USER_WIFI_PSWD, wifi_pswd);
            if (db.isOpen()) {
                String whereClause = COLUMN_USER_NAME +"=?";
                ret = db.update(TABLE_NAME, cv, whereClause, new String[]{ username} );
            }
        }
        c.close();
        return ret;
    }

    //获取wifi信息
    public Map<String , String> getWifiInfo(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        HashMap<String , String> wifiInfo = new HashMap<String , String>();
        if (db.isOpen()) {
            Cursor c = db.rawQuery("select * from "+TABLE_NAME
                  + " where " + COLUMN_USER_NAME +"=?", new String[]{username});
            if (c.getCount() != 0) {
                if(c.moveToFirst()){
                    String serverIP = c.getString(c.getColumnIndex(COLUMN_USER_SERVER_IP));
                    wifiInfo.put(COLUMN_USER_SERVER_IP, serverIP == null ? "" : serverIP);
                    String server_port = c.getString(c.getColumnIndex(COLUMN_USER_SERVER_PORT));
                    wifiInfo.put(COLUMN_USER_SERVER_PORT, server_port == null ? "" : server_port);
                    String wifi_name = c.getString(c.getColumnIndex(COLUMN_USER_WIFI_NAME));
                    wifiInfo.put(COLUMN_USER_WIFI_NAME, wifi_name == null ? "" : wifi_name);
                    String wifi_pswd = c.getString(c.getColumnIndex(COLUMN_USER_WIFI_PSWD));
                    wifiInfo.put(COLUMN_USER_WIFI_PSWD, wifi_pswd == null ? "" : wifi_pswd);
                    c.moveToNext();
                }
            }
            c.close();
        }
        return wifiInfo;
    }

//查询数据
//    public void queryUserData(SQLiteDatabase db){
//    	Cursor c = db.query(TABLE_NAME,null,null,null,null,null,null);//查询并获得游标
//
//    	if(c.getCount()==0){
//    		return;
//    	}
//
//    	if(c.moveToFirst()){
//            while(!c.isAfterLast()){
//            	//移动到指定记录
//                String username = c.getString(c.getColumnIndex("username"));
//                String password = c.getString(c.getColumnIndex("password"));
//                MyLog.e("username", username);
//                MyLog.e("password", password);
//                c.moveToNext();
//            }
//        }
//    }

  //获取用户卡号
    public String getCardno(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        String card_no = null;
        if (db.isOpen()) {
          Cursor c = db.rawQuery("select distinct " + COLUMN_USER_CARDNO + " from "+TABLE_NAME
                  + " where " + COLUMN_USER_NAME +"=?", new String[]{username});
          if (c.getCount() != 0) {
              if(c.moveToFirst()){
                  card_no = c.getString(c.getColumnIndex(COLUMN_USER_CARDNO));
                  c.moveToNext();
              }
          }
          c.close();
        }
        return card_no;
    }

  //获取用户identity    后期用于开门的唯一标识
    public String getIdentity(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        String identity = null;
        if (db.isOpen()) {
            Cursor c = db.rawQuery("select distinct " + COLUMN_USER_IDENTITY + " from "+TABLE_NAME
                  + " where " + COLUMN_USER_NAME +"=?", new String[]{username});
            if (c.getCount() != 0) {
                if(c.moveToFirst()){
                    identity = c.getString(c.getColumnIndex(COLUMN_USER_IDENTITY));
                    c.moveToNext();
                }

            }
            c.close();
        }
        return identity;
    }
  //更新密码
    public void updatePWD(String username, String password)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        //修改password
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_PWD, password);

        //修改密码账户的名称
        String whereClause = "username=?";
        String[] whereArgs ={ username };

        db.update(TABLE_NAME, cv, whereClause, whereArgs);
    }

    //更新卡号
    public void updateCard(String username, String card)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();
        //修改password
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_CARDNO, card);

        //修改密码账户的名称
        String whereClause = "username=?";
        String[] whereArgs ={ username };

        db.update(TABLE_NAME, cv, whereClause, whereArgs);
    }

    //删除账户
    public void deleteContact(String username)
    {
        SQLiteDatabase db = doormasterHelper.getWritableDatabase();

        if(db.isOpen()){
            db.delete(TABLE_NAME, COLUMN_USER_NAME + " = ?", new String[]{username});
        }
    }

}
