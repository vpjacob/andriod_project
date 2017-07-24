package com.doormaster.topkeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.adapter.IntercomDeviceAdapter;
import com.doormaster.topkeeper.bean.DevKeyBean;

import java.util.ArrayList;
import java.util.List;

public class Act_VideoList extends BaseActivity {
	
	private ListView listView = null ;
	private List<DevKeyBean> deviceData = null;
	private IntercomDeviceAdapter deviceAdapter ;


	    String[] userName={"zhangsan","lisi","wangwu","zhaoliu","lidand"}; //这里第一列所要显示的人名
	    String[] userId={"1001","1002","1003","1004","1005"};  //这里是人名对应的ID
	    
	protected void  onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videolist);
		
		listView = (ListView)this.findViewById(R.id.video_list);
		
		deviceData = new ArrayList<DevKeyBean>();
		DevKeyBean intercomDevice = null;
		deviceAdapter = new IntercomDeviceAdapter(this, deviceData);
		listView.setAdapter(deviceAdapter);
	        for(int i=0; i<5; i++){
	            intercomDevice = new DevKeyBean();
	            intercomDevice.setDev_name(userName[i]);
	            intercomDevice.setDev_sn(userId[i]);
	            deviceData.add(intercomDevice);
	        }
	        
	        deviceAdapter.setDevlist(deviceData);
            deviceAdapter.notifyDataSetChanged();
	    }

	public void click(View view)
	{
		int i = view.getId();
		if (i == R.id.img_video_back) {
			finish();


//		case R.id.lay_video:
//			String abc ="dfdfadf";
//			Log.e("abc",abc+"-----------------");
//			Intent intent = new Intent(VideoList.this,VideoRender.class);
////			intent.putExtra("dev_id", userId);
////			Log.e("abc",abc+"-----------------");
//			startActivity(intent);
//			break;
		} else {
		}
	}
}
