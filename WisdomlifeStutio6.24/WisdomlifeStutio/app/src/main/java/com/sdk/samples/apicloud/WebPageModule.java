package com.sdk.samples.apicloud;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Build;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;
import android.provider.Settings;
import com.doormaster.topkeeper.activity.Act_AccessDevList;
import com.doormaster.topkeeper.activity.Act_DoorList;
import com.doormaster.topkeeper.activity.Act_OpenRecord;
import com.doormaster.topkeeper.activity.Act_Setting;
import com.doormaster.topkeeper.activity.Act_VisitorPass;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.UserBean;
import com.doormaster.topkeeper.bean.UsersCardDom;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.UserData;
import com.doormaster.topkeeper.db.UsersCardData;
import com.doormaster.topkeeper.receiver.TimerMsgReceiver;
import com.doormaster.topkeeper.utils.CheckPermissionUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.OpenModel;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.jkyeo.splashview.SplashView;
import com.uzmap.pkg.openapi.ExternalActivity;
import com.uzmap.pkg.openapi.Html5EventListener;
import com.uzmap.pkg.openapi.WebViewProvider;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.z421614851.iga.R;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;

/**
 * 
 * 使用SuperWebview的Activity，需继承自ExternalActivity
 * @author dexing.li
 *
 */
public class WebPageModule extends ExternalActivity {

	private TextView mProgress;

	private static final int REQUEST_CODE_TEST = 999;
	private static final String RID = "rid";
	private static final String CARD_SECTIONKEY_LIST = "card_sectionkey_list";
	private String account;
	private Activity mContext;
	private boolean isPressed = false;
	private boolean isLoginSuccessed = false; //标记登录成功
	private AccessDevMetaData deviceData;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0://登录成功
					TimerMsgReceiver.scheduleAlarms(getApplicationContext(), TimerMsgReceiver.TIMER_REC_START);
					SPUtils.put(Constants.SHARED_COMPLETE_LOGIN, true, mContext);
					break;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;

