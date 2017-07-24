package com.doormaster.topkeeper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.view.TitleBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Act_NoticeDetail extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_AnnounceDetail";
    private TitleBar title_bar;

    private SimpleDateFormat sdf_datetime;
    private SimpleDateFormat sdf_date;
    private SimpleDateFormat sdf_time;
    private Date date;

    private TextView tv_content;
    private TextView tv_title;
    private TextView tv_start_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notice_detail);
        initView();
        setupView();
    }
    /**
     * 初始化界面
     */
    private void initView() {
        title_bar = (TitleBar) findViewById(R.id.title_bar);

        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_start_date = (TextView) findViewById(R.id.tv_start_date);

        sdf_datetime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        sdf_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf_time = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
    }

    /**
     * 加载事件
     */
    private void setupView() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String content = intent.getStringExtra("content");
        String start_date = intent.getStringExtra("start_date");

//        title_bar.setTitle(getString(R.string.notice));
        title_bar.setTitle(name);
        title_bar.setLeftImageResource(R.mipmap.yoho_close);
        title_bar.setLeftLayoutClickListener(this);
        title_bar.setBackgroundColor(Color.parseColor("#00000000"));

        //处理日期和时间
        String dateStr;
        String timeStr;
        try {
            date = sdf_datetime.parse(start_date);
            dateStr = sdf_date.format(date);
            timeStr = sdf_time.format(date);
        } catch (ParseException e) {
            LogUtils.w(TAG, "Server date format not available");
            dateStr = sdf_date.format(date);
            timeStr = sdf_time.format(date);
            e.printStackTrace();
        }

        tv_content.setText(content);
        tv_start_date.setText(dateStr + " " + timeStr);
        tv_title.setText(name);
        tv_title.setVisibility(View.GONE);
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
