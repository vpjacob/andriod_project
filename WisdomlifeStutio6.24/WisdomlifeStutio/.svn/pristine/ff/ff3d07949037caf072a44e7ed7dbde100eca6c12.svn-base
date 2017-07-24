package com.doormaster.topkeeper.activity.device_manager;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.UsersCardDom;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.UsersCardData;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface.ManagerCallback;

import java.util.regex.Pattern;

public class SetReadSectorKeyActivity extends BaseActivity implements OnClickListener {
	
	private ImageView backImage;
	private TextView device_id_tv;
	private TextView device_pwd_tv;
	private TextView device_sector_tv;
	private Button configBtn;
	private String device_sn;
	private String device_mac;
	private UsersCardData usersCardData;
	private AccessDevMetaData AccessDevMetaData;
	private AccessDevBean device;
	private boolean pressed = false;

	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_set_readsectorkey);
		initView();
		initEvent();
	}
	
	//界面初始化
	private void initView()
	{
		backImage = (ImageView)findViewById(R.id.set_read_sectorkey_img);
		device_id_tv = (TextView)findViewById(R.id.set_dev_id_tv);
		device_pwd_tv = (TextView)findViewById(R.id.set_read_sectorkey_tv);
		configBtn = (Button)findViewById(R.id.set_read_sectorkey_btn);
		device_sector_tv = (TextView)findViewById(R.id.set_sector_id_tv);

		username = SPUtils.getString(Constants.USERNAME);
		//获取设备编号、扇区编号、扇区密码
		usersCardData = new UsersCardData(SetReadSectorKeyActivity.this);
		device_sn = getIntent().getStringExtra(AccessDevBean.DEVICE_SN);
		device_mac = getIntent().getStringExtra(AccessDevBean.DEVICE_MAC);
		AccessDevMetaData = new AccessDevMetaData(SetReadSectorKeyActivity.this);
		device = AccessDevMetaData.queryAccessDeviceByDevSn(username, device_sn);
		if(device.getDbname_company() == null)
		{
			Toast.makeText(SetReadSectorKeyActivity.this, R.string.device_settring_param_none, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		UsersCardDom usersCardDom = usersCardData.getUsersCardDom(username, device.getDbname_company());
		
		if(usersCardDom == null && device.getDoor_no() < 0)
		{
			Toast.makeText(SetReadSectorKeyActivity.this, R.string.no_read_sector_info, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		//显示设备编号、扇区编号、扇区密码
		device_id_tv.setText(String.valueOf(device.getDoor_no()));
		device_sector_tv.setText(usersCardDom == null ? "" : usersCardDom.getSection() + "");
		device_pwd_tv.setText(usersCardDom == null ? "" : usersCardDom.getSection_key());
	}
	
	//事件初始化
	private void initEvent()
	{
		backImage.setOnClickListener(this);
		configBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int i = view.getId();
		if (i == R.id.set_read_sectorkey_img) {
			finish();

		} else if (i == R.id.set_read_sectorkey_btn) {
			setReadSectorKey(device_sn, device_mac);

		}
	}
	
	// 配置临时密钥
	private void setReadSectorKey(final String dev_sn, final String dev_mac) {
		
		if(pressed)
		{
			Toast.makeText(SetReadSectorKeyActivity.this, R.string.operationing, Toast.LENGTH_SHORT).show();
			return;
		}
		String device_id = device_id_tv.getText().toString();
		String device_pwd = device_pwd_tv.getText().toString();
		String device_sector_id = device_sector_tv.getText().toString();
		if (device_id.length() <= 0 || device_pwd.length() <= 0 || device_sector_id.length() <= 0)// 判断设备编号或密码是否为空
		{
			Toast.makeText(SetReadSectorKeyActivity.this, R.string.config_id_pwd_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		int dev_id = Integer.valueOf(device_id);
		int mifare_section = Integer.valueOf(device_sector_id);
		if(dev_id>255 || dev_id<0||!checkIsHex(device_pwd) || mifare_section < 0 || mifare_section > 15)//判断设备id是否为0~255，扇区密码是否为12位的16进制字符串, 扇区编号为0~15
		{
			Toast.makeText(BaseApplication.getInstance(), R.string.parameter_wrong,
					Toast.LENGTH_SHORT).show();
			return;
		}


		
		ManagerCallback configTmpKeyCallback = new ManagerCallback() {
			@Override
			public void setResult(final int result, Bundle bundle) {
				runOnUiThread(new Runnable() {
					public void run() {
						if (result == 0x00) {
							Toast.makeText(SetReadSectorKeyActivity.this,
									R.string.activity_config_tmpKey_success,
									Toast.LENGTH_SHORT).show();
						} else {
							ToastUtils.tips(result);
							LogUtils.d("result" + result);
						}
						pressed = false;
					}
				});
			}
		};
		AccessDevMetaData AccessDevMetaData = new AccessDevMetaData(BaseApplication.getInstance());
		AccessDevBean device = AccessDevMetaData.queryAccessDeviceByDevSn(username, dev_sn);
		if (device == null) {
			Toast.makeText(SetReadSectorKeyActivity.this, R.string.device_has_been_delete,
					Toast.LENGTH_SHORT).show();
			return;
		}
		pressed = true;
		LibDevModel deviceModel = getLibDev(device);
		int configTmpKytRet = LibDevModel.setReadSectorKey(BaseApplication.getInstance(), deviceModel,
				dev_id, mifare_section,device_pwd, configTmpKeyCallback); //配置临时密钥
		if (configTmpKytRet != 0) {
			Toast.makeText(BaseApplication.getInstance(), R.string.parameter_wrong,
					Toast.LENGTH_SHORT).show();
			pressed = false;
		}
	}
	
	//判断是否为12位的16进制的字符串
	private boolean checkIsHex(String numString)
	{
		boolean flag = false;
		String regString = "[a-f0-9A-F]{12}";
	    if (Pattern.matches(regString, numString)){
	        //匹配成功
	    	flag = true;
	    }
        return flag;
	}
	
	// 将AccessDevBean转化为LibDevModel
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
