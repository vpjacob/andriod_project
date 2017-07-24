package com.doormaster.topkeeper.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.DevKeyBean;
import com.doormaster.topkeeper.bean.RoomBean;
import com.doormaster.topkeeper.bean.UsersCardDom;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.DBBiz;
import com.doormaster.topkeeper.db.DevKeyMetaData;
import com.doormaster.topkeeper.db.UserData;
import com.doormaster.topkeeper.db.UsersCardData;
import com.doormaster.topkeeper.service.AutoOpenService;
import com.doormaster.topkeeper.service.ShakeOpenService;
import com.doormaster.topkeeper.task.ManagerDev;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallback;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.intelligoo.sdk.ScanCallBackSort;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.doormaster.topkeeper.constant.Constants.AUTO_DEFAUT_PROGRESS;
import static com.doormaster.topkeeper.constant.Constants.SHAKE_DEFAUT_PROGRESS;


/**
 * 管理类
 * Created by Liukebing on 2017/2/24.
 */

class TopkeeperManager {
    private static final String TAG = "TopkeeperManager";

    private static TopkeeperManager instance = new TopkeeperManager();

    private Context appContext;
    private Activity mainActivity;
    private static boolean pressed = false;

    /**
     * 自定义的打开 Bluetooth 的请求码，与 onActivityResult 中返回的 requestCode 匹配。
     */
    private static final int REQUEST_CODE_BLUETOOTH_ON = 1313;

    /**
     * Bluetooth 设备可见时间，单位：秒。
     */
    private static final int BLUETOOTH_DISCOVERABLE_DURATION = 250;
    private static final int REQUEST_CODE_TEST = 999;
    private static ArrayList<AccessDevBean> allDevList = null;
    private static ArrayList<AccessDevBean> curDevList = null;
    private static ArrayList<AccessDevBean> autoOpenList = null;
    private static ArrayList<AccessDevBean> shakeOpenList = null;
    private SoundPool soundPool;
    private int soundSrcId;
    private DMCallback scanCallback;
    private String username;

//    private DMGetDeviceCountCallback.DMGetDeviceCallback deviceCountCallback;
//    private BleManager bleManager;

    private Map<String, List<Integer>> rssiMap;
        private DBBiz mDbDao;
    private AccessDevMetaData deviceData;
    private UserData userData;

    //摇一摇开门参数
    private Intent shakeService;
    private boolean isShake = false;//是否开启摇一摇开门
    private int shakeProgress = SHAKE_DEFAUT_PROGRESS;//摇一摇开门距离

    //靠近开门参数
    private Intent autoService;
    private boolean auto_open =false;//是否开启靠近开门
    private int autoProgress = AUTO_DEFAUT_PROGRESS;//靠近开门距离

    private boolean isOpenSpaceEnable;//是否开启自动开门距离限制参数
    private int autoOpenSeconds = Constants.AUTO_OPEN_SPACE_SECONDS_DEFAUT;//靠近开门距离参数
    private Map<String, Long> autoOpenTimeMap = new HashMap<>();

    private TopkeeperManager() {

    }

    public static TopkeeperManager getInstance() {
        return instance;
    }

    void init(Context ctx) {
        appContext = ctx.getApplicationContext();
        mainActivity = (Activity) ctx;
        SPUtils.setContext(ctx.getApplicationContext());
        username = SPUtils.getString(Constants.USERNAME,ctx);
        mDbDao = DBBiz.getInstanter(appContext);
        deviceData = new AccessDevMetaData(appContext);
        userData = new UserData(appContext);

        allDevList = deviceData.getAllAccessDevList(username);
        shakeOpenList = deviceData.getShakeOpenList(username);
        autoOpenList = deviceData.getAutoOpenList(username);
        soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
        soundSrcId = soundPool.load(appContext, com.doormaster.topkeeper.R.raw.open_door_sound, 1);

//        bleManager = new BleManager(context.getApplicationContext());
//        bleManager.enableBluetooth();

        rssiMap = new HashMap<>();
    }


