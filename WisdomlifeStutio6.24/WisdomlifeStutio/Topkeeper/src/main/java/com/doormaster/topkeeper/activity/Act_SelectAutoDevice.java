package com.doormaster.topkeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.adapter.SelectAutoDeviceAdapter;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.topkeeper.view.TitleBar;

import java.util.List;

public class Act_SelectAutoDevice extends BaseActivity implements View.OnClickListener {


    public TitleBar titleBar;

    public Button[] btnList = new Button[3];

    private ListView listView;

    private List<AccessDevBean> mDatas;

    private SelectAutoDeviceAdapter mAdapter;
    private String username;

    private AccessDevMetaData deviceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_auto_device);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        btnList[0] = (Button) findViewById(R.id.btn_select_all);
        btnList[1] = (Button) findViewById(R.id.btn_select_none);
        btnList[2] = (Button) findViewById(R.id.btn_select_opposite);
        listView = (ListView) findViewById(R.id.listView);

        titleBar.setLeftLayoutClickListener(this);
        deviceData = new AccessDevMetaData(getApplicationContext());
        username = SPUtils.getString(Constants.USERNAME);
        mDatas = deviceData.getAllAccessDevList(username);

        if (mDatas == null || mDatas.size() == 0)
        {
            Toast.makeText(currentActivity, "当前账号暂无设备", Toast.LENGTH_LONG).show();
            return;
        }
        mAdapter = new SelectAutoDeviceAdapter(this, mDatas);
        listView.setAdapter(mAdapter);

        for (Button btn : btnList) {
            btn.setOnClickListener(this);
        }
    }

    public void selectAllList() {
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setAutoOpen(1);
        }

        mAdapter.notifyDataSetChanged();
    }

    public void selectNoList() {

        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setAutoOpen(0);
        }

        mAdapter.notifyDataSetChanged();
    }

    public void selectOppsiteList() {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getAutoOpen() == 1) {
                mDatas.get(i).setAutoOpen(0);
            } else {
                mDatas.get(i).setAutoOpen(1);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    public void operateList() {

        if (mDatas == null || mDatas.size() == 0)
        {
            return;
        }
        for (int i = 0; i < mDatas.size(); i++) {
            deviceData.saveDeviceAutoOpen(mDatas.get(i));
            LogUtils.e("Act_SelectDevice", mDatas.get(i).getDevName()+":"+mDatas.get(i).getAutoOpen());
        }
        TopkeeperModel.refeshAutoList();
//        Log.e("TAG", ids.toString());
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.left_layout) {
            operateList();
            finish();

        } else if (i == R.id.btn_select_all) {
            changeColor(0);
            selectAllList();
            operateList();

        } else if (i == R.id.btn_select_none) {
            changeColor(1);
            selectNoList();
            operateList();

        } else if (i == R.id.btn_select_opposite) {
            changeColor(2);
            selectOppsiteList();
            operateList();

        }
    }

    private void changeColor(int index)
    {
        for (int i = 0; i < btnList.length; i++)
        {
            if (i == index)
            {
                btnList[i].setTextColor(getResources().getColor(R.color.wisdomlife_blue_color));
            }else
            {
                btnList[i].setTextColor(getResources().getColor(R.color.gray_text_color));
            }
        }
    }

    @Override
    public void onBackPressed() {
        operateList();
        finish();
    }
}
