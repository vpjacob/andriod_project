package com.doormaster.topkeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.ConstantUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;


public class Act_Forget_Phone_Pwd extends BaseActivity {
	
	//初始化控件
	private EditText et_captcha_pwd;
	private EditText et_captcha_pwd_again;
	private EditText et_captcha_telephone;
	private EditText et_captcha_captcha;
	
	private Button bt_send_captcha;
	private Button bt_captcha_submit;
	
	private ImageView iv_captcha_back;

	private String phoneNum;
	private static final String TYPE = "type=2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(com.doormaster.topkeeper.R.layout.inteligoo_activity_captchar);
		
		initView();
//		final String forgive_url = Constants.URL_BASE + Constants.FORGOTPWD_URL;
//		final String verifynum_url = Constants.URL_BASE + Constants.VERIFYNUMS_URL;

		bt_send_captcha.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				//初始化数据
				phoneNum= et_captcha_telephone.getText().toString();
				final String get_url ="access_token=" + ConstantUtils.getAccessToken() +  "&phone=" + phoneNum + "&" + TYPE + "&verified=1";
				//开启线程
				getVerifyNum();
				
			}

			
		});
		
		bt_captcha_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//输入判断
				if(et_captcha_captcha.getText().length()<=0){
					Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.msg_enter_verify, Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				if(et_captcha_pwd.getText().length()<=0){
					Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.msg_write_pwd, Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(et_captcha_pwd_again.getText().length()<=0){
					Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.msg_ensure_pwd, Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!et_captcha_pwd.getText().toString().trim().equals( et_captcha_pwd_again.getText().toString().trim() )){
					Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.macth_pwd, Toast.LENGTH_SHORT).show();
					return;
				}
				
					
				modifyPassword();
				
			}
			
		});
		
		//图标回退处理
		iv_captcha_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}


	private void initView() {
		
		// TODO Auto-generated method stub
		et_captcha_telephone = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_captcha_telephone);
		et_captcha_pwd_again = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_captcha_pwd_again);
		et_captcha_pwd = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_captcha_pwd);
		et_captcha_captcha = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_captcha_captcha);
		bt_captcha_submit = (Button)findViewById(com.doormaster.topkeeper.R.id.bt_captcha_submit);
		bt_send_captcha = (Button) findViewById(com.doormaster.topkeeper.R.id.bt_captcha_send_verifyNum);
		iv_captcha_back = (ImageView) findViewById(com.doormaster.topkeeper.R.id.img_captcha_back);
	}
	
	private void getVerifyNum() {
		OkhttpHelper.getVerifyNum(phoneNum,new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {
				LogUtils.d("onError: "+ e);
			}

			@Override
			public void onResponse(String result) {
				try {
					JSONObject verify_ret = new JSONObject(result);
					if (!verify_ret.isNull("ret")) {

						int ret = verify_ret.getInt("ret");
						LogUtils.d("verify_ret: " + verify_ret.toString());

						if (ret == 0)
						{
							Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.had_send_verifynum,
									Toast.LENGTH_SHORT).show();
						}
						else if (ret == Constants.NETWORD_SHUTDOWN)
						{
							Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.check_network,
									Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.failed_to_send_verify,
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.server_not_react,
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}
	
	private void modifyPassword()
	{
//		String access_token = ConstantUtils.getAccessToken();
		String captcha_telephone = et_captcha_telephone.getText().toString().trim();
		String captcha_captcha = et_captcha_captcha.getText().toString().trim();
		String captcha_pwd = et_captcha_pwd.getText().toString().trim();

		OkhttpHelper.forgetModifyPassword(captcha_telephone, captcha_captcha, captcha_pwd, new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {
				LogUtils.d("onError: e=" + e);
				Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.server_not_react, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(String result) {
				try {
					JSONObject modify_ret = new JSONObject(result);
					LogUtils.d("result: "+ result);
					if (!modify_ret.isNull("ret")) {

						int ret = Integer.parseInt(modify_ret.getString("ret"));
						LogUtils.d("modify_ret: "+modify_ret.toString());
						if(ret==0){

							Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.modify_pwd_success,
									Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(Act_Forget_Phone_Pwd.this,Act_Login.class);
							startActivity(intent);
							finish();
						}
						else if (ret == Constants.NETWORD_SHUTDOWN)
						{
							Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.check_network,
									Toast.LENGTH_SHORT).show();
						}
						else{
							Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.modify_pwd_failed,
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), com.doormaster.topkeeper.R.string.server_not_react, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
