/* $Id: MainActivity.java 5022 2015-03-25 03:41:21Z nanang $ */
/*
 * Copyright (C) 2013 Teluu Inc. (http://www.teluu.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.doormaster.topkeeper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.adapter.BaseViewHolder;
import com.doormaster.topkeeper.bean.DevKeyBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.DevKeyMetaData;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.view.TitleBar;
import com.doormaster.vphone.inter.DMVPhoneModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Act_DoorList extends BaseActivity{
	private List<DevKeyBean> dev_list = new ArrayList<>();
	private List<String> dev_name_list;
	private GridView gridview;
	private ListView buddyListView;
	private SimpleAdapter buddyListAdapter;
	private int buddyListSelectedIdx = -1;
	ArrayList<Map<String, String>> buddyList;
	private String lastRegStatus = "";
	private TitleBar title_bar;
	// private final Handler handler = new Handler(this);
	private String username;
	private HashMap<String, String> putData(String uri, String status) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put("uri", uri);
		item.put("status", status);
		return item;
	}

	private void showCallActivity() {
		/*
		 * Intent intent = new Intent(this, CallActivity.class);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * startActivity(intent);
		 */
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.doormaster.topkeeper.R.layout.act_doorlist);

		title_bar = (TitleBar) findViewById(R.id.title_bar);
		title_bar.setTitle(getString(R.string.call_list));
		title_bar.setLeftImageResource(R.drawable.left_ac);
		title_bar.setLeftLayoutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title_bar.setBackgroundColor(Color.parseColor("#FFFFFF"));

		View cv = getWindow().getDecorView();
		initView(cv);

	}

	private void initView(View V) {
		gridview = (GridView) V.findViewById(com.doormaster.topkeeper.R.id.gridview); //这里不使用MyGridView，MyGridView禁止了滑动功能
		DevKeyMetaData deviceData = new DevKeyMetaData(getApplicationContext());
		username = SPUtils.getString(Constants.USERNAME);
		dev_list = deviceData.queryAllDevices(username);
		dev_name_list = deviceData.queryAllDevName(username);
//		LogUtils.i("测试", "打印设备名称:" + dev_name_list.toString());
		if (dev_list == null || dev_list.size() == 0) {
			Toast.makeText(currentActivity, "当前账号暂无设备", Toast.LENGTH_LONG).show();
			return;
		}
		// gridview.setAdapter(new MyGridAdapter(PjsipActivity.this,"s"));
		try {
			LogUtils.d("测试", "dev_list=" + dev_list.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		final List<String> img_text = new ArrayList<String>();

//		if (img_text.addAll(dev_name_list)) {
//			LogUtils.d("测试", "拷贝集合--:" + img_text.toString());
//		}
		/*
		 * for (int i = 0; i < dev_list.size(); i++) { // Log.i("测试",
		 * "通过for循环得到:"+i+" 的状态"+super.account.buddyList.get(i).getStatusText())
		 * ;
		 * 
		 * if(super.account.buddyList.get(i).getStatusText().equals("Available")
		 * ){ // img_text.add(dev_list.get(i).toString()+"视频在线");
		 * img_text.add(dev_name_list.get(i).toString()+"\n视频在线"); }else{ //
		 * img_text.add(dev_list.get(i).toString()+"视频不在线");
		 * img_text.add(dev_name_list.get(i).toString()+"\n视频不在线"); }
		 * Log.i("测试", "通过for循环得到:"+i+" 的状态"+dev_name_list.get(i).toString());
		 * 
		 * img_text.add(dev_name_list.get(i).toString());
		 * 
		 * }
		 */

		gridview.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = LayoutInflater.from(Act_DoorList.this).inflate(com.doormaster.topkeeper.R.layout.grid_item, parent, false);
					int width = parent.getWidth() / 2;
					int height = width;
					convertView.setLayoutParams(new AbsListView.LayoutParams(width, height)); //使item的宽与高相等
				}
				RelativeLayout relativeLayout = BaseViewHolder.get(convertView, com.doormaster.topkeeper.R.id.rl_item);
				TextView tv = BaseViewHolder.get(convertView, com.doormaster.topkeeper.R.id.tv_item);
				ImageView iv = BaseViewHolder.get(convertView, com.doormaster.topkeeper.R.id.iv_item);

				iv.setBackgroundResource(R.drawable.icon_a);
				tv.setText(dev_list.get(position).getDev_name());
				relativeLayout.setVerticalGravity(Gravity.CENTER_VERTICAL); //使item里的子空间居中显示
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return dev_list.size();
			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				DevKeyBean device = dev_list.get(arg2);
				String devSn = device.getDev_sn();
				int type = 2;
				DMVPhoneModel.callAccount(device.getDev_sn(),2);
			}
		});
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// SharedPfData.saveIsCallOut(false);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
