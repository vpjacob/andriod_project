package com.doormaster.topkeeper.task;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.db.OpenRecordData;
import com.doormaster.topkeeper.bean.RecordBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.SPUtils;
import com.intelligoo.sdk.BluetoothLeService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Liukebing on 2017/3/2.
 */

public class ManagerDev {

    //操作方式
    public static final int MANAGER_OPEN = 0x00;  //开门
    public static final int MANAGER_REGION_MODEL = 0x01;  //管理员登记模式
    public static final int MANAGER_EXIT_REGION_MODEL =0x02; //退出管理员登记模式
    public static final int MANAGER_ADD_DEV = 0x03;  //添加设备
    public static final int MANAGER_TIME_SET = 0x04;  //时间设置
    public static final int MANAGER_MODIFY_PWD = 0x05;  //修改密码
    public static final int MANAGER_ENTER_ADD_CARD_MODEL = 0x06; //进入登记卡模 式
    public static final int MANAGER_ENTER_DELETE_CARD_MODEL = 0x07; //进入删除卡模式
    public static final int MANAGER_EXIT_ADD_CARD_MODEL = 0x08;	//退出卡登记模式
    public static final int MANAGER_EXIT_DELETE_CARD_MODEL = 0x09;	//退出卡删除模式
    public static final int MANAGER_UPDATE_CONFIG = 0x0a;	//更新配置
    public static final int MANAGER_GET_CONFIG = 0x0b;	//获取配置
    public static final int MANAGER_DELETE_ALL_INFO = 0x0c;	//删除所有卡用户信息
    public static final int MANAGER_RESET = 0x0d;	//系统初始化
    public static final int MANAGER_GET_CARD_DATA = 0x0e;    //获取设备卡登记信息
    public static final int MANAGER_UPDATE_CARD_DATA = 0x0f;    //更新设备卡登记信息
    public static final int MANAGER_DELETE_CARD_DATA = 0x10;    //删除设备卡登记信息
    public static final int MANAGER_SET_RSSI = 0x11;    //删除设备卡登记信息
    //设备操作返回值定义
    public static final int RESULT_CALLBACK_SUCCESS = 0x00;
    public static final int RESULT_CALLBACK_ERROR_CRC = 0x01;
    public static final int RESULT_CALLBACK_ERROR_FORMAT = 0x02;
    public static final int RESULT_CALLBACK_ERROR_PASSWD = 0x03;
    public static final int RESULT_CALLBACK_ERROR_POWER = 0x04;
    public static final int RESULT_CALLBACK_ERROR_DB = 0x05;
    public static final int RESULT_CALLBACK_ERROR_USER_NOT_REG = 0x06;
    public static final int RESULT_CALLBACK_ERROR_RAND_CHECK = 0x07;
    public static final int RESULT_CALLBACK_ERROR_GET_RAND = 0x08;
    public static final int RESULT_CALLBACK_ERROR_LENGTH = 0x09;
    public static final int RESULT_CALLBACK_ERROR_MODE = 0x0A;
    public static final int RESULT_CALLBACK_ERROR_KEY_CKECK = 0x0B;
    public static final int RESULT_CALLBACK_ERROR_UNSUPPORT = 0x0C;
    public static final int RESULT_CALLBACK_ERROR_MAX_COUNT = 0x0d;

