package com.doormaster.topkeeper.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：用于处理退出程序时可以退出所有的activity，而编写的通用类
 */
public class ActivityManager {

	private List<Activity> activityList = new ArrayList<>();
	private static ActivityManager instance;

	private ActivityManager() {
	}

	/**
	 * 单例模式中获取唯一的AbActivityManager实例.
	 * @return
	 */
	public static ActivityManager getInstance() {
		if (null == instance) {
			instance = new ActivityManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到容器中.
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	/**
	 * 移除Activity从容器中.
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	/**
	 * 遍历所有Activity并finish.
	 */
	public void clearAllActivity() {
		for (Activity activity : activityList) {
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
	}
}