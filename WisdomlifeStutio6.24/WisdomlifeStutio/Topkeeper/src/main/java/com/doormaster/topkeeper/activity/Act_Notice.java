package com.doormaster.topkeeper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.adapter.NoticeAdapter;
import com.doormaster.topkeeper.bean.NoticeBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.view.DividerItemDecoration;
import com.doormaster.topkeeper.view.TitleBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;

public class Act_Notice extends BaseActivity implements View.OnClickListener {
    private static String TAG = "Act_Announcement";
    private TitleBar setting_title_bar;
    private RecyclerView recyList;

    private List<NoticeBean> infoList;
    private NoticeAdapter noticeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notice);
        initView();
        setupView();
    }
    /**
     * 初始化界面
     */
    private void initView() {
        setting_title_bar = (TitleBar) findViewById(R.id.setting_title_bar);
        recyList = (RecyclerView) findViewById(R.id.recyList);
    }

    /**
     * 加载事件
     */
    private void setupView() {

        setting_title_bar.setTitle(getString(R.string.community_message));
        setting_title_bar.setLeftImageResource(R.mipmap.yoho_close);
        setting_title_bar.setLeftLayoutClickListener(this);
        setting_title_bar.setBackgroundColor(Color.parseColor("#00000000"));

        //recyclerView属性
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyList.setLayoutManager(linearLayoutManager);
        //自定义分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST);
        dividerItemDecoration.setDivider(R.drawable.divider);
        recyList.addItemDecoration(dividerItemDecoration);


        String clientId = SPUtils.getString(Constants.CLIENT_ID, this);
        LogUtils.d(TAG, "clientId=" + clientId);
        infoList = new ArrayList<>();
        OkhttpHelper.getAnnouncementList(new StringCallback() {
            @Override
            public void onResponse(String result) {
                try {
                    LogUtils.d("Act_PlotInfo", "result=" + result);
                    JSONObject ret = new JSONObject(result);
                    if (!ret.optString("msg").equals("ok")) {
                        return;
                    }
                    JSONObject data = ret.optJSONObject("data");
                    if (data == null || data.length() == 0) {
                        return;
                    }

                    JSONArray array = data.getJSONArray("announcement_list");
                    LogUtils.d(TAG, "readlist=" + array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        NoticeBean notice = new NoticeBean();
                        String community_code = item.optString("community_code");
                        String name = item.optString("name");
                        String start_date = item.optString("start_date");
                        String end_date = item.optString("end_date");
                        String content = item.optString("content");
                        notice.setCommunity_code(community_code);
                        notice.setName(name);
                        notice.setStart_date(start_date);
                        notice.setEnd_date(end_date);
                        notice.setContent(content);

                        infoList.add(notice);
                    }
//                    int sum = infoList.size();
//                    String record[] = new String[sum];
//                    for (int j = 0;j<sum;j++) {
//                        record[j] = infoList.get(j).optString("name");
//                    }
                    if (infoList != null && !infoList.isEmpty()) {
                        LogUtils.d(TAG, "设置adapter,size=" + infoList.size());
                        NoticeComparator noticeComparator = new NoticeComparator();

                        Collections.sort(infoList, noticeComparator);
                        noticeAdapter = new NoticeAdapter(Act_Notice.this, infoList);
                        noticeAdapter.setOnItemClickListener(new NoticeAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, NoticeBean item) {
                                String community_code = item.getCommunity_code();
                                String name = item.getName();
                                String start_date = item.getStart_date();
                                String end_date = item.getEnd_date();
                                String content = item.getContent();
                                Intent intent = new Intent(Act_Notice.this,Act_NoticeDetail.class);
                                intent.putExtra("name", name);
                                intent.putExtra("content", content);
                                intent.putExtra("start_date", start_date);
                                startActivity(intent);
                            }
                        });
                        recyList.setAdapter(noticeAdapter);
                    }


                } catch (JSONException e) {
                    Toast.makeText(Act_Notice.this, "获取公告失败", Toast.LENGTH_SHORT).show();
                    LogUtils.d(TAG, "获取数据失败，e=" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call arg0, Exception arg1) {
                Toast.makeText(Act_Notice.this, "获取公告失败", Toast.LENGTH_SHORT).show();
                LogUtils.d(TAG, "获取数据失败，arg1=" + arg1);
            }
        }, clientId);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.left_layout) {//menu
            LogUtils.d(TAG, "点击标题左边按钮");
            finish();

        } else {
        }
    }

    /**
     * According to the announcement time custom comparator
     *
     */
    private class NoticeComparator implements Comparator<NoticeBean> {
        @Override
        public int compare(NoticeBean notice1, NoticeBean notice2) {
            return notice2.getStart_date().compareTo(notice1.getStart_date());
        }
    }
}
