package com.doormaster.topkeeper.activity.device_manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.Act_VisitorPass;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.SystemInfoBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.constant.TimerMsgConstants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.SystemInfoData;
import com.doormaster.topkeeper.utils.ConstantUtils;
import com.doormaster.topkeeper.utils.DialogUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.intelligoo.sdk.BluetoothLeService;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface.ManagerCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

import static com.doormaster.topkeeper.db.AccessDevMetaData.AccessDev.ACCESS_DEV_MAC;
import static com.doormaster.topkeeper.db.AccessDevMetaData.AccessDev.ACCESS_DEV_SN;


//import com.intelligoo.app.task.ManagerDev;
//import com.intelligoo.app.task.ManagerDev.ManagerCallback;


public class Act_Device_Info extends BaseActivity implements OnClickListener {

	private static String TAG = "Act_Device_Info";
	private TextView mType;
	private TextView mDevSn;
	private TextView mDevName;
	private TextView mDevLimtDate;
	private ImageButton mBack;
	
	//界面控制
	private LinearLayout mLyReaderWiegand; //韦根设置界面
	private LinearLayout mLyFunctionSyncTime; //同步设备时间
	private LinearLayout mLyFunctionRemote; //远程开门
	private LinearLayout mLyFuncVisitorAuthorize; //访客授权
	private LinearLayout mLyFuncCardManage; // 卡管理
	private LinearLayout mLyFuncEKeyManage; // 电子钥匙管理
	private LinearLayout mLyFuncTmpKey;//临时密钥
	private LinearLayout mPriSet; //超级管理员设置设备界面
	private LinearLayout mLyFuncWifi;//配置wifi
	
	//功能按钮
	private LinearLayout mWiegand;
	private LinearLayout mSyncTime;
	private LinearLayout mRemoteOpen;
	private LinearLayout mVisitorAuthorize;
	private LinearLayout mManageCard;
	private LinearLayout mManagerKey;
	private LinearLayout mTemporaryKey;//临时密钥
	private LinearLayout mDevSet; //设备设置
	private LinearLayout mConfigWifi;//配置wifi
	
	//标志卡挂失或卡管理
//	private int card_loss_or_manager = 0;//1:卡管理，  2：卡挂失
//	private TextView card_loss_or_manager_tv = null;
	
	private LinearLayout mRssiSet; //信号设置
	private TextView mSendPwdOrQrcode; //二维码
	//删除设备
	private Button mBtDele;
	
	private String dev_sn = null;
	private String dev_mac = null;
	AccessDevBean device = null;
	public static final int DEV_INFO_REQ_CODE = 0x01;
	private BluetoothLeService mLeService;
	
	private boolean pressed;
	private static Timer timer;    //计时器
	private static TimerTask task;
	private static final int COUNT_DELAY = 7500;

	private Dialog syncDialog;
	private Dialog openDialog;
	private Dialog deleteDialog;

//	//信号限制值
//	private static final int minRssiValue = 65;
//	private static final int maxRssiValue = 75;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inteligoo_activity_device_info);
		initView();
		Intent intent = getIntent();
		//设备信息
		final String username = SPUtils.getString(Constants.USERNAME);
		final AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
		dev_sn = intent.getStringExtra(ACCESS_DEV_SN);
		dev_mac = intent.getStringExtra(ACCESS_DEV_MAC);
		device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		if (device == null) {
			Toast.makeText(Act_Device_Info.this, R.string.device_has_been_delete, Toast.LENGTH_SHORT).show();
			Act_Device_Info.this.finish();
			return; //异常要处理
		}
		//界面控制
		resetView(device);
		
		setDevName(deviceData, device);
		
