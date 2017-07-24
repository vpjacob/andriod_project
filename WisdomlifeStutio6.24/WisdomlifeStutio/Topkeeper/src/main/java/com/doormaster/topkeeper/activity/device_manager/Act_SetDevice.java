package com.doormaster.topkeeper.activity.device_manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.doormaster.topkeeper.utils.ToastUtils;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface.ManagerCallback;


public class Act_SetDevice extends BaseActivity {
	
	private TextView mTitle;
	private ImageButton mImgScan;
	private ImageButton mBack;
	
	private RadioGroup mRGWiegand;
	private RadioGroup mRGContral;
	private Button mSubmit;
	private LinearLayout mReaderLift;
	private LinearLayout mAccessControllor;
	private EditText mOpenInterval;
	
	
	private SystemInfoBean systeminfo  = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_device_set_dev_info);
		
		Intent intent = getIntent();
		final AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
		final String username = SPUtils.getString(Constants.USERNAME);
		String dev_sn = intent.getStringExtra(AccessDevBean.DEVICE_SN);
		final String dev_mac = intent.getStringExtra(AccessDevBean.DEVICE_MAC);
		final AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		final String comm_key = device.geteKey();
		final int dev_type = device.getDevType();
		
		SystemInfoData systemData = new SystemInfoData(getApplicationContext());
		systeminfo = systemData.getDeviceSystemInfo(username, dev_mac);
		if (systeminfo == null)
		{
			LogUtils.d("systeminfo is null");
			return;
		}
		
		initView(dev_type,systeminfo);
		
		
		final ManagerCallback updateConfigCallback = new ManagerCallback() {
			
			@Override
			public void setResult(final int result, Bundle bundle) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						if (result == 0) {
							Toast.makeText(Act_SetDevice.this,
									R.string.update_configure_success, Toast.LENGTH_SHORT).show();
						} else {
							ToastUtils.tips(result);
						}
					}
				});
				if (bundle == null)
				{
					return;
				}
				byte wg_format = (byte) bundle.getInt(SystemInfoBean.WIEGAND);
				byte open_delay = (byte) bundle.getInt(SystemInfoBean.OPEN_DELAY);
				int open_delay_convert = open_delay & 0x00ff;
				byte dev_switch = (byte) bundle.getInt(SystemInfoBean.CONTROL);
				if (systeminfo != null) {					
					systeminfo.setWiegand(wg_format);
					systeminfo.setOpenDelay(open_delay_convert);
					systeminfo.setControlWay(dev_switch);
					SystemInfoData save = new SystemInfoData(getApplicationContext());
					save.saveSystemINfo(systeminfo);	
				}
			}
		};
		
//		final ManagerCallback updateConfigCallback = new ManagerCallback() {
//			
//			@Override
//			public void setResult(final int result, Bundle bundle) {
//				runOnUiThread(new Runnable() {
//					public void run() {
//						if (result == ManagerDev.RESULT_CALLBACK_SUCCESS) {
//							Toast.makeText(SetDevice.this, 
//									R.string.update_configure_success, Toast.LENGTH_SHORT).show();
//						} else {
//							Constants.tips(result);
//						}
//					}
//				});
//				if (bundle == null)
//				{
//					return;
//				}
//				byte wg_format = (byte) bundle.getInt(SystemInfoDom.WIEGAND);
//				byte open_delay = (byte) bundle.getInt(SystemInfoDom.OPEN_DELAY);
//				int open_delay_convert = open_delay & 0x00ff;
//				byte dev_switch = (byte) bundle.getInt(SystemInfoDom.CONTROL);
//				if (systeminfo != null) {					
//					systeminfo.setWiegand(wg_format);
//					systeminfo.setOpenDelay(open_delay_convert);
//					systeminfo.setControlWay(dev_switch);
//					SystemInfoData save = new SystemInfoData(MyApplication.getInstance());
//					save.saveSystemINfo(systeminfo);	
//				}
//			} 
//		};
		
		mSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtils.d("coming,mSubmit");
				Bundle bundle = getBundle(dev_mac, comm_key, dev_type);
				if (bundle == null)
				{
					return;
				}
//				bundle.putInt(AccessDevBean.DEV_FROM,device.getDevRes());
				
				UserData userData = new UserData(getApplicationContext());
				UserBean user = userData.getUser(username);
//				MyLog.debug(device.getDevResPhone());
//				if (device.getDevResPhone()  != null)
//				{
//					bundle.putString(AccessDevBean.DEV_FROM_PHONE, device.getDevResPhone());//判断设备来源
//				}
//				else
//				{
////					MyLog.debug(user.getIdentity());
//					bundle.putString(AccessDevBean.DEV_FROM_PHONE, user.getIdentity());//判断设备来源
//				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
			
				int wiegand = bundle.getInt(SystemInfoBean.WIEGAND);
				int openDelay = bundle.getInt(SystemInfoBean.OPEN_DELAY);
				int controlWay = bundle.getInt(SystemInfoBean.CONTROL);
				
				LibDevModel libDev = getLibDev(device);
				int ret = LibDevModel.setDeviceConfig(Act_SetDevice.this, libDev, wiegand, openDelay, controlWay, updateConfigCallback);
				if(ret != 0)
				{
					Toast.makeText(Act_SetDevice.this,
							R.string.update_configure_failed, Toast.LENGTH_SHORT).show();
				}
				
				
