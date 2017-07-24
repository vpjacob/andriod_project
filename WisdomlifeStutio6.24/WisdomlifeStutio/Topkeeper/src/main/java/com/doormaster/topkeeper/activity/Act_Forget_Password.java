package com.doormaster.topkeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.doormaster.topkeeper.R;


public class Act_Forget_Password extends BaseActivity{
	private Button phoneRegister;
	
	private Button mailRegeister;
	
	private ImageView registerBack;

	public void onCreate(Bundle SavedInstanceState	)
	{
		super.onCreate(SavedInstanceState);
		setContentView(com.doormaster.topkeeper.R.layout.activity_forget_password);
		
		registerBack = (ImageView)findViewById(com.doormaster.topkeeper.R.id.forget_pswd_back);
		phoneRegister = (Button)findViewById(com.doormaster.topkeeper.R.id.phone_user);
		mailRegeister = (Button)findViewById(com.doormaster.topkeeper.R.id.mail_user);
	}
	

	public void click(View view)
	{
		int i = view.getId();
		if (i == R.id.forget_pswd_back) {
			Act_Forget_Password.this.finish();
		} else if (i == R.id.phone_user) {
			Intent intent = new Intent(Act_Forget_Password.this, Act_Forget_Phone_Pwd.class);
			startActivity(intent);
		} else if (i == R.id.mail_user) {
			Intent intent1 = new Intent(Act_Forget_Password.this, Act_Forgive_Email_Pwd.class);
			startActivity(intent1);
		} else {
		}
	}
}
