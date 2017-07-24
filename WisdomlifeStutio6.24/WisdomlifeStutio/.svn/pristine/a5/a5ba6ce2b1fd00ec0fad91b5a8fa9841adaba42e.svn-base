package com.doormaster.topkeeper.utils;

import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.constant.TimerMsgConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OkhttpHelper {
	/**
	 * 获取注册验证码
	 * @param phone
	 * @param callback
	 */
	 public static void getRegistCode(String phone,Callback<?> callback) {
		    String url=Constants.URL_REGISTCODE +"?access_token="+ ConstantUtils.getAccessToken()
		    		+"&phone="+phone+"&type="+1;
		    OkHttpUtils.get().url(url).build().execute(callback);
		  }
	/**
	 * 手机号注册
	 * @param phone
	 * @param password
	 * @param nickname
	 * @param verifynum
	 * @param callback
	 */
	public static void phoneRegist(String phone,String password,String nickname,String verifynum, Callback<?> callback){
		HashMap<String, Object> mapParams=new HashMap<String, Object>();
		mapParams.put("access_token", ConstantUtils.getAccessToken());
		mapParams.put("phone", phone);
		mapParams.put("password", password);
		mapParams.put("nickname", nickname);
		mapParams.put("verifynum", verifynum);
		OkHttpUtils.postString().url(Constants.URL_PHONEREGIST).content(JsonUtil.toJson(mapParams))
		.build().execute(callback);
	}

	/**
	 * 邮箱注册
	 * @param access_token
	 * @param password
	 * @param nickname
	 * @param email
	 * @param callback
	 */
	public static void emailRegist(String access_token,String nickname,String email,String password,String language, Callback<?> callback){
		HashMap<String, Object> mapParams=new HashMap<String, Object>();
		mapParams.put("access_token", access_token);
		mapParams.put("nickname", nickname);
		mapParams.put("email", email);
		mapParams.put("password", password);
		mapParams.put("language", language);
		OkHttpUtils.postString().url(Constants.URL_EMAILREGIST).content(JsonUtil.toJson(mapParams))
				.build().execute(callback);
	}

	/**
	 * 登录接口
	 * @param username
	 * @param password
	 * @param callback
	 */
	public static void login(String username,String password,Callback<?> callback){
		HashMap<String, Object> mapParams=new HashMap<String, Object>();
		mapParams.put("access_token", ConstantUtils.getAccessToken());
		mapParams.put("username", username);
		mapParams.put("password", password);
		OkHttpUtils.postString().url(Constants.URL_LOGIN).content(JsonUtil.toJson(mapParams))
		.build().execute(callback);
	}
	/**
	 * 获取门禁列表
	 * @param callback
	 * @param clientId
	 */
	 public static void getAccessDevList(Callback<?> callback,String clientId) {
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("client_id",clientId );
		    OkHttpUtils.get().url(Constants.URL_GETACCESSDEVLIST).params(map).build().execute(callback);
	}
	/**
	 * 获取视频设备列表，房间列表
	 * @param callback
	 * @param clientId
	 */
	 public static void getDevKeyList(Callback<?> callback,String clientId) {
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("client_id",clientId );
		    OkHttpUtils.get().url(Constants.URL_COMMUNITY_PERMISS).params(map).build().execute(callback);
	}

	/**
	 * 同步时间
	 * @param callback
	 * @param clientId
	 */
	 public static void syncTime(String clientId,int time_type,int weekday,Callback<?> callback) {
		    Map<String, String> map = new HashMap<>();
		    map.put("client_id",clientId );
		    map.put("time_type", String.valueOf(time_type));
		    map.put("weekday", String.valueOf(weekday));
		    OkHttpUtils.get().url(Constants.SYNC_SERVER_URL).params(map).build().execute(callback);
	}

	 /**
		 * 获取临时密码
		 * @param callback
		 * @param json
		 */
	public static void sentTempPswd(String url, String json, Callback<?> callback) {
		OkHttpUtils.postString().url(url).content(json)
		.build().execute(callback);
	}

	/**
	 * 上传开锁记录、备份电子钥匙
	 * @param callback
	 * @param json
	 */
	public static void upLoadRecord(String url, String json, Callback<?> callback) {
		OkHttpUtils.postString().url(url).content(json)
				.build().execute(callback);
	}
	/**
	 * 请求控制权限、远程操作设备
	 * @param callback
	 * @param json
	 */
	public static void control(String json, Callback<?> callback) {
		OkHttpUtils.postString().url(Constants.CONTROL_URL).content(json)
				.build().execute(callback);
	}

	/**
	 * 获取广告内容
	 * @param callback
	 * @param clientId
	 */
	public static void getAdvertisementList(Callback<?> callback,String clientId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("client_id",clientId );
		OkHttpUtils.get().url(Constants.URL_GET_ADVERTISEMENT_LIST).params(map).build().execute(callback);
	}

	/**
	 * 获取公告内容
	 * @param callback
	 * @param clientId
	 */
	public static void getAnnouncementList(Callback<?> callback,String clientId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("client_id",clientId );
		String url = Constants.URL_GET_ANNOUNCEMENT_LIST;
		RequestCall requestCall = OkHttpUtils.get().url(url).params(map).build();
		LogUtils.d("OkhttpHelper",url);
		requestCall.execute(callback);
	}

	/**
	 * 获取手机验证码
	 * @param callback
	 * @param phone
	 */
	public static void getVerifyNum(String phone,Callback<?> callback) {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("get_url",get_url );
//		OkHttpUtils.get().url(Constants.URL_BASE + Constants.VERIFYNUMS_URL+"?"+get_url).build().execute(callback);

		String url=Constants.URL_REGISTCODE +"?access_token="+ ConstantUtils.getAccessToken()
				+"&phone="+phone+"&type="+2;
		OkHttpUtils.get().url(url).build().execute(callback);
	}

	/**
	 * 手机验证修改密码
	 *
	 * @param phone 手机号码
	 * @param verifynum 验证码
	 * @param new_pwd 新密码
	 */
	public static void forgetModifyPassword(String phone, String verifynum, String new_pwd, Callback<?> callback) {
		final JSONObject json = new JSONObject();
		try {

			json.put(TimerMsgConstants.ACCESS_TOKEN, ConstantUtils.getAccessToken());
			json.put("phone", phone);
			json.put("verifynum", verifynum);
			json.put("new_pwd", new_pwd);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
//		OkHttpClient client = new OkHttpClient();
//		// form 表单形式上传
//		MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//		for (Map.Entry entry : map.entrySet()) {
//			requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
//		}
//		Request request = new Request.Builder().url("请求地址").post(requestBody.build()).tag("modifyPassword").build();
		// readTimeout("请求超时时间" , 时间单位);
//		client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(callback);
//		OkHttpUtils.get().url(Constants.URL_BASE + Constants.FORGOTPWD_URL).params(map).build().execute(callback);
//		Log.d("OkhttpHelper", "json=" + String.valueOf(json));
		OkHttpUtils.put().url(Constants.URL_BASE + Constants.FORGOTPWD_URL).requestBody(String.valueOf(json)).build().execute(callback);
	}

	/**
	 * 旧密码、修改密码
	 *
	 * @param client_id client_id
	 * @param old_pwd 旧密码
	 * @param new_pwd 新密码
	 */
	public static void modifyPassword(String client_id, String old_pwd, String new_pwd, Callback<?> callback) {
		final JSONObject json = new JSONObject();
		try {

			json.put(TimerMsgConstants.ACCESS_TOKEN, ConstantUtils.getAccessToken());
			json.put("client_id", client_id);
			json.put("old_pwd", old_pwd);
			json.put("new_pwd", new_pwd);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
//		OkHttpClient client = new OkHttpClient();
//		// form 表单形式上传
//		MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//		for (Map.Entry entry : map.entrySet()) {
//			requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
//		}
//		Request request = new Request.Builder().url("请求地址").post(requestBody.build()).tag("modifyPassword").build();
		// readTimeout("请求超时时间" , 时间单位);
//		client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(callback);
//		OkHttpUtils.get().url(Constants.URL_BASE + Constants.FORGOTPWD_URL).params(map).build().execute(callback);
//		Log.d("OkhttpHelper", "json=" + String.valueOf(json));
		OkHttpUtils.put().url(Constants.MODIFYPWD_URL).requestBody(String.valueOf(json)).build().execute(callback);
	}

	/**
	 * 通过邮箱修改密码
	 * @param email 邮箱
	 */
	public static void modifyPasswordByEmail(String email, Callback<?> callback) {
		final JSONObject json = new JSONObject();
		try {
			json.put(TimerMsgConstants.ACCESS_TOKEN, ConstantUtils.getAccessToken());
			json.put("email", email);
			json.put("language", "en");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		OkHttpUtils.put().url(Constants.URL_BASE + Constants.FORGOTPWD_URL).requestBody(String.valueOf(json)).build().execute(callback);
	}

	/**
	 * Get system message
	 * @param callback
	 * @param clientId
	 */
	public static void getSystemMsg(String language, String clientId,Callback<?> callback) {
		String get_msg_url = Constants.URL_BASE + Constants.MSG_URL;

		Map<String, String> map = new HashMap<String, String>();
		map.put("client_id",clientId );
		map.put("language",language );
		RequestCall requestCall = OkHttpUtils.get().url(get_msg_url).params(map).build();
//		LogUtils.d("OkhttpHelper",get_msg_url);
		requestCall.execute(callback);
	}

	/**
	 * 更新接收消息状态
	 */
	public static void updateMsg(JSONObject result,Callback<?> callback) {

		String put_url = Constants.URL_BASE + Constants.UPDATE_URL;
//		HashMap<String, Object> mapParams=new HashMap<String, Object>();
//		mapParams.put("account", account);
//		mapParams.put("old_pwd", old_pwd);
//		mapParams.put("new_pwd", new_pwd);
		OkHttpUtils.put().url(put_url).requestBody(String.valueOf(result)).build().execute(callback);
	}

	/**
	 * 删除设备
	 * @param callback
	 * @param json
	 */
	public static void deleteDevice(String url, String json, Callback<?> callback) {
		OkHttpUtils.postString().url(url).content(json)
				.build().execute(callback);
	}
}
