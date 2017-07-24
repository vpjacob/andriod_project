package com.doormaster.topkeeper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.adapter.VistorChoiceDeviceAdapter;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.view.DividerItemDecoration;
import com.doormaster.topkeeper.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class Act_VisitorPassDevice extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_VisitorPassDevice";
    public TitleBar title_bar;

    public RecyclerView recyList;

    private List<AccessDevBean> accessDevList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Object[] objects = (Object[]) msg.obj;
            ((TextView)objects[1]).setTextColor(getResources().getColor(com.doormaster.topkeeper.R.color.yoho_grey));
            ((TextView)objects[2]).setTextColor(getResources().getColor(com.doormaster.topkeeper.R.color.yoho_grey));
            switch (msg.what) {
                case 0:
                    ((ImageView)objects[0]).setImageResource(com.doormaster.topkeeper.R.mipmap.yoho_device_grey);
                    break;
                case 1:
                    ((ImageView)objects[0]).setImageResource(com.doormaster.topkeeper.R.mipmap.yoho_device2_grey);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.doormaster.topkeeper.R.layout.act_visitor_pass_device);
        initView();
        initData();
        setuptView();
    }

    /**
     * Initialization interface
     */
    private void initView() {
        title_bar = (TitleBar) findViewById(R.id.visitorpass_title_bar);
        recyList = (RecyclerView) findViewById(R.id.recyList);
        accessDevList = new ArrayList<>();
    }

    /**
     * Initialization data
     */
    private void initData() {
//        mListView = (ListView)findViewById(R.id.dm_authorized_list);
//        DBBiz mDbDao= DBBiz.getInstanter(getApplication(), Constants.DB_NAME, 1);
        AccessDevMetaData deviceData = new AccessDevMetaData(getApplication());
        String username = SPUtils.getString(Constants.USERNAME);
        ArrayList<AccessDevBean> allDevList = deviceData.getAllAccessDevList(username);
        if (allDevList == null)
        {
            Toast.makeText(currentActivity, com.doormaster.topkeeper.R.string.no_permission,Toast.LENGTH_SHORT).show();
            return;
        }
//        for (int i = 0; i < allDevList.size(); i++) //筛选出一体机和二维码设备
//        {
//            int devType = allDevList.get(i).getDevType();
//            if (devType == AccessDevBean.DEV_TYPE_ACCESS_CONTROLLER || devType == AccessDevBean.DEV_TYPE_QCCODE_DEVICE ||
//                    devType == AccessDevBean.DEV_TYPE_QRCODE_DEVICE || devType == AccessDevBean.DEV_TYPE_DM_DEVICE ||
//                    devType == AccessDevBean.DEV_TYPE_M260_WIFI_ACCESS_DEVICE || devType == AccessDevBean.DEV_TYPE_M200_WIFI_ACCESS_DEVICE) {
//
//                accessDevList.add(allDevList.get(i));
//            }
//        }

        for (AccessDevBean device : allDevList) {
            int devType = device.getDevType();
            if (devType == AccessDevBean.DEV_TYPE_ACCESS_CONTROLLER
                    || devType == AccessDevBean.DEV_TYPE_QCCODE_DEVICE
                    || devType == AccessDevBean.DEV_TYPE_QRCODE_DEVICE
                    || devType == AccessDevBean.DEV_TYPE_DM_DEVICE
                    || devType == AccessDevBean.DEV_TYPE_M260_WIFI_ACCESS_DEVICE
                    || devType == AccessDevBean.DEV_TYPE_M200_WIFI_ACCESS_DEVICE) {

                accessDevList.add(device);
            }
        }

        if (accessDevList == null || accessDevList.size() == 0)
        {
            Toast.makeText(currentActivity, com.doormaster.topkeeper.R.string.no_permission,Toast.LENGTH_SHORT).show();
            return;
        }



    }

    /**
     * Loading event
     */
    private void setuptView() {

        title_bar.setTitle(getString(com.doormaster.topkeeper.R.string.visitor_pass));
        title_bar.setLeftImageResource(com.doormaster.topkeeper.R.mipmap.yoho_close);
        title_bar.setLeftLayoutClickListener(this);
        title_bar.setBackgroundColor(Color.parseColor("#00000000"));

        //recyclerView属性
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyList.setLayoutManager(linearLayoutManager);
        //自定义分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST);
        dividerItemDecoration.setDivider(com.doormaster.topkeeper.R.drawable.divider);
        recyList.addItemDecoration(dividerItemDecoration);

        if (!accessDevList.isEmpty()) {
            VistorChoiceDeviceAdapter deviceAdapter = new VistorChoiceDeviceAdapter(Act_VisitorPassDevice.this, accessDevList);
            deviceAdapter.setOnItemClickListener(new VistorChoiceDeviceAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, Object data, final ImageView iv_item, final TextView tv_item_title, final TextView tv_item_content) {


                    tv_item_title.setTextColor(getResources().getColor(com.doormaster.topkeeper.R.color.yoho_orange));
                    tv_item_content.setTextColor(getResources().getColor(com.doormaster.topkeeper.R.color.yoho_orange));
                    //点亮
                    iv_item.setImageResource(com.doormaster.topkeeper.R.mipmap.yoho_device2_orange);

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = new Object[]{iv_item,tv_item_title,tv_item_content};
                    handler.sendMessageDelayed(msg,300);
                    LogUtils.d(TAG, "data="+data);

                    AccessDevBean accessDevBean = (AccessDevBean) data;
                    Intent intent = getIntent();
                    intent.putExtra("dev_sn", accessDevBean.getDevSn());
                    setResult(RESULT_OK,intent);
                    finish();
                }

                @Override
                public void onDetailClick(Object data) {
                    //进入详情页
                    LogUtils.d(TAG,"进入详情页："+data);
                    String strs[] = new String[4];

                    AccessDevBean device = (AccessDevBean) data;
                    strs[0] = getString(com.doormaster.topkeeper.R.string.entrance_guard_machine);
                    strs[1] = device.getDevSn();
                    strs[2] = device.getDevName();
                    strs[3] = device.getEndDate();

                    Intent intent = new Intent(currentActivity, Act_Device_Details.class);
                    intent.putExtra("type", strs[0]);
                    intent.putExtra("serial", strs[1]);
                    intent.putExtra("name", strs[2]);
                    intent.putExtra("effective_date", strs[3]);
                    startActivity(intent);
                }
            });
            recyList.setAdapter(deviceAdapter);
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
