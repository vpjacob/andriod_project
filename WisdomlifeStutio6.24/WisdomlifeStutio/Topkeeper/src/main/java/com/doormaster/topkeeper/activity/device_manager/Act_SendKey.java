package com.doormaster.topkeeper.activity.device_manager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;
import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.bean.SendKeyRecordBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.constant.TimerMsgConstants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.SendKeyData;
import com.doormaster.topkeeper.utils.DialogUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.intelligoo.sdk.LibDevModel;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;


/**
 * 发送电子钥匙
 * @author huangjs
 *
 */
public class Act_SendKey extends BaseActivity {
	private String TAG = "Act_SendKey";
	//标题
	private ImageButton mBack = null;
	private ImageButton mScan = null;
	private TextView mTitle = null;
	//接收用户/信息描述/权限选择
	private EditText etRecPhone = null;
	private EditText etDescript = null;
	private LinearLayout mLyPrivilege = null;
	private RadioGroup mRgPrivilege = null;
	private RadioGroup mRgTime = null;

	private RelativeLayout mTime = null;
	private TextView mTvStartDate = null;
	private TextView mTvStartTime = null;
	private LinearLayout mLyStartDate = null;
	private LinearLayout mLyStartTime = null;
	private TextView mTvEndDate = null;
	private TextView mTvEndTime = null;
	private LinearLayout mLyEndDate = null;
	private LinearLayout mLyEndTime = null;
	private Button mSend = null;

	private String startTime = "";
	private String endTime = "";
	private String startDate = "";
	private String endDate = "";

	public static final String DATE = "com.intelligoo.app.fragment.LockAct_SendKey.DATE";
	public static final String START_DATE = "com.intelligoo.app.fragment.LockAct_SendKey.START_DATE";
	public static final String END_DATE = "com.intelligoo.app.fragment.LockAct_SendKey.END_DATE";
	public static final int ACTIVITY_RESULT_START = 0x01;
	public static final int ACTIVITY_RESULT_END = 0x02;
	private String username;

