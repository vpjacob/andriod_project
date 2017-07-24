package com.doormaster.topkeeper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.view.TitleBar;


public class Act_Userinfo extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_Userinfo";
    private TitleBar setting_title_bar;

    private EditText et_nick;
    private EditText et_email;

    private TextView tv_modify_psd;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_userinfo);
        initView();
        setupView();
    }
    /**
     * Initialization interface
     */
    private void initView() {
        setting_title_bar = (TitleBar) findViewById(R.id.setting_title_bar);
        et_nick = (EditText) findViewById(R.id.et_nick);
        et_email = (EditText) findViewById(R.id.et_email);
        tv_modify_psd = (TextView) findViewById(R.id.tv_modify_psd);
    }

    /**
     * Loading event
     */
    private void setupView() {

        setting_title_bar.setTitle(getString(R.string.user));
        setting_title_bar.setLeftImageResource(R.mipmap.yoho_close);
        setting_title_bar.setRightLayoutClickListener(this);
        setting_title_bar.setLeftLayoutClickListener(this);
        setting_title_bar.setBackgroundColor(Color.parseColor("#00000000"));

        tv_modify_psd.setOnClickListener(this);

        String nickname = SPUtils.getString(Constants.NICKNAME, this);
        username = SPUtils.getString(Constants.USERNAME, this);
        et_nick.setText(nickname);
        et_email.setText(username);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.left_layout) {//返回
            LogUtils.d(TAG, "点击标题左边按钮");
            finish();

        } else if (i == R.id.right_layout) {//确定，保存设置退出
            LogUtils.d(TAG, "点击标题右边按钮");
            finish();

        } else if (i == R.id.rv_shake) {
            LogUtils.d(TAG, "摇一摇开门");

        } else if (i == R.id.tv_modify_psd) {//修改密码
            Intent intent = new Intent(Act_Userinfo.this, Act_Modify_Psd.class);
            intent.putExtra("username", username);
            openActivity(intent);

        } else {
        }
    }
}
