package com.doormaster.topkeeper.activity.device_manager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.UserData;
import com.doormaster.topkeeper.utils.DialogUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.intelligoo.sdk.ConstantsUtils;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;

import java.util.Map;

public class ConfigWiFiActivity extends BaseActivity {
	
	private EditText edt_ip;
	private EditText edt_port;
	private EditText edt_route_name;
	private EditText edt_route_pswd;
	private Button btn_set;
	private UserData userData;
	private ImageView img_back;
	//设备序列号
	private String dev_sn = null;
	private String dev_mac = null;
	
	//配置WiFi参数
	private String ip;
	private String port;
	private String route_name;
	private String route_pswd;
	
	private boolean pressed;
	private String username;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_wifi);
		initView();
		dev_sn = getIntent().getStringExtra(AccessDevBean.DEVICE_SN);
		dev_mac = getIntent().getStringExtra(AccessDevBean.DEVICE_MAC);
		getWiFiParameter();
	}
	
	private void initView()
	{
		dialog = DialogUtils.createLoadingDialog(this, getResources().getString(R.string.configure_wifi));//保证每次登陆都能让动画转起来
		edt_ip = (EditText) findViewById(R.id.set_ip_address_edt);
		edt_port = (EditText) findViewById(R.id.set_port_edt);
		edt_route_name = (EditText) findViewById(R.id.set_wifi_name_edt);
		edt_route_pswd = (EditText) findViewById(R.id.set_wifi_pswd_edt);
		btn_set = (Button) findViewById(R.id.set_wifi_btn);
		userData = new UserData(BaseApplication.getInstance());
		img_back = (ImageView) findViewById(R.id.config_wifi_back_img);

		edt_ip.setText("120.24.229.67");
		edt_port.setText("8169");

		img_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		
		});

		username = SPUtils.getString(Constants.USERNAME);
		btn_set.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(!edt_ip.getText().toString().equals(ip) || !edt_port.getText().toString().equals(port) || !edt_route_name.getText().toString().equals(route_name) || !edt_route_pswd.getText().toString().equals(route_pswd))
				{
					new Thread(new Runnable()
					{
						@Override
						public void run() {
							int ret = userData.setWiFiInfo(username, edt_ip.getText().toString(), edt_port.getText().toString() , edt_route_name.getText().toString(), edt_route_pswd.getText().toString());
						}
					}).start();
				}

				if(edt_ip.getText().length()<=0 || edt_port.getText().length()<=0 ||
						edt_route_name.getText().length()<=0){
					Toast.makeText(ConfigWiFiActivity.this, R.string.save_succeed, Toast.LENGTH_SHORT).show();
					return;
				}
				setWifi(dev_sn, dev_mac);
				
			}
		
		});
	}
	
	//配置wifi
	private void setWifi(final String dev_sn, final String dev_mac) {
		if (dev_sn == null && dev_mac == null) {
			return;
		}
		if (pressed) {
			Toast.makeText(ConfigWiFiActivity.this, R.string.operationing,
					Toast.LENGTH_SHORT).show();
			return;
		}
		LibInterface.ManagerCallback configWifiCallback = new LibInterface.ManagerCallback() {
			@Override
			public void setResult(final int result, Bundle bundle) {
				if (dialog !=null&& dialog.isShowing()) {
					dialog.dismiss();
				}
				runOnUiThread(new Runnable() {
					public void run() {
						if (result == 0x00) {
							Toast.makeText(BaseApplication.getInstance(),
									R.string.config_wifi_succeed,
									Toast.LENGTH_SHORT).show();
							pressed = false;
						} else {
							ToastUtils.tips(result);
							pressed = false;
							LogUtils.d("result" + result);
						}
					}
				});
			}
		};
		pressed = true;
		AccessDevMetaData deviceData = new AccessDevMetaData(BaseApplication.getInstance());
		AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		LibDevModel deviceModel = getLibDev(device);

		Bundle bundle = getWiFiConfigBundle();
		if (bundle == null) {
			return;
		}

		dialog.show();
		int configWifiRet = LibDevModel.configWifi(BaseApplication.getInstance(),
				deviceModel, bundle, configWifiCallback);//注意上下文参数应使用BaseApplication.getInstance()，而不是ConfigWifiActivity.this，避免对象销毁造成的崩溃

		if (configWifiRet != 0) {
			if (dialog !=null&& dialog.isShowing()) {
				dialog.dismiss();
			}
			Toast.makeText(ConfigWiFiActivity.this, R.string.config_wifi_fail,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void getWiFiParameter()
	{
		UserData userData = new UserData(BaseApplication.getInstance());
		Map<String, String> wifiInfo = userData.getWifiInfo(username);
		if (wifiInfo != null
				&& wifiInfo.containsKey(UserData.UserBase.COLUMN_USER_SERVER_IP)) {
			if(wifiInfo.get(UserData.UserBase.COLUMN_USER_SERVER_IP).length() > 0)
			{
				ip = wifiInfo.get(UserData.UserBase.COLUMN_USER_SERVER_IP);
				edt_ip.setText(ip);
			}
			if(wifiInfo.get(UserData.UserBase.COLUMN_USER_SERVER_PORT).length() > 0)
			{
				port = wifiInfo.get(UserData.UserBase.COLUMN_USER_SERVER_PORT);
				edt_port.setText(port);
			}
			if(wifiInfo.get(UserData.UserBase.COLUMN_USER_WIFI_NAME).length() > 0)
			{
				route_name = wifiInfo.get(UserData.UserBase.COLUMN_USER_WIFI_NAME);
				edt_route_name.setText(route_name);
			}
			if(wifiInfo.get(UserData.UserBase.COLUMN_USER_WIFI_PSWD).length() > 0)
			{
				route_pswd = wifiInfo.get(UserData.UserBase.COLUMN_USER_WIFI_PSWD);
				edt_route_pswd.setText(route_pswd);
			}
		}
	}
	
	private Bundle getWiFiConfigBundle() {
		Bundle bundle = new Bundle();
		
		bundle.putString(ConstantsUtils.IP_ADDRESS, edt_ip.getText().toString());
		bundle.putInt(ConstantsUtils.PORT, Integer.parseInt(edt_port.getText().toString()));
		bundle.putString(ConstantsUtils.AP_NAME, edt_route_name.getText().toString());
		bundle.putString(ConstantsUtils.AP_PASSWORD, edt_route_pswd.getText().toString());
		
		/*UserData userData = new UserData(BaseApplication.getInstance());
		Map<String, String> wifiInfo = userData.getWifiInfo(BaseApplication
				.getInstance().getUserName());
		if (wifiInfo != null
				&& wifiInfo.containsKey(UserData.COLUMN_USER_SERVER_IP)) {
			if (wifiInfo.get(UserData.COLUMN_USER_SERVER_IP).length() >= 0
					&& wifiInfo.get(UserData.COLUMN_USER_SERVER_PORT).length() != 0
					&& wifiInfo.get(UserData.COLUMN_USER_WIFI_NAME).length() != 0
					&& wifiInfo.get(UserData.COLUMN_USER_WIFI_PSWD).length() != 0) {
				String server_ip = wifiInfo.get(UserData.COLUMN_USER_SERVER_IP);
				bundle.putString(ConstantsUtils.IP_ADDRESS, server_ip);
				int sever_port = Integer.parseInt(wifiInfo
						.get(UserData.COLUMN_USER_SERVER_PORT));
				bundle.putInt(ConstantsUtils.PORT, sever_port);
				String wifi_name = wifiInfo.get(UserData.COLUMN_USER_WIFI_NAME);
				bundle.putString(ConstantsUtils.AP_NAME, wifi_name);
				String wifi_pswd = wifiInfo.get(UserData.COLUMN_USER_WIFI_PSWD);
				bundle.putString(ConstantsUtils.AP_PASSWORD, wifi_pswd);
			} else {
				Toast.makeText(ConfigWifiActivity.this, R.string.no_wifi_param,
						Toast.LENGTH_SHORT).show();
				pressed = false;
				return null;
			}
		}*/
		return bundle;
	}

	public static LibDevModel getLibDev(AccessDevBean dev) {
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
