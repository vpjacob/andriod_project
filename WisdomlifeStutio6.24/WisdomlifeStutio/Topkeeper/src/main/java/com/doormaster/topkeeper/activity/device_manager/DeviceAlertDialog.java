	package com.doormaster.topkeeper.activity.device_manager;

    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.bluetooth.BluetoothAdapter;
    import android.bluetooth.BluetoothManager;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.os.Bundle;
    import android.view.View;
    import android.view.View.OnClickListener;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.doormaster.topkeeper.R;
    import com.doormaster.topkeeper.bean.AccessDevBean;
    import com.doormaster.topkeeper.bean.SystemInfoBean;
    import com.doormaster.topkeeper.bean.UserBean;
    import com.doormaster.topkeeper.constant.Constants;
    import com.doormaster.topkeeper.db.AccessDevMetaData;
    import com.doormaster.topkeeper.db.SystemInfoData;
    import com.doormaster.topkeeper.db.UserData;
    import com.doormaster.topkeeper.utils.LogUtils;
    import com.doormaster.topkeeper.utils.RequestTool;
    import com.doormaster.topkeeper.utils.SPUtils;
    import com.intelligoo.sdk.LibDevModel;
    import com.intelligoo.sdk.LibInterface.ManagerCallback;

    import org.json.JSONException;

    import java.util.Map;


    public class DeviceAlertDialog extends Activity {

        private TextView mWarn;
        private Button mEnsure;
        private Button mCancel;
        protected ProgressDialog dialog;
        public static final int DEV_ALERT_RES_CODE = 0x03;
        private int REQUEST_ENABLE_BT = 1;
        public static final String INTENT_ID = "com.intelligoo.app.task.DeviceAlertDialog.INTENT_ID";
        public static final String INTENT_ID_DELET = "com.intelligoo.app.task.DeviceAlertDialog.INTENT_ID_DELET";
        public static final String INTENT_ID_RESET = "com.intelligoo.app.task.DeviceAlertDialog.INTENT_ID_RESET";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_device_alert);
            Intent intent = getIntent();
            final Bundle bundle = new Bundle();
            final AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
            final String username = SPUtils.getString(Constants.USERNAME);
            String dev_sn = intent.getStringExtra(AccessDevBean.DEVICE_SN);
            final String dev_mac = intent.getStringExtra(AccessDevBean.DEVICE_MAC);
            final AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
//            final String comm_key = device.getDevPassword();
            final String id_str = intent.getStringExtra(INTENT_ID);

            final LibDevModel libDev = getLibDev(device);
            UserData userData = new UserData(getApplicationContext());
            UserBean user = userData.getUser(username);

//            if (device.getDevResPhone() != null)
//            {
//                bundle.putString(AccessDevBean.DEV_FROM_PHONE, device.getDevResPhone());
    //			bundle.putString(UserBean.USER_IDENTITY, user.getIdentity());
//            }
//            else
//            {
    //			bundle.putString(DeviceDom.DEV_FROM_PHONE, username);
