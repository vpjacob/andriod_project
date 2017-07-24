package com.sdk.samples;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sdk.samples.apicloud.BlockMasking;
import com.sdk.samples.apicloud.WebPageModule;
import com.uzmap.pkg.uzkit.request.APICloudHttpClient;
import com.uzmap.pkg.uzkit.request.HttpGet;
import com.uzmap.pkg.uzkit.request.HttpResult;
import com.uzmap.pkg.uzkit.request.RequestCallback;
import com.z421614851.iga.R;

/**
 *
 * 原生主界面Activity
 * @author dexing.li
 *
 */
public class MainActivity extends Activity implements View.OnClickListener{

	private static String TAG = "MainActivity";
	BlockMasking mBlockMasking;
	private Context context;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_main);
        bindListenner();
    }

    protected void bindListenner() {
        findViewById(R.id.item1).setOnClickListener(this);
        findViewById(R.id.item2).setOnClickListener(this);
        findViewById(R.id.item3).setOnClickListener(this);
        findViewById(R.id.item4).setOnClickListener(this);
        findViewById(R.id.SuperWebview).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.SuperWebview){
			Log.d(TAG, "onClick");

//			Toast.makeText(this, "onClick事件触发,启动WebPageModule", Toast.LENGTH_SHORT).show();
			//"13485321039", "ea0deed56f5f0c8f8684215184497156ce78c96a"

			Intent intent = new Intent(context, WebPageModule.class);
			//不传递startUrl的情况下，默认走自动加载widget的机制，即：APICloud引擎会自动去解析assets/widget目录下的资源并加载
//						String url = "file://" + UZUtility.getExternaStoragePath() + "index.html";
			intent.putExtra("startUrl", "file:///android_assets/widget/index.html");
//						intent.putExtra("startUrl", url);
			startActivity(intent);

		}else if(id == R.id.item4){
			Toast.makeText(this, "onClick事件触发,httpClientTest", Toast.LENGTH_SHORT).show();
			httpClientTest();
		}else{
			Toast.makeText(this, "即将开启", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 *APICloud网络请求框架测试Case
	 */
	private void httpClientTest(){
		mBlockMasking = new BlockMasking(this);
		mBlockMasking.setMessage("正在发送请求....");
		mBlockMasking.show();
		//Get请求
		HttpGet request = new HttpGet("http://www.baidu.com");
		//超时时间10秒
		request.setTimeout(10);
		//设置UA，不设置则取APICloud模块UA
		request.addHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.3.4; en-us; Nexus S Build/GRJ22) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
		//请求结果监听
		request.setCallback(new RequestCallback() {
			@Override
			public void onFinish(final HttpResult result) {
				//在主线程中操作UI，网络请求是非主线程回调的
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mBlockMasking.dismiss();
						showAlert("请求结果：\n" + result.data);
					}
				});
			}
		});
		APICloudHttpClient.instance().request(request);
	}

	private void showAlert(String message){
		AlertDialog.Builder dia = new AlertDialog.Builder(this);
		dia.setTitle("提示消息");
		dia.setMessage(message);
		dia.setCancelable(false);
		dia.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				;
			}
		});
		dia.show();
	}

	@Override
	public void finish() {
		super.finish();
		Process.killProcess(Process.myPid());
	}

}
