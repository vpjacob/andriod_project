package com.doormaster.topkeeper.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.UserData;
import com.doormaster.topkeeper.service.ShakeOpenService;
import com.doormaster.topkeeper.utils.DialogUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.topkeeper.view.TitleBar;



/**
 * 摇一摇开门设置
 */
public class Act_Shake extends BaseActivity implements View.OnClickListener{
    private static String TAG = "Act_Shake";

    private Activity mActivity;
    public TitleBar title_bar;

    public Switch shake_open;

    public AppCompatSeekBar sk_distance;

    public Button btn_select_device;

    private boolean isShake;
    private int progress;
    private Intent shakeService;
    private UserData userData;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shake);
        mActivity = this;
        initView();
        setupView();
    }
    /**
     * 初始化界面
     */
    private void initView() {
        title_bar = (TitleBar) findViewById(R.id.title_bar);
        shake_open = (Switch) findViewById(R.id.lock_info_shake_open);
        sk_distance = (AppCompatSeekBar) findViewById(R.id.sk_distance);
        btn_select_device = (Button) findViewById(R.id.btn_select_device);
        shakeService = new Intent(Act_Shake.this,ShakeOpenService.class);
        userData = new UserData(mActivity);
        username = SPUtils.getString(Constants.USERNAME,mActivity);
    }

    /**
     * 加载事件
     */
    private void setupView() {
        btn_select_device.setOnClickListener(this);

        title_bar.setTitle(getString(R.string.shake_to_open));
        title_bar.setLeftImageResource(R.drawable.left_ac);
        title_bar.setLeftLayoutClickListener(this);
        title_bar.setBackgroundColor(Color.parseColor("#FFFFFF"));

        isShake = userData.getShakeOpen(username);
        shake_open.setChecked(isShake);
        shake_open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isShake = isChecked;
                userData.setShakeOpen(username, isShake);
                int ret = TopkeeperModel.refeshShakeList();
                if (ret == 1) {
                    DialogUtils.showAlert(mActivity, getString(R.string.remind), getString(R.string.please_select_device), getString(R.string.ensure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openActivity(Act_SelectShakeDevice.class);
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            shake_open.setChecked(false);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        sk_distance.setMax(Constants.SHAKE_MAX_PROGRESS);
//        progress = SPUtils.getInt(Constants.DISTANCE_SHAKE, Constants.SHAKE_DEFAUT_PROGRESS,mActivity);
        progress = userData.getShakeDistance(username);
        sk_distance.setProgress(progress);
        sk_distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.d(TAG, "onProgress:"+seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
                LogUtils.d(TAG, "onStart:"+seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
//                SPUtils.put(Constants.DISTANCE_SHAKE, progress, mActivity);
                userData.setShakeDistance(username, progress);
                TopkeeperModel.refeshShakeList();
                LogUtils.d(TAG, "onStop:"+seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.left_layout) {//返回
            finish();

        } else if (i == R.id.btn_select_device) {
            openActivity(Act_SelectShakeDevice.class);

        } else {
        }
    }
}
