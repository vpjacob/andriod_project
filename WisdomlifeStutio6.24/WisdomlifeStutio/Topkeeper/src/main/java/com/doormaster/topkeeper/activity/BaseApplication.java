package com.doormaster.topkeeper.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.vphone.inter.DMVPhoneModel;

/**
 * 应用：
 * 1、方法数超限
 * 2、防止异常崩溃
 * Created by Liukebing on 2017/1/22.
 */

public class BaseApplication extends Application{
    //implements Thread.UncaughtExceptionHandler
    private static Context mContext;
    private static BaseApplication instance;
    public static int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instance = this;

        DMVPhoneModel.initDMVPhoneSDK(mContext,getResources().getString(com.doormaster.topkeeper.R.string.app_name) );
//        Thread.setDefaultUncaughtExceptionHandler(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        createApplicationLifeCallback();
    }

    public static BaseApplication getInstance(){
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//    @Override
//    public void uncaughtException(Thread thread, Throwable throwable) {
//        restart();
//    }

    private void restart() {
        Intent i = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pi);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static Context getContext(){
        return mContext;
    }

    public void setClientId(String id){
        SPUtils.put(Constants.CLIENT_ID, id, mContext);
    }

    public static String getClientId(){
        return SPUtils.getString(Constants.CLIENT_ID);
    }
    //登录状态
    public static void setCompleteLogin(boolean login)
    {
        SPUtils.put(Constants.SHARED_COMPLETE_LOGIN, login, mContext);
    }

    public static boolean getCompleteLogin()
    {
        return SPUtils.getBoolean(Constants.SHARED_COMPLETE_LOGIN, mContext);
    }

    private void createApplicationLifeCallback()
    {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0 && SPUtils.getBoolean(Constants.IS_AUTOLOGIN, mContext)) {
                    //判断是否开启自动开门服务
                    TopkeeperModel.stopAutoScan();
                    TopkeeperModel.refeshAutoList();
                    TopkeeperModel.stopShakeOpen();
                    TopkeeperModel.refeshShakeList();
                }
                count++;

            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if(count == 0) {
                    //判断是否开启自动开门服务
//                    TopkeeperModel.stopAutoScan();
                }

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

}
