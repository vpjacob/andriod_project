package com.doormaster.topkeeper.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import com.doormaster.topkeeper.utils.ToastUtils;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.UserData;
import com.doormaster.topkeeper.service.AutoOpenService;
import com.doormaster.topkeeper.utils.DialogUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.topkeeper.view.TitleBar;

import static com.doormaster.topkeeper.utils.TopkeeperModel.refeshAutoList;


/**
 * 靠近开门设置
 */
public class Act_Near_Open extends BaseActivity implements View.OnClickListener{
    private static String TAG = "Act_Near_Open";

    private Activity mActivity;
    public TitleBar title_bar;

    public Switch near_open;

    public AppCompatSeekBar near_distance;

    public Button btn_select_device;

    public Switch is_open_space_enable;

    public EditText et_open_space_second;

    private boolean auto_open;
    private int progress;
    private Intent autoOpenService;
    private UserData userData;
    private String username;

    private boolean enable_auto_open_space;
    private int auto_open_space_seconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_near_open);
        mActivity = this;
        initView();
        setupView();
    }
    /**
     * 初始化界面
     */
    private void initView() {
        title_bar = (TitleBar) findViewById(R.id.title_bar);
        near_open = (Switch) findViewById(R.id.lock_info_near_open);
        near_distance = (AppCompatSeekBar) findViewById(R.id.near_distance);
        btn_select_device = (Button) findViewById(R.id.btn_select_device);
        is_open_space_enable = (Switch) findViewById(R.id.is_open_space_enable);
        et_open_space_second = (EditText) findViewById(R.id.et_open_space_second);

        autoOpenService = new Intent(Act_Near_Open.this,AutoOpenService.class);
        userData = new UserData(mActivity);
        username = SPUtils.getString(Constants.USERNAME,mActivity);
    }

    /**
     * 加载事件
     */
    private void setupView() {
        btn_select_device.setOnClickListener(this);

        title_bar.setTitle(getString(R.string.near_to_open));
        title_bar.setLeftImageResource(R.drawable.left_ac);
        title_bar.setLeftLayoutClickListener(this);
        title_bar.setBackgroundColor(Color.parseColor("#FFFFFF"));

        auto_open = userData.getAutoOpen(username);
        near_open.setChecked(auto_open);
        near_open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                auto_open = isChecked;
                userData.setAutoOpen(username,auto_open);
                int ret = TopkeeperModel.refeshAutoList();
                if (ret == 1) {
                    DialogUtils.showAlert(mActivity, getString(R.string.remind), getString(R.string.please_select_device), getString(R.string.ensure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openActivity(Act_SelectAutoDevice.class);
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            near_open.setChecked(false);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        enable_auto_open_space = userData.isOpenSpaceEnable(username);
        auto_open_space_seconds = userData.getAutoOpenSpaceTime(username);
        is_open_space_enable.setChecked(enable_auto_open_space);
        is_open_space_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enable_auto_open_space = isChecked;
                userData.enableOpenSpace(username,enable_auto_open_space);
                userData.setAutoOpenSpaceTime(username,auto_open_space_seconds);
                TopkeeperModel.enableAutoOpenSpaceTime(enable_auto_open_space, auto_open_space_seconds);
            }
        });
        et_open_space_second.setText(String.valueOf(auto_open_space_seconds));
        et_open_space_second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtils.d(TAG, "s=" + s);
                if (s.length() > 0) {
                    if (s.length() > 7)
                    {
                        auto_open_space_seconds = Integer.valueOf(s.toString().substring(0, 7));
                        et_open_space_second.setText(s.toString().substring(0, 7));
                        ToastUtils.showMessage(Act_Near_Open.this, "已是最大位数了");
                    }else
                    {
                        auto_open_space_seconds = Integer.valueOf(s.toString());
                    }
                } else {
                    auto_open_space_seconds = 0;
                }
                LogUtils.d(TAG, "seconds=" + auto_open_space_seconds);
                userData.enableOpenSpace(username,enable_auto_open_space);
                userData.setAutoOpenSpaceTime(username,auto_open_space_seconds);
                TopkeeperModel.enableAutoOpenSpaceTime(enable_auto_open_space, auto_open_space_seconds);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        near_distance.setMax(Constants.AUTO_MAX_PROGRESS);
        progress = userData.getAutoDistance(username);
        near_distance.setProgress(progress);
        near_distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtils.d(TAG, "onProgress:"+seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                LogUtils.d(TAG, "onStart:"+progress);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
                userData.setAutoDistance(username,progress);
                refeshAutoList();
//                LogUtils.d(TAG, "onStop:"+progress);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.left_layout) {//返回
            finish();

        } else if (i == R.id.btn_select_device) {
            openActivity(Act_SelectAutoDevice.class);

        } else {
        }
    }
}