package com.doormaster.topkeeper.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.Act_AccessDevList;
import com.doormaster.topkeeper.activity.Act_DoorList;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.utils.BluetoothManager;
import com.doormaster.topkeeper.utils.CheckPermissionUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OpenModel;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface.ManagerCallback;
import com.intelligoo.sdk.ScanCallback;

import java.util.ArrayList;
import java.util.Arrays;

public class KeyDoorFragment extends BaseFragment implements OnClickListener{
	private static final String TAG= "KeyDoorFragment";
	private static final int REQUEST_CODE_TEST = 999;
	private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 122;
	private ImageView mAdvertiseImage;
	private ImageButton mBtOpen;
	private Button mLBtVideoList;
	private Button mRBtDoorList;
	private SoundPool soundPool;
	private Animation clickAnimation;
	private int soundSrcId;
	private static boolean pressed = false;
	private static final int SCAN_SECONDS = 1;
	private static ArrayList<AccessDevBean> accessDevData = null;

	private String username;
	/**
	 * 自定义的打开 Bluetooth 的请求码，与 onActivityResult 中返回的 requestCode 匹配。
	 */
	private static final int REQUEST_CODE_BLUETOOTH_ON = 1313;

	/**
	 * Bluetooth 设备可见时间，单位：秒。
	 */
	private static final int BLUETOOTH_DISCOVERABLE_DURATION = 250;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_door, container,false);
		mAdvertiseImage = (ImageView) view.findViewById(R.id.dm_iv_advertise);
		mBtOpen = (ImageButton) view.findViewById(R.id.dm_bt_open);
		clickAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.onceopenanim);//创建一个动画
		mLBtVideoList = (Button) view.findViewById(R.id.dm_open_left_button);
		mRBtDoorList = (Button) view.findViewById(R.id.dm_open_right_button);
		mBtOpen.setOnClickListener(this);
		mLBtVideoList.setOnClickListener(this);
		mRBtDoorList.setOnClickListener(this);
