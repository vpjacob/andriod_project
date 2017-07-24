package com.doormaster.topkeeper.utils;

import android.bluetooth.BluetoothAdapter;

/**
 * 蓝牙管理
 * Created by Liukebing on 2017/2/7.
 */

public class BluetoothManager {
    /**
     * 当前 Android 设备是否支持 Bluetooth
     *
     * @return true：支持 Bluetooth false：不支持 Bluetooth
     */
    public static boolean isBluetoothSupported()
    {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }
/**
 * 当前 Android 设备的 bluetooth 是否已经开启
 *
 * @return true：Bluetooth 已经开启 false：Bluetooth 未开启
 */
    public static boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }
/**
 * 强制开启当前 Android 设备的 Bluetooth
 *
 * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
 */
    public static boolean turnOnBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.enable();
    }
/**
 * 强制关闭当前 Android 设备的 Bluetooth
 *
 * @return  true：强制关闭 Bluetooth　成功　false：强制关闭 Bluetooth 失败
 */
    public static boolean turnOffBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.disable();
    }

}
