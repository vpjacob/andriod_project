package com.doormaster.topkeeper.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.fragment.CommunityFragment;
import com.doormaster.topkeeper.fragment.IntelligentFragment;
import com.doormaster.topkeeper.fragment.KeyDoorFragment;
import com.doormaster.topkeeper.receiver.TimerMsgReceiver;
import com.doormaster.topkeeper.utils.ConstantUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.topkeeper.view.CustomViewPager;

import java.util.ArrayList;

public class Act_Main extends BaseActivity {

	private static String TAG = "Act_Main";


	private static Activity mContext;
	private CustomViewPager vp_main;
	private RadioGroup rg_main;
	private ArrayList<Fragment> fragmentList;

	public static final int MAIN_ACTIVITY_REQ_CODE = 0x00;
	public final static int SCANNIN_GREQUEST_CODE = 0x06;
	public final static int ACTIVITY_BACK = 0x07;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__main);
		mContext = this;
		initView();
        initData();
        initEvent();
	}
    /**
     * 加载布局
     */
    private void initView(){
    	vp_main=(CustomViewPager) findViewById(R.id.vp_main);
    	rg_main=(RadioGroup) findViewById(R.id.rg_main);
    	fragmentList=new ArrayList<Fragment>();
//    	LinphoneManager.getLc().setVideoPolicy(true, true);

		TopkeeperModel.initTopkeeper(this);

		String clientId = SPUtils.getString(Constants.CLIENT_ID);
		if (clientId != null && clientId.length() != 0) {
			LogUtils.e("开始获取设备");
			TopkeeperModel.requestAccessDevList(mContext, clientId);
			TopkeeperModel.getDevKeyList(mContext, clientId);
		}

		//检测版本更新
//		BDAutoUpdateSDK.silenceUpdateAction(this);
//		BDAutoUpdateSDK.uiUpdateAction(this, new MyUICheckUpdateCallback());
//		BDAutoUpdateSDK.cpUpdateCheck(this, new VersionCheckUpdateCallback());
    }

	Notification notification;
	private String getPkgName() {
		return getPackageName();//"com.tencent.mm"
	}
	/**
	 * 方法描述：createNotification方法
	 * @param
	 * @return
	 */
	public void createNotification() {

		//notification = new Notification(R.drawable.dot_enable,app_name + getString(R.string.is_downing) ,System.currentTimeMillis());
		notification = new Notification(
				//R.drawable.video_player,//应用的图标
				ConstantUtils.getIconRes(),//应用的图标
				getString(R.string.app_name) + getString(R.string.downloading),
				System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		//notification.flags = Notification.FLAG_AUTO_CANCEL;

		/*** 自定义  Notification 的显示****/
//		RemoteViews contentView = new RemoteViews(getPackageName(),);
//		contentView.setTextViewText(R.id.notificationTitle, app_name + getString(R.string.is_downing));
//		contentView.setTextViewText(R.id.notificationPercent, "0%");
//		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
//		notification.contentView = contentView;

//      updateIntent = new Intent(this, AboutActivity.class);
//      updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//      //updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//      pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
//      notification.contentIntent = pendingIntent;

//		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.notify(R.layout.notification_item, notification);
	}
	/**
     * Loading data
     */
    private void initData(){
		BaseApplication.setCompleteLogin(true);
    	fragmentList.add(new CommunityFragment());
    	fragmentList.add(new KeyDoorFragment());
    	fragmentList.add(new IntelligentFragment());
//		mPrefs = LinphonePreferences.instance();
		/*设置自动接听视频通话的请求f*/
//		mPrefs.setAutomaticallyAcceptVideoRequests(true);
		//mPrefs.setVideoPreset();
        /*设置初始话视频电话，设置了这个你拨号的时候就默认为使用视频发起通话了*/
//		mPrefs.setInitiateVideoCall(true);
        /*这是允许视频通话，这个选了false就彻底不能接听或者拨打视频电话了*/
//		mPrefs.enableVideo(true);
    }
    /**
     * 加载事件
     */
    private void initEvent(){
    	//设置适配器
    	vp_main.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return fragmentList.size();
			}
			@Override
			public Fragment getItem(int arg0) {
				return fragmentList.get(arg0);
			}
		});
    	//设置Tab点击事件
    	rg_main.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_main_community) {
					vp_main.setCurrentItem(0, false);

				} else if (checkedId == R.id.rb_main_keydoor) {
					vp_main.setCurrentItem(1, false);

				} else if (checkedId == R.id.rb_main_intelligent) {
					vp_main.setCurrentItem(2, false);

				} else {
				}
			}
		});

//		String clientId = SPUtils.getString(Constants.CLIENT_ID);
//		if (clientId != null && clientId.length() != 0) {
//			LogUtils.e("开始获取设备");
//			TopkeeperModel.requestAccessDevList(Act_Main.this, clientId);
//			TopkeeperModel.getDevKeyList(Act_Main.this, clientId);
//		}
		//开启消息服务
		TimerMsgReceiver.scheduleAlarms(getApplicationContext(), TimerMsgReceiver.TIMER_REC_START);
//		checkUpdateManual();
//		checkUpdateSilent();
//		checkForceUpdateSilent();
	}

	public static void closeMain(){
		BaseApplication.setCompleteLogin(false);
		TopkeeperModel.stopAutoScan();
		TopkeeperModel.stopShakeOpen();
		//关闭消息service
		if(mContext!=null ){

			TimerMsgReceiver.scheduleAlarms(BaseApplication.getContext(), TimerMsgReceiver.TIMER_REC_END);
			//杀死相关进程
//			android.app.ActivityManager actManager = (android.app.ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
//			actManager.killBackgroundProcesses(mContext.getPackageName());
			mContext.finish();
		}
	}


	long exitTime;
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				ToastUtils.showMessage(getApplicationContext(), R.string.press_the_exit_procedure_again);
				exitTime = System.currentTimeMillis();
			}
			else
			{
				moveTaskToBack(true);//按后退直接进入后台模式
//				finish();
//				System.exit(0);
			}
			return true;
    	}
		return super.onKeyDown(keyCode, event);

	}

	int type = 1;

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
    protected void onDestroy() {
    	super.onDestroy();
    }

	public boolean isRunningForeground(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		return currentPackageName != null && currentPackageName.equals(context.getPackageName());
	}
}