		String notFirstUsed = SPUtils.getString(Constants.IS_NOT_FIRSTUSED, mContext);
		if (notFirstUsed == null || notFirstUsed.length() == 0) //第一次使用APP不需要展示闪屏广告
		{
			SPUtils.put(Constants.IS_NOT_FIRSTUSED, "is_not_firsUsed", mContext);
		}else //之后都使用闪屏广告
		{
			// call after setContentView(R.layout.activity_sample);
			SplashView.showSplashView(this, 6, R.drawable.shanping_xiaoke, new SplashView.OnSplashViewActionListener() {
				@Override
				public void onSplashImageClick(String actionUrl) {
					Log.d("SplashView", "img clicked. actionUrl: " + actionUrl);
//				Toast.makeText(WebPageModule.this, "img clicked.", Toast.LENGTH_SHORT).show();
					getWindow().setFlags(
							WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				}

				@Override
				public void onSplashViewDismiss(boolean initiativeDismiss) {
					Log.d("SplashView", "dismissed, initiativeDismiss: " + initiativeDismiss);
					getWindow().setFlags(
							WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				}
			});

			// call this method anywhere to update splash view data
//		SplashView.updateSplashData(this, "http://ww2.sinaimg.cn/large/72f96cbagw1f5mxjtl6htj20g00sg0vn.jpg", "http://jkyeo.com");  //暂时使用本地图片，不需要请求远程图片
		}
		requestPermissiontest();//请求权限

		mProgress = new TextView(this);
		mProgress.setTextColor(0xFFFF0000);
		mProgress.setTextSize(20);
		mProgress.setVisibility(View.GONE);
		addContentView(mProgress, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		bindSomething();
		TopkeeperModel.initTopkeeper(this);

		String clientId = SPUtils.getString(Constants.CLIENT_ID,mContext);
		account = SPUtils.getString(Constants.USERNAME,mContext);
		String pwd_taken = SPUtils.getString(Constants.TOKEN_PWD);
		String pwd = SPUtils.getString(Constants.PSW);
		if (clientId != null && clientId.length() != 0) { //有clientId表示登录过，直接自动登录
			LogUtils.e("开始获取设备");
			TopkeeperModel.requestAccessDevList(mContext, clientId);
			TopkeeperModel.getDevKeyList(mContext, clientId);
			if (account != null && account.length() > 0 && pwd_taken != null && pwd_taken.length() > 0)
			{
				loginDMVPhone(account, pwd_taken, null);
			}
		}
//		else if (account != null && account.length() > 0 && pwd != null && pwd.length() > 0) //如果登录过，但是登录失败，再次启动程序时，也自动登录
//		{
//			loginDoorMaster(account, pwd, null);
//		}
		TimerMsgReceiver.scheduleAlarms(getApplicationContext(), TimerMsgReceiver.TIMER_REC_START);
	}

	/**
	 * 请求权限
	 */
	public void requestPermissiontest() {
		// you needer permissions
		String[] permissions = {
				Manifest.permission.WRITE_EXTERNAL_STORAGE, //外部存储权限
				Manifest.permission.CAMERA, //摄像头权限
				Manifest.permission.ACCESS_FINE_LOCATION,  //定位权限
                Manifest.permission.RECORD_AUDIO //录音权限
		};
		// check it is needed
		permissions = CheckPermissionUtils.getNeededPermission(WebPageModule.this, permissions);
		// requestPermissions
		if (permissions.length > 0) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(permissions, REQUEST_CODE_TEST);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_TEST:
				LogUtils.d("grantResults=" + Arrays.toString(grantResults));
				if (grantResults.length > 0) {
					return;
				}
				if (!CheckPermissionUtils.isNeedAddPermission(WebPageModule.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//					Toast.makeText(WebPageModule.this, "申请权限成功:" + Manifest.permission.WRITE_EXTERNAL_STORAGE, Toast.LENGTH_LONG).show();
				}
				break;

		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private void bindSomething(){
		//设置一个监听，监听来自Html5页面发送出来的事件
		//此处我们监听"call"事件，监听Html5页面通过api.sendEvent发出"call"事件时
		addH5EventListener(new Html5EventListener("call") {
			@Override
			public void onReceive(WebViewProvider webViewProvider, Object o) {
				DMVPhoneModel.callAccount("13533754937",1);

			}
		});
//		addHtml5EventListener(new Html5EventListener("html中的方法名") {
//			@Override
//			public void onReceive(WebViewProvider provider, Object extra) {
//				showAlert("收到来自Html5的abc事件，传入的参数为：\n\n" + extra + "\n\n");
//			}
//		});
	}
	
	/**
	 * 重写该函数，可实现处理收到来自Html5页面的操作请求，处理完毕后异步回调至Html5
	 */
	@Override
	protected boolean onHtml5AccessRequest(WebViewProvider provider, UZModuleContext moduleContext) {

//		Toast.makeText(WebPageModule.this, "收到来自Html5的abc事件onHtml5AccessRequest", Toast.LENGTH_SHORT).show();
		String name = moduleContext.optString("name");
		//"requestEvent"与assets/widget/html/wind.html页面的发送请求相匹配
		if("requestEvent".equals(name)){
			JSONObject extra = new JSONObject();
			try{
				extra.put("value", "哈哈哈，我是来自Native的事件");
			}catch(Exception e){
				;
			}
			//"fromNative"与assets/widget/html/wind.html页面的apiready中注册的监听相对应
			sendEventToHtml5("fromNative", extra);
			return true;
		}
		defaultHandleHtml5AccessRequest(moduleContext);
		return true;
	}

	/**
	 * 重写该函数，可实现处理某Html5页面开始加载时，执行相应的逻辑
	 */
	@Override
	protected void onPageStarted(WebViewProvider provider, String url, Bitmap favicon) {
		//远程Url，加载较慢
		if(url.startsWith("http")){
			showProgress();
		}
	}

	/**
	 * 重写该函数，可实现处理某Html5页面结束加载时，执行相应的逻辑
	 */
	@Override
	protected void onPageFinished(WebViewProvider provider, String url) {
		//远程Url，加载较慢
		if(url.startsWith("http")){
			mProgress.setText("加载进度：100");
			hidenProgress();
		};
	}

	/**
	 * 重写该函数，可实现处理某Html5页面加载进度发生变化时，执行相应的逻辑
	 */
	@Override
	protected void onProgressChanged(WebViewProvider provider, int newProgress) {
		//远程Url，加载较慢，显示进度，提升体验
		mProgress.setText("加载进度：" + newProgress);
		if(100 == newProgress){
			hidenProgress();
		}
	}

	/**
	 * 重写该函数，可实现处理某Html5页面dom的title标签发生变化时，执行相应的逻辑
	 */
	@Override
	protected void onReceivedTitle(WebViewProvider provider, String title) {

//		Toast.makeText(WebPageModule.this, "收到来自Html5的abc事件onReceivedTitle", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 重写该函数，可实现处理拦截某Html5页面是否允许访问某API，如模块的API，APICloud引擎的API
	 */
	@Override
	protected boolean shouldForbiddenAccess(String host, String module, String api) {

//		Toast.makeText(WebPageModule.this, "收到来自Html5的abc事件shouldForbiddenAccess", Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * 重写该函数，可实现处理当某Webview即将加载某Url时，是否进行拦截，拦截后，该Webview将不继续加载该Url
	 */
	@Override
	protected boolean shouldOverrideUrlLoading(WebViewProvider provider, String url) {
//		Toast.makeText(WebPageModule.this, "收到来自Html5的abc事件shouldOverrideUrlLoading", Toast.LENGTH_SHORT).show();
		if(url.contains("taobao")){
			showAlert("不允许访问淘宝！");
			return true;
		}
		return false;
	}
	
	//默认处理收到收到来自Html5页面的操作请求，并通过UZModuleContext给予JS回调
	private void defaultHandleHtml5AccessRequest(final UZModuleContext moduleContext){
		String name = moduleContext.optString("name");
		Object extra = moduleContext.optObject("extra");
		if (name.equals("loginAccount")) {
			JSONObject json = null;
			try {
				json = new JSONObject(extra.toString());
				account = json.optString("account");
				final String pwd = json.optString("pwd");
				SPUtils.put(Constants.USERNAME, account, mContext);
				SPUtils.put(Constants.PSW, pwd, mContext);
				Log.d("HtmlAccessRequest", extra.toString());
//				Toast.makeText(WebPageModule.this, "调试回调方法：account="+account+",accountType="+accountType, Toast.LENGTH_SHORT).show();

				//"18816764052", "ceb2bd26cb33c4d8cce85a85a0fdaf7bd13ea20d"

				loginDoorMaster(account, pwd, moduleContext);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (name.equals("call")) {
			JSONObject json = null;
			try {
				json = new JSONObject(extra.toString());
				String account = json.optString("account");
//				String accountTypeStr = json.optString("accountType");
				int accountType = json.optInt("accountType");
				Log.d("HtmlAccessRequest", extra.toString());
//				DMVPhoneModel.callAccount(account, accountType);
				DMVPhoneModel.callAccount(account, accountType);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if (name.equals("exitDMVPhoneSDK")) { //退出视频登录
			DMVPhoneModel.exit();
		}

		else if (name.equals("update")) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse("http://shouji.360tpcdn.com/170818/2e29b07d889de7dcfcfec02830912b85/com.z421614851.iga_213.apk");
			intent.setData(content_url);
			startActivity(intent);
		}

		else if (name.equals("DeviceList")) {//门禁设备列表
			Intent intent = new Intent(mContext, Act_AccessDevList.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else if (name.equals("DoorVideoList")) {//视频设备列表
			startActivity(new Intent(mContext, Act_DoorList.class));
		}else if (name.equals("OpenRecord")) {//开门记录
			startActivity(new Intent(mContext, Act_OpenRecord.class));
		}else if (name.equals("VisitorPass")) {//访客授权
			startActivity(new Intent(mContext, Act_VisitorPass.class));
		} else if (name.equals("Onceopen")) {//一键开门
			if (!isPressed) {
				isPressed = true;
				TopkeeperModel.keyDoor(-120, 1000, OpenModel.NORMAL ,new DMModelCallBack.DMCallback() {
					@Override
					public void setResult(int i, DMException e) {
						isPressed = false;
					}
				});		} else {
				ToastUtils.showMessage(mContext, R.string.operationing);
			}
		}else if (name.equals("Setting")) {//设置
			startActivity(new Intent(mContext, Act_Setting.class));
		}else if (name.equals("logout")) {//退出
			TopkeeperModel.stopAutoScan();
			TopkeeperModel.stopShakeOpen();
			TimerMsgReceiver.scheduleAlarms(mContext, TimerMsgReceiver.TIMER_REC_END);
			SPUtils.put(Constants.USERNAME, null, mContext);
			DMVPhoneModel.exit();
			TopkeeperModel.cleanCurDeviceList();
		}else if (name.equals("palyCarViedo")){
			JSONObject json = null;
			try {
				json = new JSONObject(extra.toString());
				String path = json.optString("path");
//				Toast.makeText(WebPageModule.this, "视频测试路径：" +path, Toast.LENGTH_SHORT).show();
				Intent it = new Intent(Intent.ACTION_VIEW);
				it.setDataAndType(Uri.parse(path), "video/*");
				startActivity(it);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if (name.equals("ShowKey"))
		{
			JSONObject jsonObject = new JSONObject();
			if (deviceData == null) {
				deviceData = new AccessDevMetaData(mContext);
			}
			if (account == null)
			{
				try {
					jsonObject.put("ret", "0");
					jsonObject.put("msg", "false");
				}catch (JSONException e)
				{
					e.printStackTrace();
				}
				moduleContext.success(jsonObject, true);
				return;
			}
			ArrayList<AccessDevBean> allDevList = deviceData.getAllAccessDevList(account);
			try {
				if (allDevList != null && allDevList.size() > 0)
				{
					jsonObject.put("ret", "0");
					jsonObject.put("msg", "true");
				}else
				{
					jsonObject.put("ret", "0");
					jsonObject.put("msg", "false");
				}
			}catch (JSONException e)
			{
				e.printStackTrace();
			}
			moduleContext.success(jsonObject, true);

		}else if (name.equals("ConnetToWiFi")){
			Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
			startActivity(wifiSettingsIntent);
		}
		else if (name.equals("showVersionCode")){
//			Intent intent = new Intent(mContext,VersionCode.class);
//			startActivity(intent);
//			Intent intent = new Intent(mContext,VersionCode.class);
//			startActivity(intent);

		}
		else {
			ToastUtils.showMessage(mContext,name);
		}
		/*String name = moduleContext.optString("name");
		Object extra = moduleContext.optObject("extra");
		AlertDialog.Builder dia = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		dia.setTitle("提示消息");
		dia.setMessage("收到来自Html5页面的操作请求，访问的名称标识为：\n[" + name + "]\n传入的参数为：\n[" + extra + "]\n\n" + "是否处理？\n");
		dia.setCancelable(false);
		dia.setPositiveButton("不处理", null);
		dia.setNegativeButton("处理", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				JSONObject json = new JSONObject();
				try{
					json.put("result0", "value0");
					json.put("result1", "value1");
					json.put("result2", "value2");
				}catch(Exception e){
					;
				}
				moduleContext.success(json, true);
			}
		});
		dia.show();*/
	}

	public void loginDoorMaster(final String account, final String pwd, final UZModuleContext moduleContext)
	{
		OkhttpHelper.login(account, pwd, new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {

			}

			@Override
			public void onResponse(String response) {
//						Log.e(TAG, "得到登录回复:" + arg0);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					int ret = jsonObject.optInt("ret");
					String msg = jsonObject.optString("msg"); // 登录标志位
					if (jsonObject.optString("msg").equals("ok")) {
//								LogUtils.e(TAG, "得到登录回复2:" + msg);
						JSONObject dataObject = jsonObject.getJSONObject("data");
						String nickname = dataObject.optString("nickname"); // 用户昵称
						String identity = dataObject.optString("identity"); // 登录用户系统ID，12位数字
						String cardno = dataObject.optString("cardno"); // 用户的卡号
						String token_pwd = dataObject.optString("token_pwd"); // 音视频token
						String pin = dataObject.optString("pin"); // 用户的pin				// 1是）
						String clientId = dataObject.optString("client_id");
						String auto_upload_event = dataObject.optString("auto_upload_event"); // 附带信息：是否自动上传开锁记录开关（0否，
						UserBean user = new UserBean();
						user.setUserName(account);
						user.setPassWord(pwd);
						user.setPin(pin);
						user.setCardno(cardno);
						user.setToken_pwd(token_pwd);
						user.setClientID(clientId);
						user.setNickName(nickname);
						user.setIdentity(identity);
						user.setAuto_upload_event(auto_upload_event);
						UserData userData = new UserData(getApplicationContext());
						userData.saveUserData(user);

						SPUtils.put(Constants.USERNAME, account, mContext);
						SPUtils.put(Constants.PSW, pwd, mContext);

						SPUtils.put(Constants.TOKEN_PWD, token_pwd, mContext);
						SPUtils.put(Constants.PIN, pin, mContext);
						SPUtils.put(Constants.NICKNAME, nickname, mContext);
						SPUtils.put(Constants.IS_AUTOLOGIN, true, mContext);
						SPUtils.put(Constants.CARDNO, cardno, mContext);
						SPUtils.put(Constants.CLIENT_ID, clientId, mContext);

						//保存用户卡列表
						if(!dataObject.isNull(CARD_SECTIONKEY_LIST))
						{
							JSONArray card_sectionkey_list = dataObject.getJSONArray(CARD_SECTIONKEY_LIST);
							saveCardSectionKeyList(card_sectionkey_list);
						}

						sendPhoneInfo(Constants.URL_POST_COMMANDS,clientId);
						TopkeeperModel.getDevKeyList(mContext,SPUtils.getString(Constants.CLIENT_ID));
						TopkeeperModel.requestAccessDevList(mContext,SPUtils.getString(Constants.CLIENT_ID,mContext));
						loginDMVPhone(account,token_pwd, moduleContext);
					}else if ( ret == Constants.SERVER_FAILED_PROC ) {
//								Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
					} else if ( ret == Constants.ACCOUNT_PWD_FAILED ) {
//								Toast.makeText(getApplicationContext(), R.string.account_password_failed, Toast.LENGTH_SHORT).show();
					} else if (ret == Constants.NETWORD_SHUTDOWN) {
//								Toast.makeText(getApplicationContext(), R.string.check_network, Toast.LENGTH_SHORT).show();
					} else {
//								Toast.makeText(WebPageModule.this, getResources().getString(R.string.login_failure_result), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					LogUtils.e("登录出错了：" + e.toString());
//							Toast.makeText(WebPageModule.this, R.string.login_failure_result, Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	//登录音视频服务器
	private void loginDMVPhone(String username, String pwd_token, final UZModuleContext moduleContext)
	{
		DMVPhoneModel.loginVPhoneServer(username,pwd_token,1,WebPageModule.this,new DMModelCallBack.DMCallback() {
			@Override
			public void setResult(int i, DMException e) {
				JSONObject jsonObject = new JSONObject();
				try {
					if (e == null) {
						jsonObject.put("ret", "0");
						jsonObject.put("msg", "success");
//						Toast.makeText(WebPageModule.this, "登录成功", Toast.LENGTH_SHORT).show();
						mHandler.sendEmptyMessage(0);

					} else {
						jsonObject.put("ret", "1");
						jsonObject.put("msg", "failure");
//                                Toast.makeText(WebPageModule.this, "登录失败,i="+i, Toast.LENGTH_SHORT).show();
//						Toast.makeText(WebPageModule.this, "登录失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (moduleContext != null)
				{
					moduleContext.success(jsonObject, true);
				}
			}
		});
	}

	private void showAlert(String message){
		AlertDialog.Builder dia = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		dia.setTitle("提示消息");
		dia.setMessage(message);
		dia.setCancelable(false);
		dia.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				;
			}
		});
		dia.show();
	}
	
	private void showProgress(){
		mProgress.setVisibility(View.VISIBLE);
	}
	
	private void hidenProgress(){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(mProgress.isShown()){
					mProgress.setVisibility(View.GONE);
				}
			}
		}, 1500);
	}
	/**
	 * 保存用户卡扇区列表
	 * @param card_sectionkey_list
	 */
	private void saveCardSectionKeyList(JSONArray card_sectionkey_list)
	{
		if(card_sectionkey_list.length()>0)
		{
			try
			{
				for(int i = 0; i < card_sectionkey_list.length(); i++)
				{
					UsersCardDom usersCardDom = new UsersCardDom();
					JSONObject card_sectionkey = card_sectionkey_list.getJSONObject(i);
					if(!card_sectionkey.isNull(UsersCardData.UserCardColums.COLUMN_USERS_CARD_DBNAME_COMPANY))
					{
						usersCardDom.setDbname_company(card_sectionkey.getString(UsersCardData.UserCardColums.COLUMN_USERS_CARD_DBNAME_COMPANY));
					}
					if(!card_sectionkey.isNull(UsersCardData.UserCardColums.COLUMN_USERS_CARD_CARDNO))
					{
						usersCardDom.setCardno(card_sectionkey.getString(UsersCardData.UserCardColums.COLUMN_USERS_CARD_CARDNO));
					}
					if(!card_sectionkey.isNull(UsersCardData.UserCardColums.COLUMN_USERS_CARD_SECTION_KEY))
					{
						usersCardDom.setSection_key(card_sectionkey.getString(UsersCardData.UserCardColums.COLUMN_USERS_CARD_SECTION_KEY));
					}
					if(!card_sectionkey.isNull(UsersCardData.UserCardColums.COLUMN_USERS_CARD_SECTION))
					{
						usersCardDom.setSection(card_sectionkey.getInt(UsersCardData.UserCardColums.COLUMN_USERS_CARD_SECTION));
					}
					usersCardDom.setUsername(account);
					UsersCardData userdata = new UsersCardData(getApplicationContext());
					userdata.saveUsersCardData(usersCardDom);
				}
			}catch(JSONException e)
			{
				e.printStackTrace();
			}


		}
	}


	/**
	 * 发送手机信息
	 * @param post_url
	 * @param client_id
	 */
	private void sendPhoneInfo(String post_url,String client_id)
	{
		try {
			final String resource = "login_event";
			String operation = "POST";
			String mobile_dev = android.os.Build.MODEL;
			int system_version = android.os.Build.VERSION.SDK_INT;
			String app_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

			JSONObject login_event_json = new JSONObject();

			JSONObject data_jsononObject = new JSONObject();

			data_jsononObject.put("mobile_dev", mobile_dev);
			data_jsononObject.put("system_version", system_version);
			data_jsononObject.put("app_version", app_version);

			login_event_json.put("client_id", client_id);
			login_event_json.put("resource", resource);
			login_event_json.put("operation", operation);
			login_event_json.put("data", data_jsononObject);
			LogUtils.d( "login_event: " + login_event_json.toString() );
//			JSONObject ret_login_event = MyHttpClient.connectPost(post_url, login_event_json);
			OkhttpHelper.upLoadRecord(post_url, login_event_json.toString(), new StringCallback() {
				@Override
				public void onError(Call call, Exception e) {
					LogUtils.i( e.toString() );
				}

				@Override
				public void onResponse(String response) {

					JSONObject ret_login_event;
					try {
						ret_login_event = new JSONObject(response);

						if (!ret_login_event.isNull("ret")) {
							LogUtils.d( ret_login_event.toString() );
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (NameNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