	private Calendar calendar;
	private Calendar cChange;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_lock_info_sendkey);
		Intent intent = getIntent();
		init();

		dialog = DialogUtils.createLoadingDialog(this, getResources().getString(R.string.sending));//保证每次登陆都能让动画转起来
		//设备信息
		if (intent == null) {
			return;
		}
		username = SPUtils.getString(Constants.USERNAME);
		final AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext());
		String dev_sn = getIntent().getStringExtra(AccessDevBean.DEVICE_SN);
		String dev_mac = getIntent().getStringExtra(AccessDevBean.DEVICE_MAC);
		final AccessDevBean device = deviceData.queryAccessDeviceByDevSn(username, dev_sn);
		LogUtils.d("dev----:" + device.toString());
		//设置管理员和用户界面
		if (device.getPrivilege() == AccessDevBean.DEV_PRIVILEGE_SUPER)
		{
			mLyPrivilege.setVisibility(View.VISIBLE);
		} 
		else 
		{
			mLyPrivilege.setVisibility(View.GONE);
		}

		calendar = Calendar.getInstance();
		cChange = Calendar.getInstance();
		mLyStartDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				DatePickerDialog datePickerDialog = new DatePickerDialog(Act_SendKey.this, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

						cChange.set(year, monthOfYear, dayOfMonth);
						startDate = sdf_senddate.format(cChange.getTime());//Need to upload date
						String showStartDate = sdf_showdate.format(cChange.getTime());
						LogUtils.d(TAG, "startDate="+startDate);
						mTvStartDate.setText(showStartDate);
					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});
		mLyStartTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				TimePickerDialog timePicker = new TimePickerDialog(Act_SendKey.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int i, int i1) {
						cChange.set(Calendar.HOUR_OF_DAY, i);
						cChange.set(Calendar.MINUTE, i1);
						startTime = sdf_sendtime.format(cChange.getTime());//Need to upload date
						String showStartTime = sdf_showtime.format(cChange.getTime());
						LogUtils.d(TAG, "startTime="+startTime);
						mTvStartTime.setText(showStartTime);
					}
				}, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
				timePicker.setTitle(getString(R.string.select_time));
				timePicker.show();
			}
		});
		mLyEndDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(Act_SendKey.this, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

						cChange.set(year, monthOfYear, dayOfMonth);
						endDate = sdf_senddate.format(cChange.getTime());//Need to upload date
						String showEndDate = sdf_showdate.format(cChange.getTime());
						LogUtils.d(TAG, "endDate"+endDate);
						mTvEndDate.setText(showEndDate);
					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});
		mLyEndTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				TimePickerDialog timePicker = new TimePickerDialog(Act_SendKey.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int i, int i1) {
						cChange.set(Calendar.HOUR_OF_DAY, i);
						cChange.set(Calendar.MINUTE, i1);
						endTime = sdf_sendtime.format(cChange.getTime());//Need to upload date
						String showEndTime = sdf_showtime.format(cChange.getTime());
						LogUtils.d(TAG, "startTime="+endTime);
						mTvEndTime.setText(showEndTime);
					}
				}, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
				timePicker.setTitle(getString(R.string.select_time));
				timePicker.show();
			}
		});
		mSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String receiver = etRecPhone.getText().toString().trim();
				if (receiver.length() <= 0) {
					ToastUtils.showMessage(Act_SendKey.this,
							R.string.receiver_is_empty);
					return;
				}
				if (username!= null && receiver.equals(username)) {
					ToastUtils.showMessage(Act_SendKey.this,
							R.string.receiver_cannot_be_self);
					return;
				}
				String description = etDescript.getText().toString().trim();
				int privilege = AccessDevBean.DEV_PRIVILEGE_USER;
				if (device.getPrivilege() == AccessDevBean.DEV_PRIVILEGE_SUPER &&
						mRgPrivilege.getCheckedRadioButtonId() == R.id.mn_rb_admin) {
					privilege = AccessDevBean.DEV_PRIVILEGE_ADMIN;
				}
				if (mRgTime.getCheckedRadioButtonId() == R.id.mn_rb_limittime) {
					if (startDate.isEmpty()) {
						ToastUtils.showMessage(getApplicationContext(), R.string.please_select_a_start_date);
						return;
					}else if (endDate.isEmpty()) {
						ToastUtils.showMessage(getApplicationContext(), R.string.please_select_a_end_date);
						return;
					}else if (startTime.isEmpty()) {
						ToastUtils.showMessage(getApplicationContext(), R.string.please_select_a_start_time);
						return;
					}else if (endTime.isEmpty()) {
						ToastUtils.showMessage(getApplicationContext(), R.string.please_select_a_end_time);
						return;
					}
					device.setStartDate(startDate+startTime);
					device.setEndDate(endDate+endTime);
				} else {
					device.setStartDate("");
					device.setEndDate("");
				}
				device.setUsername(null);
				device.setPrivilege(privilege);
				device.setVerified(AccessDevBean.DEV_VALID_TYPE_TIME);
