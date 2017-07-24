package com.doormaster.topkeeper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.utils.DmUtil;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface.ManagerCallback;

import java.util.ArrayList;
import java.util.List;

public class InputManageCard extends BaseActivity {
	
	private EditText mInputEdit = null;
	
	private ProgressDialog mDialog = null;
	
	private Button addButton = null;
	
	private Button delButton = null;

	private AccessDevBean mDevice = null;
	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_input_manage);
		
//		initTileView();
		
		Intent intent = getIntent();

		//troditional
		String devSn = intent.getStringExtra(AccessDevBean.DEVICE_SN);
		String devMac = intent.getStringExtra(AccessDevBean.DEVICE_MAC);
		AccessDevMetaData AccessDevMetaData = new AccessDevMetaData(BaseApplication.getInstance());
		username = SPUtils.getString(Constants.USERNAME);
		mDevice  = AccessDevMetaData.queryAccessDeviceByDevSn(username, devSn);
		
		initView();
		
	}
	//初始化界面
	private void initView() {
		
		addButton = (Button) findViewById(R.id.bt_input_add);
		delButton = (Button) findViewById(R.id.bt_input_del);
		String title = getResources().getString(R.string.card_manage_input_manage);
		if(mDevice != null && (mDevice.getDevType() == AccessDevBean.DEV_TYPE_DM_DEVICE || mDevice.getDevType() == AccessDevBean.DEV_TYPE_M260_WIFI_ACCESS_DEVICE))
		{
			addButton.setText(getResources().getString(R.string.activity_device_enter_cardno_add_loss));
			delButton.setText(getResources().getString(R.string.activity_device_enter_cardno_del_loss));
			title = getResources().getString(R.string.card_manage_input_manage_loss);
		}
		mInputEdit = (EditText) findViewById(R.id.et_cardno);
		mDialog = new ProgressDialog(InputManageCard.this);
		mDialog.setMessage(getResources().getString(R.string.operationing));
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        initTileView(title);
	}

	private void initTileView(String title) {
		
		ImageButton btBack =(ImageButton) findViewById(R.id.ib_frag_back_img);
		TextView tvTitle = (TextView) findViewById(R.id.ib_frag_title);
		ImageButton btFind =(ImageButton) findViewById(R.id.ib_activity_scan_add);
		
		tvTitle.setText(title);
		btBack.setVisibility(View.VISIBLE);
		btFind.setVisibility(View.INVISIBLE);
		btBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				InputManageCard.this.finish();
			}
		});
	}
	
	//输入添加
	public void inputAdd(View view)
	{
		if (mInputEdit != null)
		LogUtils.d("开始增加:" + mInputEdit.getText().toString());
		if (mDevice == null) 
		{
			Toast.makeText(InputManageCard.this, getResources().getString(R.string.no_dev_info), Toast.LENGTH_SHORT).show();
			InputManageCard.this.finish();
			return;
		}
		
		mDialog.show();
		String cardno = mInputEdit.getText().toString().trim();
		if (cardno == null || cardno.length() == 0)
		{
			return;
		}
		
		ManagerCallback callback = new ManagerCallback() {
			
			@Override
			public void setResult(final int arg0, Bundle arg1) {
				runOnUiThread(new Runnable() {
					public void run() {						
						mDialog.dismiss();
						if (arg0 == 0) 
						{
							Toast.makeText(InputManageCard.this, R.string.operation_success, Toast.LENGTH_SHORT).show();
						}
						else 
						{
							ToastUtils.tips(arg0);
						}
					}
				});
			}
		}; 
		List<String> cardnoList = new ArrayList<String>();
		cardnoList.add(cardno);
		int ret = LibDevModel.writeCard(InputManageCard.this,
					DmUtil.getLibDev(mDevice), cardnoList, callback, true);
		LogUtils.d("操作增加卡命令返回：" + ret);
	}
	
	//输入删除
	public void inputDel(View view)
	{
		if (mInputEdit != null)
			LogUtils.d("开始增加:" + mInputEdit.getText().toString());
		if (mDevice == null) 
		{
			Toast.makeText(InputManageCard.this, getResources().getString(R.string.no_dev_info), Toast.LENGTH_SHORT).show();
			InputManageCard.this.finish();
			return;
		}
		
		mDialog.show();
		String cardno = mInputEdit.getText().toString().trim();
		if (cardno == null || cardno.length() == 0)
		{
			return;
		}
		
		ManagerCallback callback = new ManagerCallback() {
			
			@Override
			public void setResult(final int arg0, Bundle arg1) {
				runOnUiThread(new Runnable() {
					public void run() {						
						mDialog.dismiss();
						if (arg0 == 0) 
						{
							Toast.makeText(InputManageCard.this, R.string.operation_success, Toast.LENGTH_SHORT).show();
						}
						else 
						{
							ToastUtils.tips(arg0);
						}
					}
				});
			}
		}; 
		List<String> cardnoList = new ArrayList<String>();
		cardnoList.add(cardno);
		int ret = LibDevModel.deleteCard(InputManageCard.this,
					DmUtil.getLibDev(mDevice), cardnoList, callback);
		LogUtils.d("操作删除卡命令返回：" + ret);
	}
}