		initEvent();
		
		
		/*//benson测试
		UsersCardData data = new UsersCardData(this);
		UsersCardDom dom = data.getUsersCardDom(MyApplication.getInstance().getUserName(), device.getDbname_company());
		Toast.makeText(this, dom.getCardno(), Toast.LENGTH_LONG).show();*/
	}
	
	private void initView() {

		syncDialog = DialogUtils.createLoadingDialog(this, getResources().getString(R.string.synchronizing));//保证每次登陆都能让动画转起来
		openDialog = DialogUtils.createLoadingDialog(this, getResources().getString(R.string.remote_door_opening));//保证每次登陆都能让动画转起来
		deleteDialog = DialogUtils.createLoadingDialog(this, getResources().getString(R.string.deleting));//保证每次登陆都能让动画转起来

		mType = (TextView) findViewById(R.id.activity_device_type);
		mDevSn = (TextView) findViewById(R.id.activity_device_sn);
		mDevName = (TextView) findViewById(R.id.activity_device_name);
		mDevLimtDate = (TextView) findViewById(R.id.activity_device_limit_date);
		mBack = (ImageButton) findViewById(R.id.ib_device_info_back);
		
		//界面控制
		mLyReaderWiegand = (LinearLayout) findViewById(R.id.ly_device_wiegand_reader_set); //韦根
		mLyFunctionSyncTime = (LinearLayout) findViewById(R.id.ly_device_function_sync_time); //同步时间
		mLyFunctionRemote = (LinearLayout) findViewById(R.id.ly_device_function_remote_open); //远程开门
		mLyFuncVisitorAuthorize = (LinearLayout) findViewById(R.id.ly_device_function_visitor_authorize); //访客授权
		mLyFuncCardManage = (LinearLayout) findViewById(R.id.ly_device_cardmanage);
		mLyFuncEKeyManage = (LinearLayout) findViewById(R.id.ly_device_ekeymanage);
		mLyFuncTmpKey = (LinearLayout) findViewById(R.id.ly_device_tmpKey);
		mPriSet = (LinearLayout) findViewById(R.id.ly_device_privilege_setting);
		mLyFuncWifi = (LinearLayout) findViewById(R.id.ly_device_wifi);
		mConfigWifi = (LinearLayout) findViewById(R.id.ly_device_config_wifi);
//		card_loss_or_manager_tv  = (TextView) findViewById(R.id.card_loss_or_manager_tv);
		
		mWiegand = (LinearLayout) findViewById(R.id.ly_device_set_dev_wiegand);
		mRemoteOpen = (LinearLayout) findViewById(R.id.ly_device_remote_open);
		mSyncTime = (LinearLayout) findViewById(R.id.ly_device_sycn_dev_time);
		mVisitorAuthorize = (LinearLayout)findViewById(R.id.ly_device_temp_password);
		mManagerKey = (LinearLayout) findViewById(R.id.ly_device_digital_key);
		mTemporaryKey = (LinearLayout) findViewById(R.id.ly_device_digital_tmpKey);
		mManageCard = (LinearLayout)findViewById(R.id.ly_device_manage_card);
		mDevSet = (LinearLayout) findViewById(R.id.ly_device_setting);
		
		mSendPwdOrQrcode = (TextView) findViewById(R.id.tv_activity_device_send_pswd);
		mRssiSet = (LinearLayout) findViewById(R.id.ly_device_set_dev_rssi); //信号设置
		mBtDele = (Button) findViewById(R.id.bt_device_info_delete_dev);
	}
	
	private void initEvent()
	{
		// 卡片管理模式
		mManageCard.setOnClickListener(this);
		// 读头梯控显示设置
		mWiegand.setOnClickListener(this);
		// 发送临时密码
		mVisitorAuthorize.setOnClickListener(this);
		// 电子钥匙管理
		mManagerKey.setOnClickListener(this);
		// 配置读卡扇区
		mTemporaryKey.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mRssiSet.setOnClickListener(this);
		mSyncTime.setOnClickListener(this);
		// mLyFuncWifi.setOnClickListener(this);
		mConfigWifi.setOnClickListener(this);

		mRemoteOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendRemoteCMD(dev_sn);
			}
		});

		// 设备设置
		mDevSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Act_Device_Info.this,
						Act_DeviceSetting.class);
				intent.putExtra(AccessDevBean.DEVICE_SN, dev_sn);
				intent.putExtra(AccessDevBean.DEVICE_MAC, dev_mac);
				startActivityForResult(intent, DEV_INFO_REQ_CODE);
			}
		});
		//删除设备
		mBtDele.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(Act_Device_Info.this).setTitle(R.string.warning)//设置对话框标题
				  
			     .setMessage(R.string.whether_delete_device)//设置显示的内容  
			  
			     .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {//添加确定按钮
			         @Override
			  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
			  
			             // TODO Auto-generated method stub  
			        	 deleteDevice(dev_sn); //删除设备
			         }  
			  
			     }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {//添加返回按钮
			         @Override
			  
			         public void onClick(DialogInterface dialog, int which) {//响应事件
			  
			             // TODO Auto-generated method stub  
			         }  
			  
			     }).show();//在按键响应事件中显示此对话框  
			}

		});
	}
	
	

	/*
	 private LinearLayout mLyReaderWiegand; //韦根设置界面
	private LinearLayout mLyFunctionSyncTime; //同步设备时间
	private LinearLayout mLyFunctionRemote; //远程开门
	private LinearLayout mLyFuncVisitorAuthorize; //访客授权
	private LinearLayout mLyCommom; //通用功能显示
	private LinearLayout mPriSet; //超级管理员设置设备界面
	 * */
	private void resetView (AccessDevBean device) {
		// 设备类型显示
//		int devTypeShow[] = {R.string.activity_device_type_reader, R.string.activity_device_type_access_controller, R.string.activity_device_type_lift_controller,
//				R.string.activity_device_type_door, R.string.activity_device_type_ble_controller, R.string.activity_device_type_controller,
//				R.string.activity_device_type_touch_controller, R.string.activity_device_type_qc_device, R.string.activity_device_type_qr_device, 
//				R.string.activity_device_type_dm_controller, R.string.activity_device_type_wifi_touch_controller, R.string.activity_device_type_wifi_dm_controller, R.string.activity_device_type_wifi_access_controller};
		int devTypeShow[] = ConstantUtils.getDevTypeName();//获取门管家设备名称或中性名称
		int devType = device.getDevType();
		if (devType == 0 || devType > devTypeShow.length)
		{
			mType.setText(getResources().getString(R.string.unknown_device));
		}
		else
		{
			mType.setText(devTypeShow[devType - 1]);
		}
		//显示设备序列号
		mDevSn.setText(dev_sn);
		// 设备名称显示
		mDevName.setText(device.getDevName());
		mDevName.setTextColor(android.graphics.Color.rgb(0x19,98,0xd5));
		
		// 有效期显示
		//1998D5
		String start_date = device.getStartDate();
		String end_date = device.getEndDate();
		if (start_date == null || end_date == null ||
				start_date.isEmpty() || end_date.isEmpty()) 
		{
			mDevLimtDate.setText(R.string.mn_tv_lock_send_forevertime);
		}
		else
		{
			String show_limit = new String();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
			SimpleDateFormat sdf_set = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
			try {
				Date start_time = sdf_set.parse(start_date);
				Date end_time = sdf_set.parse(end_date);
				String show_start = sdf.format(start_time);
				String show_end = sdf.format(end_time);
				show_limit = show_start + "\n" + show_end;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mDevLimtDate.setText(show_limit); // 有效期
		}
		mBtDele.setVisibility(View.VISIBLE); // 所有用户都可以删除自己的设备电子钥匙
		if (device.getFunction() != null && !"".equals(device.getFunction()))
		{
			Map<String, String> devFuncMap = AccessDevBean.getDevFuncMap(device.getFunction());
			if (devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_TEMP_PWD) || devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_TEMP_QRCODE))
			{
				mLyFuncVisitorAuthorize.setVisibility(View.VISIBLE);//普通用户就可以访客授权 ---临时密码
			}
//			
//			if (devType == DeviceDom.DEV_TYPE_LOCK || devType == DeviceDom.DEV_TYPE_QRCODE_DEVICE || devType == DeviceDom.DEV_TYPE_ACCESS_CONTROLLER ||
//					devType == DeviceDom.DEV_TYPE_DM_DEVICE || devType == DeviceDom.DEV_TYPE_WIFI_DM_DEVICE || devType == DeviceDom.DEV_TYPE_WIFI_ACCESS_CONTROLLER)
//			{
//				mLyFuncVisitorAuthorize.setVisibility(View.VISIBLE);//普通用户就可以访客授权 ---临时密码
//			}
//			
			if (device.getPrivilege() != AccessDevBean.DEV_PRIVILEGE_USER) // 管理员以上权限可操作
			{
//				if (devType == DeviceDom.DEV_TYPE_LOCK || devType == DeviceDom.DEV_TYPE_QRCODE_DEVICE || devType == DeviceDom.DEV_TYPE_ACCESS_CONTROLLER ||
//						devType == DeviceDom.DEV_TYPE_DM_DEVICE || devType == DeviceDom.DEV_TYPE_WIFI_DM_DEVICE || devType == DeviceDom.DEV_TYPE_WIFI_ACCESS_CONTROLLER)
//				{
//					mLyFunctionSyncTime.setVisibility(View.VISIBLE);
//					mLyFuncCardManage.setVisibility(View.VISIBLE);
//				}
				if (devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_SYNC_TIME))
				{
					mLyFunctionSyncTime.setVisibility(View.VISIBLE);
				}
				if (devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_CARD_MANAGE) || devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_CARD_LOSS))
				{
					mLyFuncCardManage.setVisibility(View.VISIBLE);//卡管理与卡挂失功能一样，只是在非脱机与脱机版的叫法不一样
//					card_loss_or_manager = 1;
//					if(devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_CARD_LOSS))
//					{
//						card_loss_or_manager = 2;
//						card_loss_or_manager_tv.setText(getResources().getString(R.string.activity_device_loss_card));
//					}
				}
				
				if(device.getPrivilege() == AccessDevBean.DEV_PRIVILEGE_SUPER)
				{
					//超级管理员才能设置扇区密码
					if(devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_READ_SECTIONKEY))
					{
						mLyFuncTmpKey.setVisibility(View.VISIBLE);
					}
					//超级管理员才能设置韦根参数
					if (devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_WIEGAND))
					{
						mLyReaderWiegand.setVisibility(View.VISIBLE);
					}
				}
				
				mLyFuncEKeyManage.setVisibility(View.VISIBLE); // 所有设备都可以发送电子钥匙
				// 设备是否支持联网
				if (device.getNetWorkSupport() == AccessDevBean.WIFI_ENABLE)
				{
					mLyFunctionRemote.setVisibility(View.VISIBLE);
					if(device.getPrivilege() == AccessDevBean.DEV_PRIVILEGE_SUPER)
					{
						mLyFuncWifi.setVisibility(View.VISIBLE);//能联网的才能配置wifi
					}
				}
				mPriSet.setVisibility(View.VISIBLE); // 开放设备设置(管理员以上即可读取设备信息)
			}
			
