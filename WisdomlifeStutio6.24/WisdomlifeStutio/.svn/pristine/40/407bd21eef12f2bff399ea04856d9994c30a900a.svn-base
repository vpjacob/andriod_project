package com.doormaster.topkeeper.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.view.TitleBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.Call;


/**
 * 手机号注册
 */
public class Act_Regist_Phone extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_Regist_Phone";
    //组件
    private TitleBar reg_title_bar;
    private TextView tv_getcede;
    private EditText et_regist_code;
    private EditText et_nick;
    private RadioGroup rg_sex;
    private EditText et_birthdate;
    private EditText et_password;
    private EditText et_password2;

    private String code;
    private String username;
    private String nick;
    private Calendar calendar;
    private String sex;
    private String birthdate;
    private String password;
    private String password2;

    private ProgressDialog pd;
    private TimeCount time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_phone);
        initView();
        setuptView();
        time.start();
    }
    private void initView() {
        reg_title_bar = (TitleBar) findViewById(R.id.reg_title_bar);
        et_regist_code = (EditText) findViewById(R.id.et_regist_code);
        et_nick = (EditText) findViewById(R.id.et_nick);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        et_birthdate = (EditText) findViewById(R.id.et_birthdate);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password2 = (EditText) findViewById(R.id.et_password2);
        tv_getcede = (TextView) findViewById(R.id.tv_getcede);
        time = new TimeCount(60000, 1000);
    }

    private void setuptView() {
        pd = new ProgressDialog(this);
        reg_title_bar.setLeftImageResource(R.mipmap.yoho_close);
        reg_title_bar.setLeftLayoutClickListener(this);
        reg_title_bar.setRightImageResource(R.mipmap.yoho_ensure);
        reg_title_bar.setRightLayoutClickListener(this);
        reg_title_bar.setTitle(getString(R.string.phone_number_registration));
        Intent intent = getIntent();
        username = intent.getStringExtra(Constants.PHONE);

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
        findViewById(R.id.bt_login).setOnClickListener(this);
        tv_getcede.setOnClickListener(this);
    }

    protected void showDatePickDlg() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(Act_Regist_Phone.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                et_birthdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
        if (i == R.id.left_layout) {//返回
            finish();

        } else if (i == R.id.right_layout || i == R.id.bt_login) {//确认，注册
            register();

        } else if (i == R.id.tv_getcede) {//获取验证码
            sendSmsClick();

        } else {
        }
    }

    public void sendSmsClick(){
//        requestVerCode(USERNAME);
        time.start();
    }

    private void register() {
        code = et_regist_code.getText().toString().trim();
        nick = et_nick.getText().toString().trim();
        int i = rg_sex.getCheckedRadioButtonId();
        if (i == R.id.radioMale) {
            sex = getString(R.string.male);

        } else if (i == R.id.radioFemale) {
            sex = getString(R.string.female);

        }
        birthdate = et_birthdate.getText().toString().trim();
        password = et_password.getText().toString().trim();
        password2 = et_password2.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, getString(R.string.enter_verification_code), Toast.LENGTH_SHORT).show();
            et_regist_code.requestFocus();
            return;
        } else if (TextUtils.isEmpty(nick)) {
            Toast.makeText(this, getString(R.string.enter_nickname), Toast.LENGTH_SHORT).show();
            et_birthdate.requestFocus();
            return;
        } else if (TextUtils.isEmpty(birthdate)) {
            Toast.makeText(this, getString(R.string.enter_birth_date), Toast.LENGTH_SHORT).show();
            et_birthdate.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.msg_write_pwd, Toast.LENGTH_SHORT).show();
            et_password.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password2)) {
            Toast.makeText(this, R.string.msg_ensure_pwd, Toast.LENGTH_SHORT).show();
            et_password2.requestFocus();
            return;
        } else if (!password.equals(password2)) {
            Toast.makeText(this, R.string.macth_pwd, Toast.LENGTH_SHORT).show();
            et_password.requestFocus();
            return;
        }

        pd.setMessage(getResources().getString(com.doormaster.topkeeper.R.string.is_the_registered));
        pd.show();
        OkhttpHelper.phoneRegist(username, password, nick, code, new StringCallback() {
            @Override
            public void onResponse(String arg0) {
                LogUtils.i(TAG,"注册返回："+arg0);
                try {
                    JSONObject jsonObject=new JSONObject(arg0);
                    int ret = jsonObject.optInt("ret");
                    if (ret == 0) {
                        Toast.makeText(currentActivity, R.string.register_succeed, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(currentActivity, Act_Login.class);
                        i.putExtra(Constants.PHONE, username);
                        i.putExtra(Constants.PSW, password);
                        startActivity(i);
                        finish();
                    }else if (ret == Constants.VERIFY_NUM_WRONG){
                        Toast.makeText(getApplicationContext(), R.string.verify_num_wrong, Toast.LENGTH_LONG).show();
                    }else if (ret == Constants.VERIFY_NUMBER_EXPIRED_VALIDITY){
                        Toast.makeText(getApplicationContext(), R.string.verify_num_ex_valid, Toast.LENGTH_LONG).show();
                    }else if (ret == Constants.ACCOUNT_REGISETERED){
                        Toast.makeText(getApplicationContext(), R.string.account_had_registed, Toast.LENGTH_SHORT).show();
                    }else if (ret == Constants.FAILED_CRERATE_COUNT) {
                        Toast.makeText(getApplicationContext(), R.string.failed_create_account, Toast.LENGTH_SHORT).show();
                    }else if (ret == Constants.NETWORD_SHUTDOWN){
                        Toast.makeText(getApplicationContext(), R.string.check_network, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), R.string.registration_failure, Toast.LENGTH_LONG).show();
                    }
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }

                } catch (Exception e) {
                    Toast.makeText(Act_Regist_Phone.this, R.string.registration_failure+":"+e.toString(), Toast.LENGTH_LONG).show();
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            }
            @Override
            public void onError(Call arg0, Exception arg1) {
                Toast.makeText(Act_Regist_Phone.this, R.string.registration_failure+":"+arg1.toString(), Toast.LENGTH_LONG).show();
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });

    }

    /**
     * 短信验证码计时器
     */
    public class TimeCount extends CountDownTimer {

        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            tv_getcede.setEnabled(false);//防止重复点击
            tv_getcede.setTextColor(Color.parseColor("#808080"));
            tv_getcede.setText(getString(R.string.obtain_again)+"("+millisUntilFinished / 1000 + ")");
        }

        @Override
        public void onFinish() {
            // 计时完毕
            tv_getcede.setText(getString(R.string.send_verifynum));
            tv_getcede.setTextColor(Color.parseColor("#000000"));
            tv_getcede.setEnabled(true);
        }
    }
}
