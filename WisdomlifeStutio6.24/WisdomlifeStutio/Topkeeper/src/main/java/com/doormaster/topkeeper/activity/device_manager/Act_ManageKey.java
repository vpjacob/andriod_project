package com.doormaster.topkeeper.activity.device_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseActivity;


public class Act_ManageKey extends BaseActivity {
	
	private TextView mTitle;
	private ImageButton mBack;
	private ImageButton mScan;
	private Button mSend;
	private Button mDelete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_manager_key);
		initView();
		final Intent intent = getIntent();
		if (intent == null) 
		{
			return;
		}
		mSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent.setClass(Act_ManageKey.this, Act_SendKey.class);
				startActivity(intent);
			}
		});
		
		mDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent.setClass(Act_ManageKey.this, Act_DeleteKey.class);
				startActivity(intent);
			}
		});
		
		
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Act_ManageKey.this.finish();
			}
		});
	}

	private void initView() {
		mBack = (ImageButton) findViewById(R.id.ib_frag_back_img);
		mTitle = (TextView) findViewById(R.id.ib_frag_title);
		mScan = (ImageButton) findViewById(R.id.ib_activity_scan_add);
		mSend = (Button) findViewById(R.id.bt_send_key);
		mDelete = (Button) findViewById(R.id.bt_delete_key);
		
		mBack.setVisibility(View.VISIBLE);
		mScan.setVisibility(View.INVISIBLE);
		mTitle.setText(R.string.activity_device_manager_key);
	}
}
