package com.doormaster.topkeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.Utils;
import com.doormaster.topkeeper.view.TitleBar;


public class Act_About extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_About";
    private TitleBar setting_title_bar;

    public TextView[] textViews = new TextView[2];

    public ImageView iv_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about);
        initView();
        setupView();
    }
    /**
     * 初始化界面
     */
    private void initView() {
        textViews[0] = (TextView) findViewById(R.id.text1);
        textViews[1] = (TextView) findViewById(R.id.text2);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        setting_title_bar = (TitleBar) findViewById(R.id.setting_title_bar);
    }

    /**
     * 加载事件
     */
    private void setupView() {

//        setting_title_bar.setTitle(getString(R.string.about));
//        setting_title_bar.setLeftImageResource(R.mipmap.yoho_close);
        setting_title_bar.setLeftLayoutClickListener(this);
//        setting_title_bar.setBackgroundColor(Color.parseColor("#00000000"));
        textViews[0].setText(getString(R.string.app_name));
        textViews[1].setText(getString(R.string.version_name)+"："+Utils.getVersion(this));
        iv_logo.setImageResource(R.mipmap.ic_logo);
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

        } else {
        }
    }
}
