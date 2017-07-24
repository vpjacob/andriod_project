package com.doormaster.topkeeper.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.bean.DevKeyBean;
import com.doormaster.topkeeper.utils.ToastUtils;

import java.util.List;


public class IntercomDeviceAdapter extends BaseAdapter {

	public List<DevKeyBean> devlist = null ;
	private Context context =null;
	

	public IntercomDeviceAdapter(Context context, List<DevKeyBean> deviceData)
	{

		this.context = context;
		this.devlist = deviceData;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return devlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return devlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public List<DevKeyBean> getDevlist() {
		return devlist;
	}
	
	public void setDevlist(List<DevKeyBean> devlist) {
		this.devlist = devlist;
//		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(convertView ==null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.video_listitem, parent,false);
			viewHolder = new ViewHolder();
			viewHolder.devName = (TextView) convertView.findViewById(R.id.device_name);
			viewHolder.devId =  (TextView) convertView.findViewById(R.id.device_id);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final DevKeyBean intercomDevice = devlist.get(position);
		viewHolder.devName.setText(intercomDevice.getDev_name());
		viewHolder.devId.setText(intercomDevice.getDev_sn());
		
		viewHolder.devId.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ToastUtils.showMessage(context,"Function developing");
//				Intent intent = new Intent(context,VideoRender.class);
//				intent.putExtra("dev_id",intercomDevice.getDev_sn());
//				context.startActivity(intent);
			}
		});

		return convertView;
	}
	
	class ViewHolder {
		TextView devName;
		TextView devId;
	}
}
