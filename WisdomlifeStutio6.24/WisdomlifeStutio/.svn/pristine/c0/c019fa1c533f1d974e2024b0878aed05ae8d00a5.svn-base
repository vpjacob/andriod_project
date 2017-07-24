package com.doormaster.topkeeper.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Toast工具类
 * Created by Liukebing on 2017/3/29.
 */

public class ToastUtils {

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static final Object synObj = new Object();

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     * @param act
     * @param msg
     */
    public static void showMessage(final Context act, final String msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_LONG
     * @param act
     * @param msg
     */
    public static void showMessageLong(final Context act, final String msg) {
        showMessage(act, msg, Toast.LENGTH_LONG);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     * @param act
     * @param msg
     */
    public static void showMessage(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_LONG
     * @param act
     * @param msg
     */
    public static void showMessageLong(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_LONG);
    }

    /**
     * Toast发送消息
     * @param act
     * @param msg
     * @param len
     */
    private static void showMessage(final Context act, final int msg,
                                    final int len) {
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        synchronized (synObj) {
                            if (toast != null) {
//                                toast.cancel();
                                toast.setText(msg);
                                toast.setDuration(len);
                            } else {
                                toast = Toast.makeText(act, msg, len);
                            }
                            toast.show();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Toast发送消息
     * @param act
     * @param msg
     * @param len
     */
    public static void showMessage(final Context act, final String msg,
                                   final int len) {
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        synchronized (synObj) {
                            if (toast != null) {
//                                toast.cancel();
                                toast.setText(msg);
                                toast.setDuration(len);
                            } else {
                                toast = Toast.makeText(act, msg, len);
                            }
                            toast.show();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 关闭当前Toast
     */
    public static void cancelCurrentToast() {
        if (toast != null) {
            toast.cancel();
        }
    }


    //设备操作返回值定义
    public static final int RESULT_CALLBACK_SUCCESS = 0;
    public static final int RESULT_CALLBACK_ERROR_CRC = 1;
    public static final int RESULT_CALLBACK_ERROR_FORMAT = 2;
    public static final int RESULT_CALLBACK_ERROR_PASSWD = 3;
    public static final int RESULT_CALLBACK_ERROR_POWER = 4;
    public static final int RESULT_CALLBACK_ERROR_DB = 5;
    public static final int RESULT_CALLBACK_ERROR_USER_NOT_REG = 6;
    public static final int RESULT_CALLBACK_ERROR_RAND_CHECK = 7;
    public static final int RESULT_CALLBACK_ERROR_GET_RAND = 8;
    public static final int RESULT_CALLBACK_ERROR_LENGTH = 9;
    public static final int RESULT_CALLBACK_ERROR_MODE = 10;
    public static final int RESULT_CALLBACK_ERROR_KEY_CHECK = 11;
    public static final int RESULT_CALLBACK_ERROR_UNSUPPORT = 12;
    public static final int RESULT_CALLBACK_ERROR_MAX_COUNT = 13;

    public static final int RESULT_ERROR_TIMER_OUT = 48;
    public static final int RESULT_ERROR_BLESERVICE = 49;
    public static final int RESULT_ERROR_DATA_LEN = 50;
    public static final int RESULT_ERROR_DATA_CALLBACK_NULL = 51;
    public static final int RESULT_ERROR_L1_REC_CMD = 52;
    public static final int RESULT_ERROR_RAND_NOT_GET = 53;
    public static final int RESULT_ERROR_RESO_PARA_CFG_CMD = 54;
    public static final int RESULT_ERROR_RESO_DATA_MANAGER_CMD = 55;
    public static final int RESULT_ERROR_OPERATING = 0x0108;

    public static final int RESULT_ERROR_NOT_SUPPORT_BLE = -100;
    public static final int RESULT_ERROR_BLE_NOT_OPEN = -101;
    public static final int RESULT_ERROR_SN_NOT_EXIST = -102;
    public static final int RESULT_ERROR_BLE_RETURN_NULL = -103;
    public static final int RESULT_ERROR_OPEN = -104;
    public static final int RESULT_ERROR_DEVICE_NOT_REACTION = -105;
    public static final int RESULT_ERROR_DEVICE_NOT_NEAR = -106;
    public static final int RESULT_ERROR_DEVICE_OPERATION = -107;
    public static final int RESULT_ERROR_SCAN_UNIT = -108;
    public static final int RESULT_ERROR_SCAN_TIME_BEYOND = -109;
    public static final int RESULT_ERROR_SUPER_ADMIN_EXIST = -110;
    public static final int RESULT_ERROR_DEVICE_MAC = -111;

    public static final int RESULT_ERROR_CARDNO_NULL = -1;
    public static final int RESULT_ERROR_SN_NULL = -2;
    public static final int RESULT_ERROR_MAC_NULL = -3;
    public static final int RESULT_ERROR_E_KEY_NULL = -4;
    public static final int RESULT_ERROR_TYPE_NULL = -5;
    public static final int RESULT_ERROR_DEVICE_PRIVILEGE = -6;
    public static final int RESULT_ERROR_OPEN_WAY = -7;

    private static Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
    private static final int [][] result_id =
            {{RESULT_CALLBACK_SUCCESS, R.string.open_succeed},
                    {RESULT_CALLBACK_ERROR_CRC, R.string.result_callback_error_crc},
                    {RESULT_CALLBACK_ERROR_FORMAT, R.string.result_callback_error_format},
                    {RESULT_CALLBACK_ERROR_PASSWD, R.string.result_callback_error_passwd},
                    {RESULT_CALLBACK_ERROR_POWER, R.string.result_callback_error_power},
                    {RESULT_CALLBACK_ERROR_DB, R.string.result_callback_error_db},
                    {RESULT_CALLBACK_ERROR_USER_NOT_REG, R.string.result_callback_error_use_not_reg},
                    {RESULT_CALLBACK_ERROR_RAND_CHECK, R.string.result_callback_error_rand_check},
                    {RESULT_CALLBACK_ERROR_GET_RAND, R.string.result_callback_error_get_rand},
                    {RESULT_CALLBACK_ERROR_LENGTH, R.string.result_callback_error_length},
                    {RESULT_CALLBACK_ERROR_MODE, R.string.result_callback_error_mode},
                    {RESULT_CALLBACK_ERROR_KEY_CHECK, R.string.result_callback_error_key_check},
                    {RESULT_CALLBACK_ERROR_UNSUPPORT, R.string.result_callback_error_unsupport},
                    {RESULT_CALLBACK_ERROR_MAX_COUNT, R.string.result_callback_error_max_count},

                    {RESULT_ERROR_TIMER_OUT, R.string.result_error_timer_out},
                    {RESULT_ERROR_BLESERVICE, R.string.result_error_bleservice},

                    {RESULT_ERROR_DATA_LEN, R.string.result_error_data_len},
                    {RESULT_ERROR_DATA_CALLBACK_NULL,R.string.result_error_data_null},
                    {RESULT_ERROR_L1_REC_CMD,R.string.result_error_l1_cmd},
                    {RESULT_ERROR_RAND_NOT_GET,R.string.result_error_rand_not_get},

                    {RESULT_ERROR_RESO_PARA_CFG_CMD,R.string.result_error_para_cfg_cmd},
                    {RESULT_ERROR_RESO_DATA_MANAGER_CMD,R.string.result_error_data_manager_cmd},

                    {RESULT_ERROR_OPERATING,R.string.operationing},
                    {RESULT_ERROR_NOT_SUPPORT_BLE, R.string.ble_not_supported},
                    {RESULT_ERROR_BLE_NOT_OPEN, R.string.ble_not_open},
                    {RESULT_ERROR_SN_NOT_EXIST, R.string.sn_not_exist},
                    {RESULT_ERROR_BLE_RETURN_NULL, R.string.ble_return_null},
                    {RESULT_ERROR_OPEN, R.string.open_fail},
                    {RESULT_ERROR_DEVICE_NOT_REACTION, R.string.device_not_reaction},
                    {RESULT_ERROR_DEVICE_NOT_NEAR, R.string.device_not_near},
                    {RESULT_ERROR_DEVICE_OPERATION, R.string.device_operationing},
                    {RESULT_ERROR_SCAN_UNIT, R.string.time_unit_error},
                    {RESULT_ERROR_SCAN_TIME_BEYOND, R.string.scan_time_beyond},
                    {RESULT_ERROR_SUPER_ADMIN_EXIST, R.string.super_admin_exist},
                    {RESULT_ERROR_DEVICE_MAC, R.string.device_mac_error},
                    {RESULT_ERROR_CARDNO_NULL, R.string.cardno_null},
                    {RESULT_ERROR_SN_NULL, R.string.sn_null},
                    {RESULT_ERROR_MAC_NULL, R.string.mac_null},
                    {RESULT_ERROR_E_KEY_NULL, R.string.eKey_null},
                    {RESULT_ERROR_TYPE_NULL, R.string.type_null},
                    {RESULT_ERROR_DEVICE_PRIVILEGE, R.string.privilege_null},
                    {RESULT_ERROR_OPEN_WAY, R.string.open_way_error}
            };

    static
    {
        for (int i = 0;i < (result_id.length);i++) {
            resultMap.put(result_id[i][0], result_id[i][1]);
        }
    }

    public static void tips(int result)
    {
        Integer rStringId = resultMap.get(result);
        if (rStringId != null)
        {
//			Toast.makeText(MyApplication.getInstance(),
//					rStringId,Toast.LENGTH_SHORT).show();
            showMessage(BaseApplication.getInstance(), rStringId);
        }
        else
        {
            String unknow_error = BaseApplication.getInstance().getResources().getString(R.string.unknown_error) + result;
            showMessage(BaseApplication.getInstance(), unknow_error);
        }
    }

}
