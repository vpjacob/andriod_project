package com.doormaster.topkeeper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class Act_Device_Details extends BaseActivity implements View.OnClickListener{
    private static String TAG = "Act_DeviceList";

    private TitleBar setting_title_bar;

//    @BindViews({com.doormaster.topkeeper.R.id.tv_type, com.doormaster.topkeeper.R.id.tv_serial, com.doormaster.topkeeper.R.id.tv_name, com.doormaster.topkeeper.R.id.tv_effective_date})
    public List<TextView> textViews;
    public String[] strs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.doormaster.topkeeper.R.layout.act_device_details);
        initView();
        initData();
        setupView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        textViews = new ArrayList<>();
        textViews.add((TextView) findViewById(R.id.tv_type));
        textViews.add((TextView) findViewById(R.id.tv_serial));
        textViews.add((TextView) findViewById(R.id.tv_name));
        textViews.add((TextView) findViewById(R.id.tv_effective_date));
        setting_title_bar = (TitleBar) findViewById(com.doormaster.topkeeper.R.id.setting_title_bar);
    }

    private void initData() {
        strs = new String[4];
        Intent intent = getIntent();
        strs[0] = intent.getStringExtra("type");
        strs[1] = intent.getStringExtra("serial");
        strs[2] = intent.getStringExtra("name");
        strs[3] = intent.getStringExtra("effective_date");
    }
    /**
     * 加载事件
     */
    private void setupView() {

        setting_title_bar.setTitle(getString(com.doormaster.topkeeper.R.string.about_device));
        setting_title_bar.setLeftImageResource(com.doormaster.topkeeper.R.mipmap.yoho_close);
        setting_title_bar.setLeftLayoutClickListener(this);
        setting_title_bar.setBackgroundColor(Color.parseColor("#00000000"));
        for (int i =0;i<textViews.size();i++) {
            textViews.get(i).setText(strs[i]);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.left_layout) {//返回
            LogUtils.d(TAG, "点击标题左边按钮");
            finish();

        } else {
        }

    }
}