//		DBBiz mDbDao=DBBiz.getInstanter(getActivity(), Constants.DB_NAME, 1);
		AccessDevMetaData deviceData = new AccessDevMetaData(getActivity().getApplication());
		username = SPUtils.getString(Constants.USERNAME);
		accessDevData = deviceData.getAllAccessDevList(username);
		soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
		soundSrcId = soundPool.load(getActivity(), R.raw.open_door_sound, 1);
		requestPermissiontest();
		return view;
	}

	//open按键触发
	public void startScan()
	{
		if ((BluetoothManager.isBluetoothSupported()) && (!BluetoothManager.isBluetoothEnabled()))
		{
			this.turnOnBluetooth();
		}
		requestPermissiontest();
		if (accessDevData == null || accessDevData.size() == 0 )
		{
			ToastUtils.showMessage(getActivity(), R.string.without_authorize_device);
			return;
		}
		if (pressed)
		{
			ToastUtils.showMessage(getActivity(), R.string.operationing);
			return;
		}

		//如果设备列表中只有一个设备就不需要进行扫描了，直接蓝牙开门
		if (accessDevData.size() == 1)
		{
			AccessDevBean device = accessDevData.get(0);
			if (device != null)
			{
				open(device);
				return;
			}
		}

		ScanCallback callback = new ScanCallback()
		{

			@Override
			public void onScanResult(final ArrayList<String> deviceList,
					final ArrayList<Integer> rssiList)
			{
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						int maxRssi = -100;
						if (rssiList == null || deviceList == null || rssiList.size() == 0 || deviceList.size() == 0)
						{
							ToastUtils.showMessage(getActivity(), R.string.no_device_scaned);
							pressed = false;
							return;
						}
						LogUtils.d("sanDevList" + deviceList.toString() + "rssi:" +rssiList.toString());
						int pos = -1;
						for (int i = 0; i < rssiList.size(); i++)
						{
							if (rssiList.get(i) > maxRssi )
							{
								if (i < deviceList.size() && deviceList.get(i) != null
										&& queryDevice(deviceList.get(i)) != null)
								{
									maxRssi = rssiList.get(i);
									pos = i;
								}
							}
						}
						if (pos >-1 && pos < deviceList.size())
						{
							String devSn = deviceList.get(pos);
							AccessDevBean device = queryDevice(devSn);
							open(device);
						}
						else
						{
							ToastUtils.showMessage(getActivity(), R.string.no_device_scaned );
							pressed = false;
						}
					}
				});
			}

			@Override
			public void onScanResultAtOnce(final String devSn, int rssi) {
				LogUtils.d("111111111111111111111111" + devSn + "" + rssi);
			}
		};
		int ret = LibDevModel.scanDevice(getActivity(), false, SCAN_SECONDS , callback);
		if (ret == 0)
		{
			pressed = true;
			//获取音乐音量，用于设置开门铃声的音量
			AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
			int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC);
			int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC);
			soundPool.play(soundSrcId, 1.0f*current/max, 1.0f*current/max, 1, 0, 1);
			LogUtils.d("开始扫描了");
		}
		else
		{
			pressed = false;
			LogUtils.d("开始扫描了ret" + ret);
		}
	}

	//查询是否存在该设备
	public AccessDevBean queryDevice(String devSn)
	{
		if (devSn == null)
		{
			return null;
		}
//		DBBiz mDbDao=DBBiz.getInstanter(getActivity(), Constants.DB_NAME, 1);
		AccessDevMetaData deviceData = new AccessDevMetaData(getActivity().getApplication());
		return deviceData.queryAccessDeviceByDevSn(username,devSn);
	}

	//开门
	private void open(AccessDevBean device)
	{
		if(null == device )
		{
			ToastUtils.showMessage(getActivity(), R.string.no_device_scaned);
			pressed = false;
			return;
		}

		ManagerCallback callback = new ManagerCallback() {

			@Override
			public void setResult(final int result, Bundle arg1) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						pressed = false;
						if (result == 0)
						{
							ToastUtils.showMessage(getActivity(), R.string.open_succeed);
						}
						else
						{
							ToastUtils.showMessage(getActivity(), R.string.open_fail);
						}
					}
				});

			}
		};
		LibDevModel libModel = getLibDev(device);
		int ret = LibDevModel.controlDevice(getActivity(), 0x00, libModel, null, callback);
		if (ret == 0 )
		{
			LogUtils.d("开门操作中...");
		}
		else
		{
			pressed = false;
			ToastUtils.showMessage(getActivity(), R.string.open_fail);
		}

	}


	boolean isPressed = false;
	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = null;
		if (id == R.id.dm_bt_open) {//			startScan();
			if (!isPressed) {
				isPressed = true;
				TopkeeperModel.keyDoor(-120, 1000, OpenModel.NORMAL, new DMModelCallBack.DMCallback() {
					@Override
					public void setResult(int i, DMException e) {
						isPressed = false;
					}
				});
			} else {
				ToastUtils.showMessage(getContext(), R.string.operationing);
			}
			mBtOpen.startAnimation(clickAnimation);//执行动画

		} else if (id == R.id.dm_open_left_button) {
			intent = new Intent(getActivity(), Act_DoorList.class);
			startActivity(intent);

		} else if (id == R.id.dm_open_right_button) {
			intent = new Intent(getActivity(), Act_AccessDevList.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		} else {
		}

	}

	public static LibDevModel getLibDev(AccessDevBean device)
	{
		LibDevModel libDev = new LibDevModel();
		libDev.devSn = device.getDevSn();
		libDev.devMac = device.getDevMac();
		libDev.devType = device.getDevType();
		libDev.eKey = device.geteKey();
//		libDev.encrytion = device.getEncrytion();
		libDev.endDate = device.getEndDate();
		libDev.openType = device.getOpenType();
		libDev.privilege = device.getPrivilege();
		libDev.startDate = device.getStartDate();
		libDev.useCount = device.getUseCount();
		libDev.verified = device.getVerified();
		return libDev;
	}


	/*  校验蓝牙权限  */
	private void checkBluetoothPermission() {
		if (Build.VERSION.SDK_INT >= 23) {
			//校验是否已具有模糊定位权限
			if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
						MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
			} else {
				//具有权限
//				scanBluetooth();
			}
		} else {
			//系统不高于6.0直接执行
//			scanBluetooth();
		}
	}
	/**
	 * 请求权限
	 */
	public void requestPermissiontest() {
		// you needer permissions
		String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.RECORD_AUDIO,
				Manifest.permission.CAMERA};
		// check it is needed
		permissions = CheckPermissionUtils.getNeededPermission(getActivity(), permissions);
		// requestPermissions
		if (permissions.length > 0) {
			requestPermissions(permissions, REQUEST_CODE_TEST);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_TEST:
				LogUtils.d("grantResults=" + Arrays.toString(grantResults));
				if (grantResults.length > 0) {
					return;
				}
				if (!CheckPermissionUtils.isNeedAddPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
					ToastUtils.showMessage(getActivity(), "申请权限成功:" + Manifest.permission.ACCESS_COARSE_LOCATION);
				}
				if (!CheckPermissionUtils.isNeedAddPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
					ToastUtils.showMessage(getActivity(), "申请权限成功:" + Manifest.permission.ACCESS_FINE_LOCATION);
				}
				if (!CheckPermissionUtils.isNeedAddPermission(getActivity(), Manifest.permission.RECORD_AUDIO)) {
					ToastUtils.showMessage(getActivity(), "申请权限成功:" + Manifest.permission.RECORD_AUDIO);
				}
				if (!CheckPermissionUtils.isNeedAddPermission(getActivity(), Manifest.permission.CAMERA)) {
					ToastUtils.showMessage(getActivity(), "申请权限成功:" + Manifest.permission.CAMERA);
				}

				break;

		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	/**
	 * 弹出系统弹框提示用户打开 Bluetooth
	 */
	private void turnOnBluetooth()
	{
		// 请求打开 Bluetooth
		Intent requestBluetoothOn = new Intent(
				BluetoothAdapter.ACTION_REQUEST_ENABLE);
		// 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
		requestBluetoothOn
				.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		// 设置 Bluetooth 设备可见时间
		requestBluetoothOn.putExtra(
				BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
				BLUETOOTH_DISCOVERABLE_DURATION);
		// 请求开启 Bluetooth
		this.startActivityForResult(requestBluetoothOn,
				REQUEST_CODE_BLUETOOTH_ON);
	}

}
