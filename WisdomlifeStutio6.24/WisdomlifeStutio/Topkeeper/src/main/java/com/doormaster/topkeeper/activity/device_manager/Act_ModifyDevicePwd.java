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
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.UserBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.UserData;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface.ManagerCallback;

public class Act_ModifyDevicePwd extends BaseActivity {
	
	private TextView mTitle;
	private ImageButton mImgScan;
	private ImageButton mBack;
	
	private EditText mTVCurrPwd;
	private EditText mEtNewPwd;
	private EditText mEtEnsure;
	private Button mSubmitModify;
//	protected BluetoothLeService mLeService;
	
	public static final String DEVICE_MODIFY_OLD_PWD =
			"com.intelligoo.app.task.Act_ModifyDevicePwd.DEVICE_MODIFY_OLD_PWD";
	public static final String DEVICE_MODIFY_NEW_PWD =
			"com.intelligoo.app.task.Act_ModifyDevicePwd.DEVICE_MODIFY_NEW_PWD";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_device_modify_pwd);
		initView();
		Intent intent = getIntent();
//		final Bundle bundle = new Bundle();
		final AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
		final String username = SPUtils.getString(Constants.USERNAME);
		String dev_sn = intent.getStringExtra(AccessDevBean.DEVICE_SN);
		final String dev_mac = intent.getStringExtra(AccessDevBean.DEVICE_MAC);
		final AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		final String comm_key = device.geteKey();
		
		final ManagerCallback deviceModifyCallback = new ManagerCallback() {
			
			@Override
			public void setResult(final int result, final Bundle bundle) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						if (result == 0) {
							Toast.makeText(getApplicationContext(), R.string.modify_device_password,
									Toast.LENGTH_SHORT).show();
							String new_manager_pwd = bundle.getString(DEVICE_MODIFY_NEW_PWD);
							LogUtils.d("new pwd"+new_manager_pwd);
							mTVCurrPwd.setText(new_manager_pwd);
							device.setDevManagerPassword(new_manager_pwd);
							deviceData.saveAccessDev(device);
						} else {
//							Constants.tips(result);
						}
					}
				});
			}
		};
		
		mSubmitModify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mEtNewPwd.getText().length()!=6
						|| mEtEnsure.getText().length()!=6 
						|| mEtNewPwd.getText().equals(mEtEnsure.getText())) {
					Toast.makeText(Act_ModifyDevicePwd.this, R.string.error_format_device_password,
							Toast.LENGTH_SHORT).show();
					return;
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
				
				String newPWD = mEtNewPwd.getText().toString().trim();
				String oldPWD = mTVCurrPwd.getText().toString().trim();
				if (mTVCurrPwd.length() == 0)
				{
					oldPWD = "000000";
				}
				else if (oldPWD.length() != 6)
				{
					Toast.makeText(Act_ModifyDevicePwd.this, R.string.error_format_device_password,
							Toast.LENGTH_SHORT).show();
					return;
				}
				
//				bundle.putString (DEVICE_MODIFY_OLD_PWD,oldPWD);
//				bundle.putString (DEVICE_MODIFY_NEW_PWD,newPWD);
//				bundle.putString (AccessDevBean.DEVICE_KEY,comm_key);
//				bundle.putInt(AccessDevBean.DEV_FROM,device.getDevRes());
				
				UserData userData = new UserData(getApplicationContext());
				UserBean user = userData.getUser(username);
				
//				if (device.getDevResPhone() == null)
//				{
//					bundle.putString(AccessDevBean.DEV_FROM_PHONE, device.getDevResPhone());
////					bundle.putString(UserDom.USER_IDENTITY, user.getIdentity());
//				}
//				else
//				{
////					bundle.putString(DeviceDom.DEV_FROM_PHONE, username);
//					bundle.putString(AccessDevBean.DEV_FROM_PHONE, user.getIdentity());
//				}
				
				final LibDevModel libDev = getLibDev(device);
				int ret = LibDevModel.modifyPwd(Act_ModifyDevicePwd.this, libDev, oldPWD, newPWD, deviceModifyCallback);
				if(ret != 0)
				{
//					Constants.tips(ret);
				}
				
				
//				if ( ManagerDev.registBleReceiver(Act_ModifyDevicePwd.this,
//						ManagerDev.MANAGER_MODIFY_PWD, mLeService, 
//						bundle, deviceModifyCallback)){
//					mLeService.connect(dev_mac);
//				}
				
			}
		});
		
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		mTitle = (TextView) findViewById(R.id.ib_frag_title);
		mBack = (ImageButton) findViewById(R.id.ib_frag_back_img);
		mImgScan = (ImageButton) findViewById(R.id.ib_activity_scan_add);
		mTVCurrPwd = (EditText) findViewById(R.id.activity_setting_current_pwd);
		mEtNewPwd = (EditText) findViewById(R.id.activity_setting_new_pwd);
		mEtEnsure = (EditText) findViewById(R.id.activity_setting_ensure_new_pwd);
		mSubmitModify =(Button) findViewById(R.id.activity_setting_btn_modify_pwd);
		
		mTitle.setText(R.string.activity_device_modify_pwd);
		mBack.setVisibility(View.VISIBLE);
		mImgScan.setVisibility(View.INVISIBLE);
		
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Act_ModifyDevicePwd.this.finish();
			}
		});
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
