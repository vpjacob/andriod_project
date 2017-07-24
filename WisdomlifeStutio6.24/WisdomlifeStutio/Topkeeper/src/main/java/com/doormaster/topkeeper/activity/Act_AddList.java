package com.doormaster.topkeeper.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.adapter.ScanListRVAdapter;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.UserBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.UserData;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.doormaster.topkeeper.view.DividerItemDecoration;
import com.intelligoo.sdk.BleManager;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.ScanCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Act_AddList extends BaseActivity {

	private static final String TAG = "Act_AddList";
	private BluetoothAdapter mAdapter;
	private Button mButton;
	private ImageButton mBack;
	private Handler mHandler;
	private boolean mScanning = false;
//	private ScanListAdapter mScanAdapter;
	private ScanListRVAdapter adapter;
	private TextView mTvScan = null;
//	private ListView scanListView;
	private RecyclerView rv_scan_list;
	private String username;
	private static final int BLE_OPEN_REQUEST = 1;
	private static final int SCAN_STOP_DELAY = 8000;
	private static AccessDevMetaData deviceData = null;
	private Map<String, Integer> deviceMap;
//	private LeScanCallback mLeCallback = new LeScanCallback() {
//		@Override
//		public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
//			runOnUiThread(new Runnable() {
//				public void run() {
//					if ((scanRecord == null) || (scanRecord.length <= 6)
//							|| (device == null)) {
//						return;
//					}
//					if (scanRecord[5] == (byte) 0xb8 && scanRecord[6] == (byte) 0x5c) {
//						if (!deviceData.isAccessDevDeviceExist(username, device.getAddress())) {
//							mTvScan.setVisibility(View.GONE);
////							mScanAdapter.addDevice(device);
//							AccessDevBean devBean = deviceData.getDeviceByMac(username, device.getAddress());
//							deviceMap.put(devBean, rssi);
//						}
//					}
//				}
//			});
//		}
//	};
//	protected BluetoothLeService mLeService;
//	private ServiceConnection conn = new ServiceConnection() {
//
//		@Override
//		public void onServiceConnected(ComponentName className, IBinder service) {
//			mLeService = ((BluetoothLeService.LocalBinder) service).getService(Act_AddList.this);
//			LogUtils.d("mLeService: " + mLeService == null ? "null" : "");
//			if (!mLeService.initialize()) {
//				Log.e("mLeService initial", "Unable to initialize Bluetooth");
//				mLeService = null;
//			}
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//
//			mLeService = null;
//		}
//	};

	private ScanCallback scanCallback = new ScanCallback() {
		@Override
		public void onScanResult(ArrayList<String> arrayList, ArrayList<Integer> arrayList1) {
			for (int i =0;i<arrayList.size();i++) {
//				LogUtils.d(TAG, arrayList.get(i) + "------" + arrayList1.get(i));
//				if ()
				deviceMap.put(arrayList.get(i), arrayList1.get(i));
			}
			List<Map.Entry<String, Integer>> list = new ArrayList<>(deviceMap.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				@Override
				public int compare(Map.Entry<String, Integer> t1, Map.Entry<String, Integer> t2) {
					return t2.getValue().compareTo(t1.getValue());
				}
			});
			mTvScan.setVisibility(View.GONE);

			adapter = new ScanListRVAdapter(Act_AddList.this,list);
			adapter.setOnItemClickListener(new ScanListRVAdapter.OnRecyclerViewItemClickListener() {
				@Override
				public void onItemClick(View view, String sn) {
					ToastUtils.showMessage(Act_AddList.this, "暂不能添加");
				}
			});
			rv_scan_list.setAdapter(adapter);
			mScanning = false;
		}

		@Override
		public void onScanResultAtOnce(String s, int i) {

		}
	};

	BleManager bleManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inteligoo_activity_scan);
		username = SPUtils.getString(Constants.USERNAME);
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		deviceMap = new TreeMap<>();
		initList();

		bleManager = new BleManager(this);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (mScanning == false) {
//					scanLeDevice(true);
//				}
				if (!mScanning) {
					scanDevice();
				} else {
					ToastUtils.showMessage(Act_AddList.this, R.string.activity_scanning);
				}

			}
		});
