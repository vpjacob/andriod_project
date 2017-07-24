package com.doormaster.topkeeper.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.adapter.MessageAdapter;
import com.doormaster.topkeeper.bean.MessageBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.MessageData;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class MsgActivity extends BaseActivity {
    //界面设置
    private ListView msgList;
    static MessageAdapter adapter;

    public TitleBar titleBar;
    private static List<MessageBean> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_msglist);
        String sender = getIntent().getStringExtra("sender");
        msgList = (ListView) findViewById(R.id.msg_list);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        if (sender==null) {
            LogUtils.d("no msg");
            return;
        }

        messageList = new ArrayList<>();
        MessageData msgData = new MessageData(BaseApplication.getContext());
        messageList.addAll(msgData.getMsgContent(SPUtils.getString(Constants.USERNAME), sender));

//        String username = SPUtils.getString(Constants.USERNAME);
        adapter = new MessageAdapter(this,messageList);
        msgList.setAdapter(adapter);

        titleBar.setTitle(sender);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
