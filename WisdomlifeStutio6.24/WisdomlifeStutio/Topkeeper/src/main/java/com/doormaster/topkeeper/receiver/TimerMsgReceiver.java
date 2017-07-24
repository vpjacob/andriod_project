package com.doormaster.topkeeper.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Vibrator;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.Act_Main;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.MessageBean;
import com.doormaster.topkeeper.bean.UsersCardDom;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.constant.TimerMsgConstants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.MessageData;
import com.doormaster.topkeeper.db.UsersCardData;
import com.doormaster.topkeeper.task.UpLoadRecord;
import com.doormaster.topkeeper.utils.ConstantUtils;
import com.doormaster.topkeeper.utils.FunctionUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Call;


public class TimerMsgReceiver extends BroadcastReceiver {

	private static final String TAG = "TimerMsgReceiver";
	public static final String ACTION = "com.intelligoo.TimerMsgReceiver.ACTION";
	public static final int PERIOD = 30000;
	
	private static String put_url = null;
	private static Vibrator vibrator;
	private static final int VIBRATOR_MS = 300;
	NotificationManager notifyManager;
	Builder msgNotifyBuilde;
	int i=0;
	
	private String dbname_company = null;
	private int door_no = -1;
	
	//Extra信息
	public static final String NOTIFICATION_MSG = "com.intelligoo.activity.TimerMsgReceiver.NOTIFICATION_MSG";
	public static final String MSG_REC_EXTRA = "com.intelligoo.activity.TimerMsgReceiver.MSG_REC_Extra";
	public static final String CONTACTS_REC_EXTRA = "com.intelligoo.activity.TimerMsgReceiver.CONTACTS_REC_EXTRA";
	public static final String READER_CHANGED_EXTRA = "com.intelligoo.activity.TimerMsgReceiver.READER_CHANGED_EXTRA";
	public static final String TIMER_REC_START = "com.intelligoo.activity.TimerMsgReceiver.START";
	public static final String TIMER_REC_END = "com.intelligoo.activity.TimerMsgReceiver.END";

