package com.doormaster.topkeeper.activity.device_manager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.adapter.SendKeyAdapter;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.SendKeyRecordBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.constant.TimerMsgConstants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.SendKeyData;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;

public class Act_DeleteKey extends BaseActivity {
	private TextView mTitle;
	private ImageButton mBack;
	private ImageButton mScan;
	private ListView listview;
	private ProgressDialog dialog;
	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendkey_list);
		initView();
		username = SPUtils.getString(Constants.USERNAME);
		dialog = new ProgressDialog(Act_DeleteKey.this);
		dialog.setMessage(getResources().getString(R.string.download_send_key_record));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
		final Intent intent = getIntent();

		String request_url = Constants.URL_POST_COMMANDS;
		JSONObject json = new JSONObject();
		String client_id = BaseApplication.getInstance().getClientId();
		String dev_sn = intent.getStringExtra(AccessDevBean.DEVICE_SN);
		final String dev_mac = intent.getStringExtra(AccessDevBean.DEVICE_MAC);
		AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
		final AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		try {
			json.put(TimerMsgConstants.CLIENT_ID, client_id);
			json.put(TimerMsgConstants.RESOURCE, "key");
			json.put(TimerMsgConstants.OPERATION, "GET");
			json.put(TimerMsgConstants.READER_DEV_SN, dev_sn);
//					JSONObject record_ret = MyHttpClient.connectPost(request_url, json);
			OkhttpHelper.upLoadRecord(request_url, json.toString(), new StringCallback() {
				@Override
				public void onError(Call call, Exception e) {

				}

				@Override
				public void onResponse(String response) {
					try {
						JSONObject record_ret = new JSONObject(response);
						dialog.dismiss();
						LogUtils.d(record_ret.toString());
						if (!record_ret.isNull(TimerMsgConstants.RET))
                        {
                            int ret = record_ret.getInt(TimerMsgConstants.RET);
                            if (ret == 0)
                            {
                                if (!record_ret.isNull(TimerMsgConstants.DATA))
                                {
                                    JSONArray data_record = record_ret.getJSONArray(TimerMsgConstants.DATA);
                                    final ArrayList<SendKeyRecordBean> recordList = getSendKeyRecordList(data_record,device);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            SendKeyAdapter adapter = new SendKeyAdapter(Act_DeleteKey.this, recordList);
                                            listview.setAdapter(adapter);
                                        }
                                    });
                                }
                                return;
                            }
                        }
						LogUtils.d("saveList  network" + dev_mac);
						setRecordListAdapter(dev_mac);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogUtils.d("json error");
		}

		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Act_DeleteKey.this.finish();
			}
		});
	}
	
	private void setRecordListAdapter(final String dev_mac)
	{
		runOnUiThread(new Runnable() {
			public void run() {			
				SendKeyData recordData = new SendKeyData(getApplicationContext());
				ArrayList<SendKeyRecordBean> recordList = recordData.getSendKeyRecordList(username, dev_mac);
				SendKeyAdapter adapter = new SendKeyAdapter(Act_DeleteKey.this, recordList);
				listview.setAdapter(adapter);
			}
		});
	}

	private ArrayList<SendKeyRecordBean> getSendKeyRecordList(
            JSONArray data_recordList, AccessDevBean device) throws JSONException {
		ArrayList<SendKeyRecordBean> record_list = new ArrayList<SendKeyRecordBean>();
		SendKeyData recordData = new SendKeyData(getApplicationContext());
		recordData.clearAllData(username, device.getDevMac());
		for (int i = 0; i < data_recordList.length() ;i++) 
		{			
			JSONObject data_record = data_recordList.getJSONObject(i);
			SendKeyRecordBean record = new SendKeyRecordBean();
			String description = data_record.getString(TimerMsgConstants.KEY_REMARK);
			String start_date = data_record.getString(TimerMsgConstants.KEY_START_DATE);
			String end_date = data_record.getString(TimerMsgConstants.KEY_END_DATE);
			String limit_time = "";
			if (start_date != null && !start_date.isEmpty()
					&& end_date != null && !end_date.isEmpty()) 
			{				
				limit_time = getLocaleTime(start_date) + "-/n" + getLocaleTime(end_date);
			}
			//发送时间
			String send_time = data_record.getString(TimerMsgConstants.KEY_CREATE_DATE);
			if (send_time != null && !send_time.isEmpty()) {
				long send_long = Long.parseLong(send_time);
				send_long = send_long + 8 * 3600;
				send_time = getLocaleTime(Long.toString(send_long));
			}
			//接收者
			String receiver = data_record.getString(TimerMsgConstants.KEY_RECIEVER);
			record.setDevMac(device.getDevMac());
			record.setLimitTime(limit_time);
			record.setReceiver(receiver);
			record.setSendTime(send_time);
			record.setDescription(description);
			record.setSender(username);
			record_list.add(record);
			recordData.saveData(record);
		}
		return record_list;
	}
	
	private String getLocaleTime(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
		SimpleDateFormat sdf_server = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		try {
			Date date_server = sdf_server.parse(date);
			date = sdf.format(date_server);
		} catch (ParseException e) {
			LogUtils.d("error date format");
		}
		return date;
	}

	private void initView() {
		mBack = (ImageButton) findViewById(R.id.ib_frag_back_img);
		mTitle = (TextView) findViewById(R.id.ib_frag_title);
		mScan = (ImageButton) findViewById(R.id.ib_activity_scan_add);
		listview = (ListView) findViewById(R.id.sendkey_list);
		
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.mn_ly_lock_delete_title);
		mScan.setVisibility(View.INVISIBLE);
	}
}