//
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (mScanning == true) {
//					scanLeDevice(false);
//				}
				finish();
			}
		});
	}

	//初始化列表项
	private void initList() {
//		final BluetoothManager bluetoothManager =
//				(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//		mAdapter = bluetoothManager.getAdapter();
		mButton = (Button) findViewById(R.id.activity_scan_cancel);
//		scanListView = (ListView) findViewById(R.id.activity_scan_list);
		rv_scan_list = (RecyclerView) findViewById(R.id.rv_scan_list);
		mTvScan = (TextView) findViewById(R.id.activity_scan_tv_mark);
		mBack = (ImageButton) findViewById(R.id.ib_scan_back);

//		mScanAdapter = new ScanListAdapter(Act_AddList.this);
		mHandler = new Handler();
//		scanListView.setAdapter(mScanAdapter);

		//recyclerView属性
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		rv_scan_list.setLayoutManager(linearLayoutManager);
		//自定义分割线
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
				DividerItemDecoration.VERTICAL_LIST);
		dividerItemDecoration.setDivider(R.drawable.divider);
		rv_scan_list.addItemDecoration(dividerItemDecoration);

		deviceData = new AccessDevMetaData(BaseApplication.getInstance());
		scanDevice();
//		scanLeDevice(true);
	}


	//初始化服务
	private void initSevice() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(Act_AddList.this, BluetoothLeService.class);
//		bindService(intent, conn, BIND_AUTO_CREATE);
	}

	private void scanDevice() {
		mScanning = true;
		LibDevModel.scanDevice(this, false, 3000, scanCallback);
	}
	//蓝牙扫描开关
//	private void scanLeDevice(boolean enable) {
//		if (mAdapter == null || !mAdapter.isEnabled()) {
//			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			startActivityForResult(intent, BLE_OPEN_REQUEST);
//			return;
//		}
//
//		if (enable) {
//			mHandler.postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					mScanning = false;
//					mAdapter.stopLeScan(mLeCallback);
//					mButton.setText(R.string.activity_scanning_again);
//				}
//			}, SCAN_STOP_DELAY);
//			mAdapter.startLeScan(mLeCallback);
//			mScanning = true;
//			mButton.setText(R.string.activity_scanning_stop);
//		} else {
//			mAdapter.stopLeScan(mLeCallback);
//			mScanning = false;
//			mButton.setText(R.string.activity_scanning_stop);
//		}
//
//	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
//		initSevice();
		super.onStart();
	}

	@Override
	protected void onStop() {
//		scanLeDevice(false);
//		if (mLeService != null) {
//			mLeService.close();
//		}
//		if (conn != null) {
//			unbindService(conn);
//		}
		super.onStop();
	}


	//适配器类
	private class ScanListAdapter extends BaseAdapter {

//		private ArrayList<BluetoothDevice> scanList = new ArrayList<>();
		private List<Map.Entry<String, Integer>> list = new ArrayList<>();
		private LayoutInflater inflater;

		public ScanListAdapter(Activity activity) {
			inflater = LayoutInflater.from(activity);
		}

//		private void addDevice(BluetoothDevice device) {
//			if (scanList != null && !scanList.contains(device)) {
//				scanList.add(device);
//				notifyDataSetChanged();
//			}
//		}

		private void addDevices(List<Map.Entry<String, Integer>> list) {
//			if (scanList != null && !scanList.contains(device)) {
//				scanList.add(device);
//				notifyDataSetChanged();
//			}
			this.list = list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int position) {

			return list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_activity_scanlist, null);
				viewHolder = new ViewHolder();
				viewHolder.devName = (TextView) convertView.findViewById(R.id.tv_iten_name);
				viewHolder.devRssi = (TextView) convertView.findViewById(R.id.tv_iten_rssi);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
//			final BluetoothDevice device = scanList.get(position);
//			if ((device.getName() != null) && (device.getName().length() > 0)) {
//				viewHolder.devName.setText(device.getName());
//			} else {
//				viewHolder.devName.setText("unknow device");
//			}
			viewHolder.devName.setText(list.get(position).getKey());
			viewHolder.devRssi.setText(list.get(position).getValue());
			// 点击添加设备回调函数
//			final ManagerCallback addCallback = new ManagerCallback() {
//				@Override
//				public void setResult(final int result, final Bundle bundle) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							if (result != 0x00) {
//								ToastUtils.tips(result);
//								return;
//							}
//							final AccessDevBean devDom = getAccessDevBean(device, bundle);
//							Thread backup_th = new Thread(new Runnable() {
//								public void run() {
//									final int ret = RequestTool.backupDevEkey(devDom);
//									runOnUiThread(new Runnable() {
//										public void run() {
//											if (ret == 0) {
////													saveScanDevice(devDom);
//												// ret 为失败值时，对应界面上给以提示
//												Toast.makeText(Act_AddList.this, R.string.activity_add_dev_sucess,
//														Toast.LENGTH_SHORT).show();
//											}
//											Act_AddList.this.setResult(Act_Main.MAIN_ACTIVITY_REQ_CODE);
//											Act_AddList.this.finish();
//											//更新界面
//											Intent intent = new Intent(TimerMsgReceiver.READER_CHANGED_EXTRA);
//											BaseApplication.getInstance().sendBroadcast(intent);
//										}
//									});
//								}
//							});
//							backup_th.start();
//						}
//					});
//				}
//			};
			viewHolder.devName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtils.showMessage(Act_AddList.this, "Not");
					/**
					 Bundle bundle = new Bundle();

					 UserData userData = new UserData(BaseApplication.getInstance());
					 UserBean user  = userData.getUser(username);
					 bundle.putString(UserBean.USER_IDENTITY,user.getIdentity());
					 if (ManagerDev.registBleReceiver(Act_AddList.this, ManagerDev.MANAGER_ADD_DEV,
					 mLeService, bundle, addCallback)) {

					 mLeService.connect(device.getAddress(),Act_AddList.this);
					 } else {
					 Toast.makeText(Act_AddList.this, R.string.device_is_for_other_operation,
					 Toast.LENGTH_SHORT).show();
					 }*/
				}
			});

			return convertView;
		}

		protected String getDevSn(String name) {
			int pos = name.indexOf("-");
			String dev_sn = name.substring(pos + 1);
			return dev_sn;
		}

		private void saveDevice(final BluetoothDevice device,
								final Bundle bundle) {

			AccessDevBean deviceSave = new AccessDevBean();
			AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext()
			);
			deviceSave.setUsername(username);
			deviceSave.setDevType(bundle.getInt(AccessDevBean.DEVICE_TYPE));