//				device.setDevResPhone(username);
				LogUtils.d("receiver:" +receiver + "desc:" +description + device.toString());
				send(receiver,description,device);

			}
		});
		mRgTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.mn_rb_limittime) {
					mTime.setVisibility(View.VISIBLE);
				} else {
					mTime.setVisibility(View.GONE);
				}
			}
		});
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Act_SendKey.this.finish();
			}
		});
	}

	SimpleDateFormat sdf_showdate = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
	SimpleDateFormat sdf_showtime = new SimpleDateFormat("HH:mm",Locale.getDefault());
	SimpleDateFormat sdf_senddate = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
	SimpleDateFormat sdf_sendtime = new SimpleDateFormat("HHmmss",Locale.getDefault());


	private void send(final String receiver, final String description, final AccessDevBean device)
	{

		try {
			final JSONObject key_json = new JSONObject();
			key_json.put(TimerMsgConstants.CLIENT_ID, BaseApplication.getInstance().getClientId());
			key_json.put(TimerMsgConstants.RESOURCE, TimerMsgConstants.KEY);
			key_json.put(TimerMsgConstants.OPERATION, "POST");
			JSONArray data = getKeyData(receiver,description,device);
			key_json.put(TimerMsgConstants.DATA,data);
			LogUtils.d("key_json"+ key_json.toString());
			String send_url = Constants.URL_POST_COMMANDS;
//			JSONObject sendkey_ret = MyHttpClient.connectPost(send_url, key_json);
			dialog.show();
			OkhttpHelper.upLoadRecord(send_url, key_json.toString(), new StringCallback() {
				@Override
				public void onError(Call call, Exception e) {
					ToastUtils.showMessage(Act_SendKey.this, R.string.network_error);
					if (dialog!=null&&dialog.isShowing()) {
						dialog.dismiss();
					}
				}

				@Override
				public void onResponse(String response) {
					if (dialog!=null&&dialog.isShowing()) {
						dialog.dismiss();
					}
					try {
						JSONObject sendkey_ret = new JSONObject(response);
						if(sendkey_ret.length() > 0){
							LogUtils.d("sendkey_ret"+sendkey_ret.toString());
							int ret = sendkey_ret.getInt(TimerMsgConstants.RET);
							if (ret == Constants.RET_SUCCESS) {
								saveSendKeyRecord(receiver, description, device);
								ToastUtils.showMessage(Act_SendKey.this, R.string.send_digital_key_success);
								finish();
							}else if (ret == Constants.THE_ACOUNT_HAS_SEND_KEY){
								ToastUtils.showMessage(Act_SendKey.this, R.string.the_acount_has_send_key);
							}else if (ret == Constants.NETWORD_SHUTDOWN){
								ToastUtils.showMessage(Act_SendKey.this, R.string.check_network);
							}else if (ret == Constants.RECEIVER_IS_NOT_EXIST){
								ToastUtils.showMessage(Act_SendKey.this, R.string.check_user);
							}else if(ret == Constants.RECEIVE_MORE_PRIVILEGE)
							{//用户已有更高的权限
								ToastUtils.showMessage(Act_SendKey.this, R.string.unauthorized_sendkey);
							}else {
								String error_info = getResources().getString(R.string.send_digital_key_failed) +":"+ ret;
								ToastUtils.showMessage(Act_SendKey.this, error_info);
							}
						} else {
							ToastUtils.showMessage(Act_SendKey.this, R.string.server_not_react);
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

	private void saveSendKeyRecord(final String receiver,
                                   final String description, final AccessDevBean device) {
		SendKeyRecordBean sendkey = new SendKeyRecordBean();
		sendkey.setDevMac(device.getDevMac());
		sendkey.setSender(username);
		sendkey.setReceiver(receiver);
		String limit_date = getLocaleTime(device.getStartDate())
				+ "-" + getLocaleTime(device.getEndDate());
		sendkey.setLimitTime(limit_date);
		String send_time = Long.toString(System.currentTimeMillis());
		sendkey.setSendTime(getLocaleTime(send_time));
		sendkey.setDescription(description);
		SendKeyData sendKeyData = new SendKeyData(getApplicationContext());
		sendKeyData.saveData(sendkey);
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
	
	private JSONArray getKeyData(String receiver, String description,
                                 AccessDevBean device) throws JSONException
	{
		JSONArray data = new JSONArray();
		JSONObject key = new JSONObject();
//		String username = MyApplication.getInstance().getUserName();
		key.put(TimerMsgConstants.COMM_ID, 1);
//		key.put(TimerMsgConstants.KEY_SENDER, username);
		key.put(TimerMsgConstants.KEY_RECIEVER, receiver);
		if (device.getDevName() != null && device.getDevName().length() > 0) 
		{			
			key.put(TimerMsgConstants.KEY_DEV_NAME, device.getDevName());
		}
		else 
		{
			key.put(TimerMsgConstants.KEY_DEV_NAME, device.getDevSn());
		}
		key.put(TimerMsgConstants.KEY_DEV_SN, device.getDevSn());
		key.put(TimerMsgConstants.KEY_DEV_MAC, device.getDevMac());
		key.put(TimerMsgConstants.KEY_EKEY, device.geteKey());
		
		key.put(TimerMsgConstants.KEY_DEV_TYPE, device.getDevType());
		key.put(TimerMsgConstants.KEY_DEV_PRI, device.getPrivilege());
		
		Map<String, String> eKeyMap = LibDevModel.getEkeyIdentityAndResource(device.geteKey());
		if (eKeyMap.containsKey("resIdentity") && eKeyMap.containsKey("keyResource"))
		{
			key.put(TimerMsgConstants.KEY_RES_INDENTITY, eKeyMap.get("resIdentity"));
			key.put(TimerMsgConstants.KEY_RESOURCE, eKeyMap.get("keyResource"));
		}
		else
		{
			key.put(TimerMsgConstants.KEY_RES_INDENTITY, "00000000000");
			key.put(TimerMsgConstants.KEY_RESOURCE, "1");
		}
		key.put(TimerMsgConstants.KEY_START_DATE, device.getStartDate());
		key.put(TimerMsgConstants.KEY_END_DATE, device.getEndDate());
		key.put(TimerMsgConstants.KEY_USER_COUNT, device.getUseCount());
		key.put(TimerMsgConstants.KEY_OPEN_TYPE,device.getOpenType());
		key.put(TimerMsgConstants.KEY_VERIFIED, device.getVerified());
		key.put(TimerMsgConstants.KEY_OPEN_PWD, "");
		key.put(TimerMsgConstants.KEY_REMARK, description);
		LogUtils.d(key.toString());
		data.put(key);
		return data;
	}

	private void init() {
		mBack = (ImageButton) findViewById(R.id.ib_frag_back_img);
		mTitle = (TextView) findViewById(R.id.ib_frag_title);
		mScan = (ImageButton) findViewById(R.id.ib_activity_scan_add);
		
		
		etRecPhone = (EditText) findViewById(R.id.mn_et_lock_send_phone);
		etDescript = (EditText) findViewById(R.id.mn_et_lock_send_remark);
		mLyPrivilege = (LinearLayout) findViewById(R.id.ly_device_info_auth_visible);
		mRgPrivilege = (RadioGroup) findViewById(R.id.mn_radio_group_privilige);
		mRgTime = (RadioGroup) findViewById(R.id.mn_radio_group);
		mTime = (RelativeLayout) findViewById(R.id.mn_rl_limit_time);

		mTvStartDate = (TextView) findViewById(R.id.mn_tv_lock_start_date);
		mTvStartTime = (TextView) findViewById(R.id.mn_tv_lock_start_time);
		mLyStartDate = (LinearLayout) findViewById(R.id.mn_ly_start);
		mLyStartTime = (LinearLayout) findViewById(R.id.mn_ly_start_time);
		mTvEndDate = (TextView) findViewById(R.id.mn_tv_lock_end_date);
		mTvEndTime = (TextView) findViewById(R.id.mn_tv_lock_end_time);
		mLyEndDate = (LinearLayout) findViewById(R.id.mn_ly_end);
		mLyEndTime = (LinearLayout) findViewById(R.id.mn_ly_end_time);
		mSend = (Button) findViewById(R.id.mn_bt_send_key);
		
		mBack.setVisibility(View.VISIBLE);
		mScan.setVisibility(View.INVISIBLE);
		mTitle.setText(R.string.mn_ly_lock_send);
	}

}
