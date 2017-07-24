package com.doormaster.topkeeper.utils;

import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.UserBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.constant.TimerMsgConstants;
import com.doormaster.topkeeper.db.UserData;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class RequestTool {

	/**
	 *  备份设备电子钥匙
	 * @param devDom : DeviceDom 对象
	 * @return ret: int 类型 ，操作结果
	 */
	public static int backupDevEkey(AccessDevBean devDom) {
		try {
			final JSONObject key_json = new JSONObject();
			key_json.put(TimerMsgConstants.CLIENT_ID, BaseApplication.getInstance().getClientId());
			key_json.put(TimerMsgConstants.RESOURCE, TimerMsgConstants.KEY);
			key_json.put(TimerMsgConstants.OPERATION, "PUT");
			JSONArray dataArray = convertBackDevEkey(devDom);
			if (dataArray == null || dataArray.length() <= 0) {
				return -1001;
			}
			key_json.put(TimerMsgConstants.DATA,  dataArray);
			LogUtils.d("key_json" + key_json.toString());

			String backup_url = Constants.URL_POST_COMMANDS;
//			JSONObject backup_ret = MyHttpClient.connectPost(backup_url, key_json);
			OkhttpHelper.upLoadRecord(backup_url, key_json.toString(), new StringCallback() {
				@Override
				public void onError(Call call, Exception e) {

				}

				@Override
				public void onResponse(String response) {
					try {
						JSONObject backup_ret = new JSONObject(response);

						if (backup_ret.length() > 0) {
							LogUtils.d("method:backupDevEkey,Backup_ret:"
									+ backup_ret.toString());
//							return backup_ret.getInt(TimerMsgConstants.RET);
						} else {
//							return 10061; //
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			return 0;
		} catch (JSONException e) {
			e.printStackTrace();
			return -1500;
		}
	}

	// 转化传入的参数数据为 JsonArray格式返回
	private static JSONArray convertBackDevEkey(final AccessDevBean device)  {
		JSONArray dataArray = new JSONArray();
		JSONObject keyJson = new JSONObject();
		try
		{
//			MyLog.debug("getBackupData:"+device.toString());
			keyJson.put(TimerMsgConstants.COMM_ID, 1);
			String username = SPUtils.getString(Constants.USERNAME);
			UserData userData = new UserData(BaseApplication.getContext());
			UserBean user = userData.getUser(username);
			if (device.getDevName() != null && device.getDevName().length() > 0) {
				keyJson.put(TimerMsgConstants.KEY_DEV_NAME, device.getDevName());
			} else {
				keyJson.put(TimerMsgConstants.KEY_DEV_NAME, device.getDevSn());
			}
			keyJson.put(TimerMsgConstants.KEY_DEV_SN, device.getDevSn());
			keyJson.put(TimerMsgConstants.KEY_DEV_MAC, device.getDevMac());
			keyJson.put(TimerMsgConstants.KEY_EKEY, device.geteKey());
			keyJson.put(TimerMsgConstants.KEY_DEV_TYPE, device.getDevType());
			keyJson.put(TimerMsgConstants.KEY_DEV_PRI, device.getPrivilege());
			
//			keyJson.put(TimerMsgConstants.KEY_RES_INDENTITY,device.getDevResPhone());
			keyJson.put(TimerMsgConstants.KEY_RES_INDENTITY, user.getIdentity());
			
			keyJson.put(TimerMsgConstants.KEY_START_DATE, device.getStartDate());
			keyJson.put(TimerMsgConstants.KEY_END_DATE, device.getEndDate());
			keyJson.put(TimerMsgConstants.KEY_USER_COUNT, device.getUseCount());
			keyJson.put(TimerMsgConstants.KEY_OPEN_TYPE, device.getOpenType());
			keyJson.put(TimerMsgConstants.KEY_VERIFIED, device.getVerified());
			keyJson.put(TimerMsgConstants.KEY_OPEN_PWD, "");
			
			dataArray.put(keyJson);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return dataArray;
	}
	
	/*
	 * 删除一体机设备的服务器备份
	 * */
	public static void deleteDevice(AccessDevBean device) throws JSONException
	{
		JSONObject json = new JSONObject();
		String client_id = BaseApplication.getInstance().getClientId();
		final String username = SPUtils.getString(Constants.USERNAME);
		json.put(TimerMsgConstants.CLIENT_ID, client_id);
		json.put(TimerMsgConstants.RESOURCE, TimerMsgConstants.KEY);
		json.put(TimerMsgConstants.OPERATION, "DELETE");
		JSONArray data = getDelData(username,device.getDevSn());
		json.put(TimerMsgConstants.DATA, data);
		String del_url = Constants.URL_POST_COMMANDS;
//		JSONObject delete_ret = MyHttpClient.connectPost(del_url, json);
		OkhttpHelper.upLoadRecord(del_url, json.toString(), new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {

			}

			@Override
			public void onResponse(String response) {
				try {
					JSONObject backup_ret = new JSONObject(response);

					if (backup_ret.length() > 0) {
						LogUtils.d("method:backupDevEkey,Backup_ret:"
								+ backup_ret.toString());
//							return backup_ret.getInt(TimerMsgConstants.RET);
					} else {
//							return 10061; //
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//删除一体机数据请求的Json格式
	private static JSONArray getDelData(String username, String dev_sn) throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray data = new JSONArray();
		json.put(TimerMsgConstants.COMM_ID, 1);
		json.put(TimerMsgConstants.KEY_DEV_SN, dev_sn);
		json.put(TimerMsgConstants.KEY_RECIEVER, username);
		data.put(json);
		return data;
	}
	
	
	//获取服务器卡号
	public static void getCardList(String devSn, String model,StringCallback callback) throws JSONException
	{
		String client_id = BaseApplication.getInstance().getClientId();
		JSONObject getCardReqJson = new JSONObject();
		getCardReqJson.put(TimerMsgConstants.CLIENT_ID, client_id);
		getCardReqJson.put(TimerMsgConstants.RESOURCE, "cardno");
		getCardReqJson.put(TimerMsgConstants.OPERATION, "GET");
		JSONObject data = new JSONObject();
		data.put("dev_sn", devSn);
		data.put("cmd_status", model);
		data.put("limit", 2000);
		getCardReqJson.put(TimerMsgConstants.DATA, data);
		LogUtils.d(getCardReqJson.toString());
		String get_card_url = Constants.URL_POST_COMMANDS;
//		JSONObject getCard_ret = MyHttpClient.connectPost( get_card_url, getCardReqJson);
		OkhttpHelper.upLoadRecord(get_card_url, getCardReqJson.toString(), callback);
	}

	public static void updateServiceCardList(String devSn, String last_time, String model,StringCallback callback) throws JSONException
	{
		String client_id = BaseApplication.getInstance().getClientId();
		JSONObject getCardReqJson = new JSONObject();
		getCardReqJson.put(TimerMsgConstants.CLIENT_ID, client_id);
		getCardReqJson.put(TimerMsgConstants.RESOURCE, "cardno");
		getCardReqJson.put(TimerMsgConstants.OPERATION, "PUT");
		JSONObject data = new JSONObject();
		data.put("dev_sn", devSn);
		data.put("last_time", last_time);
		data.put("cmd_status", model);
		data.put("limit", 2000);
		getCardReqJson.put(TimerMsgConstants.DATA, data);
		LogUtils.d(getCardReqJson.toString());
		String get_card_url = Constants.URL_POST_COMMANDS;
//		JSONObject getCard_ret = MyHttpClient.connectPost(get_card_url, getCardReqJson);
		OkhttpHelper.upLoadRecord(get_card_url, getCardReqJson.toString(), callback);
	}
	
}