    /**
     * 选晒开门设备
     *
     * @param model
     */
    private void selectDevice(OpenModel model) {
        switch (model) {
            case NORMAL:
                curDevList = allDevList;
                break;
            case SHAKEOPEN:
                if (shakeOpenList != null && shakeOpenList.size() != 0) {
                    curDevList = shakeOpenList;
                }
                break;
            case AUTOOPEN:
                if (autoOpenList != null && autoOpenList.size() != 0) {
                    curDevList = autoOpenList;
                }
                break;
            default:
                break;

        }
    }
    /**
     * refesh ShakeOpen list
     */
    void refeshAllDevList() {
        if (deviceData == null) {
            deviceData = new AccessDevMetaData(appContext);
        }
        allDevList = deviceData.getAllAccessDevList(username);
    }
    /**
     * refesh ShakeOpen list
     */
    int refeshShakeList() {
        if (deviceData == null) {
            deviceData = new AccessDevMetaData(appContext);
        }
        shakeOpenList = deviceData.getShakeOpenList(username);
//        isShake = SPUtils.getBoolean(Constants.IS_SHAKE, appContext);
        isShake = userData.getShakeOpen(username);
//        shakeProgress = SPUtils.getInt(Constants.DISTANCE_SHAKE, SHAKE_MAX_PROGRESS,appContext);
        shakeProgress = userData.getShakeDistance(username);
        if (isShake ) {
            if (shakeOpenList == null || shakeOpenList.size() == 0) {
                return 1;
            }
            if (!Utils.isServiceRunning(appContext, "ShakeOpenService")) {

                if (shakeService == null) {
                    shakeService = new Intent(appContext, ShakeOpenService.class);
                }
                shakeService.putExtra("distance", shakeProgress);
                appContext.startService(shakeService);
            }
        } else {
            if (shakeService == null) {
                shakeService = new Intent(appContext, ShakeOpenService.class);
            }
            appContext.stopService(shakeService);
        }
        return 0;
    }
    /**
     * refest AutoOpen list
     */
    int refeshAutoList() {
        if (deviceData == null) {
            deviceData = new AccessDevMetaData(appContext);
        }
        autoOpenList = deviceData.getAutoOpenList(username);
        isOpenSpaceEnable = userData.isOpenSpaceEnable(username);
        autoOpenSeconds = userData.getAutoOpenSpaceTime(username);
        auto_open = userData.getAutoOpen(username);
        autoProgress = userData.getAutoDistance(username);
        if (auto_open) {
            if (autoOpenList == null || autoOpenList.size() == 0) {
                return 1;
            }
            if (!Utils.isServiceRunning(appContext, "AutoOpenService")) {
                if (autoService == null) {
                    autoService = new Intent(appContext, AutoOpenService.class);
                }
                autoService.putExtra("distance", autoProgress);
                appContext.startService(autoService);
            }
        } else {
            if (autoService == null) {
                autoService = new Intent(appContext, AutoOpenService.class);
            }
            appContext.stopService(autoService);
        }
        return 0;
    }

    void stopAutoScan() {
        if (auto_open) {
            appContext.stopService(autoService);
        }
    }
     void stopShakeOpen() {
        if (isShake) {
            appContext.stopService(shakeService);
        }
    }

    void openSpaceTime(boolean isOpenSpaceEnable,int autoOpenSeconds) {
        this.isOpenSpaceEnable = isOpenSpaceEnable;
        this.autoOpenSeconds = autoOpenSeconds;
//        refeshAutoList();
    }

