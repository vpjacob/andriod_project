package com.doormaster.topkeeper.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.ConstantUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack;
import com.doormaster.vphone.inter.DMVPhoneModel;

public class Act_Welcome extends BaseActivity {

	private static String TAG = "Act_Welcome";
	private static long displayTime = 1000;
	private long currentTime;
	private long endTime;
	private long last;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.doormaster.topkeeper.R.layout.act_welcom);
		ImageView welcomeImage = (ImageView) findViewById(com.doormaster.topkeeper.R.id.iv_splash);
		welcomeImage.setImageResource(ConstantUtils.getSplashResource());
		TopkeeperModel.initTopkeeper(this);
//		DBBiz mDbDao= DBBiz.getInstanter(getApplication());
		if (BaseApplication.getInstance().getClientId() != null && SPUtils.getBoolean(Constants.IS_AUTOLOGIN, BaseApplication.getContext())) {//SPUtils.getBoolean(Constants.IS_AUTOLOGIN, currentActivity)

			currentTime = System.currentTimeMillis();
			DMVPhoneModel.loginVPhoneServer(this, new DMModelCallBack.DMCallback() {
				@Override
				public void setResult(int i, DMException e) {
					if (e == null) {
						LogUtils.d(TAG, "登录成功");
						endTime = System.currentTimeMillis();
						last = endTime - currentTime;
						if (displayTime >= last) {
							mhandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									openActivity(Act_Main.class);
									finish();
								}
							}, displayTime - last);
						} else {
							openActivity(Act_Main.class);
							finish();
						}
					} else {
						Toast.makeText(Act_Welcome.this, "登录失败，进入离线状态", Toast.LENGTH_SHORT).show();
						LogUtils.d(TAG, "登录失败，e=" + e);
						endTime = System.currentTimeMillis();
						last = endTime - currentTime;
						if (displayTime >= last) {
							mhandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									openActivity(Act_Main.class);
									finish();
								}
							}, displayTime - last);
						} else {
							openActivity(Act_Main.class);
							finish();
						}
					}
				}
			});
			//登录过直接进入主界面

		} else {
			mhandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					openActivity(Act_Login.class);
					finish();
				}
			}, displayTime);
		}
	}

}
