package com.doormaster.topkeeper.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.OpenModel;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack;
import com.intelligoo.sdk.BluetoothLeService;


public class ShakeOpenService extends Service {
    private static final String TAG = "ShakeOpenService";
    //摇一摇参数
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private static boolean shaked = false;
    public long current_time;
    public long open_door_time;

    int distance;
    int rssi;

//    private ServiceConnection conn = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
////            mLeService = ((BluetoothLeService.LocalBinder) service).getService(ShakeOpenService.this);
//            LogUtils.d("get mLeService");
////            if (!mLeService.initialize()) {
////                MyLog.debug("Unable to initialize Bluetooth");
////                mLeService = null;
////            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    };
    public ShakeOpenService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Intent intent = new Intent(getApplicationContext(), BluetoothLeService.class);

//        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        distance = intent.getIntExtra("distance", 25);
        rssi = -50 - distance;
        ToastUtils.showMessage(this, R.string.shake_to_open_is_started);
        if (sensorManager != null)
        {
            sensorManager.registerListener(sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
        return START_REDELIVER_INTENT;
    }

    private SensorEventListener sensorEventListener = new  SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            float [] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            float shakelimit = 13.0f;
            float average = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
            if ((average > shakelimit) && !shaked) {
                vibrator.vibrate(400);
                shaked = true;
                current_time = System.currentTimeMillis();

                TopkeeperModel.keyDoor(rssi, Constants.SHAKE_SCAN_SECONDS, OpenModel.SHAKEOPEN, new DMModelCallBack.DMCallback() {
                    @Override
                    public void setResult(int i, DMException e) {
                        shaked = false;
                    }
                });

            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (conn != null)
//        {
//            unbindService(conn);
//        }
        if ((sensorManager != null) && (sensorEventListener != null))
        {
            sensorManager.unregisterListener(sensorEventListener);
        }
        sensorManager = null;
        sensorEventListener = null;
        vibrator = null;
    }
}
