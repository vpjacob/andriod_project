package com.doormaster.topkeeper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.Utils;
import com.doormaster.topkeeper.view.TitleBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import okhttp3.Call;

public class Act_Regist extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_Regist";
    private TitleBar reg_title_bar;
    private TextView tv_account;
    private EditText et_username;

    private Button bt_reg;
    private Button bt_reg_email;
    private Button bt_reg_phone;

    private String username;
    private boolean isFirstPage = true;
    private boolean isEmailReg;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.doormaster.topkeeper.R.layout.activity_regist_main);

        reg_title_bar = (TitleBar) findViewById(com.doormaster.topkeeper.R.id.reg_title_bar);
        tv_account = (TextView) findViewById(com.doormaster.topkeeper.R.id.tv_account);
        et_username = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_username);
        bt_reg = (Button) findViewById(com.doormaster.topkeeper.R.id.bt_reg);
        bt_reg_email = (Button) findViewById(com.doormaster.topkeeper.R.id.bt_reg_email);
        bt_reg_phone = (Button) findViewById(com.doormaster.topkeeper.R.id.bt_reg_phone);

        bt_reg.setOnClickListener(this);
        bt_reg_email.setOnClickListener(this);
        bt_reg_phone.setOnClickListener(this);

        reg_title_bar.setTitle(getString(com.doormaster.topkeeper.R.string.register));
        reg_title_bar.setLeftImageResource(com.doormaster.topkeeper.R.mipmap.yoho_close);
        reg_title_bar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        pd = new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_reg) {
            username = et_username.getText().toString().trim();
            if (isEmailReg) {
//                    pd.show();
                if (!Utils.isEmail(username)) {
                    Toast.makeText(currentActivity, R.string.enter_right_email_address, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Act_Regist.this, Act_Regist_Email.class);
                intent.putExtra(Constants.EMAIL, username);
                startActivity(intent);
                //邮箱注册
            } else {
                //手机号注册
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Act_Regist.this, R.string.enter_phone_number, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Utils.isMobileNO(username)) {
                    Toast.makeText(Act_Regist.this, R.string.enter_right_phone_number, Toast.LENGTH_SHORT).show();
                    return;
                }

                pd.show();
                OkhttpHelper.getRegistCode(username, new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        LogUtils.i(TAG, "获取注册码返回：" + arg0);
                        try {
                            JSONObject jsonObject = new JSONObject(arg0);
                            int ret = jsonObject.getInt("ret");
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                            if (ret == 0) {
                                Toast.makeText(Act_Regist.this, R.string.verification_sent, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Act_Regist.this, Act_Regist_Phone.class);
                                intent.putExtra(Constants.PHONE, username);
                                startActivity(intent);
                            } else if (ret == Constants.VERIFY_ONE_MININUTES) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.exceed_ten_one_min, Toast.LENGTH_SHORT).show();
                            } else if (ret == Constants.VERIFY_OUT_FIVE_ONEDAY) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.exceed_five_one_day, Toast.LENGTH_SHORT).show();
                            } else if (ret == Constants.VERIFY_OUT_FIVE_PHONE) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.exceed_five_request, Toast.LENGTH_SHORT).show();
                            } else if (ret == Constants.ACCOUNT_REGISETERED) {
                                Toast.makeText(Act_Regist.this, R.string.phone_number_been_registered, Toast.LENGTH_LONG).show();
                            } else if (ret == Constants.NETWORD_SHUTDOWN) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.check_network, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        R.string.failed_to_send_verify, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(Act_Regist.this, R.string.verification_code_obtain_failed, Toast.LENGTH_LONG).show();
                    }
                });
            }

        } else if (i == R.id.bt_reg_email) {
            bt_reg_email.setVisibility(View.GONE);
            bt_reg_phone.setVisibility(View.GONE);
            tv_account.setVisibility(View.VISIBLE);
            et_username.setVisibility(View.VISIBLE);
            bt_reg.setVisibility(View.VISIBLE);
            tv_account.setText(getString(R.string.email));
            et_username.setText("");
            et_username.setHint(getString(R.string.enter_email));
            isFirstPage = false;
            isEmailReg = true;

        } else if (i == R.id.bt_reg_phone) {
            bt_reg_email.setVisibility(View.GONE);
            bt_reg_phone.setVisibility(View.GONE);
            tv_account.setVisibility(View.VISIBLE);
            et_username.setVisibility(View.VISIBLE);
            bt_reg.setVisibility(View.VISIBLE);
            tv_account.setText(getString(R.string.phone_number));
            et_username.setText("");
            et_username.setHint(getString(R.string.enter_phone));
            isFirstPage = false;
            isEmailReg = false;

        } else {
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        if (isFirstPage) {
            finish();
        } else {
            bt_reg_email.setVisibility(View.VISIBLE);
            bt_reg_phone.setVisibility(View.VISIBLE);
            tv_account.setVisibility(View.GONE);
            et_username.setVisibility(View.GONE);
            bt_reg.setVisibility(View.GONE);
            isFirstPage = true;
        }
    }
}
