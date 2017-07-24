package com.sdk.samples;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.uzmap.pkg.openapi.APICloud;
import com.uzmap.pkg.uzapp.UZApplication;
import com.uzmap.pkg.uzkit.request.APICloudHttpClient;

/**
 * 本项目的Application，需要在该类中初始化APICloud
 * @author dexing.li
 *
 */
public class MyApplication extends UZApplication {
	private static Context appContext;

	public MyApplication() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this;

		APICloud.initialize(this);//初始化APICloud，SDK中所有的API均需要初始化后方可调用执行
		//初始化APICloud网络请求框架，如果不需要则忽略，具体使用方式见MainActivity中的Case
		APICloudHttpClient.createInstance(this);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}


}