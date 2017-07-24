package com.doormaster.topkeeper.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OpenModel;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack;
import com.intelligoo.sdk.LibDevModel;

import java.util.ArrayList;

/**
 * New task, used to open the door
 * Created by Liukebing on 2017/2/27.
 */

public class AutoOpenAsyncTask extends AsyncTask<Boolean,LibDevModel,String> {
    private static BluetoothAdapter mAdapter = null;

    private static Context context = null;

    private ScanStatus mStatus = ScanStatus.START;

    private static SoundPool soundPool = null;

    private static int soundSrcId = 0;

    private static ArrayList<AccessDevBean> accessDevData = null;

    private int curRssi;

    //构造器，初始化变量
    public AutoOpenAsyncTask(Context con, int curRssi)
    {
        this.curRssi = curRssi;
        BluetoothManager manager = (BluetoothManager)
            con.getSystemService(Context.BLUETOOTH_SERVICE);
        context = con;
        mAdapter = manager.getAdapter();
        soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
        soundSrcId = soundPool.load(context, R.raw.open_door_sound, 1);

        AccessDevMetaData deviceData = new AccessDevMetaData(context.getApplicationContext());
        String username = SPUtils.getString(Constants.USERNAME);
        accessDevData = deviceData.getAllAccessDevList(username);
    }


    @Override
    protected String doInBackground(Boolean... params) {
        LogUtils.d("" + params[0]);

        mStatus = ScanStatus.START;
        while(mStatus != ScanStatus.FINISHED)
        {
            if (mStatus == ScanStatus.START) {    //开始扫描两秒
                TopkeeperModel.keyDoor(curRssi, 2000, OpenModel.AUTOOPEN, new DMModelCallBack.DMCallback() {
                    @Override
                    public void setResult(int i, DMException e) {
                        LogUtils.d("开门结束：" + i);
                        if (mStatus == ScanStatus.SCANNING)
                            mStatus = ScanStatus.OPEN_END;
                    }
                });
                mStatus = ScanStatus.SCANNING;
            }else if (mStatus == ScanStatus.SCANNING){ //间隔扫描设置
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (mStatus == ScanStatus.OPEN_END) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mStatus == ScanStatus.OPEN_END)
                    mStatus = ScanStatus.START;
            }

        }
        LogUtils.d("ScanStatus" + mStatus);
        mStatus = ScanStatus.FINISHED;
        return "123";
    }

    private static String getDeviceSn(String name) {
        if (name == null || (!name.contains("-")))
        {
            return null;
        }
        int markIndex = name.indexOf("-");
        String devSn = name.substring(markIndex + 1);
        return devSn;
    }

    public void stopScanAsyncTask()
    {
        mStatus = ScanStatus.FINISHED;
    }

    public void setCurRssi(int curRssi) {
        this.curRssi = curRssi;
    }

    //后台运行状态
    public enum ScanStatus {

        START,

        STOP,

        SCANNING,

        OPEN_START,

        OPEN_END,

        FINISHED,
    }

}