//			if (ConstantUtils.isKTZApp()) //开拓者屏蔽同步时间和访客授权功能
//			{
//				mLyReaderWiegand.setVisibility(View.GONE);
//				mLyFunctionSyncTime.setVisibility(View.GONE);
//				mLyFunctionRemote.setVisibility(View.GONE);
//				mLyFuncVisitorAuthorize.setVisibility(View.GONE);
//			}
		}
	}
	
	@Override
	public void onClick(View v) {
		if (dev_sn == null || dev_mac == null) {
			Toast.makeText(Act_Device_Info.this,
					R.string.get_nothing_about_the_device, Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = null;
		int i = v.getId();
		if (i == R.id.ly_device_temp_password) {
			intent = new Intent(Act_Device_Info.this, Act_VisitorPass.class);

		} else if (i == R.id.ly_device_digital_key) {
			LogUtils.d(TAG, "ly_device_digital_key");
			intent = new Intent(Act_Device_Info.this, Act_ManageKey.class);

		} else if (i == R.id.ly_device_set_dev_wiegand) {
			intent = new Intent(Act_Device_Info.this, Act_SetDevice.class);

		} else if (i == R.id.ly_device_manage_card) {
			LogUtils.d(TAG, "ly_device_manage_card");
			intent = new Intent(Act_Device_Info.this, Act_CardManage.class);

		} else if (i == R.id.ly_device_digital_tmpKey) {
			LogUtils.d(TAG, "ly_device_digital_tmpKey");
			intent = new Intent(Act_Device_Info.this, SetReadSectorKeyActivity.class);
//			configTmpKey(dev_sn, dev_mac);

		} else if (i == R.id.ly_device_set_dev_rssi) {
			LogUtils.d(TAG, "ly_device_set_dev_rssi");
//			intent = new Intent(Act_Device_Info.this,SetShakeRssiActivity.class);

		} else if (i == R.id.ib_device_info_back) {
			Act_Device_Info.this.finish();

		} else if (i == R.id.ly_device_sycn_dev_time) {//			MyLog.debug("click");
			syncTime(dev_sn, dev_mac);

		} else if (i == R.id.ly_device_config_wifi) {
			LogUtils.d(TAG, "ly_device_config_wifi");
			intent = new Intent(Act_Device_Info.this, ConfigWiFiActivity.class);
//			setWifi(dev_sn, dev_mac);

		} else {
		}
		if (intent != null) {
			intent.putExtra(AccessDevBean.DEVICE_SN, dev_sn);
			intent.putExtra(AccessDevBean.DEVICE_MAC, dev_mac);
//			if(card_loss_or_manager != 0)
//			{//将是否为
//				intent.putExtra(DeviceDom.DEVICE_CARD_MANAGER_OR_LOSS, card_loss_or_manager);
//			}
			startActivity(intent);
		}
	}
	//删除自己的备份
	private void deleteDevice(final String dev_sn) {
		JSONObject json = new JSONObject();
		String client_id = BaseApplication.getInstance().getClientId();
		final String username = SPUtils.getString(Constants.CLIENT_ID);
		try {
			json.put(TimerMsgConstants.CLIENT_ID, client_id);
			json.put(TimerMsgConstants.RESOURCE, TimerMsgConstants.KEY);
			json.put(TimerMsgConstants.OPERATION, "DELETE");
			JSONArray data = getDelData(username,dev_sn);
			json.put(TimerMsgConstants.DATA, data);
			deleteDialog.show();
			OkhttpHelper.upLoadRecord(Constants.URL_POST_COMMANDS, json.toString(), new StringCallback() {
				@Override
				public void onError(Call call, Exception e) {
					if (deleteDialog!=null&&deleteDialog.isShowing()) {
						deleteDialog.dismiss();
					}
				}

				@Override
				public void onResponse(String response) {
					try {

						if (deleteDialog!=null&&deleteDialog.isShowing()) {
							deleteDialog.dismiss();
						}
						JSONObject delete_ret = new JSONObject(response);
						if (delete_ret.isNull(TimerMsgConstants.RET))
						{
							Toast.makeText(Act_Device_Info.this, R.string.delete_device_failed,
									Toast.LENGTH_SHORT).show();
						}
						LogUtils.d("dele_msg" + delete_ret.toString());
						int ret = delete_ret.getInt(TimerMsgConstants.RET);
						if (ret == 0)
						{
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(Act_Device_Info.this,  R.string.delete_device_successful,
											Toast.LENGTH_SHORT).show();
									AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
									deviceData.deleteDevice(username, device);
									SystemInfoData sysData = new SystemInfoData(getApplicationContext());
									SystemInfoBean info = new SystemInfoBean();
									info.setUsername(username);
									info.setDevMac(device.getDevMac());
									sysData.deleteDeviceSystemInfo(info);
									setResult(DEV_INFO_REQ_CODE);
									Act_Device_Info.this.finish();
								}
							});
						}
						else if (ret == Constants.NETWORD_SHUTDOWN)
						{
							Toast.makeText(Act_Device_Info.this, R.string.check_network,
									Toast.LENGTH_SHORT).show();
						}
						else
						{
							String delete = getResources().getString(R.string.delete_device_failed) + ":"+ret;
							Toast.makeText(Act_Device_Info.this, delete,
									Toast.LENGTH_SHORT).show();
						}

					} catch (JSONException e) {

						if (deleteDialog!=null&&deleteDialog.isShowing()) {
							deleteDialog.dismiss();
						}
						e.printStackTrace();
					}
				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block

			if (deleteDialog!=null&&deleteDialog.isShowing()) {
				deleteDialog.dismiss();
			}
			e.printStackTrace();
		}
		
	}
	protected JSONArray getDelData(String username, String dev_sn2) throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray data = new JSONArray();
		json.put(TimerMsgConstants.COMM_ID, 1);
		json.put(TimerMsgConstants.KEY_DEV_SN, dev_sn);
		json.put(TimerMsgConstants.KEY_RECIEVER, username);
		data.put(json);
		return data;
	}
	//远程开门
	private void sendRemoteCMD(final String remote_dev_sn)
	{
		 try {
			JSONObject open_json = new JSONObject();
			String client_id = BaseApplication.getInstance().getClientId();
			if (client_id == null)
			{
				Toast.makeText(Act_Device_Info.this, R.string.open_remote_device_failed,
						Toast.LENGTH_SHORT).show();
				return;
			}
			open_json.put(TimerMsgConstants.CLIENT_ID, client_id);
			open_json.put(TimerMsgConstants.RESOURCE, "door");
			open_json.put(TimerMsgConstants.OPERATION, "OPEN");

			JSONObject data = new JSONObject();
			data.put(TimerMsgConstants.READER_DEV_SN, remote_dev_sn);
			data.put("door_no", 1);
			data.put("action_time", 5);
			open_json.put(TimerMsgConstants.DATA, data);

			LogUtils.d(open_json.toString());
//					JSONObject open_ret = MyHttpClient.connectPost(open_url, open_json);
			 openDialog.show();
			 OkhttpHelper.control(open_json.toString(), new StringCallback() {
				 @Override
				 public void onError(Call call, Exception e) {
					 ToastUtils.showMessage(Act_Device_Info.this, R.string.network_error);
					 if (openDialog!=null&&openDialog.isShowing()) {
						 openDialog.dismiss();
					 }
				 }

				 @Override
				 public void onResponse(String response) {
					 try {
						 JSONObject open_ret = new JSONObject(response);
						 if (openDialog!=null&&openDialog.isShowing()) {
							 openDialog.dismiss();
						 }
						 if (!open_ret.isNull(TimerMsgConstants.RET))
						 {
							 int ret = open_ret.getInt(TimerMsgConstants.RET);
							 LogUtils.d("remote_open_ret: "+ open_ret.toString());
							 if ( ret == 0)
							 {
								 Toast.makeText(Act_Device_Info.this, R.string.open_remote_device_success,
										 Toast.LENGTH_SHORT).show();
							 }
							 else if (ret == Constants.NETWORD_SHUTDOWN )
							 {
								 Toast.makeText(Act_Device_Info.this, R.string.check_network,
										 Toast.LENGTH_SHORT).show();
							 }
							 else
							 {
								 Toast.makeText(Act_Device_Info.this, R.string.open_remote_device_failed,
										 Toast.LENGTH_SHORT).show();
							 }
						 }
						 else
						 {
							 Toast.makeText(Act_Device_Info.this, R.string.open_remote_device_failed,
									 Toast.LENGTH_SHORT).show();
						 }
					 } catch (JSONException e) {
						 if (openDialog!=null&&openDialog.isShowing()) {
							 openDialog.dismiss();
						 }
						 e.printStackTrace();
					 }
				 }
			 });

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			 if (openDialog!=null&&openDialog.isShowing()) {
				 openDialog.dismiss();
			 }
			e.printStackTrace();
		}
		
	}

	//同步时间
	private void syncTime(final String dev_sn, final String dev_mac)
	{
		if (dev_sn == null && dev_mac == null)
		{
			return;
		}
		syncDialog.show();
		LogUtils.d("dev_sn:" + dev_sn + "dev_mac:" + dev_mac);
		final String client_id = BaseApplication.getInstance().getClientId();
//		String parameters = "client_id=" + client_id + "&time_type=2&weekday=1";
//				JSONObject asyncTime_ret = MyHttpClient.connectGet(url, parameters);
		OkhttpHelper.syncTime(client_id, 2, 1, new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {
				if (syncDialog !=null&& syncDialog.isShowing()) {
					syncDialog.dismiss();
				}
			}

			@Override
			public void onResponse(String response) {
				try {
					JSONObject asyncTime_ret = new JSONObject(response);
					if (!asyncTime_ret.isNull("ret"))
					{
						try {
							LogUtils.d(asyncTime_ret.toString());
							int ret = asyncTime_ret.getInt(TimerMsgConstants.RET);
							if (ret == 0 && !asyncTime_ret.isNull("data"))
							{
								JSONObject data = asyncTime_ret.getJSONObject("data");
								String servertime = data.getString("server_time");
								servertime = getLocaleTime(servertime);
								int weekday = data.getInt("weekday") + 1;
								final String sync_time = servertime.substring(2) + "0"+weekday;
								LogUtils.d("sync_time:" + sync_time);
								runOnUiThread(new Runnable() {
									public void run() {
										LogUtils.d("come  syncdevtime");
										SyncDevTime(client_id, sync_time, dev_sn, dev_mac);
									}
								});
							}
							else
							{
								if (syncDialog !=null&& syncDialog.isShowing()) {
									syncDialog.dismiss();
								}
								LogUtils.d("ret:" + ret
										+ "msg=" + asyncTime_ret.getString(TimerMsgConstants.MSG));
							}
						} catch (JSONException e) {
							if (syncDialog !=null&& syncDialog.isShowing()) {
								syncDialog.dismiss();
							}
							LogUtils.d("sync_time json_resolver error");
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							if (syncDialog !=null&& syncDialog.isShowing()) {
								syncDialog.dismiss();
							}
							LogUtils.d("sync_time ParseException error");
							e.printStackTrace();
						}

					}
				} catch (JSONException e) {
					if (syncDialog !=null&& syncDialog.isShowing()) {
						syncDialog.dismiss();
					}
					e.printStackTrace();
				}
			}
		});
	}
	
	/*//配置临时密钥
	private void configTmpKey(final String dev_sn, final String dev_mac)
	{
		com.intelligoo.sdk.LibInterface.ManagerCallback configTmpKeyCallback = new com.intelligoo.sdk.LibInterface.ManagerCallback() 
		{
			@Override
			public void setResult(final int result, Bundle bundle) 
			{
				runOnUiThread(new Runnable() {
					public void run() {						
						if (result == 0x00)
						{
							Toast.makeText(DeviceInfoActivity.this, R.string.activity_config_tmpKey_success, Toast.LENGTH_SHORT).show();
						}
						else 
						{
							Constants.tips(result);
							MyLog.debug("result" + result);
						} 
					}
				});
			}
		};
		DeviceData deviceData = new DeviceData(MyApplication.getInstance());
		String username = MyApplication.getInstance().getUserName();
		DeviceDom device = deviceData.getDevice(username, dev_sn, dev_mac);
		LibDevModel deviceModel = getLibDev(device);
		int dev_id = 1;
		int mifare_section = 1;	
		String tmp_pwd = "FFFFFFFFFF11";
		int configTmpKytRet = LibDevModel.setReadSectorKey(MyApplication.getInstance(), deviceModel, dev_id, mifare_section, tmp_pwd, configTmpKeyCallback);
		if(configTmpKytRet!=0){
			Toast.makeText(MyApplication.getInstance(), "ConfigWifiFailure",
					Toast.LENGTH_SHORT).show();
		}
	}*/

	private String getLocaleTime(String servertime)
			throws ParseException {
		SimpleDateFormat sdf_set = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		Date date = sdf_set.parse(servertime);
		long date_ms = date.getTime() + 8*3600*1000;
		Date curr_date = new Date(date_ms);
		servertime = sdf_set.format(curr_date);
		return servertime;
	}
	
	private void SyncDevTime(String client_id,
                             String time, String dev_sn, String dev_mac)
	{
	
		ManagerCallback syncTimeCallback = new ManagerCallback() {
			
			@Override
			public void setResult(final int result, Bundle bundle) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						if (syncDialog !=null&& syncDialog.isShowing()) {
							syncDialog.dismiss();
						}
						if (result == 0x00)
						{
							Toast.makeText(Act_Device_Info.this, R.string.success_sync, Toast.LENGTH_SHORT).show();
						}
						else 
						{
							String test_str = "failed result:" + result;
//							Constants.tips(result);
							LogUtils.d("result" + result);
						} 
					}
				});
			}
		};
		
		
		