//			deviceSave.setDevRes(AccessDevBean.DEV_FROM_SCAN);
//			deviceSave.setDevResPhone(BaseApplication.getInstance().getUserName());
			deviceSave.setPrivilege(bundle.getInt(AccessDevBean.PRIVILEGE));
			deviceSave.setDevSn(getDevSn(device.getName()));
//			deviceSave.setReaderSn(getDevSn(device.getName()));
			deviceSave.setDevMac(device.getAddress());
			deviceSave.seteKey(bundle.getString(AccessDevBean.DEVICE_KEY));
//			deviceSave.setSupportFun(bundle.getInt(AccessDevBean.DEV_SUPPORT_FUN));
			int manager_pwd = bundle.getInt(AccessDevBean.DEVICE_MANAGER_PWD);
			deviceSave.setDevManagerPassword(Integer.toString(manager_pwd));
			deviceData.saveAccessDev(deviceSave);
		}

		private void saveScanDevice(final AccessDevBean deviceSave) {
			AccessDevMetaData deviceData = new AccessDevMetaData(BaseApplication.getInstance());
			deviceData.saveAccessDev(deviceSave);
		}

		private AccessDevBean getAccessDevBean(final BluetoothDevice device, final Bundle bundle) {
			// 以下实例化对象代码需要抽离 --By Jason20160513
			AccessDevBean devDom = new AccessDevBean();
			devDom.setUsername(username);
			devDom.setDevType(bundle.getInt(AccessDevBean.DEVICE_TYPE));
//			devDom.setDevRes(AccessDevBean.DEV_FROM_SCAN);

			UserData userData = new UserData(BaseApplication.getInstance());
			UserBean user = userData.getUser(username);
//			devDom.setDevResPhone(user.getIdentity());

			devDom.setPrivilege(bundle.getInt(AccessDevBean.PRIVILEGE));
			devDom.setDevSn(getDevSn(device.getName()));
//			devDom.setReaderSn(getDevSn(device.getName()));
			devDom.setDevMac(device.getAddress());
			devDom.seteKey(bundle.getString(AccessDevBean.DEVICE_KEY));
//			devDom.setSupportFun(bundle.getInt(AccessDevBean.DEV_SUPPORT_FUN));
			int manager_pwd = bundle.getInt(AccessDevBean.DEVICE_MANAGER_PWD);
			devDom.setDevManagerPassword(Integer.toString(manager_pwd));
			return devDom;
		}

	}

	class ViewHolder {
		TextView devName;
		TextView devRssi;
	}
}