//                bundle.putString(AccessDevBean.DEV_FROM_PHONE, user.getIdentity());
//            }
//            bundle.putString(AccessDevBean.DEVICE_KEY, comm_key);
//            bundle.putInt(AccessDevBean.DEV_FROM,device.getDevRes());
            initView(id_str);

            final ManagerCallback delCallback = new ManagerCallback() {

                @Override
                public void setResult(final int result, Bundle arg1) {
                    // TODO Auto-generated method stub
                    runOnUiThread(new Runnable()
                    {
                        public void run() {
                            dialog.dismiss();
                            if (result == 0) {
    //							deviceData.deleteDevice(device.getUsername(), device);
    //							SystemInfoData infoData = new SystemInfoData(MyApplication.getInstance());
    //							SystemInfoDom info = infoData.getDeviceSystemInfo( device.getUsername(), device.getDevMac());
    //							if (info != null) {
    //								infoData.deleteDeviceSystemInfo(info);
    //							}
                                Toast.makeText(DeviceAlertDialog.this,
                                        R.string.clear_empty_success, Toast.LENGTH_SHORT).show();
                                //蓝牙通讯成功删除数据后再向服务器请求删除设备
                                Thread delete_th = new Thread(new Runnable() {
                                    public void run() {
                                        try {

                                            Map<String, String> eKeyMap = LibDevModel.getEkeyIdentityAndResource(device.geteKey());
                                            if (eKeyMap.containsKey("resIdentity") && eKeyMap.containsKey("keyResource") && Integer.parseInt(eKeyMap.get("keyResource")) == AccessDevBean.DEV_FROM_SCAN)//扫描添加
                                            {
                                                RequestTool.deleteDevice(device);
                                            }
                                        } catch (JSONException e) {
                                            LogUtils.d("JsonException e:"+e.toString());
                                        }
                                    }
                                });
                                delete_th.start();
                                DeviceAlertDialog.this.setResult(DEV_ALERT_RES_CODE);
                                DeviceAlertDialog.this.finish();
                            } else {
//                                Constants.tips(result);
                            }
                            DeviceAlertDialog.this.finish();

                        }
                    });
                }
            };

    //		final ManagerCallback delCallback = new ManagerCallback() {
    //
    //			@Override
    //			public void setResult(final int result, final Bundle bundle)
    //			{
    //
    //			}
    //		};

            final ManagerCallback resetCallback = new ManagerCallback() {

                @Override
                public void setResult(final int result, Bundle bundle) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            if (result == 0) {
                                SystemInfoData infoData = new SystemInfoData(getApplicationContext());
                                SystemInfoBean info = new SystemInfoBean();
                                info.setUsername(username);
                                info.setDevMac(dev_mac);
                                infoData.saveSystemINfo(info);
                                Toast.makeText(DeviceAlertDialog.this,
                                        R.string.device_initialize_success, Toast.LENGTH_SHORT).show();
                            } else {
//                                Constants.tips(result);
                            }
                            DeviceAlertDialog.this.finish();
                        }
                    });

                }
            };

            mEnsure.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v)
                {
                    dialog = new ProgressDialog(DeviceAlertDialog.this);
                    if (id_str.equals(INTENT_ID_DELET)) { //清空卡、用户信息
                        if (!checkBle()) //蓝牙未打开
                        {
                            return;
                        }
                        dialog.setMessage(getResources().getString(R.string.clearring));
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.show();

                        int ret = LibDevModel.deleteDeviceData(DeviceAlertDialog.this, libDev, delCallback);
                        if(ret != 0)
                        {
//                            Constants.tips(ret);
                        }

    //	                if ( ManagerDev.registBleReceiver(DeviceAlertDialog.this,
    //							ManagerDev.MANAGER_DELETE_ALL_INFO, mLeService,
    //							bundle, delCallback)){
    //						mLeService.connect(dev_mac);
    //					}


                    } else if (id_str.equals(INTENT_ID_RESET)) {
    //					Map<String, String> eKeyMap = LibDevModel.getEkeyIdentityAndResource(device.getEkey());
    //					if (eKeyMap != null && eKeyMap.containsKey("keyResource"))
    //					{
    //						if (Constants.KEY_RESOURCE_SCAN.equals(eKeyMap.get("resIdentity")))//判断是否为扫描添加
    //						{
    //
    //						}
    //					}

                        int ret = LibDevModel.resetDeviceConfig(DeviceAlertDialog.this, libDev, resetCallback);
                        if (ret == 0)
                        {
                            dialog.setMessage(getResources().getString(R.string.device_initializing));
                            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            dialog.show();
                        }else
                        {
//                            Constants.tips(ret);
                        }

                    }

                }
            });

            mCancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    DeviceAlertDialog.this.finish();
                }
            });

        }

        private void initView(String id_str) {
            mWarn = (TextView) findViewById(R.id.tv_alert_activity_remind);
            mEnsure = (Button) findViewById(R.id.bt_alert_activity_ensure);
            mCancel = (Button) findViewById(R.id.bt_alert_activity_cancel);
            if (id_str.equals(INTENT_ID_DELET)) {
                mWarn.setText(R.string.activity_device_delete_all_info_ensure_alert);
                mEnsure.setText(R.string.activity_device_delete_all_info_enusre);
            } else if (id_str.equals(INTENT_ID_RESET)) {
                mWarn.setText(R.string.activity_device_reset_ensure_alert);
                mEnsure.setText(R.string.ensure);
            }
        }


        //检查蓝牙开启情况
        private boolean checkBle()
        {
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            {
                Toast.makeText(getApplicationContext(), R.string.ble_not_supported,
                        Toast.LENGTH_SHORT).show();
                return false;
            }

            BluetoothManager manager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
            final BluetoothAdapter adapter = manager.getAdapter();

            if (adapter == null || !adapter.isEnabled())
            {
                Toast.makeText(getApplicationContext(), R.string.ble_disable,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
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
