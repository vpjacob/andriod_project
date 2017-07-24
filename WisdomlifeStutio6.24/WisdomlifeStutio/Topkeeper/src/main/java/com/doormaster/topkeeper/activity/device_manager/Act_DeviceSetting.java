package com.doormaster.topkeeper.activity.device_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.constant.TimerMsgConstants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.utils.SPUtils;

import java.util.Map;

public class Act_DeviceSetting extends BaseActivity implements OnClickListener {
	
	private TextView mTitle;
	private ImageButton mImgScan;
	private ImageButton mBack;
	private LinearLayout mLyModifyDevPwd;
	private LinearLayout mModifyDevPwd;
	private LinearLayout mSetDevice;
	private LinearLayout mGetDevSysInfo;
	private LinearLayout mClearAllInfo;
	private LinearLayout mResetDevice;
	private String dev_sn;
	private String dev_mac;
	public static final int DEV_SET_REQ_CODE = 0x02;
	private AccessDevBean device;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_setting);
		Intent intent = getIntent();
		dev_sn = intent.getStringExtra(AccessDevBean.DEVICE_SN);
		dev_mac = intent.getStringExtra(AccessDevBean.DEVICE_MAC);
		AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
		String username = SPUtils.getString(Constants.USERNAME);
		device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		initView();
//		if (device != null )
//		{
//			int support = device.getSupportFun();
//			MyLog.debug("support:" + support);
//			if ( ((support>>3) % 2) == 0) 
//			{
//				mLyModify.setVisibility(View.GONE);
//			}
//		}
		initOnClick();
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.ib_frag_title);
		mBack = (ImageButton) findViewById(R.id.ib_frag_back_img);
		mImgScan = (ImageButton) findViewById(R.id.ib_activity_scan_add);
		mLyModifyDevPwd = (LinearLayout) findViewById(R.id.ly_modify_device_password);
		mModifyDevPwd = (LinearLayout) findViewById(R.id.ly_device_modify_admin_password);
		mSetDevice = (LinearLayout) findViewById(R.id.ly_device_set_dev);
		mClearAllInfo = (LinearLayout) findViewById(R.id.ly_device_delete_all);
		mGetDevSysInfo = (LinearLayout) findViewById(R.id.ly_device_get_system_info);
		mResetDevice = (LinearLayout) findViewById(R.id.ly_device_reset);
		
		mTitle.setText(R.string.activity_device_setting);
		mBack.setVisibility(View.VISIBLE);
		mImgScan.setVisibility(View.INVISIBLE);
		
//		if(device.getPrivilege() == AccessDevBean.DEV_PRIVILEGE_SUPER) //超级管理员才能设置设备信息和恢复出厂设置
//		{
//			mSetDevice.setVisibility(View.VISIBLE);
//			mResetDevice.setVisibility(View.VISIBLE);
//		}
//		//M100门禁一体机   M160门禁一体机  无线锁 可修改密码和清空卡、用户信息
//		if (device.getDevType() == AccessDevBean.DEV_TYPE_ACCESS_CONTROLLER || device.getDevType() == AccessDevBean.DEV_TYPE_LOCK ||
//				device.getDevType()==AccessDevBean.DEV_TYPE_DM_DEVICE)
//		{
//			mLyModifyDevPwd.setVisibility(View.VISIBLE); // 管理员即可修改密码
//			if(device.getPrivilege() == AccessDevBean.DEV_PRIVILEGE_SUPER)
//			{
//				mClearAllInfo.setVisibility(View.VISIBLE);
//			}
//		}
		
		
		
		if (device.getFunction() != null && !"".equals(device.getFunction()))
		{
			Map<String, String> devFuncMap = AccessDevBean.getDevFuncMap(device.getFunction());
			if (devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_MODIFY_PWD))
			{
				mLyModifyDevPwd.setVisibility(View.VISIBLE);
			}
			if(device.getPrivilege() == AccessDevBean.DEV_PRIVILEGE_SUPER)
			{
				mResetDevice.setVisibility(View.VISIBLE);
				
				if (devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_CLEAR_DATA))
				{
					mClearAllInfo.setVisibility(View.VISIBLE);
				}
				if(devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_LOCK_SETTING))
				{
					mSetDevice.setVisibility(View.VISIBLE);
				}
			}
			
		}
		
		
		//开拓者专门限制
//		if (ContentUtils.isKTZApp())
//		{
//			mLyModifyDevPwd.setVisibility(View.GONE);
//		}
	}
	
	private void initOnClick() {
		mModifyDevPwd.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mGetDevSysInfo.setOnClickListener(this);
		mResetDevice.setOnClickListener(this);
		mSetDevice.setOnClickListener(this);
		mClearAllInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Act_DeviceSetting.this,DeviceAlertDialog.class);
				intent.putExtra(AccessDevBean.DEVICE_SN, dev_sn);
				intent.putExtra(AccessDevBean.DEVICE_MAC, dev_mac);
				intent.putExtra(DeviceAlertDialog.INTENT_ID, DeviceAlertDialog.INTENT_ID_DELET);
				startActivityForResult(intent, DEV_SET_REQ_CODE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		int i = v.getId();
		if (i == R.id.ly_device_modify_admin_password) {
			intent = new Intent(Act_DeviceSetting.this, Act_ModifyDevicePwd.class);

		} else if (i == R.id.ly_device_set_dev) {
			intent = new Intent(Act_DeviceSetting.this, Act_SetDevice.class);

		} else if (i == R.id.ly_device_get_system_info) {
			intent = new Intent(Act_DeviceSetting.this, Act_GetDeviceSystemInfo.class);

		} else if (i == R.id.ly_device_reset) {
			intent = new Intent(Act_DeviceSetting.this, DeviceAlertDialog.class);
			intent.putExtra(DeviceAlertDialog.INTENT_ID, DeviceAlertDialog.INTENT_ID_RESET);

		} else if (i == R.id.ib_frag_back_img) {
			Act_DeviceSetting.this.finish();

		} else {
		}
		if (intent != null){
			intent.putExtra(AccessDevBean.DEVICE_SN, dev_sn);
			intent.putExtra(AccessDevBean.DEVICE_MAC, dev_mac);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DEV_SET_REQ_CODE) {
			if (resultCode == DeviceAlertDialog.DEV_ALERT_RES_CODE) {
				setResult(DEV_SET_REQ_CODE);
				Act_DeviceSetting.this.finish();
			}
		}
	}
}
