package com.sdk.samples.apicloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.doormaster.vphone.inter.DMVPhoneModel;
import com.z421614851.iga.R;

;

/**
 * Created by Jaeger on 15-9-8.
 * StatusBarDemo
 */
public class SplashActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,WebPageModule.class);
                startActivity(intent);
                finish();
            }
        },2000);
        DMVPhoneModel.initDMVPhoneSDK(SplashActivity.this,getResources().getString(R.string.app_name) );
    }
}
