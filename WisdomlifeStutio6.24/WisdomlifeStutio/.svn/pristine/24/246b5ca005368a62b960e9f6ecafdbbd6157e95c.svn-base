package com.doormaster.topkeeper.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.ConstantUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.Utils;
import com.doormaster.topkeeper.view.TitleBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.Call;

public class Act_Regist_Email extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_Regist_Email";
    private TitleBar reg_title_bar;
    private EditText et_username;
    private EditText et_nick;
    private RadioGroup rg_sex;
    private EditText et_birthdate;
    private EditText et_password;
    private EditText et_password2;

    private String username;
    private String nick;
    private Calendar calendar;
    private String sex;
    private String birthdate;
    private String password;
    private String password2;

    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.doormaster.topkeeper.R.layout.activity_regist_email);
        initView();
        setuptView();

    }

    private void initView() {
        reg_title_bar = (TitleBar) findViewById(com.doormaster.topkeeper.R.id.reg_title_bar);
        et_username = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_username);
        et_nick = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_nick);
        rg_sex = (RadioGroup) findViewById(com.doormaster.topkeeper.R.id.rg_sex);
        et_birthdate = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_birthdate);
        et_password = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_password);
        et_password2 = (EditText) findViewById(com.doormaster.topkeeper.R.id.et_password2);
    }

    private void setuptView() {
        reg_title_bar.setLeftImageResource(com.doormaster.topkeeper.R.mipmap.yoho_close);
        reg_title_bar.setLeftLayoutClickListener(this);
        reg_title_bar.setRightImageResource(com.doormaster.topkeeper.R.mipmap.yoho_ensure);
        reg_title_bar.setRightLayoutClickListener(this);
        reg_title_bar.setTitle(getString(com.doormaster.topkeeper.R.string.mailbox_registration));
        Intent intent = getIntent();
        username = intent.getStringExtra(Constants.EMAIL);
        et_username.setText(username);

        calendar = Calendar.getInstance();
        et_birthdate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        et_birthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        findViewById(com.doormaster.topkeeper.R.id.bt_login).setOnClickListener(this);
    }

    protected void showDatePickDlg() {
            DatePickerDialog datePickerDialog = new DatePickerDialog(Act_Regist_Email.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                et_birthdate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                calendar.set(year,monthOfYear,dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        hideInputSoft();
    }

    private void hideInputSoft(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == com.doormaster.topkeeper.R.id.left_layout) {//返回
            finish();

        } else if (i == com.doormaster.topkeeper.R.id.right_layout || i == com.doormaster.topkeeper.R.id.bt_login) {//确认，注册
            register();

        } else {
        }
    }

    private void register() {
        username = et_username.getText().toString().trim();
        nick = et_nick.getText().toString().trim();
        int i = rg_sex.getCheckedRadioButtonId();
        if (i == com.doormaster.topkeeper.R.id.radioMale) {
            sex = getString(com.doormaster.topkeeper.R.string.male);

        } else if (i == com.doormaster.topkeeper.R.id.radioFemale) {
            sex = getString(com.doormaster.topkeeper.R.string.female);

        }
        birthdate = et_birthdate.getText().toString().trim();
        password = et_password.getText().toString().trim();
        password2 = et_password2.getText().toString().trim();
        if (TextUtils.isEmpty(birthdate)) {
            Toast.makeText(this, getString(com.doormaster.topkeeper.R.string.enter_birth_date), Toast.LENGTH_SHORT).show();
            et_birthdate.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, com.doormaster.topkeeper.R.string.msg_write_pwd, Toast.LENGTH_SHORT).show();
            et_password.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password2)) {
            Toast.makeText(this, com.doormaster.topkeeper.R.string.msg_ensure_pwd, Toast.LENGTH_SHORT).show();
            et_password2.requestFocus();
            return;
        } else if (!password.equals(password2)) {
            Toast.makeText(this, com.doormaster.topkeeper.R.string.macth_pwd, Toast.LENGTH_SHORT).show();
            et_password.requestFocus();
            return;
        }else if(!Utils.isEmail(username)){
            Toast.makeText(currentActivity, com.doormaster.topkeeper.R.string.enter_right_email_address, Toast.LENGTH_SHORT).show();
            return;
        }
        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(com.doormaster.topkeeper.R.string.is_the_registered));
        pd.show();
        emailRegister();
    }

    private void emailRegister()
    {
        String access_token = ConstantUtils.getAccessToken();
        OkhttpHelper.emailRegist(access_token, nick, username, password, "en", new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(Act_Regist_Email.this, com.doormaster.topkeeper.R.string.registration_failure+":"+e.toString(), Toast.LENGTH_LONG).show();
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG,"注册返回："+response);
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                try {
                    JSONObject register_ret=new JSONObject(response);
                    LogUtils.d("register_ret"+register_ret);
                    if (!register_ret.isNull("ret"))
                    {
                        int ret = register_ret.getInt("ret");
                        LogUtils.d(ret+"--");
                        if(ret==0)
                        {
                            Intent intent = new Intent(Act_Regist_Email.this,Act_Email_Activate.class);
                            LogUtils.d("register success");
                            startActivity(intent);
                            finish();
                        }else if (ret == Constants.VERIFY_NUM_WRONG){
                            Toast.makeText(getApplicationContext(),
                                    com.doormaster.topkeeper.R.string.verify_num_wrong, Toast.LENGTH_SHORT).show();
                        }else if (ret == Constants.FAILED_CRERATE_COUNT) {
                            Toast.makeText(getApplicationContext(),
                                    com.doormaster.topkeeper.R.string.failed_create_account, Toast.LENGTH_SHORT).show();
                        }else if (ret == Constants.ACCOUNT_REGISETERED){
                            Toast.makeText(getApplicationContext(),
                                    com.doormaster.topkeeper.R.string.account_had_registed, Toast.LENGTH_SHORT).show();
                        }else if (ret == Constants.NETWORD_SHUTDOWN){
                            Toast.makeText(getApplicationContext(),
                                    com.doormaster.topkeeper.R.string.check_network, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    com.doormaster.topkeeper.R.string.failed_register_unknow, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(Act_Regist_Email.this, com.doormaster.topkeeper.R.string.registration_failure+":"+e.toString(), Toast.LENGTH_LONG).show();
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            }
        });
    }
}