	private Context curContext;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		curContext = context;
		if (intent.getAction().equalsIgnoreCase(ACTION)) {
			String state = intent.getStringExtra("state");
			
			if (state == null) {
				return;
			}
			
			if (state.equalsIgnoreCase(TIMER_REC_START)) {
				scheduleAlarms(context,TIMER_REC_START);
				put_url = Constants.URL_BASE + Constants.UPDATE_URL;
				vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				msgNotifyBuilde = new Builder(context);
				getMessage();
				new Thread(new Runnable() {
					public void run() {						
						UpLoadRecord.upLoadOpenRecord();
					}
				}).start();
			} else if (intent.getStringExtra("state").equalsIgnoreCase(TIMER_REC_END)) {
				scheduleAlarms(context,TIMER_REC_END);
			}
		}
	}
	
	//删除过期设备
	private void deleteOverDueKey(String servertime) {
		if (servertime == null)
		{
			return;
		}
		AccessDevMetaData deviceData = new AccessDevMetaData(curContext);
		String username = SPUtils.getString(Constants.USERNAME);
		ArrayList<AccessDevBean> deviceList = deviceData.getAllAccessDevList(username);
		SimpleDateFormat sdf_set = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		try {
			Date date = sdf_set.parse(servertime);
			TimeZone tz = TimeZone.getDefault();
			long tzDif = tz.getRawOffset();
//			long date_ms = date.getTime() + 8*3600*1000;
			long date_ms = date.getTime() + tzDif;//转成相应手机时区对应的时间
			Date curr_date = new Date(date_ms);
			servertime = sdf_set.format(curr_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long current_time = Long.parseLong(servertime);
		if (deviceList == null || deviceList.size() <= 0)
		{
			return;
		}
		for (AccessDevBean device : deviceList)
		{
			String startDate = device.getStartDate();
			String endDate = device.getEndDate();
			if (endDate == null || endDate.isEmpty() ||
					startDate == null || endDate.isEmpty() )
			{
				continue;
			}
			long limit_start_time = Long.parseLong(startDate);
			long limit_end_time = Long.parseLong(endDate);
			LogUtils.d("start:" + limit_start_time + "end:" + limit_end_time);
			if (current_time < limit_start_time )
			{
				device.setEnable(AccessDevBean.NOT_ARRIVED);
				LogUtils.d(device.toString());
				deviceData.saveAccessDev(device);
			}
			if (current_time > limit_end_time )
			{
				device.setEnable(AccessDevBean.OUT_DATE);
				LogUtils.d(device.toString());
				deviceData.saveAccessDev(device);
			}
			if(current_time > limit_start_time  && current_time < limit_end_time)
			{
				device.setEnable(AccessDevBean.USEFUL);
				LogUtils.d(device.toString());
				deviceData.saveAccessDev(device);
			}
		}
	}

	public static void scheduleAlarms(Context context, String state)
	{
		if (FunctionUtils.httpConnectServer)
		{
			AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent intentRec = new Intent(context,TimerMsgReceiver.class);
			intentRec.setAction(ACTION);
			intentRec.putExtra("state", state);
			PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intentRec, 0);
			manager.cancel(pIntent);
			if (state.equalsIgnoreCase(TIMER_REC_START)) {
				manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + PERIOD, pIntent);
			}
		}
	}
	
	public void getMessage()
	{
		final MessageData messageData = new MessageData(curContext.getApplicationContext());

		String client_id = SPUtils.getString(Constants.CLIENT_ID,curContext);
		String language = Locale.getDefault().getLanguage();		//得到当前语言
//判断语言是否为中文简体
		if(language.equals("zh"))
		{
			language = "zh-CN";
		}
		else if(language.equals("zh-CN"))
		{
			language = "zh-CN";
		}
		else
		{
			language = "en";
		}

		//查询登录状态
//		if(!BaseApplication.getCompleteLogin()){
//			return;
//		}
		OkhttpHelper.getSystemMsg(language, client_id, new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {
				LogUtils.d("onErroe" +e.toString());
				Toast.makeText(curContext, curContext.getString(R.string.check_network_error), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(String response) {
				try {
					JSONObject getMsg_ret = new JSONObject(response);
					if(!getMsg_ret.isNull(TimerMsgConstants.RET))
					{
						int ret = getMsg_ret.getInt(TimerMsgConstants.RET);
						LogUtils.d("getMsg_ret: "+ getMsg_ret.toString());

						if(ret == 0){
							if (!getMsg_ret.isNull(TimerMsgConstants.RESOURCE))
							{
								dealHttpCallback(messageData, getMsg_ret);
							}
							if (!getMsg_ret.isNull(TimerMsgConstants.SERVER_DATE))
							{
								String server_time = getMsg_ret.getString(TimerMsgConstants.SERVER_DATE);
								LogUtils.d(""+server_time);
								SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
								Date date = format.parse(server_time);
								server_time = sdf.format(date);
								deleteOverDueKey(server_time);
							}

						}else if (ret == Constants.CLIENT_ID_NO_EXIST)
						{
//							Toast.makeText(curContext, R.string.login_in_another_phone, Toast.LENGTH_SHORT).show();
							SPUtils.put(Constants.IS_AUTOLOGIN, false, curContext);
//							ActivityManager.getInstance().clearAllActivity();
//							Intent intent = new Intent(curContext, Act_Login.class);
//							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							curContext.startActivity(intent);
							DMVPhoneModel.exit();
//							Act_Main.closeMain();
						} else if (ret == Constants.NETWORD_SHUTDOWN)
						{
							LogUtils.d("newwork shutdown");
						}
						else
						{
							Toast.makeText(curContext, R.string.failed_to_get_msg, Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						LogUtils.d("getMsg_ret null");
					}
				} catch (JSONException | ParseException e) {
					LogUtils.d("error" +e.toString());
					e.printStackTrace();
				}
			}
		});

	}
	
	private void dealHttpCallback(final MessageData messageData,
			JSONObject getMsg_ret) throws JSONException {
		if(getMsg_ret.getString(TimerMsgConstants.RESOURCE).equals(TimerMsgConstants.MSG)){
			JSONArray data = getMsg_ret.getJSONArray(TimerMsgConstants.DATA);
			if(data.length()>0){
				vibrator.vibrate(VIBRATOR_MS);
				dealMsg(SPUtils.getString(Constants.CLIENT_ID),
						messageData, data);
			}else{
				//data_msg
				LogUtils.i(TAG, "data_msg is null");
			}	
		}else if(getMsg_ret.getString(TimerMsgConstants.RESOURCE).equals(TimerMsgConstants.READER)){
			JSONArray data = getMsg_ret.getJSONArray(TimerMsgConstants.DATA);
			if(data.length()>0){
				vibrator.vibrate(VIBRATOR_MS);
				dealReader(SPUtils.getString(Constants.CLIENT_ID), data);
			}else {
				//data_reader
				LogUtils.i(TAG, "data_reader is null");
			}
		}
	}

	private void dealMsg(String client_id, final MessageData messageData, JSONArray jsonMsg) throws JSONException {

//		MyLog.Assert(client_id != null);
//		MyLog.Assert(messageData != null);
//		MyLog.Assert(jsonMsg != null);
		
		JSONArray status = new JSONArray();
		
		for(int i=0;i<jsonMsg.length();i++)
		{	
			int comm_id = jsonMsg.getJSONObject(i).getInt(TimerMsgConstants.COMM_ID);
			int show_msg = jsonMsg.getJSONObject(i).getInt(TimerMsgConstants.MSG_SHOW_MSG);
			String sender = jsonMsg.getJSONObject(i).getString(TimerMsgConstants.MSG_SENDER);
			String send_time = jsonMsg.getJSONObject(i).getString(TimerMsgConstants.MSG_SEND_TIME);
			send_time = formatTime(send_time);
			String content = jsonMsg.getJSONObject(i).getString(TimerMsgConstants.MSG_CONTENT);
			JSONObject attach_content = jsonMsg.getJSONObject(i).getJSONObject(TimerMsgConstants.MSG_ATTACH_CONTENT);
			String door_no = "";
			String qrcode_img = null;
			if (attach_content!=null) {
				
				if (!attach_content.isNull(TimerMsgConstants.MSG_ATTACH_AUTO_UPLOAD)) {
					LogUtils.i(TAG, "MSG_ATTACH_AUTO_UPLOAD");
//					curContext.setUploadOpenEvent(attach_content.getInt(TimerMsgConstants.MSG_ATTACH_AUTO_UPLOAD));
				}
				
				if (!attach_content.isNull(TimerMsgConstants.MSG_ATTACH_DEL_USERINFO)) {
					/*DeviceData deviceData = new DeviceData(curContext);
					String username = SPUtils.getString(Constants.USERNAME);
					deviceData.clearAllDevice(username);
					UserData userData = new UserData(curContext.getApplicationContext());
					userData.updateCard(SPUtils.getString(Constants.USERNAME), "");*/
					LogUtils.i(TAG, "MSG_ATTACH_DEL_USERINFO");
					String dbname_company = attach_content.getString(TimerMsgConstants.MSG_ATTACH_DEL_USERINFO);
					String username = SPUtils.getString(Constants.USERNAME);
//					//删除指定管理账号的用户卡信息
					UsersCardData usersCardData = new UsersCardData(curContext);
					usersCardData.deleUsersCardData(username, dbname_company);
//					//删除指定管理账号的设备信息
					AccessDevMetaData deviceData = new AccessDevMetaData(curContext);
					deviceData.deleteAllDeviceFromDbcompany(username, dbname_company);
				}
				
				if (!attach_content.isNull(TimerMsgConstants.MSG_ATTACH_NEW_CARDNO)) {
					LogUtils.i(TAG, "MSG_ATTACH_NEW_CARDNO");

					String cardno = attach_content.getString(TimerMsgConstants.MSG_ATTACH_NEW_CARDNO);
					AccessDevMetaData devData = new AccessDevMetaData(curContext);
					String username = SPUtils.getString(Constants.USERNAME);
					devData.updateUserCardno(username, cardno);
				}
				
				if (!attach_content.isNull(TimerMsgConstants.MSG_ATTACH_UPDATE_DEV)) 
				{
					LogUtils.i(TAG, "MSG_ATTACH_UPDATE_DEV");

					JSONObject update_dev = attach_content.getJSONObject("update_dev");
					if (!update_dev.isNull(TimerMsgConstants.READER_DEV_SN) &&
							!update_dev.isNull(TimerMsgConstants.READER_DEV_MAC))
					{
						updateDevName(update_dev.getString(TimerMsgConstants.READER_DEV_SN),
								update_dev.getString(TimerMsgConstants.READER_DEV_MAC));
					}
					if(!update_dev.isNull(TimerMsgConstants.READER_DEV_SN))
					{
						String dev_sn = update_dev.getString(TimerMsgConstants.READER_DEV_SN);
						String dev_name = null;
						String dbname_company = null;
						int doorno = -1;
						if(!update_dev.isNull(TimerMsgConstants.READER_DEV_NAME))
						{
							dev_name = update_dev.getString(TimerMsgConstants.READER_DEV_NAME);
						}
						if(!update_dev.isNull(TimerMsgConstants.READER_DBNAME_COMPANY))
						{
							dbname_company = update_dev.getString(TimerMsgConstants.READER_DBNAME_COMPANY);
						}
						if(!update_dev.isNull(TimerMsgConstants.READER_DOOR_NO))
						{
							doorno = update_dev.getInt(TimerMsgConstants.READER_DOOR_NO);
						}
						AccessDevMetaData deviceData = new AccessDevMetaData(curContext);
						deviceData.updateDevice(dev_sn, dev_name, dbname_company, doorno);

					}
				}
				
				if (!attach_content.isNull(TimerMsgConstants.MSG_ATTACH_REQUEST_OPEN)) {
					LogUtils.i(TAG, "MSG_ATTACH_REQUEST_OPEN");
					door_no = attach_content.getString(TimerMsgConstants.MSG_ATTACH_REQUEST_OPEN);
				}
				
				if (!attach_content.isNull(TimerMsgConstants.MSG_ATTACH_QRCODE_IMG)) 
				{
					LogUtils.i(TAG, "MSG_ATTACH_QRCODE_IMG");
					qrcode_img = attach_content.getString(TimerMsgConstants.MSG_ATTACH_QRCODE_IMG);
					LogUtils.d("-----test" +qrcode_img);
				}
				if(!attach_content.isNull(TimerMsgConstants.MSG_ATTACH_CARD_SECTION_KEY))
				{
					LogUtils.i(TAG, "MSG_ATTACH_CARD_SECTION_KEY");
					JSONObject card_section_key = attach_content.getJSONObject(TimerMsgConstants.MSG_ATTACH_CARD_SECTION_KEY);
					/*if(!card_section_key.isNull(TimerMsgConstants.MSG_ATTACH_DBNAME_COMPANY) && 
							!card_section_key.isNull(TimerMsgConstants.MSG_ATTACH_CARDNO) &&
							!card_section_key.isNull(TimerMsgConstants.MSG_ATTACH_SECTION) && 
							!card_section_key.isNull(TimerMsgConstants.MSG_ATTACH_SECTIONKEY))
					{
						UsersCardDom usersCardDom =  new UsersCardDom();
						String username = SPUtils.getString(Constants.USERNAME);
						usersCardDom.setUsername(username);
						String cardno = card_section_key.getString(TimerMsgConstants.MSG_ATTACH_CARDNO);
						usersCardDom.setCardno(cardno);
						String dbname_company = card_section_key.getString(TimerMsgConstants.MSG_ATTACH_DBNAME_COMPANY);
						usersCardDom.setDbname_company(dbname_company);
						int section = card_section_key.getInt(TimerMsgConstants.MSG_ATTACH_SECTION);
						usersCardDom.setSection(section);
						String sectionKey = card_section_key.getString(TimerMsgConstants.MSG_ATTACH_SECTIONKEY);
						usersCardDom.setSection_key(sectionKey);
						//保存用户卡信息
						UsersCardData usersCardData = new UsersCardData(curContext);
						usersCardData.saveUsersCardData(usersCardDom);
					}*/
					UsersCardDom usersCardDom =  new UsersCardDom();
					String username = SPUtils.getString(Constants.USERNAME);
					usersCardDom.setUsername(username);
					String cardno = null;
					String dbname_company = null;
					int section = -1;
					String sectionKey = null;
					if(!card_section_key.isNull(TimerMsgConstants.MSG_ATTACH_DBNAME_COMPANY))
					{
						dbname_company = card_section_key.getString(TimerMsgConstants.MSG_ATTACH_DBNAME_COMPANY);
					}
					if(!card_section_key.isNull(TimerMsgConstants.MSG_ATTACH_CARDNO))
					{
						cardno = card_section_key.getString(TimerMsgConstants.MSG_ATTACH_CARDNO);
					}
					if(!card_section_key.isNull(TimerMsgConstants.MSG_ATTACH_SECTION))
					{
						section = card_section_key.getInt(TimerMsgConstants.MSG_ATTACH_SECTION);
					}
					if(!card_section_key.isNull(TimerMsgConstants.MSG_ATTACH_SECTIONKEY))
					{
						sectionKey = card_section_key.getString(TimerMsgConstants.MSG_ATTACH_SECTIONKEY);
					}
					usersCardDom.setCardno(cardno);
					usersCardDom.setDbname_company(dbname_company);
					usersCardDom.setSection(section);
					usersCardDom.setSection_key(sectionKey);
//					//保存用户卡信息
					UsersCardData usersCardData = new UsersCardData(curContext);
					usersCardData.saveUsersCardData(usersCardDom);

					//benson变更卡号、扇区地址测试
					UsersCardDom us = usersCardData.getUsersCardDom(username, dbname_company);
					us.getCardno();
				}
			}
			if (show_msg != 0) {
				Intent intentMain = new Intent(curContext,Act_Main.class);
				intentMain.putExtra(NOTIFICATION_MSG, 30);
				intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intentMain.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				PendingIntent intent = PendingIntent.getActivity(curContext,
										0,intentMain , 0);
				long time = System.currentTimeMillis();
				msgNotifyBuilde.setWhen(time);
				msgNotifyBuilde.setContentText(content);
				msgNotifyBuilde.setSmallIcon(ConstantUtils.getIconRes());
				msgNotifyBuilde.setTicker(curContext
						.getResources().getString(R.string.remind));
				msgNotifyBuilde.setDefaults(Notification.DEFAULT_LIGHTS);
				if (sender.equals("System")||sender.equals("手机门管家")) {
					sender = curContext.getString(R.string.app_name);
				}
				msgNotifyBuilde.setContentTitle(sender);
				msgNotifyBuilde.setContentIntent(intent);
				
				Notification msgNotify = msgNotifyBuilde.build();
				notifyManager.notify(1, msgNotify);

				String username = SPUtils.getString(Constants.USERNAME);
				//msg数据存储
				MessageBean message = new MessageBean();
				
				message.setUsername(username);
				message.setId(comm_id);
				message.setSender(sender);
				message.setSendTime(send_time);
				message.setContent(content);
				message.setDoorNo(door_no);
				message.setRead(MessageBean.NOT_READ);
				if (qrcode_img != null && qrcode_img.length() != 0) 
				{					
					message.setImageType(MessageBean.MSG_IMG_QRCODE);
					message.setImageContent(qrcode_img);
				}
				else 
				{
					message.setImageType(MessageBean.MSG_IMG_NULL);
					message.setImageContent(null);
				}
				messageData.saveMessageData(message);
				
				//发送更新UI广播
				Intent msgIntent = new Intent(MSG_REC_EXTRA);
				msgIntent.putExtra("sender", sender);
				curContext.sendBroadcast(msgIntent);
			}
			//设置传输参数值
			JSONObject updata_msg = new JSONObject();
			updata_msg.put(TimerMsgConstants.COMM_ID,comm_id);
			updata_msg.put(TimerMsgConstants.RET, 0);
			updata_msg.put(TimerMsgConstants.RET_MSG, "ok");
			status.put(updata_msg);
		}
		if(status.length()>0){
			JSONObject result = new JSONObject();
			result.put(TimerMsgConstants.CLIENT_ID, client_id);
			result.put(TimerMsgConstants.STATUS, status);
			result.put(TimerMsgConstants.RESOURCE, "msg");

			LogUtils.d("dealMsgRet"+ result.toString());
			updataState(result);
		}
	}
	
	private void updateDevName(String dev_sn, String dev_name)
	{
		String username = SPUtils.getString(Constants.USERNAME);
		AccessDevMetaData deviceData = new AccessDevMetaData(curContext);
		AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);

		device.setDevName(dev_name);
		deviceData.saveAccessDev(device);


	}

	//格式化时间
	private String formatTime(String send_time) {
		SimpleDateFormat sdf_rec = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
		SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		try {
			Date date = sdf_rec.parse(send_time);
			TimeZone tz = TimeZone.getDefault();
			long tzDif = tz.getRawOffset();
//			long send_time_long = date.getTime() + 8*3600*1000;
			long send_time_long = date.getTime() + tzDif;//转成相应手机时区对应的时间
			Date curr_date = new Date(send_time_long);
			send_time = sdf_format.format(curr_date);
		} catch (ParseException e) {
			LogUtils.d("msg send time error");
		}
		return send_time;
	}
	
	private void dealReader(String client_id, JSONArray dataArray) throws JSONException
	{
		JSONArray status = new JSONArray();
		LogUtils.d("data: "+ dataArray.toString());
		for(int i=0;i<dataArray.length();i++){
			//data全局参数
			JSONObject dataObj = dataArray.getJSONObject(i);
			int comm_id = dataObj.getInt(TimerMsgConstants.COMM_ID);
			String operation = dataObj.getString(TimerMsgConstants.READER_OPERATION);
			String dev_mac_del = dataObj.getString(TimerMsgConstants.READER_DEV_MAC);
			String dev_sn = dataObj.getString(TimerMsgConstants.READER_DEV_SN);
			int dev_id = dataObj.getInt(TimerMsgConstants.READER_DEV_ID);
			String username = SPUtils.getString(Constants.USERNAME);
			if(!dataObj.isNull(TimerMsgConstants.READER_DBNAME_COMPANY))
			{
				dbname_company = dataObj.getString(TimerMsgConstants.READER_DBNAME_COMPANY);
			}
			if(!dataObj.isNull(TimerMsgConstants.READER_DOOR_NO))
			{
				door_no = dataObj.getInt(TimerMsgConstants.READER_DOOR_NO);
			}
			if(operation!=null && operation.equalsIgnoreCase(TimerMsgConstants.READER_OPERATION_ADD)){
				//设置reader全局dev_name和dev_type
				String dev_name = dataObj.getString(TimerMsgConstants.READER_SHOW_NAME);
				JSONArray readerArray = dataObj.getJSONArray(TimerMsgConstants.READER);
				for(int j = 0;j<readerArray.length();j++){
					LogUtils.d("reader" + readerArray.toString());
					JSONObject readerObj = readerArray.getJSONObject(j);
					saveDevice(dev_sn, dev_id, dev_name,
							readerObj);
				}//for
			}else if(operation!=null && operation.equalsIgnoreCase("delete")){
				deleteDevice(dev_mac_del, dev_sn, username);
			}
			Intent intent = new Intent(READER_CHANGED_EXTRA);
			curContext.sendBroadcast(intent);
			//result key 返回
			JSONObject updata_reader = new JSONObject();
			updata_reader.put(TimerMsgConstants.COMM_ID,comm_id);
			updata_reader.put(TimerMsgConstants.RET,0);
			updata_reader.put(TimerMsgConstants.MSG, "ok");
			status.put(updata_reader);
			LogUtils.d("status: "+status.toString());
			
		}
		if(status.length() > 0){
			JSONObject result = new JSONObject();
			result.put(TimerMsgConstants.CLIENT_ID, client_id);
			result.put(TimerMsgConstants.STATUS, status);
			result.put(TimerMsgConstants.RESOURCE, TimerMsgConstants.READER);
			
			LogUtils.d("result: " +result.toString());
			updataState(result);
		}
	}

	private void deleteDevice(String dev_mac_del, String dev_sn, String username)
	{
		AccessDevBean device = new AccessDevBean();
		AccessDevMetaData deviceData = new AccessDevMetaData(curContext);
		device.setUsername(username);
		device.setDevSn(dev_sn);
		device.setDevMac(dev_mac_del);
//		device.setDbname_company(dbname_company);
		deviceData.deleteDevice(username, device);
	}

	private void saveDevice(String dev_sn, int dev_id, String dev_name, JSONObject readerObj) throws JSONException{

		AccessDevMetaData deviceData = new AccessDevMetaData(curContext);
		if (readerObj.isNull(TimerMsgConstants.READER_SN) || readerObj.isNull(TimerMsgConstants.READER_MAC)
			|| readerObj.isNull(TimerMsgConstants.READER_PRIVILEGE) || readerObj.isNull(TimerMsgConstants.READER_USE_COUNT)
			|| readerObj.isNull(TimerMsgConstants.READER_VERIFIED) || readerObj.isNull(TimerMsgConstants.READER_OPEN_TYPE)
			|| readerObj.isNull(TimerMsgConstants.READER_EKEY) || readerObj.isNull(TimerMsgConstants.READER_ENCRYPTION) ||
			readerObj.isNull(TimerMsgConstants.READER_DEV_FUNCTION))
		{
			Toast.makeText(curContext,R.string.server_error, Toast.LENGTH_SHORT).show();
			String keys [] = {TimerMsgConstants.READER_SN,
					TimerMsgConstants.READER_MAC,
					TimerMsgConstants.READER_PRIVILEGE,
					TimerMsgConstants.READER_USE_COUNT,
					TimerMsgConstants.READER_VERIFIED,
					TimerMsgConstants.READER_OPEN_TYPE,
					TimerMsgConstants.READER_EKEY,
					TimerMsgConstants.READER_ENCRYPTION,
					TimerMsgConstants.READER_DEV_FUNCTION};
			for (int i=0; i<keys.length;i++)
			{
				if (readerObj.isNull(keys[i]))
				{
					LogUtils.d(String.format("%s is empty", keys[i]));
				}
			}
		} else {

			int privilege = readerObj.getInt(TimerMsgConstants.READER_PRIVILEGE);
			int dev_type = readerObj.getInt(TimerMsgConstants.READER_DEV_TYPE);
			//设备操作/有效时间/距离/操作方式
			int support_wifi = AccessDevBean.WIFI_UNENABLE;
			if (!readerObj.isNull(TimerMsgConstants.READER_SUPPORT_WIFI))
				support_wifi = readerObj.getInt(TimerMsgConstants.READER_SUPPORT_WIFI);

			//Reader信息
			if ((dev_type != AccessDevBean.DEV_TYPE_ACCESS_CONTROLLER)
					&& (readerObj.isNull(TimerMsgConstants.READER_CARD_NO)))
			{
				LogUtils.d( "device to not exist cardno ");
				return;
			}
			String reader_sn = readerObj.getString(TimerMsgConstants.READER_SN);
			int dev_res = AccessDevBean.DEV_FROM_DISTRIBUTE;
			String dev_mac = readerObj.getString(TimerMsgConstants.READER_MAC);
			String dev_password = readerObj.getString(TimerMsgConstants.READER_EKEY);
			if (dev_mac == null || dev_mac.length() <= 0)
			{
				LogUtils.d( "device to not exist return; ");
				return;
			}
			else if( dev_password == null
				|| dev_password.length() <= 0 )
			{
				LogUtils.d( "device to not exist dev_password ");
				return;
			}
			else if ( reader_sn ==  null || reader_sn.length() <= 0 ) {
				LogUtils.d( "device to not exist reader_sn ");
				return;
			}
			String eKey = readerObj.getString(TimerMsgConstants.READER_EKEY);
			LogUtils.d("encryption" + readerObj.getInt(TimerMsgConstants.READER_ENCRYPTION) + " res: " +dev_res);
			AccessDevBean device = new AccessDevBean();
			device.setUsername(SPUtils.getString(Constants.USERNAME));
			device.setDevType(dev_type);
//			device.setDevRes(Integer.parseInt(Ekey.getRes(eKey, false)));
//			device.setDevResPhone(Ekey.getResIdentity(eKey, false));
			privilege = getNativePri(SPUtils.getString(Constants.USERNAME),dev_sn, dev_mac,privilege);
			device.setPrivilege(privilege);
			device.setDevSn(dev_sn);
			device.setDevMac(dev_mac);
			device.setDevName(dev_name);
//			device.setDevPassword(Ekey.getDevKey(eKey, false));
			device.setStartDate(readerObj.getString(TimerMsgConstants.READER_START_DATE));
			device.setEndDate(readerObj.getString(TimerMsgConstants.READER_END_DATE));
			device.setUseCount(readerObj.getInt(TimerMsgConstants.READER_USE_COUNT));
			device.setVerified(readerObj.getInt(TimerMsgConstants.READER_VERIFIED));
			device.setOpenType(readerObj.getInt(TimerMsgConstants.READER_OPEN_TYPE));
			device.setCardno(readerObj.getString(TimerMsgConstants.READER_CARD_NO));
			device.setFunction(readerObj.getString(TimerMsgConstants.READER_DEV_FUNCTION));
//			device.setDevId(dev_id);
//			device.setReaderSn(reader_sn);
			device.seteKey(eKey);
			device.setNetWorkSupport(support_wifi);
			device.setDbname_company(dbname_company);
			device.setDoor_no(door_no);
			LogUtils.d( "device to save: "+device.toString());
			deviceData.saveAccessDev(device);
		}
	}
	
	private int getNativePri(String username, String dev_sn, String dev_mac,
                             int privilege)
	{
		AccessDevMetaData deviceData = new AccessDevMetaData(curContext);
		AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		if (device == null)
		{
			return privilege;
		}
		if (device.getPrivilege() == AccessDevBean.DEV_PRIVILEGE_SUPER) {
			return device.getPrivilege();
		} else {
			return privilege;
		}
	}

	//更新实时消息状态
	public static void updataState(final JSONObject result){
		OkhttpHelper.updateMsg(result, new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {

			}

			@Override
			public void onResponse(String response) {
				try {
					JSONObject updata_ret = new JSONObject(response);
					LogUtils.d("update_ret: "+updata_ret.toString());
					if(!updata_ret.isNull("ret")){

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
//		Thread updateThread = new Thread(new Runnable() {
//			public void run() {
//				JSONObject updata_ret= MyHttpClient.connectPut(put_url,result);
//				LogUtils.d("update_ret: "+updata_ret.toString());
//				if(updata_ret!=null && !updata_ret.isNull("ret")){
//
//				}else{
//
//				}
//			}
//		});
//		updateThread.start();
			
	}

}