//		ManagerCallback syncTimeCallback = new ManagerCallback() 
//		{
//			@Override
//			public void setResult(final int result, Bundle bundle) 
//			{
//				runOnUiThread(new Runnable() {
//					public void run() {						
//						if (result == 0x00)
//						{
//							Toast.makeText(DeviceInfoActivity.this, R.string.success_sync, Toast.LENGTH_SHORT).show();
//						}
//						else 
//						{
//							String test_str = "failed result:" + result;
//							Constants.tips(result);
//							MyLog.debug("result" + result);
//						} 
//					}
//				});
//			}
//		};
		AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
		String username = SPUtils.getString(Constants.USERNAME);
		AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		if(device == null)
		{
			Toast.makeText(Act_Device_Info.this, R.string.device_not_exist, Toast.LENGTH_SHORT).show();
		}
		LibDevModel libDev = TopkeeperModel.getLibDev(device);
		int ret = LibDevModel.syncDeviceTime(Act_Device_Info.this, libDev, time, syncTimeCallback);
		if(ret != 0)
		{
			ToastUtils.tips(ret);
		}
		
//		Bundle bundle = new Bundle();
//		bundle.putString(DeviceDom.DEVICE_KEY, device.getDevPassword());
//		bundle.putString(DeviceDom.DEVICE_SYC_TIME, time);
//		if (ManagerDev.registBleReceiver(DeviceInfoActivity.this,
//				ManagerDev.MANAGER_TIME_SET, mLeService, bundle, syncTimeCallback)) 
//		{
//			mLeService.connect(dev_mac);
////			ManagerDev.startShortCount();
//		}
//		else
//		{
//			Toast.makeText(DeviceInfoActivity.this, R.string.device_is_for_other_operation, Toast.LENGTH_SHORT).show();
//		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DEV_INFO_REQ_CODE) {
			if (resultCode == Act_DeviceSetting.DEV_SET_REQ_CODE) {
				setResult(DEV_INFO_REQ_CODE);
				Act_Device_Info.this.finish();
			}
		}
		
	}
	
	
