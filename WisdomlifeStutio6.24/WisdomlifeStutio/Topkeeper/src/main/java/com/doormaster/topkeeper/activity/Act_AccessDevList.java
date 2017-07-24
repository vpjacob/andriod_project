package com.doormaster.topkeeper.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.adapter.AccessDeviceAdapter;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.receiver.TimerMsgReceiver;
import com.doormaster.topkeeper.utils.BluetoothManager;
import com.doormaster.topkeeper.utils.CheckPermissionUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.PopupUtil;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.Utils;
import com.doormaster.topkeeper.view.TitleBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Act_AccessDevList extends BaseActivity{
	private static final int REQUEST_CODE_TEST = 999;
	private static final int REQUEST_CODE_BLUETOOTH_ON = 1313;
	private static final int BLUETOOTH_DISCOVERABLE_DURATION = 250;

	private static final long STOP_SCAN_DELAY = 7000;
	private Activity mContext;
	private ArrayList<AccessDevBean> accessDevList = new ArrayList<AccessDevBean>();
	
	private ListView mListView = null;
	
	private static boolean pressed = false;
	
	private SoundPool soundPool = null;
	
	private int soundSrcId;
	private AccessDeviceAdapter deviceAdapter;

	private TitleBar title_bar;

	PopupWindow popup = null;

	android.bluetooth.BluetoothManager manager;
	BluetoothAdapter mAdapter;
	private static ArrayList<String> scanedDoor = new ArrayList<String>();
	AccessDevMetaData deviceData;
	private String username;
	private BroadcastReceiver devChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent)
		{

			String action = intent.getAction();
			if (action.equalsIgnoreCase(TimerMsgReceiver.READER_CHANGED_EXTRA)) {
				deviceAdapter = new AccessDeviceAdapter(mContext);
//				listView = (ListView) getView().findViewById(R.id.record_list);
				mListView.setAdapter(deviceAdapter);
				initBle();
			}
		}
	};
	private LeScanCallback callback = new LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			if (device == null )
			{
				return;
			}
//            MyLog.Assert(deviceData  != null);
			LogUtils.d("LeScanCallback", "device:"+device.toString());
//            String mac = device.getAddress();
			final String mac = Utils.exChange(device.getAddress());
			LogUtils.d("LeScanCallback","mac="+deviceData.isAccessDevDeviceExist(username, mac));
			if ( deviceData.isAccessDevDeviceExist(username, mac) ) {
				if (!scanedDoor.contains(mac)){
					scanedDoor.add(mac);
					//MyLog.debug("device scaned "+device.getAddress());
					final AccessDevBean deviceScan = deviceData.getDeviceByMac(username, mac);
					if (deviceScan != null ) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								deviceAdapter.sortList(deviceScan);
							}
						});

					}
				}
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_access_devlist);
		mContext = this;

		title_bar = (TitleBar) findViewById(R.id.title_bar);
		title_bar.setTitle(getString(R.string.guard_list));
		title_bar.setLeftImageResource(R.drawable.left_ac);
		title_bar.setRightImageResource(R.drawable.find_pressed);

		title_bar.setLeftLayoutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title_bar.setRightLayoutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popup = PopupUtil.getPopup(Act_AccessDevList.this);
				PopupUtil.setEnable(true);
				popup.showAsDropDown(view, 0, 0);
			}
		});
		title_bar.setBackgroundColor(Color.parseColor("#FFFFFF"));

		soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
		soundSrcId = soundPool.load(Act_AccessDevList.this, com.doormaster.topkeeper.R.raw.open_door_sound, 1);

		deviceData = new AccessDevMetaData(getApplication());
		accessDevList = deviceData.getAllAccessDevList(SPUtils.getString(Constants.USERNAME));
		if (accessDevList == null || accessDevList.size() == 0)
		{
			Toast.makeText(currentActivity, com.doormaster.topkeeper.R.string.no_permission,Toast.LENGTH_SHORT).show();
			return;
		}
		initView();
		username = SPUtils.getString(Constants.USERNAME);

		initBle();
	}

	private void initView() 
	{
		mListView = (ListView) findViewById(com.doormaster.topkeeper.R.id.dm_access_list);
//		DBBiz mDbDao = DBBiz.getInstanter(getApplication(), Constants.DB_NAME, 1);
		final HashMap<String,AccessDevBean> showList = new HashMap<String, AccessDevBean>();
		for (int i = 0; i < accessDevList.size(); i++)
		{
			AccessDevBean device = accessDevList.get(i);
			if (device != null && device.getDevName() != null && device.getDevName().length() > 0) 
			{
				showList.put(device.getDevName(), device);
			}
			else if (device.getDevSn() != null && device.getDevSn().length() > 0) 
			{
				showList.put(device.getDevSn(), device);
			}
		}
		final ArrayList<String> devNameList = new ArrayList<String>();
		Set<String> devSnSet = showList.keySet();
		Iterator<String> iterator = devSnSet.iterator(); 
		while(iterator.hasNext())
		{
			String devName = iterator.next();
			devNameList.add(devName);
		}

		deviceAdapter = new AccessDeviceAdapter(this);
		mListView.setAdapter(deviceAdapter);

	}

	//执行增加设备跳转或扫一扫开门
	public void click(View view)
	{
		popup.dismiss();
		int i = view.getId();
		if (i == R.id.add_dev) {
			Intent intent = new Intent(Act_AccessDevList.this, Act_AddList.class);
			LogUtils.d("coming addlistactivity");
			startActivityForResult(intent, Act_Main.MAIN_ACTIVITY_REQ_CODE);

		}
//		else if (i == R.id.scan_dev) {
//			Intent intent1 = new Intent(Act_AccessDevList.this, MipcaActivityCapture.class);
//			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivityForResult(intent1, Act_Main.SCANNIN_GREQUEST_CODE);
//
//		}
		else if (i == R.id.video_dev) {
			Intent intent2 = new Intent(Act_AccessDevList.this, Act_VideoList.class);
			startActivity(intent2);

		}

	}
	/**
	 * 请求权限
	 */
	public void requestPermissiontest() {

		if ((BluetoothManager.isBluetoothSupported()) && (!BluetoothManager.isBluetoothEnabled()))
		{
			this.turnOnBluetooth();
		}

		// you needer permissions
		String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
		// check it is needed
		permissions = CheckPermissionUtils.getNeededPermission(this, permissions);
		// requestPermissions
		if (permissions.length > 0) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(permissions, REQUEST_CODE_TEST);
			}
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
				if (!CheckPermissionUtils.isNeedAddPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
					// do something
					Toast.makeText(this, "申请权限成功:" + Manifest.permission.ACCESS_COARSE_LOCATION, Toast.LENGTH_LONG).show();
				}
				if (!CheckPermissionUtils.isNeedAddPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
					// do something
					Toast.makeText(this, "申请权限成功:" + Manifest.permission.ACCESS_FINE_LOCATION, Toast.LENGTH_LONG).show();
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


	private void initBle() {
		manager = (android.bluetooth.BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mAdapter = manager.getAdapter();
		if (mAdapter!=null && mAdapter.isEnabled() &&
				accessDevList !=null && !accessDevList.isEmpty()) {
			getScanList(true);
		}
	}

	public void getScanList(boolean enable) {
		if (mAdapter!=null && mAdapter.isEnabled()) {
			if (enable) {
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mAdapter.stopLeScan(callback);
						scanedDoor.clear();
					}
				}, STOP_SCAN_DELAY);
				scanedDoor.clear();
				mAdapter.startLeScan(callback);
			} else {
				mAdapter.stopLeScan(callback);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getScanList(true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getScanList(false);
	}


}
