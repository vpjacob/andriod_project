package com.doormaster.topkeeper.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.bean.UserBean;
import com.doormaster.topkeeper.bean.UsersCardDom;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.DBBiz;
import com.doormaster.topkeeper.db.UserData;
import com.doormaster.topkeeper.db.UsersCardData;
import com.doormaster.topkeeper.utils.CheckPermissionUtils;
import com.doormaster.topkeeper.utils.ConstantUtils;
import com.doormaster.topkeeper.utils.DialogUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.Call;

import static com.doormaster.topkeeper.constant.Constants.NICKNAME;

public class Act_Login extends BaseActivity implements View.OnClickListener{
	private static final String TAG = "Act_Login";

	private static final int REQUEST_CODE_TEST = 999;
	private EditText et_login_username, et_login_psw;
	private Button bt_login;
	private TextView tv_reset_psw, tv_regist;

	private DMModelCallBack.DMCallback loginCallback;
	private String domain, port, voip_account, voip_pwd;
	private DBBiz mDbDao;
	private Dialog loadingDialog;
	private String username;
	private RelativeLayout loginLayout;

	private AccessDevMetaData deviceData;

	private static final String RID = "rid";
	private static final String CARD_SECTIONKEY_LIST = "card_sectionkey_list";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		initView();
		initEvent();
		processExtraData();
	}

	/**
	 * 加载布局
	 */
	private void initView() {

		deviceData = new AccessDevMetaData(getApplicationContext());
		loadingDialog = DialogUtils.createLoadingDialog(this, getResources().getString(R.string.logining));//保证每次登陆都能让动画转起来
		mDbDao = DBBiz.getInstanter(getApplicationContext());
		et_login_username = (EditText) findViewById(R.id.et_login_username);
		et_login_psw = (EditText) findViewById(R.id.et_login_psw);
		et_login_psw.setTypeface(Typeface.SANS_SERIF);//输入格式为密码时，会导致hint全角，使用setTypeface可将其设置为默认字体格式
		bt_login = (Button) findViewById(R.id.bt_login);
		tv_reset_psw = (TextView) findViewById(R.id.tv_reset_psw);
		tv_regist = (TextView) findViewById(R.id.tv_regist);
		loginLayout = (RelativeLayout)findViewById(R.id.layout_login);
		loginLayout.setBackgroundResource(ConstantUtils.getLoginResource());
		domain = Constants.DOMAIN;
		port = Constants.PORT;
		loginCallback = new DMModelCallBack.DMCallback() {
			@Override
			public void setResult(int i, DMException e) {
				if (e == null) {
					LogUtils.d(TAG, "登录成功");
					Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
					openActivity(Act_Main.class);
					finish();
					if (loadingDialog!=null&&loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				} else {
					LogUtils.d(TAG, "登录失败，e="+e);
//                                    accountCreated = false;
					Toast.makeText(Act_Login.this, R.string.login_failure, Toast.LENGTH_SHORT).show();
					openActivity(Act_Main.class);
					finish();
					if (loadingDialog!=null&&loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				}
			}
		};

	}

	/**
	 * 加载事件
	 */
	private void initEvent() {

        requestPermissiontest();
        bt_login.setOnClickListener(this);
        tv_regist.setOnClickListener(this);
        tv_reset_psw.setOnClickListener(this);
		// 自动登录
		username = SPUtils.getString(Constants.USERNAME, Act_Login.this);
		et_login_username.setText(username);
		String psw = SPUtils.getString(Constants.PSW, Act_Login.this);
		et_login_psw.setText(psw);
		et_login_username.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				et_login_psw.setText(null);
			}
			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
	}

    @Override
    public void onClick(View view) {
		int i = view.getId();
		if (i == R.id.bt_login) {
			username = et_login_username.getText().toString().trim();
			String psw = et_login_psw.getText().toString().trim();
			login2server(username, psw);

			// 注册
		} else if (i == R.id.tv_regist) {
			openActivity(Act_Regist.class);
			//finish();

			// 忘记密码
		} else if (i == R.id.tv_reset_psw) {
			Intent intent = new Intent(Act_Login.this, Act_Forget_Password.class);
			username = et_login_username.getText().toString().trim();
			intent.putExtra(Constants.USERNAME, username);
			openActivity(intent);

		} else {
		}
    }

	/**
	 * 登录到服务器
	 */
	private void login2server(final String username, final String pwd) {
		if(et_login_username.getText().length()<=0 || et_login_psw.getText().length()<=0)
		{
			Toast.makeText(Act_Login.this, R.string.account_pwd_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!isFinishing()) loadingDialog.show();
		OkhttpHelper.login(username, pwd, new StringCallback() {
			@Override
			public void onResponse(String arg0) {
				LogUtils.e(TAG, "得到登录回复:" + arg0);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(arg0);
					int ret = jsonObject.optInt("ret");
					String msg = jsonObject.optString("msg"); // 登录标志位
					if (jsonObject.optString("msg").equals("ok")) {
						LogUtils.e(TAG, "得到登录回复2:" + msg);
						JSONObject dataObject = jsonObject.getJSONObject("data");
						String nickname = dataObject.optString("nickname"); // 用户昵称
						String identity = dataObject.optString("identity"); // 登录用户系统ID，12位数字
						String cardno = dataObject.optString("cardno"); // 用户的卡号
						String token_pwd = dataObject.optString("token_pwd"); // 音视频token
						String pin = dataObject.optString("pin"); // 用户的pin				// 1是）
						String clientId = dataObject.optString("client_id");
						String auto_upload_event = dataObject.optString("auto_upload_event"); // 附带信息：是否自动上传开锁记录开关（0否，
						UserBean user = new UserBean();
						user.setUserName(username);
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

						SPUtils.put(Constants.USERNAME, username, Act_Login.this);
						SPUtils.put(Constants.PSW, pwd, Act_Login.this);

						SPUtils.put(Constants.TOKEN_PWD, token_pwd, Act_Login.this);
						SPUtils.put(Constants.PIN, pin, Act_Login.this);
                        SPUtils.put(NICKNAME, nickname, Act_Login.this);
						SPUtils.put(Constants.IS_AUTOLOGIN, true, Act_Login.this);
						SPUtils.put(Constants.CARDNO, cardno, Act_Login.this);
						SPUtils.put(Constants.CLIENT_ID, clientId, Act_Login.this);


						LogUtils.e(TAG, "nickname:" + nickname + ",identity:" + identity + ",CARDNO=" + cardno);
						//保存用户卡列表
						if(!dataObject.isNull(CARD_SECTIONKEY_LIST))
						{
							JSONArray card_sectionkey_list = dataObject.getJSONArray(CARD_SECTIONKEY_LIST);
							saveCardSectionKeyList(card_sectionkey_list);
						}

						sendPhoneInfo(Constants.URL_POST_COMMANDS,clientId);
						DMVPhoneModel.loginVPhoneServer(username,token_pwd,1,Act_Login.this,loginCallback);
					}else if ( ret == Constants.SERVER_FAILED_PROC ) {
                        Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    } else if ( ret == Constants.ACCOUNT_PWD_FAILED ) {
                        Toast.makeText(getApplicationContext(), R.string.account_password_failed, Toast.LENGTH_SHORT).show();
                    } else if (ret == Constants.NETWORD_SHUTDOWN) {
                        Toast.makeText(getApplicationContext(), R.string.check_network, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Act_Login.this, getResources().getString(R.string.login_failure_result), Toast.LENGTH_SHORT).show();
                    }
					if (loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				} catch (JSONException e) {
					LogUtils.e("登录出错了：" + e.toString());
					if (loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
					Toast.makeText(Act_Login.this, R.string.login_failure_result, Toast.LENGTH_SHORT).show();
				}
				
			}

			@Override
			public void onError(Call arg0, Exception arg1) {
				arg1.printStackTrace();
				LogUtils.e("登录出错了：" + arg1.toString());
				if (loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
				Toast.makeText(Act_Login.this, R.string.check_network, Toast.LENGTH_SHORT).show();
			}
		});
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
					usersCardDom.setUsername(username);
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

	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		PackageManager pm = getPackageManager();
		PackageInfo pi = null;//getPackageName()是你当前类的包名，0代表是获取版本信息
		try {
			pi = pm.getPackageInfo(getPackageName(), 0);
			String name = pi.versionName;
			int code = pi.versionCode;
			return this.getString(R.string.app_name)+name;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return this.getString(R.string.can_not_find_version_name);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		processExtraData();
		LogUtils.d(TAG, "onNewIntent"+intent);

	}

	private void processExtraData(){

		Intent intent = getIntent();
		// 注册后自动登录
		if (intent.getStringExtra(Constants.PHONE) != null) {
			username = intent.getStringExtra(Constants.PHONE);
			et_login_username.setText(username);
		}
		if (intent.getStringExtra(Constants.PSW) != null) {
			String psw = getIntent().getStringExtra(Constants.PSW);
			et_login_psw.setText(psw);
			login2server(username, psw);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	/**
	 * 请求权限
	 */
	public void requestPermissiontest() {
		// you needer permissions
		String[] permissions = {
				Manifest.permission.RECORD_AUDIO,
				Manifest.permission.CAMERA,
				Manifest.permission.ACCESS_COARSE_LOCATION};
		// check it is needed
		permissions = CheckPermissionUtils.getNeededPermission(Act_Login.this, permissions);
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
				if (!CheckPermissionUtils.isNeedAddPermission(Act_Login.this, Manifest.permission.RECORD_AUDIO)) {
					Toast.makeText(Act_Login.this, "申请权限成功:" + Manifest.permission.RECORD_AUDIO, Toast.LENGTH_LONG).show();
				}
				if (!CheckPermissionUtils.isNeedAddPermission(Act_Login.this, Manifest.permission.CAMERA)) {
					Toast.makeText(Act_Login.this, "申请权限成功:" + Manifest.permission.CAMERA, Toast.LENGTH_LONG).show();
				}
				if (!CheckPermissionUtils.isNeedAddPermission(Act_Login.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
					Toast.makeText(Act_Login.this, "申请权限成功:" + Manifest.permission.ACCESS_COARSE_LOCATION, Toast.LENGTH_LONG).show();
				}
				break;

		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}
}