    //开门方式
    public static final int TOUCH = 0X01;
    public static final int SHAKE = 0x02;
    public static final String OPEN_WAY = "com.intelligoo.app.task.ManagerDev.OPEN_WAY";
    //开门连接参数
    private static final int LINK_DISCONNECTED = 0x01;
    private static final int LINK_CONNECTED = 0x02;
    private static final int LINK_SERVICES_DISCOVERED = 0x03;
    private static final int LINK_DATA_CALLBACK = 0x04;
    private static final int LINK_DATA_WRITE_SUCCESS = 0x05;
    private static final int LINK_READ_RSSI = 0X06;
    private static final int LINK_GET_RAND = 0x07;
    private static final int LINK_OPENNING = 0x08;
    private static final int LINK_RESOLVE_DATA = 0x09;
    private final static String SERVER_UUID_STR = "0886b765-9f76-6472-96ef-ab19c539878a";
    private final static String CHARACTER_RX_UUID_STR = "0000878b-0000-1000-8000-00805f9b34fb";
    private final static String CHARACTER_TX_UUID_STR = "0000878c-0000-1000-8000-00805f9b34fb";
    //CMD控制命令
    private static final byte CMD_DEV_MANAGER = 0x01;
    private static final byte CMD_PARAM_CFG = 0x02;
    private static final byte CMD_DEV_CONTROL = 0x03;
    private static final byte CMD_DATA_MANAGER = 0x04;
    //计数器常量
    private static final int START_TASK = 20;
    private static final int TASK_PRIOD = 18;
    private static final int COUNT_DELAY = 7500;
    private static final int COUNT_DELAY_LONG = 1000*60*15; //15分钟
    //蓝牙通信
    private static BluetoothLeService mService = null;
    private static LocalBroadcastManager mBroadcastManager = null;
    private static BluetoothGattCharacteristic mReadCharacteristic = null;
    private static BluetoothGattCharacteristic mWriteCharacteristic = null;
    private static boolean mOperating = false;    //控制当前只进行一次操作
    private static int mOperation = 0x00;    //操作
    private static Bundle mBundle = null;    //附带参数
    private static ManagerCallback mCallback = null;    //结果回调
    private static int mOpenWay = 0x00;    //开门方式
    private static Timer timer;    //计时器
    private static TimerTask task;
    private static int count = 0;
    private static long startTime = 0;    //开始时间记录
    private static RecordBean mRecord = null;    //要上传的开锁记录
    private static int state = LINK_DISCONNECTED;    //初始化连接状态
    private static int rssiCount = 0;
    private static int setRssi = 0;

    private static boolean synchTimeRes = false; //记录同步时间的结果

    //保存开锁记录
    public static void saveRecord(boolean isSuccess) {
        if (isSuccess) {
            mRecord.setOpRet(RESULT_CALLBACK_SUCCESS);
        }
        int action_time = (int) (System.currentTimeMillis());
        mRecord.setActionTime(action_time);
        OpenRecordData recordData = new OpenRecordData(BaseApplication.getContext());
        mRecord.setUpload(RecordBean.UPLOAD_FAILED);
        recordData.saveOpenRecord(mRecord);
        UpLoadRecord.upLoadOpenRecord();
    }

    public static RecordBean setRecordInfo(AccessDevBean device) {
        String username = SPUtils.getString(Constants.USERNAME);
        RecordBean record = new RecordBean();

        if (device.getDevName() != null)
        {
            record.setDevName(device.getDevName());
        } else
        {
            record.setDevName(device.getDevSn());
        }
        record.setDevMac(device.getDevMac());
        record.setDevSn(device.getDevSn());
        String event_time = getCurrentDate();

        record.setEventTime(event_time);
        record.setOpUser(username);
        mRecord = record;
        return record;
    }

    private static String getCurrentDate() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        Date date = c.getTime();
        SimpleDateFormat sdf_send = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
        return sdf_send.format(date);
    }

    public interface ManagerCallback
    {
        void setResult(int result, Bundle bundle);
    }

    private static void setManagerCallback(int operation, int result, Bundle bundle)
    {
//        MyLog.Assert(mCallback != null);

//        MyLog.debug("setManagerCallback : "+result);
        switch (operation) {
            case MANAGER_OPEN: //开门
                Thread save_th = new Thread(new Runnable() {
                    public void run() {
//                        saveRecord();
                    }
                });
                save_th.start();
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_REGION_MODEL: //超级管理员开启登记模式
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_EXIT_REGION_MODEL: //超级管理员退出登记模式
                mCallback.setResult(result, null);
                break;
            case MANAGER_ADD_DEV:	//添加设备
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_TIME_SET:	 //时间设置
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_MODIFY_PWD:	//修改密码
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_ENTER_ADD_CARD_MODEL:	//进入登记卡模式
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_ENTER_DELETE_CARD_MODEL:	//进入删除卡模式
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_EXIT_ADD_CARD_MODEL:	//退出登记卡模式
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_EXIT_DELETE_CARD_MODEL:	//退出删除卡模式
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_UPDATE_CONFIG:	//更新配置
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_GET_CONFIG:	//获取配置
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_DELETE_ALL_INFO:	//删除所有信息
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_RESET:	//设备初始化
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_GET_CARD_DATA:    //获取设备卡登记信息
                mCallback.setResult(result, bundle);
                break;
            case MANAGER_UPDATE_CARD_DATA :    //更新设备卡登记信息
                mCallback.setResult(result, bundle);
                break;
            case  MANAGER_DELETE_CARD_DATA:    //删除卡登记信息
                mCallback.setResult(result, bundle);
                break;
            case  MANAGER_SET_RSSI:    //删除卡登记信息
                mCallback.setResult(result, bundle);
                break;
            default:
                break;
        }
    }
}