//	private void setDeviceRssi(int rssi) {
//		DeviceData deviceData = new DeviceData(MyApplication.getInstance());
//		String username = MyApplication.getInstance().getUserName();
//		device.setOpenDistance(rssi);
//		MyLog.debug(device.toString());
//		deviceData.saveDevice(device);
//		Toast.makeText(DeviceInfoActivity.this, 
//				getResources().getString(R.string.setting_distance_success),
//				Toast.LENGTH_LONG).show();
//		final ProgressDialog dialog = new ProgressDialog(DeviceInfoActivity.this);
//		dialog.setMessage(getResources().getString(R.string.remind_of_setting_distance));
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.show();
//		ManagerCallback rssiCallback  = new ManagerCallback() {
//			@Override
//			public void setResult(final int result, final Bundle bundle) {
//				
//				runOnUiThread(new Runnable() {
//					public void run() {
//						dialog.dismiss();
//						if (result == ManagerDev.RESULT_CALLBACK_SUCCESS) {
//							DeviceData deviceData = new DeviceData(MyApplication.getInstance());
//							String username = MyApplication.getInstance().getUserName();
//							DeviceDom device = deviceData.getDevice(username, dev_sn, dev_mac);
//							MyLog.Assert(bundle != null);
//							int distance = bundle.getInt(DeviceDom.SHAKE_DISTANCE, -100);
//							device.setOpenDistance(distance);
//							MyLog.debug(device.toString());
//							deviceData.saveDevice(device);
//							Toast.makeText(DeviceInfoActivity.this, 
//									getResources().getString(R.string.setting_distance_success),
//									Toast.LENGTH_SHORT).show();
//						} else {
//							Constants.tips(result);
//						}
//					}
//				});
//			}
//		};
//		Bundle bundle = new Bundle();
//		if (ManagerDev.registBleReceiver(DeviceInfoActivity.this,
//				ManagerDev.MANAGER_SET_RSSI, mLeService, bundle, rssiCallback)){
//			mLeService.connect(dev_mac);
//		}
//			
//	}
	private void setDevName(final AccessDevMetaData deviceData,
			final AccessDevBean device) {
		mDevName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.dialog_input_editor,null);
				final EditText etDevName = (EditText) layout.findViewById(R.id.et_show_name);
				etDevName.setText(device.getDevName());
				etDevName.setSelection(etDevName.getText().length());
				
				AlertDialog.Builder  alertDialog = new AlertDialog.Builder(Act_Device_Info.this).
					setTitle(getResources().getString(R.string.modify_device_name)).
					setIcon(android.R.drawable.ic_dialog_info).
					setView(layout);
				alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String newDevName = etDevName.getText().toString();
						if (newDevName != null) {
							device.setDevName(newDevName);
							deviceData.saveAccessDev(device);
							mDevName.setText(newDevName);
						}
						dialog.dismiss();
					}
				});
				alertDialog.show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (syncDialog!=null&&syncDialog.isShowing()) {
			syncDialog.dismiss();
		}
		if (openDialog!=null&&openDialog.isShowing()) {
			openDialog.dismiss();
		}
		if (deleteDialog!=null&&deleteDialog.isShowing()) {
			deleteDialog.dismiss();
		}
	}
}