//				if (ManagerDev.registBleReceiver(SetDevice.this, 
//						ManagerDev.MANAGER_UPDATE_CONFIG, mLeService, 
//						bundle, updateConfigCallback)){
//					mLeService.connect(dev_mac);
//				}
			}
			
		});
		
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Act_SetDevice.this.finish();
			}
		});
	}
	
	private Bundle getBundle(String mac, String comm_key, int dev_type)
	{
		
		int wg_format = SystemInfoBean.WIEGAND_26;
		
		if (mReaderLift.getVisibility() == View.VISIBLE)
		{
			if (mRGWiegand.getCheckedRadioButtonId()== R.id.rb_set_device_info_Wiegand_26) {
				wg_format = SystemInfoBean.WIEGAND_26;
			} else if (mRGWiegand.getCheckedRadioButtonId() == R.id.rb_set_device_info_Wiegand_34) {
				wg_format = SystemInfoBean.WIEGAND_34;
			}
			
		}
		int dev_switch = SystemInfoBean.CONTROL_lOCK;
		int open_delay = 5;
		if (mAccessControllor.getVisibility() == View.VISIBLE)
		{
			String openInterval = mOpenInterval.getText().toString();
			try {
				open_delay = Integer.parseInt(openInterval);
			} catch (NumberFormatException e) {
				Toast.makeText(Act_SetDevice.this, R.string.error_open_delay_parament, Toast.LENGTH_SHORT).show();
				return null;
			}
			if (mRGContral.getCheckedRadioButtonId() == R.id.rb_set_device_electric_lock) {
				dev_switch = SystemInfoBean.CONTROL_lOCK;
			} else if (mRGContral.getCheckedRadioButtonId() == R.id.rb_set_device_electric_button) {
				dev_switch = SystemInfoBean.CONTROL_ELECTRIC;
			}
		}
		if ((open_delay == 0) || (open_delay > 254))
		{
			Toast.makeText(Act_SetDevice.this, R.string.error_open_delay_parament, Toast.LENGTH_SHORT).show();
			return null;
		}
		
		Bundle bundle = new Bundle();
		bundle.putInt(SystemInfoBean.WIEGAND, wg_format);
		bundle.putInt(SystemInfoBean.CONTROL, dev_switch);
		bundle.putInt(SystemInfoBean.OPEN_DELAY, open_delay);
		bundle.putString(AccessDevBean.DEVICE_MAC, mac);
		bundle.putString(AccessDevBean.DEVICE_KEY, comm_key);
		bundle.putInt(AccessDevBean.DEVICE_TYPE, dev_type);
		return bundle;
	}
	
	private void initView(int dev_type,SystemInfoBean sys) {
		// TODO Auto-generated method stub
		
		mTitle = (TextView) findViewById(R.id.ib_frag_title);
		mBack = (ImageButton) findViewById(R.id.ib_frag_back_img);
		mImgScan = (ImageButton) findViewById(R.id.ib_activity_scan_add);
		mRGWiegand = (RadioGroup) findViewById(R.id.rg_set_device_info_Wiegand);
		mRGContral = (RadioGroup) findViewById(R.id.rg_set_device_electric_button);
		mSubmit = (Button) findViewById(R.id.bt_set_device_submit);
		mReaderLift = (LinearLayout) findViewById(R.id.ly_set_device_reader_lift);
		mAccessControllor = (LinearLayout) findViewById(R.id.ly_set_device_access_controllor);
		mOpenInterval = (EditText)findViewById(R.id.sp_set_device_open_time);
		
		mTitle.setText(R.string.activity_device_set_dev);
		mBack.setVisibility(View.VISIBLE);
		mImgScan.setVisibility(View.INVISIBLE);
		
		if (dev_type != AccessDevBean.DEV_TYPE_READER &&
				dev_type != AccessDevBean.DEV_TYPE_QRCODE_DEVICE ) {
			mAccessControllor.setVisibility(View.VISIBLE);
			mReaderLift.setVisibility(View.GONE);
			mOpenInterval.setText(String.format("%d",sys.getOpenDelay()));
			
			RadioButton wg_lock = (RadioButton)findViewById(R.id.rb_set_device_electric_lock);
			RadioButton wg_switch = (RadioButton)findViewById(R.id.rb_set_device_electric_button);
			if (sys.getControlWay()  == SystemInfoBean.CONTROL_lOCK)
			{
				wg_lock.setChecked(true);
				wg_switch.setChecked(false);
			}
			else
			{
				wg_lock.setChecked(false);
				wg_switch.setChecked(true);
			}
		} else {
			mAccessControllor.setVisibility(View.GONE);
			mReaderLift.setVisibility(View.VISIBLE);
			RadioButton wg_26 = (RadioButton)findViewById(R.id.rb_set_device_info_Wiegand_26);
			RadioButton wg_34 = (RadioButton)findViewById(R.id.rb_set_device_info_Wiegand_34);
			if (sys.getWiegand() == SystemInfoBean.WIEGAND_26)
			{
				wg_26.setChecked(true);
				wg_34.setChecked(false);
			}
			else
			{
				wg_26.setChecked(false);
				wg_34.setChecked(true);
			}
			
		}
		
	}

	private static LibDevModel getLibDev(AccessDevBean dev) {
		LibDevModel device = new LibDevModel();
		device.devSn = dev.getDevSn();
		device.devMac = dev.getDevMac();
		device.devType = dev.getDevType();
		device.eKey = dev.geteKey();
		// device.encrytion = dev.getEncrytion();
		device.endDate = dev.getEndDate();
		device.openType = dev.getOpenType();
		device.privilege = dev.getPrivilege();
		device.startDate = dev.getStartDate();
		device.useCount = dev.getUseCount();
		device.verified = dev.getVerified();
		return device;
	}
}
