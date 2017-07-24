package com.doormaster.topkeeper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.view.TitleBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class Act_Modify_Psd extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_Modify_Psd";

    public TitleBar modify_title_bar;

    private ProgressDialog pd;

    public EditText et_old_pwd;

    public EditText et_new_pwd;

    public EditText et_new_pwd2;

    public Button btn_commit_modify;

    private String username;
    private String old_psd;
    private String new_psd;
    private String new_psd2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_modify_psd);
        initView();
        setuptView();

    }

    /**
     * Initialization interface
     */
    private void initView() {
        modify_title_bar = (TitleBar) findViewById(R.id.modify_title_bar);
        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        et_new_pwd2 = (EditText) findViewById(R.id.et_new_pwd2);
        btn_commit_modify = (Button) findViewById(R.id.btn_commit_modify);
        pd = new ProgressDialog(this);
    }

    /**
     * Loading event
     */
    private void setuptView() {

        btn_commit_modify.setOnClickListener(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        modify_title_bar.setLeftImageResource(R.mipmap.yoho_close);
        modify_title_bar.setLeftLayoutClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.left_layout) {
            finish();

        } else if (i == R.id.btn_commit_modify) {
            modifyPsd();

        } else {
        }
    }

    private void modifyPsd(){
        pd.show();

        String client_id = SPUtils.getString(Constants.CLIENT_ID);
        old_psd = et_old_pwd.getText().toString().trim();
        new_psd = et_new_pwd.getText().toString().trim();
        new_psd2 = et_new_pwd2.getText().toString().trim();
        if (TextUtils.isEmpty(old_psd)) {
            Toast.makeText(this, getString(R.string.enter_old_password), Toast.LENGTH_SHORT).show();
            et_old_pwd.requestFocus();
            return;
        }else if (TextUtils.isEmpty(new_psd)){
            Toast.makeText(this, getString(R.string.enter_new_password), Toast.LENGTH_SHORT).show();
            et_new_pwd.requestFocus();
            return;
        } else if (TextUtils.isEmpty(new_psd2)) {
            Toast.makeText(this, getString(R.string.msg_ensure_pwd), Toast.LENGTH_SHORT).show();
            et_new_pwd2.requestFocus();
            return;
        }
        OkhttpHelper.modifyPassword(client_id, old_psd, new_psd, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                LogUtils.d("onError: e=" + e);
                Toast.makeText(getApplicationContext(), R.string.server_not_react, Toast.LENGTH_SHORT).show();
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                LogUtils.d(TAG,"response: "+ response);

                try {
                    JSONObject modify_ret = new JSONObject(response);

                    int ret;
                    if(modify_ret.length() > 0){
                        try {
                            ret = modify_ret.getInt("ret");
                            LogUtils.d(TAG,"modify_para_ret: " + modify_ret.toString());
                            if(ret == 0){
                                Toast.makeText(currentActivity, R.string.modify_pwd_success, Toast.LENGTH_SHORT).show();
                                SPUtils.put(Constants.PSW,new_psd,currentActivity);
                                finish();
                            } else if (ret == Constants.NETWORD_SHUTDOWN){
                                Toast.makeText(currentActivity,  R.string.check_network, Toast.LENGTH_SHORT).show();
                            } else if (ret == Constants.PASSWORD_FAILED){
                                Toast.makeText(currentActivity,  R.string.failed_to_change_password, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(currentActivity,  R.string.modify_pwd_failed, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }else{
                        Toast.makeText(currentActivity, R.string.server_not_react, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }
}
