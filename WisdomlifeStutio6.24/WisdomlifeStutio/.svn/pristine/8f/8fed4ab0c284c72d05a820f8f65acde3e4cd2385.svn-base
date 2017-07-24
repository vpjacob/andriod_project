package com.doormaster.topkeeper.task;


import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.db.OpenRecordData;
import com.doormaster.topkeeper.bean.RecordBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.constant.TimerMsgConstants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;


public class UpLoadRecord {

	private static final String TAG = "UpLoadRecord";
	//上传开锁记录
	public static void upLoadOpenRecord() 
	{
		String resource = "event";
		String operation = "POST";
		String client_id = SPUtils.getString(Constants.CLIENT_ID);

		OpenRecordData record_data = new OpenRecordData(BaseApplication.getContext());
		final ArrayList<RecordBean> record_List = record_data.getUpLoadRecord();
//		int auto_upload = MyApplication.getInstance().getUploadOpenEvent();
//		if (auto_upload == RecordDom.AUTO_UPLOAD_CANCEL)
//		{
//			return;
//		}
		if (client_id == null || record_List == null || record_List.size() <= 0 ) {
			return;
		}
		
		String upload_url = Constants.URL_POST_COMMANDS;
		JSONObject event_json = new JSONObject();
		try {
			event_json.put("resource", resource);
			event_json.put("operation", operation);
			event_json.put("client_id", client_id);
			JSONArray record_jsonArr = getJsonArrRecord(record_List);
			if (record_jsonArr == null || record_jsonArr.length() <= 0) {
				return;
			}
			event_json.put("data", record_jsonArr);

			OkhttpHelper.upLoadRecord(upload_url, event_json.toString(), new StringCallback() {
				@Override
				public void onError(Call call, Exception e) {

				}

				@Override
				public void onResponse(String response) {
					LogUtils.e(TAG, "得到发送临时密码回复:" + response);
					JSONObject ret_upLoadOpenRecord;
					try {
						ret_upLoadOpenRecord = new JSONObject(response);
						int ret = ret_upLoadOpenRecord.getInt("ret");
						LogUtils.d("ret_upLoadOpenRecord:"+ ret_upLoadOpenRecord.toString() );

						if (ret == 0) {
                            removeUpdate(record_List);
                        } else if (!ret_upLoadOpenRecord.isNull("comm_id")) {
                            String comm_id_str = ret_upLoadOpenRecord.getString("comm_id");
                            String[] comm_id_arr = null;
                            if (comm_id_str.contains(",")) {
                                comm_id_arr = comm_id_str.split(",");
                            }
                            ArrayList<RecordBean> temp_list = new ArrayList<>();
							assert comm_id_arr != null;
							for (String comm_id:comm_id_arr){
                                for ( RecordBean record : temp_list)
                                {
                                    int id = record.getId();
                                    if (comm_id.equalsIgnoreCase(Integer.toString(id)) )
                                    {
                                        continue;
                                    }
                                    temp_list.add(record);
                                }
                            }
                            removeUpdate(temp_list);
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

	private static JSONArray getJsonArrRecord(ArrayList<RecordBean> record_list) throws JSONException {
		if (record_list == null) 
		{
			return null;
		}
		JSONArray data_arr = new JSONArray();
		for (int i = 0;i<record_list.size(); i++) 
		{
			RecordBean record = record_list.get(i);
			JSONObject record_json = new JSONObject();
			record_json.put(TimerMsgConstants.COMM_ID, record.getId());
			record_json.put(TimerMsgConstants.RECORD_DEV_NAME, record.getDevName());
			record_json.put(TimerMsgConstants.RECORD_DEV_MAC, record.getDevMac());
			record_json.put(TimerMsgConstants.RECORD_DEV_SN, record.getDevSn());
			record_json.put(TimerMsgConstants.RECORD_EVENT_TIME, record.getEventTime());
			record_json.put(TimerMsgConstants.RECORD_ACTION_TIME, record.getActionTime());
			record_json.put(TimerMsgConstants.RECORD_OP_TIME, record.getOptime());
			record_json.put(TimerMsgConstants.RECORD_OP_RET, record.getOpRet());
			record_json.put(TimerMsgConstants.RECORD_OP_USER, record.getOpUser());
			data_arr.put(record_json);
		}
		return data_arr;
	}

	//更新上传状态
	private static void removeUpdate(ArrayList<RecordBean> recordList)
	{
//		MyLog.Assert(recordList != null);
		OpenRecordData openData = new OpenRecordData(BaseApplication.getContext());
		openData.updateCommId(recordList);
	}
}
