package com.doormaster.topkeeper.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.utils.ConstantUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.ToastUtils;


public class AutoOpenService extends Service {
    public static final int NOTIFICATION_ID = 123;

    private static AutoOpenAsyncTask myAsyncTask = null;

    private ScanStatus mStatus = ScanStatus.START;

    int distance;
    int rssi;
    private Handler mhandler = new Handler();
    public AutoOpenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //设置为前台服务
    private void showNotification() {
        Builder mBuilder =
                new Builder(this).setSmallIcon(ConstantUtils.getIconRes())
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.automatic_door_opening));
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID,notification);
        startForeground(NOTIFICATION_ID, notification);
        autoScan();
    }

    /**
     * 自动开门，新起一个线程
     */
    private void autoScan(){
        myAsyncTask = new AutoOpenAsyncTask(this,rssi);
        myAsyncTask.execute(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(">>>>>>>>>>>>>>>>2");
        distance = intent.getIntExtra("distance", 25);
        rssi = -50 - distance;
        myAsyncTask.setCurRssi(rssi);
        ToastUtils.showMessage(this, R.string.near_to_open_is_started);
        return START_REDELIVER_INTENT;
    }

    @Override
    public boolean stopService(Intent name) {
        LogUtils.d(">>>>>>>>>>>>>>>>1");
        stopForeground(true);
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        if (myAsyncTask != null) {
            LogUtils.d(">>>>>>>>>>>>>>>>4");
            myAsyncTask.stopScanAsyncTask();
            myAsyncTask.cancel(true);
        }
        stopSelf();
        stopForeground(true);
        super.onDestroy();
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
