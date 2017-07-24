package com.doormaster.topkeeper.activity.device_manager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.utils.DmUtil;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.RequestTool;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface.ManagerCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SerialCardManageActivity extends BaseActivity implements Callback {

	private ListView mListView = null;
	
	private AccessDevBean mDevice = null;
	
	private String mLastTime  = null;
	
	private String mCmdStatus = null;
	
	private String mDevSn = null;
	
	private Button mBtSerialStart = null;
	
	private TextView mProBarInfo = null;
	
	private ProgressBar mProgressBar = null;
	
	private ArrayList<String> mCardData = null;
	
	private static final int NEXT_DATA = 0x00;
	
	private static final int UPDATE_ERROR = 0x01;
	
	private static final int SYNC_DATA_NULL = 0x02;
	
	private static final int SYNC_DATA_CHECK = 0x03;
	
	private static final int SYNC_DATA_UPDATE = 0x04;
	
	private static int reconnect_count = 0;
	
	private Handler mHandler = new Handler(this);

	private String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_card_serial_manager);
		
		initTileView();
		
		initView();
	}
	
	private void initTileView() {
		
		ImageButton btBack =(ImageButton) findViewById(R.id.ib_frag_back_img);
		TextView tvTitle = (TextView) findViewById(R.id.ib_frag_title);
		ImageButton btFind =(ImageButton) findViewById(R.id.ib_activity_scan_add);
		
		tvTitle.setText(R.string.card_manage_batch_manage);
		btBack.setVisibility(View.VISIBLE);
		btFind.setVisibility(View.INVISIBLE);
		btBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SerialCardManageActivity.this.finish();
			}
		});
	}
	
	/*
	bundle.putString("last_time", last_time);
	bundle.putString("cmd_status", model);
	bundle.putStringArrayList("cardData",cardList);
	bundle.putString("devSn", mDevice.getDevSn());
	 */
	
	private void initView()
	{
		mListView = (ListView) findViewById(R.id.list_card_view);
		mProgressBar = (ProgressBar) findViewById(R.id.sync_proBar);
		mProBarInfo = (TextView) findViewById(R.id.tv_sync_state);
		mBtSerialStart = (Button) findViewById(R.id.bt_serial_start);
		
		Bundle bundle = getIntent().getBundleExtra("data");
		mLastTime =  bundle.getString("last_time");
		mCmdStatus = bundle.getString("cmd_status");
		mCardData = bundle.getStringArrayList("cardData");
		mDevSn = bundle.getString("devSn");
		//获取设备
		AccessDevMetaData devData = new AccessDevMetaData(SerialCardManageActivity.this);
		username = SPUtils.getString(Constants.USERNAME);
		mDevice = devData.queryAccessDeviceByDevSn(username, mDevSn);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SerialCardManageActivity.this,
				R.layout.serialcard_list, R.id.serialcard_textView, mCardData);
		
		mListView.setAdapter(adapter);
		
		
		
		if (mCardData != null && mCardData.size() > 0) 
		{			
			mProgressBar.setMax(mCardData.size());
			modifyProgressBar(0);
		}
	}
	
	
	public void syncDataStart(View view)
	{
		List<String> data = null;
		
		if (mCardData.size() <= 0)
		{
			Toast.makeText(SerialCardManageActivity.this, R.string.card_manage_batch_sync_finished, Toast.LENGTH_SHORT).show();
		}
		if (mCardData.size() > 59)
		{
			//exclude (i + 59) data
			data = mCardData.subList(0, 59);
		}
		else 
		{
			data = mCardData.subList(0, mCardData.size());
		}
		mBtSerialStart.setText(R.string.card_manage_batch_synchronizing);
		mBtSerialStart.setClickable(false);
		syncData(0, data);
	}
	
	//更新数据
	public void syncData(final int i,List<String> cardData)
	{
		ManagerCallback callback = new ManagerCallback() {
			
			@Override
			public void setResult(int result, Bundle bundle) {
				if (result == 0) 
				{
					Message msg = new Message();
					msg.what = NEXT_DATA;
					msg.obj = (i + 59);
					mHandler.sendMessage(msg);
				}
				else if (result == 48)
				{
					reconnect_count = reconnect_count + 1;
					if (reconnect_count < 10) 
					{						
						Message msg = new Message();
						msg.what = NEXT_DATA;
						msg.obj = i;
						mHandler.sendMessage(msg);
						return;
					}
					else 
					{
						Message msg = new Message();
						msg.what = UPDATE_ERROR;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				}
				else
				{
					Message msg = new Message();
					msg.what = UPDATE_ERROR;
					msg.obj = result;
					mHandler.sendMessage(msg);
					LogUtils.d(" SerialCardManager Result" + result);
				}
				reconnect_count = 0;
			}
		};
		if (mCmdStatus.equals("add")) 
		{			
			int ret = LibDevModel.writeCard(SerialCardManageActivity.this, DmUtil.getLibDev(mDevice), cardData, callback, true);
		}
		else 
		{
			int ret = LibDevModel.deleteCard(SerialCardManageActivity.this, DmUtil.getLibDev(mDevice), cardData, callback);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case NEXT_DATA:
			int next = (Integer) msg.obj;
			if (next >= mCardData.size()) 
			{
				//1、完成通知
				mBtSerialStart.setText(R.string.card_manage_batch_sync_finished);
				modifyProgressBar(mCardData.size());
				//2、回馈服务器数据
				getUpdate();
			}
			else 
			{
				//1、修改进度条
				modifyProgressBar(next);
				List<String> data  = null;
				LogUtils.d("********************next:*********"+ next +"cardSize:"+ mCardData.size());
				if (next + 59 <= mCardData.size()) 
				{
					data = mCardData.subList(next, next + 59);
				}
				else 
				{
					data = mCardData.subList(next, mCardData.size());
				}
				syncData(next, data);
				
			}
			break;
		case UPDATE_ERROR:
			int result = (Integer) msg.obj;
			mBtSerialStart.setText(R.string.card_manage_batch_sync_finished);
			ToastUtils.tips(result);
			break;
		case SYNC_DATA_NULL:
			Toast.makeText(SerialCardManageActivity.this, R.string.card_manage_batch_sync_finished, Toast.LENGTH_SHORT).show();
			break;
		case SYNC_DATA_CHECK:
			reqServer(mCmdStatus);
			break;
		case SYNC_DATA_UPDATE:
			resetView();
			syncCheckDataStart();
			break;
		default:
			break;
		}
		return false;
	}
	
	//同步更新检查到的数据
	public void syncCheckDataStart()
	{
		List<String> data = null;
		
		if (mCardData.size() <= 0)
		{
			Toast.makeText(SerialCardManageActivity.this, R.string.card_manage_batch_synchronizing, Toast.LENGTH_SHORT).show();
		}
		if (mCardData.size() > 59)
		{
			//exclude (i + 59) data
			data = mCardData.subList(0, 59);
		}
		else 
		{
			data = mCardData.subList(0, mCardData.size());
		}
		mBtSerialStart.setText(getResources().getString(R.string.activity_device_synchronizeing));
		mBtSerialStart.setClickable(false);
		syncData(0, data);
	}
	
	//重置界面
	private void resetView()
	{
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SerialCardManageActivity.this,
				android.R.layout.simple_expandable_list_item_1, mCardData);
		
		mListView.setAdapter(adapter);
		
		if (mCardData != null && mCardData.size() > 0) 
		{			
			mProgressBar.setMax(mCardData.size());
			modifyProgressBar(0);
		}
	}
	
	//更新服务器
	private void getUpdate()
	{
		try {
			 RequestTool.updateServiceCardList(mDevSn, mLastTime, mCmdStatus, new StringCallback() {
				 @Override
				 public void onError(Call call, Exception e) {

				 }

				 @Override
				 public void onResponse(String response) {
					 try {
						 JSONObject update_ret = new JSONObject(response);
						 if (update_ret.isNull("ret"))
						 {
							 return;
						 }
						 int ret = update_ret.getInt("ret");
						 if (ret == 0)
						 {
							 LogUtils.d("更新卡完成");
							 mHandler.sendEmptyMessage(SYNC_DATA_CHECK);
						 }
						 else
						 {
							 LogUtils.d("update sever card list failed" + update_ret.toString());
						 }

					 } catch (JSONException e) {
						 e.printStackTrace();
					 }
				 }
			 });


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogUtils.d("error:" + e.getMessage());
		}
	}

	//请求服务器
	public void reqServer(final String model)
	{
		try {
			RequestTool.getCardList(mDevice.getDevSn(), model, new StringCallback() {
				@Override
				public void onError(Call call, Exception e) {

				}

				@Override
				public void onResponse(String response) {
					try {
						JSONObject card_req_ret = new JSONObject(response);
						if (!card_req_ret.isNull("ret"))
						{
							int ret = card_req_ret.getInt("ret");
							if (ret == 0 )
							{
								LogUtils.d(card_req_ret.toString());
								if (card_req_ret.isNull("data") || card_req_ret.getJSONObject("data").isNull("card_list"))
								{
									mHandler.sendEmptyMessage(SYNC_DATA_NULL);
									return;
								}
								JSONObject data = card_req_ret.getJSONObject("data");
								String last_time = data.getString("last_time");
								JSONArray jsonCardlist = data.getJSONArray("card_list");
								ArrayList<String> cardList = new ArrayList<String>();
								for (int i = 0; i < jsonCardlist.length(); i++)
								{
									cardList.add(jsonCardlist.getString(i));
								}
								//这里的数据用于跳转，必须设置
								Bundle bundle = new Bundle();
								bundle.putString("last_time", last_time);
								mLastTime = last_time;
								mCmdStatus =  model;
								mCardData = cardList;


								Message msg = new Message();
								msg.what = SYNC_DATA_UPDATE;
								mHandler.sendMessage(msg);
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//修改设备进度条
	private void modifyProgressBar(int currentValue) 
	{
		String info = currentValue + "/" + mCardData.size();
		
		mProBarInfo.setText(info);
		
		mProgressBar.setProgress(currentValue);
	}
	
 
}
