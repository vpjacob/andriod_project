package com.doormaster.topkeeper.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseApplication;
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

import java.util.ArrayList;

import okhttp3.Call;

public class SendKeyAdapter extends BaseAdapter {
	
	private ArrayList<SendKeyRecordBean> recordList = new ArrayList<SendKeyRecordBean>();
	private LayoutInflater mInflator;
	private Activity context;
	private String username;
	public SendKeyAdapter(Activity context, ArrayList<SendKeyRecordBean> recordList) {
		if (recordList != null) 
		{
			this.recordList = recordList;
		}
		this.context = context;
		mInflator = LayoutInflater.from(context);
		username = SPUtils.getString(Constants.USERNAME);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return recordList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return recordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView ==null)
		{
			convertView = mInflator.inflate(R.layout.item_frag_contactslist, null);
			viewHolder = new ViewHolder();
			viewHolder.item = (LinearLayout) convertView.findViewById(R.id.item_contact_badgeview);
			viewHolder.receiver = (TextView) convertView.findViewById(R.id.frag_msg_name);
			viewHolder.sendTime = (TextView) convertView.findViewById(R.id.frag_msg_time);
			viewHolder.limit =  (TextView) convertView.findViewById(R.id.frag_msg_info);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.frag_img_msg);
			convertView.setTag(viewHolder);
		}
		else 
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final SendKeyRecordBean record = recordList.get(position);
		if (record.getReceiver() != null )
		{	
			String receiver_str = record.getReceiver();
			LogUtils.d(record.toString());
			viewHolder.receiver.setText(receiver_str + "  " + record.getDescription());
		}
		String limit_time = record.getLimitTime();
		String send_time = record.getSendTime();
		if (limit_time == null || limit_time.isEmpty())
		{			
			viewHolder.limit.setText(R.string.device_limit_forever);
		}
		else
		{
			viewHolder.limit.setText(context.getResources().getString(R.string.device_limit_limit)
					+ limit_time);
		}
		if (send_time != null && !send_time.isEmpty()) 
		{	
			viewHolder.sendTime.setText(send_time);
		}
		else 
		{
			viewHolder.sendTime.setText("");
		}
		viewHolder.item.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder deletMsgDialog = new Builder(context);
				deletMsgDialog.setTitle(R.string.remind);
				deletMsgDialog.setMessage(R.string.remind_of_delete_send_key);
				deletMsgDialog.setPositiveButton(R.string.lock_info_input_ensure, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteDigitalKey(record);
						dialog.dismiss();
					}

				});
				
				deletMsgDialog.setNegativeButton(R.string.lock_info_input_cancel, new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				deletMsgDialog.create().show();
				return true;
			}
		});
		return convertView;
	}
	
	private void deleteDigitalKey(final SendKeyRecordBean record) {
				try {
					JSONObject del_key = new JSONObject();
					String client_id = BaseApplication.getInstance().getClientId();
					del_key.put(TimerMsgConstants.CLIENT_ID, client_id);
					del_key.put(TimerMsgConstants.RESOURCE, TimerMsgConstants.KEY);
					del_key.put(TimerMsgConstants.OPERATION, "DELETE");
					AccessDevMetaData AccessDevMetaData = new AccessDevMetaData(context.getApplicationContext());
					AccessDevBean device = AccessDevMetaData.getDeviceByMac(username, record.getDevMac());
					JSONArray data = getData(record.getReceiver(), device.getDevSn());
					if (data == null) 
					{
						LogUtils.d("data == null");
						return;
					}
					del_key.put(TimerMsgConstants.DATA, data);

//					JSONObject json_ret = MyHttpClient.connectPost(url, del_key);
					OkhttpHelper.upLoadRecord(Constants.URL_POST_COMMANDS, del_key.toString(), new StringCallback() {
						@Override
						public void onError(Call call, Exception e) {

						}

						@Override
						public void onResponse(String response) {
							JSONObject json_ret = null;
							try {
								json_ret = new JSONObject(response);
//								Looper.prepare();
								if (!json_ret.isNull(TimerMsgConstants.RET))
								{
									int ret = json_ret.getInt(TimerMsgConstants.RET);
									if (ret == 0)
									{
										SendKeyData recordData = new SendKeyData(BaseApplication.getInstance());
										recordData.deleteData(record);
										Toast.makeText(BaseApplication.getInstance(), R.string.delete_send_key_success,
												Toast.LENGTH_SHORT).show();
										context.runOnUiThread(new Runnable() {
											public void run() {
												recordList.remove(record);
												notifyDataSetChanged();
											}
										});
									}
									else if (ret == Constants.NETWORD_SHUTDOWN)
									{
										Toast.makeText(BaseApplication.getInstance(), R.string.check_network,
												Toast.LENGTH_SHORT).show();
									}
									else
									{
										String failed_ret = BaseApplication.getInstance().getResources()
												.getString(R.string.delete_send_key_failed)+":"+ret;
										Toast.makeText(BaseApplication.getInstance(), failed_ret,
												Toast.LENGTH_SHORT).show();
									}
								}
								else
								{
									Toast.makeText(BaseApplication.getInstance(), R.string.delete_send_key_failed,
											Toast.LENGTH_SHORT).show();
								}
//								Looper.loop();
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					});

				} catch (JSONException e) {
					e.printStackTrace();
				}
		
	}
	
	private JSONArray getData(String receiver, String dev_sn) throws JSONException
	{
		if (receiver == null || dev_sn == null ) 
		{
			return null;
		}
		JSONArray data = new JSONArray();
		JSONObject json = new JSONObject();
		json.put(TimerMsgConstants.COMM_ID, 1);
		json.put(TimerMsgConstants.KEY_RECIEVER, receiver);
		json.put(TimerMsgConstants.KEY_DEV_SN, dev_sn);
		data.put(json);
		return data;
	}

	class ViewHolder 
	{
		LinearLayout item;
		TextView receiver;
		TextView limit;
		TextView sendTime;
		ImageView image;
	}
}
