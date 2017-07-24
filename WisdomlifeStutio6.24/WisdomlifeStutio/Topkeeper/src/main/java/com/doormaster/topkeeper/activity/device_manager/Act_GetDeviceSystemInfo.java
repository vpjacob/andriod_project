package com.doormaster.topkeeper.activity.device_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.SystemInfoBean;
import com.doormaster.topkeeper.bean.UserBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.SystemInfoData;
import com.doormaster.topkeeper.db.UserData;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface.ManagerCallback;


public class Act_GetDeviceSystemInfo extends BaseActivity {
	
	private TextView mTitle;
	private ImageButton mImgScan;
	private ImageButton mBack;
	private Button mBtGetSys;
	private TextView mMark;
	private TextView mWieGand;
	private TextView mOpenDelay;
	private TextView mRecordCardsNums;
	private TextView mRecordPhoneNums;
	private TextView mMaxContainer;
	private TextView mDevSwitch;
	private TextView mDevSysVerion;
	private LinearLayout mLySystemInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_get_system_info);

		Intent intent = getIntent();
//		final Bundle bundle = new Bundle();
		final AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
		final String username = SPUtils.getString(Constants.USERNAME);
		String dev_sn = intent.getStringExtra(AccessDevBean.DEVICE_SN);
		final String dev_mac = intent.getStringExtra(AccessDevBean.DEVICE_MAC);
		final AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		if(device == null)
		{
			LogUtils.e("device == null");
			return;
		}
		final String comm_key = device.geteKey();
		SystemInfoData systemData = new SystemInfoData(getApplicationContext());
		SystemInfoBean systeminfo = systemData.getDeviceSystemInfo(username, dev_mac);
		if (systeminfo == null)
		{
			LogUtils.e("systeminfo == null");
			return;
		}
		initView(systeminfo);
		
		final ManagerCallback getSysCallback = new ManagerCallback() {
			
			@Override
			public void setResult(final int result, final Bundle bundle) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {	
						mMark.setVisibility(View.GONE);
						mBtGetSys.setText(R.string.activity_device_get_system_info_touch_again);
						if (result == 0) {
							resetView( bundle, dev_mac);
							mLySystemInfo.setVisibility(View.VISIBLE);
						} else {
//							Constants.tips(result);
						}
					}
				});
			}
		};
		
//		final ManagerCallback getSysCallback = new ManagerCallback() {
//			@Override
//			public void setResult(final int result, final Bundle bundle) 
//			{
//				
//				runOnUiThread(new Runnable() {
//					public void run() {	
//						mMark.setVisibility(View.GONE);
//						mBtGetSys.setText(R.string.activity_device_get_system_info_touch_again);
//						if (result == ManagerDev.RESULT_CALLBACK_SUCCESS) {
//							MyLog.Assert(bundle != null);
//							resetView( bundle, dev_mac);
//							mLySystemInfo.setVisibility(View.VISIBLE);
//						} else {
//							Constants.tips(result);
//						}
//					}
//				});
//			}
//		};
		
		
		mBtGetSys.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtils.d("coming mBtGetSys");
//				bundle.putString(AccessDevBean.DEVICE_KEY, comm_key);
//				bundle.putInt(AccessDevBean.DEV_FROM,device.getDevRes());
				String username = SPUtils.getString(Constants.USERNAME);
				UserData userData = new UserData(getApplicationContext());
				UserBean user = userData.getUser(username);
//				LogUtils.d();(device.getDevResPhone());
//				if (device.getDevResPhone()== null)
//				{
//					LogUtils.d(user.getIdentity());
//					bundle.putString(AccessDevBean.DEV_FROM_PHONE, user.getIdentity());//判断设备来源
////				bundle.putString(AccessDevBean.DEV_FROM_PHONE, device.getDevResPhone());
//				}
//				else
//				{
////					LogUtils.d();(user.getIdentity());
////					bundle.putString(UserDom.USER_IDENTITY, user.getIdentity());
//					bundle.putString(AccessDevBean.DEV_FROM_PHONE, device.getDevResPhone());//判断设备来源
//				}
				
				final LibDevModel libDev = getLibDev(device);
				
				int ret = LibDevModel.getDeviceConfig(Act_GetDeviceSystemInfo.this, libDev, getSysCallback);
				if(ret == 0)
				{
					mMark.setVisibility(View.VISIBLE);
				}else
				{
//					Constants.tips(ret);
				}
				