    /**
     * 开始扫描设备
     */
    void startScan(final int rssi, int scanSeconds, final OpenModel model, final DMModelCallBack.DMCallback scanCallback) {
        this.scanCallback = scanCallback;
        selectDevice(model);
        if ((BluetoothManager.isBluetoothSupported()) && (!BluetoothManager.isBluetoothEnabled())) {
            turnOnBluetooth();
            scanCallback.setResult(3, new DMException(R.string.bluetooth_is_not_open));
            return;
        }
        requestPermissiontest();
        if (curDevList == null || curDevList.size() == 0) {
            if (model != OpenModel.AUTOOPEN) {
                Toast.makeText(appContext, com.doormaster.topkeeper.R.string.without_authorize_device, Toast.LENGTH_SHORT).show();
            }
            scanCallback.setResult(4, new DMException(R.string.without_authorize_device));
            return;
        }
        if (pressed) {
            if (model != OpenModel.AUTOOPEN) {
                Toast.makeText(appContext, com.doormaster.topkeeper.R.string.operationing, Toast.LENGTH_SHORT).show();
            }
            scanCallback.setResult(3, new DMException(R.string.operationing));
            return;
        }

        //如果设备列表中只有一个设备就不需要进行扫描了，直接蓝牙开门
        if (model == OpenModel.NORMAL) {
            if (curDevList.size() == 1) {
                AccessDevBean device = curDevList.get(0);
                if (device != null) {
                    open(model,device);
                    return;
                }
            }
        }

        ScanCallBackSort callBackSort = new ScanCallBackSort() {
            @Override
            public void onScanResult(ArrayList<Map<String, Integer>> deviceMap) {
                int minRssi = rssi;
                if (deviceMap == null || deviceMap.size() == 0) {
                    if (model == OpenModel.NORMAL) {
                        Toast.makeText(appContext, R.string.no_device_scaned, Toast.LENGTH_SHORT).show();
                    }
                    pressed = false;
                    scanCallback.setResult(0, null);
                    return;
                }
                LogUtils.d("deviceMap" + deviceMap.toString());
                switch (model) {
                    case NORMAL:
                        minRssi = rssi;
                        break;
                    case AUTOOPEN:
                        minRssi = -50 - autoProgress;
                        break;
                    case SHAKEOPEN:
                        minRssi = -50 - shakeProgress;
                        break;
                }
                w1:
                for (Map<String, Integer> map : deviceMap) {
                    for (String sn : map.keySet()) {
                        if (map.get(sn) < minRssi) {
                            break w1;
                        }
                        for (AccessDevBean device : curDevList) {
                            String devSn = device.getDevSn();
                            if (model == OpenModel.AUTOOPEN && isOpenSpaceEnable &&
                                    autoOpenTimeMap.containsKey(devSn) &&
                                    System.currentTimeMillis() - autoOpenTimeMap.get(devSn) < autoOpenSeconds * 1000) {
                                continue;
                            }
                            if (devSn.equals(sn)) {
                                open(model,device);
                                return;
                            }
                        }
                    }
                }
//                for (AccessDevBean devBean : curDevList) {
//                    String devSn = devBean.getDevSn();
//                    if (model == OpenModel.AUTOOPEN && isOpenSpaceEnable &&
//                            autoOpenTimeMap.containsKey(devSn) &&
//                            System.currentTimeMillis() - autoOpenTimeMap.get(devSn) < autoOpenSeconds * 1000) {
//                        continue;
//                    }
//                    for (Map<String, Integer> map : deviceMap) {
//                        if (map.containsKey(devSn) && map.get(devSn) >= minRssi) {
//
//                            open(model,devBean);
//                            return;
//                        }
//                    }
//                }
                if (model != OpenModel.AUTOOPEN) {
                    Toast.makeText(appContext, R.string.distance_is_too_far_away, Toast.LENGTH_SHORT).show();
                }
                pressed = false;
                scanCallback.setResult(1, new DMException(R.string.distance_is_too_far_away));
            }

            @Override
            public void onScanResultAtOnce(String s, int i) {

            }
        };

        int ret = LibDevModel.scanDeviceSort(appContext, false, scanSeconds, callBackSort);
        if (ret == 0) {
//        if (ret) {
            pressed = true;
            LogUtils.d("开始扫描了");
        } else {
            pressed = false;
            LogUtils.d("开始扫描了ret" + ret);
            scanCallback.setResult(2, new DMException(R.string.no_device_scaned));
        }
    }

    /**
     * OpenDoor voice
     */
    private void palyOpenDoorVoice() {
        //获取音乐音量，用于设置开门铃声的音量
        AudioManager mAudioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        soundPool.play(soundSrcId, 1.0f * current / max, 1.0f * current / max, 1, 0, 1);
    }

    /**
     * Open the specified device
     *
     * @param device
     * @param callback
     */
    void openDevice(AccessDevBean device, DMModelCallBack.DMCallback callback) {
        scanCallback = callback;
        if ((BluetoothManager.isBluetoothSupported()) && (!BluetoothManager.isBluetoothEnabled())) {
            turnOnBluetooth();
        }
        requestPermissiontest();
        open(OpenModel.NORMAL,device);
    }

