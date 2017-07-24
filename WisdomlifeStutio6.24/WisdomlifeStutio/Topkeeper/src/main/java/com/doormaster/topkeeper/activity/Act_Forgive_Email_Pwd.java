package com.doormaster.topkeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;


public class Act_Forgive_Email_Pwd extends BaseActivity {

	private static String TAG = "Act_Forgive_Email_Pwd";
	private EditText userEdit  = null;
	
	private Button	sumbitBtn = null;
	
//	final String url = Constants.URL_BASE + Constants.FORGOTPWD_URL;
	
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(com.doormaster.topkeeper.R.layout.activity_device_email_findpswd);
		
		userEdit = (EditText)findViewById(com.doormaster.topkeeper.R.id.et_captcha_email);
		sumbitBtn = (Button)findViewById(com.doormaster.topkeeper.R.id.bt_captcha_email_submit);
	}
	
	public void click(View view){
		int i = view.getId();
		if (i == com.doormaster.topkeeper.R.id.img_captcha_email_back) {
			finish();

		} else if (i == com.doormaster.topkeeper.R.id.bt_captcha_email_submit) {
			findPwsd();


		} else {
		}
	}
	
	public void findPwsd()
	{

		final String email= userEdit.getText().toString();

		if(!Utils.isEmail(email)){
			Toast.makeText(currentActivity, com.doormaster.topkeeper.R.string.enter_right_email_address, Toast.LENGTH_SHORT).show();
			return;
		}

		OkhttpHelper.modifyPasswordByEmail(email, new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {
				LogUtils.d("server_not_react,e="+e.toString());
				Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.server_not_react, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(String result) {
				try {
					JSONObject modify_ret = new JSONObject(result);
					if (!modify_ret.isNull("ret")){
						try {
							LogUtils.d(TAG,"modify_ret: "+modify_ret.toString());
							JSONObject str = new JSONObject(modify_ret.getString("ret"));
							int ret = Integer.parseInt(str.getString("ret"));
							if(ret==0){

								Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.mail_sent_successfully,
										Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(Act_Forgive_Email_Pwd.this,Act_Login.class);
								startActivity(intent);
								finish();
							} else if (ret == Constants.NETWORD_SHUTDOWN)
							{
								Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.check_network,
										Toast.LENGTH_SHORT).show();
							} else if (ret == Constants.ACCOUNT_DOES_NOT_EXIST)
							{
								Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.phone_does_not_exist,
										Toast.LENGTH_SHORT).show();
							} else{
								Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.modify_pwd_failed,
										Toast.LENGTH_SHORT).show();
							}

						} catch (NumberFormatException | JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}else{
						Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.server_not_react, Toast.LENGTH_SHORT).show();
					}
				}catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		/**
		Thread findPwsd = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				try
				{
					String access_token = ConstantUtils.getAccessToken();
					JSONObject json = new JSONObject();
					json.put("access_token", access_token);
					json.put("EMAIL",EMAIL);
					json.put("language","en");
					JSONObject modify_ret = MyHttpClient.connectPut(url ,json);
					if (modify_ret!=null && !modify_ret.isNull("ret")){
						try {
							int ret = Integer.parseInt(modify_ret.getString("ret"));
							LogUtils.d("modify_ret: "+modify_ret.toString());
							if(ret==0){
								
								Toast.makeText(getApplicationContext(), "Mail sent successfully, please amend the password to E-mail",
										Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(EmailForgivePwdActivity.this,Act_Login.class);
								startActivity(intent);
								finish();
							}
							else if (ret == Constants.NETWORD_SHUTDOWN)
							{
								Toast.makeText(getApplicationContext(), R.string.check_network,
										Toast.LENGTH_SHORT).show();
							}
							else{
								Toast.makeText(getApplicationContext(), "modify_pwd_failed",
										Toast.LENGTH_SHORT).show();
							}
							
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Assert(false);
							return;
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Assert(false);
							return;
						}
						
					}else{
						Toast.makeText(getApplicationContext(), R.string.server_not_react, Toast.LENGTH_SHORT).show();
					}
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Looper.loop();	
			}

		});
		findPwsd.start();*/
	}
}
