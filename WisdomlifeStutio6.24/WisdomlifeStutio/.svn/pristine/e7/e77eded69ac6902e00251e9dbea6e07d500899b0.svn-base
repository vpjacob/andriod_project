package com.doormaster.topkeeper.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.utils.LogUtils;

public class Act_Email_Activate extends BaseActivity {
	
	private TextView textView   = null;
	
	private Button	activateBtn = null;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(com.doormaster.topkeeper.R.layout.activity_device_email_atcivate);
		LogUtils.d("come success");
		
		textView = (TextView)findViewById(com.doormaster.topkeeper.R.id.email_hint);
		activateBtn = (Button)findViewById(com.doormaster.topkeeper.R.id.bt_register_submit);
		
	}
	public void click(View view)
	{
		int i = view.getId();
		if (i == R.id.bt_register_submit) {
			Intent intent = new Intent(Act_Email_Activate.this, Act_Login.class);
			LogUtils.d("leave success");
			startActivity(intent);
			finish();

		} else {
		}
	}
}
