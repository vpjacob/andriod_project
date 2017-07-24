package com.doormaster.topkeeper.activity.device_manager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.activity.InputManageCard;
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

import okhttp3.Call;

public class Act_CardManage extends BaseActivity implements Callback {
	
	private static final int SWIPE_ADD = 0x01;
	
	private static final int SWIPE_DEL = 0x02;
	
	private static final int SWIPE_EXIT = 0x03;
	
	private static final int SERIAL_OPERATION = 0x04;
	
	private static final int SYNC_DATA_NULL = 0x05;
	
	private static final int REQUEST_CODE = 0x0f;
	
	private static final String DATA = "data";
	
	private static boolean isAdd = true;
	
	private Button mSwipeAdd = null;
	
	private Button mSwipeDel = null;
	
	private Button mSerialAdd = null;
	
	private Button mSerialDel = null;
	
	private Button mExit = null;
	
	private Button mInputCard = null;
	
	private AccessDevBean mDevice = null;
	
	private Handler handler =  new Handler(this);

	private String username;
	/*serializable/Parcelable to get device
	public class MyParcelable implements Parcelable {
		private int mData;
	
	    public int describeContents() {
	        return 0;
	    }
		
		public void writeToParcel(Parcel out, int flags) {
		    out.writeInt(mData);
		}
		
		public static final Parcelable.Creator<MyParcelable> CREATOR
		        = new Parcelable.Creator<MyParcelable>() {
		    public MyParcelable createFromParcel(Parcel in) {
		        return new MyParcelable(in);
		}
		
		public MyParcelable[] newArray(int size) {
		         return new MyParcelable[size];
		    }
		};
		 
		private MyParcelable(Parcel in) {
		    mData = in.readInt();
		}
	}
	 * */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_card_manager);
		
		initTileView("卡片管理");
		
		Intent intent = getIntent();

		//troditional
		String devSn = intent.getStringExtra(AccessDevBean.DEVICE_SN);
		String devMac = intent.getStringExtra(AccessDevBean.DEVICE_MAC);
		AccessDevMetaData AccessDevMetaData = new AccessDevMetaData(BaseApplication.getInstance());
		username = SPUtils.getString(Constants.USERNAME);
		if (username != null && username.length() > 0 && devSn != null && devSn.length() > 0) {
			mDevice = AccessDevMetaData.queryAccessDeviceByDevSn(username, devSn);
		} else {
			LogUtils.e("devSn is none");
			finish();
		}
		
		initView();
	}
	
	private void initTileView(String title) {
		
		ImageButton btBack =(ImageButton) findViewById(R.id.ib_frag_back_img);
		TextView tvTitle = (TextView) findViewById(R.id.ib_frag_title);
		ImageButton btFind =(ImageButton) findViewById(R.id.ib_activity_scan_add);
		
		tvTitle.setText(getResources().getString(R.string.activity_device_manage_card));
		btBack.setVisibility(View.VISIBLE);
		btFind.setVisibility(View.INVISIBLE);
		btBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Act_CardManage.this.finish();
			}
		});
	}

	/**
	 * 
	 * 初始化界面
	 * 
	 * */
	private void initView()
	{
		mSwipeAdd = (Button) findViewById(R.id.bt_swipe_add);
		
		mSwipeDel = (Button) findViewById(R.id.bt_swipe_del);
		
		mSerialAdd = (Button) findViewById(R.id.bt_serial_add);
		
		mSerialDel = (Button) findViewById(R.id.bt_serial_del);
		
		mExit = (Button) findViewById(R.id.bt_exit_swipe);
		
		mInputCard = (Button) findViewById(R.id.bt_input_manager);
		
		if(mDevice != null && (mDevice.getDevType() == AccessDevBean.DEV_TYPE_DM_DEVICE || mDevice.getDevType() == AccessDevBean.DEV_TYPE_M260_WIFI_ACCESS_DEVICE))
		{
			mSwipeAdd.setText(getResources().getString(R.string.card_manage_swipe_add_loss));
			mSwipeDel.setText(getResources().getString(R.string.card_manage_swipe_del_loss));
			mSerialAdd.setText(getResources().getString(R.string.card_manage_batch_add_loss));
			mSerialDel.setText(getResources().getString(R.string.card_manage_batch_del_loss));
			mInputCard.setText(getResources().getString(R.string.card_manage_input_manage_loss));
		}
		
//		if(ContentUtils.isKTZApp()) //开拓者屏蔽批量增加，批量删除功能
//		{
//			mSerialAdd.setVisibility(View.GONE);
//			mSerialDel.setVisibility(View.GONE);
//		}
		
	}
	
	/**
	 * 
	 * 刷卡添加
	 * 
	 * */
	public void swipeAdd(View view)
	{
		ManagerCallback callback = new ManagerCallback() {
			
			@Override
			public void setResult(final int result, Bundle bundle) {
				if (result == 0)
				{
					handler.sendEmptyMessage(SWIPE_ADD);
				}
				else 
				{					
					dealOperateResult(result);
				}
			}
		};
		LibDevModel.controlDevice(Act_CardManage.this, 0x06, DmUtil.getLibDev(mDevice), null, callback);
	}
	
	/**
	 * 
	 * 刷卡删除
	 * 
	 * */
	public void swipeDel(View view)
	{
		ManagerCallback callback = new ManagerCallback() {
			
			@Override
			public void setResult(int result, Bundle bundle) {
				if (result == 0)
				{
					handler.sendEmptyMessage(SWIPE_DEL);
				}
				else 
				{					
					dealOperateResult(result);
				}
			}
		};
		LibDevModel.controlDevice(Act_CardManage.this, 0x07, DmUtil.getLibDev(mDevice), null, callback);
	}
	
	/**
	 * 
	 * 退出刷卡模式
	 * 
	 * */
	public void exitSwipe(View view)
	{
		ManagerCallback callback = new ManagerCallback() {
			
			@Override
			public void setResult(int result, Bundle bundle) {
				if (result ==0)
				{
					handler.sendEmptyMessage(SWIPE_EXIT);
				}
				else 
				{
					dealOperateResult(result);
				}
			}
		};
		if (isAdd) 
		{			
			LibDevModel.controlDevice(Act_CardManage.this, 0x08, DmUtil.getLibDev(mDevice), null, callback);
		}
		else 
		{
			int ret = LibDevModel.controlDevice(Act_CardManage.this, 0x09, DmUtil.getLibDev(mDevice), null, callback);
		}
	}
	
	/**
	 * 
	 * 同步增加按钮
	 * 
	 * */
	public void serialAdd(View view)
	{
		reqServer("add");
	}
	
	/**
	 * 
	 * 同步删除按钮
	 * 
	 * */
	public void serialDel(View view)
	{
		reqServer("del");
	}
	
	
	//请求服务器
	public void reqServer(final String model)
	{
		Thread serial_add_th = new Thread(new Runnable()
		{
			public void run() 
			{
				try {
					RequestTool.getCardList(mDevice.getDevSn(), model,new StringCallback() {
						@Override
						public void onError(Call call, Exception e) {

						}

						@Override
						public void onResponse(String response) {
							try {
								JSONObject card_req_ret = new JSONObject(response);

								if (card_req_ret.length() > 0) {
									LogUtils.d("method:backupDevEkey,Backup_ret:"
											+ card_req_ret.toString());
									if (!card_req_ret.isNull("ret"))
									{
										int ret = card_req_ret.getInt("ret");
										if (ret == 0 )
										{
											LogUtils.d(card_req_ret.toString());
											if (card_req_ret.isNull("data") || card_req_ret.getJSONObject("data").isNull("card_list"))
											{
												handler.sendEmptyMessage(SYNC_DATA_NULL);
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
											bundle.putString("cmd_status", model);
											bundle.putStringArrayList("cardData",cardList);
											bundle.putString("devSn", mDevice.getDevSn());

											Message msg = new Message();
											msg.what = SERIAL_OPERATION;
											msg.setData(bundle);
											handler.sendMessage(msg);
										}
									}
								} else {
									LogUtils.d("getCardList no data");
//							return 10061; //
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
		});
		serial_add_th.start();
	}
	/**
	 * 输入增删卡
	 * */
	public void inputCardnoView(View view)
	{
		//跳转界面
		if (mDevice == null) 
		{
			return;
		}
		Intent intent = new Intent(Act_CardManage.this, InputManageCard.class);
		intent.putExtra(AccessDevBean.DEVICE_SN, mDevice.getDevSn());
		intent.putExtra(AccessDevBean.DEVICE_MAC, mDevice.getDevMac());
		startActivity(intent);
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case SWIPE_ADD:
			Toast.makeText(Act_CardManage.this, R.string.operation_success, Toast.LENGTH_SHORT).show();
			hideView();
			isAdd = true;
			break;
		case SWIPE_DEL:
			Toast.makeText(Act_CardManage.this, R.string.operation_success, Toast.LENGTH_SHORT).show();
			hideView();
			isAdd = false;
			break;
		case SWIPE_EXIT:
			Toast.makeText(Act_CardManage.this, R.string.operation_success, Toast.LENGTH_SHORT).show();
			resetView();
			break;
			
		case SERIAL_OPERATION:
			Intent intent = new Intent(Act_CardManage.this, SerialCardManageActivity.class);
			intent.putExtra(DATA, msg.getData());
			startActivityForResult(intent, REQUEST_CODE);
			break;
			
		case SYNC_DATA_NULL:
			Toast.makeText(Act_CardManage.this, R.string.activity_device_no_syncInfo, Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return false;
	}
	
	private void dealOperateResult(final int result)
	{
		runOnUiThread(new Runnable() {
			public void run() {
				ToastUtils.tips(result);
			}
		});
	}
	
	private void hideView()
	{
		mSerialAdd.setVisibility(View.GONE);
		
		mSerialDel.setVisibility(View.GONE);
		
		mSwipeAdd.setVisibility(View.GONE);
		
		mSwipeDel.setVisibility(View.GONE);
		
		mInputCard.setVisibility(View.GONE);
		
		mExit.setVisibility(View.VISIBLE);
	}
	
	
	//重置到起始界面
	private void resetView() {
		
//		if(!ContentUtils.isKTZApp()) //开拓者屏蔽批量增加，批量删除功能
//		{
//			mSerialAdd.setVisibility(View.VISIBLE);
//
//			mSerialDel.setVisibility(View.VISIBLE);
//		}
		mSwipeAdd.setVisibility(View.VISIBLE);
		mSwipeDel.setVisibility(View.VISIBLE);
		mInputCard.setVisibility(View.VISIBLE);
		mExit.setVisibility(View.GONE);
		
	}
 
}