//				if (ManagerDev.registBleReceiver(GetDeviceSystemInfo.this, 
//						ManagerDev.MANAGER_GET_CONFIG, mLeService,
//						bundle, getSysCallback)) {
//					mLeService.connect(dev_mac);
//					mMark.setVisibility(View.VISIBLE);
//				}
			}
		});
		
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Act_GetDeviceSystemInfo.this.finish();
			}
		});
	}

	private void initView(SystemInfoBean systeminfo)
	{
		// TODO Auto-generated method stub
		mTitle = (TextView) findViewById(R.id.ib_frag_title);
		mBack = (ImageButton) findViewById(R.id.ib_frag_back_img);
		mImgScan = (ImageButton) findViewById(R.id.ib_activity_scan_add);
		mBtGetSys = (Button) findViewById(R.id.bt_device_get_system_info);
		mLySystemInfo = (LinearLayout) findViewById(R.id.ly_device_system_info);
		
		mMark = (TextView) findViewById(R.id.tv_device_getting_system);
		mWieGand = (TextView) findViewById(R.id.tv_device_get_system_wiegand);
		mOpenDelay = (TextView) findViewById(R.id.tv_device_get_system_open_delay);
		mRecordCardsNums = (TextView) findViewById(R.id.tv_device_get_system_cards_numbers);
		mRecordPhoneNums = (TextView) findViewById(R.id.tv_device_get_system_phone_numbers);
		mMaxContainer = (TextView) findViewById(R.id.tv_device_get_system_max_user_container);
		mDevSwitch = (TextView) findViewById(R.id.tv_device_get_system_device_button);
		mDevSysVerion = (TextView) findViewById(R.id.tv_device_get_system_version);
		
		if (systeminfo == null) {
			mLySystemInfo.setVisibility(View.GONE);
		} else {
			mWieGand.setText(R.string.activity_device_set_dev_Wiegand+ Integer.toString(systeminfo.getWiegand()));
			mOpenDelay.setText(Integer.toString(systeminfo.getOpenDelay()));
			mRecordCardsNums.setText(Integer.toString(systeminfo.getRegCardNum()));
			if (systeminfo.getControlWay() == SystemInfoBean.CONTROL_ELECTRIC) {
				mDevSwitch.setText(R.string.activity_device_set_dev_electric_button);
			} else {
				mDevSwitch.setText(R.string.activity_device_set_dev_electric_lock);
			}
			mRecordPhoneNums.setText(Integer.toString(systeminfo.getRegPhoneNum()) );
			mMaxContainer.setText( Integer.toString(systeminfo.getMaxContainer()) );
			mDevSysVerion.setText(Integer.toString(systeminfo.getVersion()));
			
			mLySystemInfo.setVisibility(View.VISIBLE);
			mBtGetSys.setText(R.string.activity_device_get_system_info_touch_again);
			
		}
		
		mTitle.setText(R.string.activity_device_get_system_info);
		mBack.setVisibility(View.VISIBLE);
		mImgScan.setVisibility(View.INVISIBLE);
	}
	
	private void resetView(Bundle bundle, String dev_mac)
	{
		SystemInfoData infoData = new SystemInfoData(getApplicationContext());
		SystemInfoBean info = new SystemInfoBean();
		info.setUsername(SPUtils.getString(Constants.USERNAME));
		info.setDevMac(dev_mac);
		info.setWiegand(bundle.getInt(SystemInfoBean.WIEGAND));
		int open_delay = bundle.getInt(SystemInfoBean.OPEN_DELAY) & 0x00ff;
		info.setOpenDelay(open_delay);
		info.setRegCardNum(bundle.getInt(SystemInfoBean.REG_CARDS_NUMS));
		info.setRegPhoneNum(bundle.getInt(SystemInfoBean.REG_PHONE_NUMS));
		info.setControlWay(bundle.getInt(SystemInfoBean.CONTROL));
		info.setMaxContainer(bundle.getInt(SystemInfoBean.MAX_CONTAINER));
		info.setVersion( bundle.getInt(SystemInfoBean.DEV_SYSTEM_VERSION));
		
		mWieGand.setText(R.string.activity_device_set_dev_Wiegand+ Integer.toString(info.getWiegand()));
		mOpenDelay.setText(Integer.toString(info.getOpenDelay()));
		mRecordCardsNums.setText(Integer.toString(info.getRegCardNum()));
		if (info.getControlWay() == SystemInfoBean.CONTROL_ELECTRIC) {
			mDevSwitch.setText(R.string.activity_device_set_dev_electric_button);
		} else {
			mDevSwitch.setText(R.string.activity_device_set_dev_electric_lock);
		}
		mRecordPhoneNums.setText(Integer.toString(info.getRegPhoneNum()) );
		mMaxContainer.setText( Integer.toString(info.getMaxContainer()) );
		mDevSysVerion.setText(Integer.toString(info.getVersion()));
		infoData.saveSystemINfo(info);
	}

	private static LibDevModel getLibDev(AccessDevBean dev) {
		LibDevModel device = new LibDevModel();
		device.devSn = dev.getDevSn();
		device.devMac = dev.getDevMac();
		device.devType = dev.getDevType();
		device.eKey = dev.geteKey();
		device.endDate = dev.getEndDate();
		device.openType = dev.getOpenType();
		device.privilege = dev.getPrivilege();
		device.startDate = dev.getStartDate();
		device.useCount = dev.getUseCount();
		device.verified = dev.getVerified();
		return device;
	}
	
}