    /**
     * 查询是否存在该设备
     *
     * @param devSn
     * @return
     */
    public AccessDevBean queryDevice(String devSn) {
        if (devSn == null) {
            return null;
        }
        AccessDevMetaData deviceData = new AccessDevMetaData(appContext);
        return deviceData.queryAccessDeviceByDevSn(username, devSn);
    }

    /**
     * 开门
     *
     * @param device
     */
    private void open(final OpenModel openModel, final AccessDevBean device) {
//        if ((BluetoothManager.isBluetoothSupported()) && (!BluetoothManager.isBluetoothEnabled())) {
//            turnOnBluetooth();
//        }
//        requestPermissiontest();

        ManagerDev.setRecordInfo(device);

        LibInterface.ManagerCallback callback = new LibInterface.ManagerCallback() {

            @Override
            public void setResult(final int result, Bundle arg1) {
                boolean hasLooper = false;
                if (Looper.myLooper() == null)
                {
                    hasLooper = true;
                    Looper.prepare();
                }
                pressed = false;
                if (result == 0) {
                    Toast.makeText(appContext, com.doormaster.topkeeper.R.string.open_succeed, Toast.LENGTH_SHORT).show();
                    ManagerDev.saveRecord(true);
                    scanCallback.setResult(0, null);
                    if (openModel == OpenModel.AUTOOPEN) {
                        autoOpenTimeMap.put(device.getDevSn(), System.currentTimeMillis());

                    }
                } else {
                    Toast.makeText(appContext, com.doormaster.topkeeper.R.string.open_fail, Toast.LENGTH_SHORT).show();
                    scanCallback.setResult(0, null);
                    ManagerDev.saveRecord(false);
                }
                if (hasLooper == true)
                {
                    Looper.loop();
                }

            }

        };
        LibDevModel libModel = getLibDev(device);

        int ret = LibDevModel.controlDevice(appContext, 0x00, libModel, null, callback);
        if (ret == 0) {
            LogUtils.d("开门操作中...");
            palyOpenDoorVoice();
        } else {
            pressed = false;
            ToastUtils.showMessage(appContext,R.string.open_fail);
            scanCallback.setResult(0, null);
        }
    }

    /**
     * 弹出系统弹框提示用户打开 Bluetooth
     */
    private void turnOnBluetooth() {
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
        mainActivity.startActivityForResult(requestBluetoothOn,
                REQUEST_CODE_BLUETOOTH_ON);
    }

    public LibDevModel getLibDev(AccessDevBean device) {
        LibDevModel libDev = new LibDevModel();
        libDev.devSn = device.getDevSn();
        libDev.devMac = device.getDevMac();
        libDev.devType = device.getDevType();
        libDev.eKey = device.geteKey();
        libDev.endDate = device.getEndDate();
        libDev.openType = device.getOpenType();
        libDev.privilege = device.getPrivilege();
        libDev.startDate = device.getStartDate();
        libDev.useCount = device.getUseCount();
        libDev.verified = device.getVerified();
        if (libDev.devType == 1)
        {
            String userCard = null;
            UsersCardData userCardData = new UsersCardData(BaseApplication.getInstance());

            if(device.getDbname_company() == null) //为了兼容之前的设备没有dbcompany，暂时这样--benson
            {
                libDev.cardno = "123456";
            }else
            {
                UsersCardDom userCardDom = userCardData.getUsersCardDom(username, device.getDbname_company());
                if(userCardDom != null)
                {
                    userCard = userCardDom.getCardno();
                }
                libDev.cardno = userCard;
            }
        }
        return libDev;
    }

    /**
     * 请求权限
     */
    public void requestPermissiontest() {
        // you needer permissions
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        // check it is needed
        permissions = CheckPermissionUtils.getNeededPermission(mainActivity, permissions);
        // requestPermissions
        if (permissions.length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mainActivity.requestPermissions(permissions, REQUEST_CODE_TEST);
            }
        }
    }

