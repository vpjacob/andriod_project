package com.doormaster.topkeeper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.view.TitleBar;


public class Act_Setting extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_Setting";
    private TitleBar setting_title_bar;
    private RelativeLayout rv_shake;
    private RelativeLayout rv_near;
    private RelativeLayout rv_about;
    private RelativeLayout rv_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);
        initView();
        setupView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        setting_title_bar = (TitleBar) findViewById(R.id.setting_title_bar);
        rv_shake = (RelativeLayout) findViewById(R.id.rv_shake);
        rv_near = (RelativeLayout) findViewById(R.id.rv_near);
        rv_about = (RelativeLayout) findViewById(R.id.rv_about);
        rv_user = (RelativeLayout) findViewById(R.id.rv_user);
    }

    /**
     * 加载事件
     */
    private void setupView() {

        setting_title_bar.setTitle(getString(R.string.setting));
        setting_title_bar.setLeftImageResource(R.drawable.left_ac);
        setting_title_bar.setLeftLayoutClickListener(this);
        setting_title_bar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        rv_shake.setOnClickListener(this);
        rv_near.setOnClickListener(this);
        rv_about.setOnClickListener(this);
        rv_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.left_layout) {
            LogUtils.d(TAG, "点击标题左边按钮");
            finish();

        } else if (i == R.id.rv_shake) {
            LogUtils.d(TAG, "摇一摇开门");
            Intent shake_intent = new Intent(Act_Setting.this, Act_Shake.class);
            startActivity(shake_intent);

        } else if (i == R.id.rv_near) {
            LogUtils.d(TAG, "靠近开门");
            Intent near_intent = new Intent(Act_Setting.this, Act_Near_Open.class);
            startActivity(near_intent);

        } else if (i == R.id.rv_about) {
            LogUtils.d(TAG, "关于");
            Intent about_intent = new Intent(Act_Setting.this, Act_About.class);
            startActivity(about_intent);

        } else if (i == R.id.rv_user) {
            LogUtils.d(TAG, "用户");
            Intent userinfo_intent = new Intent(Act_Setting.this, Act_Userinfo.class);
            startActivity(userinfo_intent);

        } else {
        }
    }
}
