package com.doormaster.topkeeper.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.doormaster.topkeeper.utils.ActivityManager;
import com.doormaster.topkeeper.view.SystemBarTintManager;

public class BaseActivity extends FragmentActivity {
	public static BaseActivity currentActivity;
	public static Handler mhandler;
	private Intent intent;
	private static String TAG = "BaseActivity";

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentActivity = this;
		TAG = this.getClass().getSimpleName();
		mhandler = new Handler();
//		setStateBarColor(com.doormaster.topkeeper.R.color.state_coclor);
		ActivityManager.getInstance().addActivity(this);
	}


	@SuppressLint("InlinedApi")
	public void setStateBarColor(int colorId) {
		// 给状态栏设置颜色
		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		// 创建状态栏的管理实例
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 激活导航栏设置
		tintManager.setNavigationBarTintEnabled(true);
		// 设置一个颜色给系统栏
		tintManager.setTintColor(getResources().getColor(colorId));
	}

	/**
	 * 返回默认
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	/**
	 * 安全移除一个activity
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		ActivityManager.getInstance().removeActivity(this);
		super.finish();
	}

	public void openActivity(Class<?> c) {
		openActivity(c, null);
	}

	protected void openActivity(Class<?> c, Bundle bundle) {
		if (intent == null) {
			intent = new Intent();
		}
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		intent.setClass(this, c);
		startActivity(intent);
	}

	protected void openActivity(Intent intent) {
		startActivity(intent);
	}


}