//    //获取设备，并将设备数量回调出去
//    public void getDeviceCount(final Context context, String clientId, DMGetDeviceCountCallback.DMGetDeviceCallback callback)
//    {
//        deviceCountCallback = callback;
//        getDevKeyList(context, clientId);
//    }

    /**
     * Get DevKey control list
     * @param context
     * @param clientId
     */
    public void getDevKeyList(final Context context, String clientId) {
        if (clientId == null) {
            return;
        }
        if (mDbDao == null) {
            mDbDao = DBBiz.getInstanter(context.getApplicationContext());
        }
        username = SPUtils.getString(Constants.USERNAME,context);
        OkhttpHelper.getDevKeyList(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String result) {
                try {
                    JSONObject ret = new JSONObject(result);
                    if (!ret.optString("msg").equals("ok")) {
                        return;
                    }
                    JSONObject data = ret.getJSONObject("data");
                    if (data == null || data.length() == 0) {
                        return;
                    }
                    JSONArray dev_listArray = data.optJSONArray("dev_list");// 获取门列表
                    JSONObject jsonObject;

//                    if (dev_listArray != null && deviceCountCallback != null) //将蓝牙设备的数量回调给外层
//                    {
//                        deviceCountCallback.setCount(0, dev_listArray.length());
//                    }
                    for (int i = 0; i < dev_listArray.length(); i++) {
                        jsonObject = dev_listArray.getJSONObject(i);
                        String community_code = jsonObject.optString("community_code"); // 设备所属小区编号，唯一键，用于区分小区
                        String dev_name = jsonObject.optString("dev_name"); // 设备名称
                        String dev_sn = jsonObject.optString("dev_sn"); // 设备序列号，远程开门使用
                        int dev_type = jsonObject.optInt("dev_type"); // 设备类型
                        dev_type = (dev_type == 0) ? 2 : dev_type;
                        String dev_voip_account = jsonObject.optString("dev_voip_account"); // 可视对讲设备音视频账号
                        DevKeyBean devEntity = new DevKeyBean();
                        devEntity.setCommunity_code(community_code);
                        devEntity.setDev_name(dev_name);
                        devEntity.setDev_sn(dev_sn);
                        devEntity.setDev_type(dev_type);
                        devEntity.setDev_voip_account(dev_voip_account);
                        devEntity.setUsername(username);
                        DevKeyMetaData deviceData = new DevKeyMetaData(context.getApplicationContext());
                        deviceData.addDevKeyList(devEntity);

                        LogUtils.e(TAG,
                                "收到的视频门禁设备:" + i + "---" + "community_code:" + community_code + "-dev_name-"
                                        + dev_name + "--dev_sn-" + dev_sn + ",dev_voip_account="
                                        + dev_voip_account);
                    }
                    JSONArray room_listArray = data.optJSONArray("room_list");// 获取用户列表
                    for (int i = 0; i < room_listArray.length(); i++) {
                        jsonObject = room_listArray.getJSONObject(i);
                        String community_code = jsonObject.optString("community_code"); // 房间所属小区编号，唯一键，用于区分小区
                        String community = jsonObject.optString("community"); // 小区名称
                        String building = jsonObject.optString("building"); // 楼栋单元名称
                        int room_id = jsonObject.optInt("room_id"); // 房间id，和community_code一起确定唯一小区房间（删除或更新时id+community_code一起作为主键判断）
                        String room_name = jsonObject.optString("room_name"); // 房间名称
                        String room_code = jsonObject.optString("room_code"); // 房间编号
                        String start_datetime = jsonObject.optString("start_datetime"); // 设备序列号，远程开门使用
                        String end_datetime = jsonObject.optString("end_datetime"); // 可视对讲设备音视频账号
                        RoomBean roomEntity = new RoomBean();
                        roomEntity.community_code = community_code;
                        roomEntity.community = community;
                        roomEntity.building = building;
                        roomEntity.room_id = room_id;
                        roomEntity.room_name = room_name;
                        roomEntity.room_code = room_code;
                        roomEntity.start_datetime = start_datetime;
                        roomEntity.end_datetime = end_datetime;

                        LogUtils.e("要保存的信息：" + roomEntity.toString());
                        mDbDao.addRoomList(roomEntity);

                        LogUtils.e(TAG,
                                "收到的用户列表:" + i + "---" + "community_code:" + community_code + "-community-"
                                        + community + "--building-" + building + ",room_name=" + room_name);
                        LogUtils.e(TAG, ",room_id=" + room_id + "--start_datetime=" + start_datetime
                                + ",end_datetime=" + end_datetime);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },clientId);
    }

    /**
     * Get Access control list
     * @param clientId
     */
    public void requestAccessDevList(Context context,String clientId) {
        if (clientId == null) {
            return;
        }
        if (deviceData == null) {
            deviceData = new AccessDevMetaData(context.getApplicationContext());
        }
        username = SPUtils.getString(Constants.USERNAME,context);
        OkhttpHelper.getAccessDevList(new StringCallback() {

            @Override
            public void onResponse(String result) {
                try {
                    JSONObject ret = new JSONObject(result);
                    if (!ret.optString("msg").equals("ok")) {
                        return;
                    }
                    JSONArray data = ret.optJSONArray("data");
                    if (data == null || data.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        String dbname_company = null;
                        int door_no = -1;
                        if(!data.getJSONObject(i).isNull(AccessDevMetaData.AccessDev.ACCESS_DEV_DBNAME_COMPANY))
                        {
                            dbname_company = data.getJSONObject(i).getString(AccessDevMetaData.AccessDev.ACCESS_DEV_DBNAME_COMPANY);
                        }
                        if(!data.getJSONObject(i).isNull(AccessDevMetaData.AccessDev.ACCESS_DEV_DOOR_NO))
                        {
                            door_no = data.getJSONObject(i).getInt(AccessDevMetaData.AccessDev.ACCESS_DEV_DOOR_NO);
                        }


                        JSONArray readList = item.optJSONArray("reader");
                        if (readList == null || readList.length() == 0) {
                            return;
                        }
                        String showName = item.getString("show_name");
                        for (int rdPos = 0; rdPos < readList.length(); rdPos++) {
                            JSONObject reader = readList.getJSONObject(rdPos);
                            LogUtils.e(TAG, "reader" + rdPos + ":" + reader.toString());
                            String cardno  = "";
                            if (!reader.isNull(AccessDevMetaData.AccessDev.ACCESS_DEV_CARDNO)) {
                                cardno = reader.getString(AccessDevMetaData.AccessDev.ACCESS_DEV_CARDNO);
                            }
                            AccessDevBean device = new AccessDevBean();
                            device.setDevSn(reader.optString("reader_sn"));
                            device.setDevMac(reader.optString("reader_mac"));
                            device.setDevName(showName);
                            device.setStartDate(reader.optString("start_date"));
                            device.setEndDate(reader.optString("end_date"));
                            device.seteKey(reader.optString("ekey"));
                            device.setDevType((reader.optInt("dev_type") == 0 ? AccessDevBean.DEV_TYPE_READER
                                    : reader.optInt("dev_type")));
                            device.setNetWorkSupport(reader.optInt("network"));
                            device.setOpenType((reader.optInt("open_type") == 0 ? 1 : reader.optInt("open_type")));
                            device.setPrivilege((reader.optInt("privilege") == 0 ? AccessDevBean.DEV_PRIVILEGE_USER
                                    : reader.optInt("privilege")));
                            device.setVerified((reader.optInt("verified") == 0 ? 1 : reader.optInt("verified")));
                            device.setUseCount(reader.optInt("use_count"));
                            device.setFunction(reader.optString(AccessDevMetaData.AccessDev.ACCESS_FUNCTION));
                            device.setUsername(username);

                            //保存设备编号、设备管理账户
                            device.setDbname_company(dbname_company);
                            device.setDoor_no(door_no);
                            device.setCardno(cardno);
                            // 这里开始保存代码
                            deviceData.saveAccessDev(device);
                            LogUtils.e(TAG,
                                    "收到的手机门禁设备:" + i + "---" + "dev_sn:" + reader.optString("reader_sn") + "-dev_name-"
                                            + showName);
                        }
                        //刷新缓存
                        refeshAllDevList();
                        refeshAutoList();
                        refeshShakeList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call arg0, Exception arg1) {
            }
        }, clientId);
    }

    /**
     * Clean control list
     */
    public void cleanCurDeviceList()
    {
        if (allDevList != null && allDevList.size() > 0)
        {
            allDevList.clear();
        }
        if (shakeOpenList != null && shakeOpenList.size() > 0)
        {
            shakeOpenList.clear();
        }
        if (autoOpenList != null && autoOpenList.size() > 0)
        {
            autoOpenList.clear();
        }
    }

}
