package com.doormaster.topkeeper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.percent.PercentRelativeLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.constant.TimerMsgConstants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.db.DBBiz;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.doormaster.topkeeper.view.TitleBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import okhttp3.Call;


public class Act_VisitorPass extends BaseActivity implements View.OnClickListener{

    private static String TAG = "Act_VisitorPass";

    //临时密码类型
    public static final int NUMBER_PWD_REQ = 1;

    public static final int QRCODE_PWD_REQ = 2;

    public TitleBar visitorpass_title_bar;

//    public ExpandableListView exp_end_date;

    public List<PercentRelativeLayout> rvList = new ArrayList<>();

    //开始日期，结束日期，开始时间，结束时间
    public List<TextView> tvList = new ArrayList<>();

    public List<TextView> tvList0 = new ArrayList<>();

    public View view2;

    public Button btn_establish;

    //访客姓名，可通行次数
    public List<EditText> etList = new ArrayList<>();

    public TextView tv_transit_times;

    public LinearLayout qrcodeLayout;

    public ImageView show_qrcode_image;

    public ImageButton wechat_share;

    public ImageButton qq_share;

    private String startDate="",endDate="",startTime="",endTime="";

    private List<AccessDevBean> accessDevList;
    private AccessDevBean curDev;
    private String curDevSn = "";
    private ProgressDialog pd;
    private Calendar calendar;
    private Calendar cChange;

    private DBBiz mDbDao;
    private AccessDevMetaData deviceData;
    private String username;
    private Activity mActivity;

    private List<String> phoneNumberArray = new ArrayList<String>();
    private static final int REQUEST_CONTACT = 1;//用于通讯录
    private Map<String, String> devFuncMap;

    private int type = 0;//0:普通设备  1：二维码设备
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_visitor_pass);
        mActivity = this;
        initView();
        initData();
        setuptView();
    }

    /**
     * Initialization interface
     */
    private void initView() {
        visitorpass_title_bar = (TitleBar) findViewById(R.id.visitorpass_title_bar);
        rvList.add((PercentRelativeLayout) findViewById(R.id.rv_start_date));
        rvList.add((PercentRelativeLayout) findViewById(R.id.rv_end_date));
        rvList.add((PercentRelativeLayout) findViewById(R.id.rv_start_time));
        rvList.add((PercentRelativeLayout) findViewById(R.id.rv_end_time));
        tvList.add((TextView) findViewById(R.id.et_start_date));
        tvList.add((TextView) findViewById(R.id.et_end_date));
        tvList.add((TextView) findViewById(R.id.et_start_time));
        tvList.add((TextView) findViewById(R.id.et_end_time));
        tvList.add((TextView) findViewById(R.id.et_explain));
        tvList0.add((TextView) findViewById(R.id.tv_start_date));
        tvList0.add((TextView) findViewById(R.id.tv_end_date));
        tvList0.add((TextView) findViewById(R.id.tv_start_time));
        tvList0.add((TextView) findViewById(R.id.tv_end_time));
        view2 = findViewById(R.id.view2);
        btn_establish = (Button) findViewById(R.id.btn_establish);
        etList.add((EditText) findViewById(R.id.et_visitor));
        etList.add((EditText) findViewById(R.id.et_transit_times));
        tv_transit_times = (TextView) findViewById(R.id.tv_transit_times);
        qrcodeLayout = (LinearLayout) findViewById(R.id.qrcode_layout);
        show_qrcode_image = (ImageView) findViewById(R.id.show_qrcode_image);
        wechat_share = (ImageButton) findViewById(R.id.wechat_share);
        qq_share = (ImageButton) findViewById(R.id.qq_share);

        pd = new ProgressDialog(this);
        accessDevList = new ArrayList<>();
    }

    /**
     * Initialization data
     */
    private void initData() {
        username = SPUtils.getString(Constants.USERNAME);
        mDbDao= DBBiz.getInstanter(getApplication());
        deviceData = new AccessDevMetaData(getApplication());

        Intent intent = getIntent();
        curDevSn = intent.getStringExtra(AccessDevBean.DEVICE_SN);
        if (curDevSn != null && curDevSn.length() > 0) {
            curDev = deviceData.queryAccessDeviceByDevSn(username, curDevSn);

            if (curDev != null && !curDev.getDevName().isEmpty()) {
                setupQrcodeUI();
            } else if (curDev != null) {
                tvList.get(4).setText(curDev.getDevSn());
            }
        }

        ArrayList<AccessDevBean> allDevList = deviceData.getAllAccessDevList(username);
        if (allDevList == null)
        {
            Toast.makeText(mActivity, R.string.no_permission,Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < allDevList.size(); i++) //筛选出一体机和二维码设备
        {
            int devType = allDevList.get(i).getDevType();
            if(devType == AccessDevBean.DEV_TYPE_ACCESS_CONTROLLER || devType == AccessDevBean.DEV_TYPE_QCCODE_DEVICE ||
                    devType == AccessDevBean.DEV_TYPE_QRCODE_DEVICE || devType == AccessDevBean.DEV_TYPE_DM_DEVICE||
                    devType == AccessDevBean.DEV_TYPE_M260_WIFI_ACCESS_DEVICE || devType == AccessDevBean.DEV_TYPE_M200_WIFI_ACCESS_DEVICE)
            {
                accessDevList.add(allDevList.get(i));
            }
        }

        if (accessDevList == null || accessDevList.size() == 0) {
            Toast.makeText(mActivity, R.string.no_permission,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loading event
     */
    private void setuptView() {
        calendar = Calendar.getInstance();
        cChange = Calendar.getInstance();
        for (PercentRelativeLayout rv : rvList) {
            rv.setOnClickListener(this);
        }
        btn_establish.setOnClickListener(this);
        tvList.get(4).setOnClickListener(this);

        visitorpass_title_bar.setLeftImageResource(R.drawable.left_ac);
        visitorpass_title_bar.setLeftLayoutClickListener(this);
        visitorpass_title_bar.setBackgroundColor(Color.parseColor("#FFFFFF"));

        qq_share.setOnClickListener(this);
        wechat_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.wechat_share) {
            share("Wechat");

        } else if (i == R.id.qq_share) {
            share("QQ");

        } else if (i == R.id.left_layout) {
            finish();

        } else if (i == R.id.et_explain) {
            LogUtils.d(TAG, "Select device");
            Intent intent = new Intent(Act_VisitorPass.this, Act_VisitorPassDevice.class);
            startActivityForResult(intent, 0);

        } else if (i == R.id.rv_start_date) {
            LogUtils.d(TAG, "Select Start Date");
            showDatePickDlg(0);

        } else if (i == R.id.rv_end_date) {
            showDatePickDlg(1);

        } else if (i == R.id.rv_start_time) {//                showTimePickDlg(2);
            myTimePicker(2);

        } else if (i == R.id.rv_end_time) {//                showTimePickDlg(3);
            myTimePicker(3);

        } else if (i == R.id.btn_establish) {
            String name = etList.get(0).getText().toString().trim();
            String numStr = etList.get(1).getText().toString().trim();
            int num = (numStr.isEmpty()) ? 0 : Integer.parseInt(numStr);

            if (type == 1) {
                if (startDate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.please_select_a_start_date, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (startTime.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.please_select_a_start_time, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (endDate.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.please_select_a_end_date, Toast.LENGTH_SHORT).show();
                return;
            } else if (endTime.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.please_select_a_end_time, Toast.LENGTH_SHORT).show();
                return;
            } else if (name.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.null_receiver, Toast.LENGTH_SHORT).show();
                return;
            } else if (curDev == null || curDevSn.length() <= 0) {
                Toast.makeText(getApplicationContext(), R.string.please_select_device, Toast.LENGTH_SHORT).show();
                return;
            } else if (num < 0 || num > 60) {
                ToastUtils.showMessage(getApplicationContext(), R.string.number_is_illegal);
                return;
            } else {
                pd.show();
                String clientId = SPUtils.getString(Constants.CLIENT_ID);
                getTempPassword(clientId, curDevSn, name, num);
            }

        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.d(TAG, "requestCode="+requestCode+",resultCode="+resultCode+",data="+data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle bundle=data.getExtras(); //data为B中回传的Intent
                curDevSn = bundle.getString("dev_sn");//str即为回传的值
                curDev = deviceData.queryAccessDeviceByDevSn(username,curDevSn);
                if (curDev != null && !curDev.getDevName().isEmpty()) {
                    setupQrcodeUI();
                } else if (curDev != null) {
                    tvList.get(4).setText(curDev.getDevSn());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取临时密码
     * @param clientId
     * @param devSn
     * @param phone
     */
    private void getTempPassword(final String clientId,final String devSn,final String phone,int num){
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",clientId);
            json.put("resource", "password");
            json.put("operation", "POST");

            JSONObject data = new JSONObject();
            data.put("dev_sn", devSn);
            data.put("receiver", phone);
//					data.put("verifynum", code_edit.getText().toString());
            data.put("verifynum", "123456");
            data.put("use_count", num);

            int deviceType = curDev.getDevType();
            if (devFuncMap != null && devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_TEMP_PWD) ) {
                data.put("pwd_type", NUMBER_PWD_REQ); //1、短信按键密码 2、二维码
            } else {
                data.put("pwd_type", QRCODE_PWD_REQ);
            }

//            if (deviceType != AccessDevBean.DEV_TYPE_QRCODE_DEVICE) {
//                data.put("pwd_type", NUMBER_PWD_REQ); //1、短信按键密码 2、二维码
//            } else {
//                data.put("pwd_type", QRCODE_PWD_REQ);
//            }

            String language = Locale.getDefault().getLanguage();
            if (language.equals("zh")) {
                language = "zh-CN";
            }
            data.put("language", language);
            String sta = startDate + startTime;
            if (sta.length() > 0) {
                data.put("start_date", sta);
            }
            String end_date = endDate+endTime;//将结束时间精确到秒

            data.put("end_date", end_date);

            json.put("data", data);

            String url = Constants.URL_POST_COMMANDS;
            Log.d("json", "json:" + json.toString());
            OkhttpHelper.sentTempPswd(url, json.toString(), new StringCallback(){

                @Override
                public void onError(Call arg0, Exception arg1) {
                    // TODO Auto-generated method stub
                    arg1.printStackTrace();
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }

                @Override
                public void onResponse(String arg0) {
                    // TODO Auto-generated method stub
                    LogUtils.e(TAG, "得到发送临时密码回复:" + arg0);
                    JSONObject jsonObject;
                    try {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                        jsonObject = new JSONObject(arg0);
//                        dialog.dismiss();
                        if (!jsonObject.isNull("ret"))
                        {
                            Log.e("tempPwd_ret", "tempPwd_ret:" + jsonObject.toString());
                            final int ret = jsonObject.getInt("ret");
                            if (ret == 0 && !jsonObject.isNull("pwd"))
                            {
                                final String pwd = jsonObject.getString("pwd");
                                Toast.makeText(mActivity, getString(R.string.send_succeed), Toast.LENGTH_LONG).show();

                                String qrTmpCode = null;
                                if(!jsonObject.isNull("qrcode"))
                                {
                                    qrTmpCode = jsonObject.getString("qrcode");
                                }
                                final String qrcode = qrTmpCode;

                                if (devFuncMap != null && devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_TEMP_QRCODE) && qrcode != null)
                                {//若是请求二维码，则请求成功后直接在页面显示，并添加分享功能
                                    qrcodeLayout.setVisibility(View.VISIBLE);
//										String encode = "iVBORw0KGgoAAAANSUhEUgAAAUoAAAFKCAYAAAB7KRYFAAAGGElEQVR4nO3d240cORQFQfVi/Xe55EAvksBQyytWhAEz/Sgk+NEH/DzP8/wC4D/9c/oFAEwnlABBKAGCUAIEoQQIQgkQhBIgCCVAEEqAIJQAQSgBglACBKEECEIJEIQSIAglQBBKgCCUAEEoAYJQAgShBAhCCRCEEiAIJUAQSoAglABBKAGCUAIEoQQIQgkQhBIgCCVAEEqAIJQAQSgBglACBKEECEIJEIQSIPx7+gWUz+dz+iWM9DzP1r+3+3Pe/fpu4Xn+bvrz4kQJEIQSIAglQBBKgCCUAEEoAYJQAgShBAhCCRDGL3NWTf9l/6rdy43Vvzd96bPb6vs99fl5nmdxogQIQgkQhBIgCCVAEEqAIJQAQSgBglACBKEECNcsc1adWgrcsrTY7W2LoN08z/8PJ0qAIJQAQSgBglACBKEECEIJEIQSIAglQBBKgPC6ZQ7fuVvnu7ctUPjOiRIgCCVAEEqAIJQAQSgBglACBKEECEIJEIQSIFjm8EecWrTsXvqcWiwxixMlQBBKgCCUAEEoAYJQAgShBAhCCRCEEiAIJUB43TLHguK7U3fhTF/wTH9epr++WzhRAgShBAhCCRCEEiAIJUAQSoAglABBKAGCUAKEa5Y5u+9K4bvdi5ZTC5npr8/zPIsTJUAQSoAglABBKAGCUAIEoQQIQgkQhBIgCCVA+Dwu3eDXuSXI9Mfvlrt1+BknSoAglABBKAGCUAIEoQQIQgkQhBIgCCVAEEqAcM2dOavcgfIzu++aWTV9IXPqLqHpblksOVECBKEECEIJEIQSIAglQBBKgCCUAEEoAYJQAoRr7syZvmS4Zelz6n1c8pgum/797n59079fJ0qAIJQAQSgBglACBKEECEIJEIQSIAglQBBKgHDNnTm3LF+mLzKm/99Ti5Hp39vq65u+cDvFiRIgCCVAEEqAIJQAQSgBglACBKEECEIJEIQSIFyzzDl198r0v7dq9+d3y9/b7dSSZvqdNNM5UQIEoQQIQgkQhBIgCCVAEEqAIJQAQSgBglAChGuWOW9beOx2y8Jo1fQF1HRvWwQ5UQIEoQQIQgkQhBIgCCVAEEqAIJQAQSgBglAChGuWOaumL3imLxl8Lj9zy/tYdcv360QJEIQSIAglQBBKgCCUAEEoAYJQAgShBAhCCRBet8yZfmfJ2+70uWUptXtZ8rbnYDonSoAglABBKAGCUAIEoQQIQgkQhBIgCCVAEEqA8LplzvQlw/S7Q059frfcweO5+js5UQIEoQQIQgkQhBIgCCVAEEqAIJQAQSgBglAChM9zyU/2Ty1Bdju1LJn++bnz5Wfe9jzv5kQJEIQSIAglQBBKgCCUAEEoAYJQAgShBAhCCRCuuTNn+i/7V01ftJx6fZYl351aLL1tKeVECRCEEiAIJUAQSoAglABBKAGCUAIEoQQIQgkQxi9z3rYAWDV9iWQx8t30pQ/fOVECBKEECEIJEIQSIAglQBBKgCCUAEEoAYJQAoTxy5xVtywZpi9LVq1+H7e831W3PKe3vI9VTpQAQSgBglACBKEECEIJEIQSIAglQBBKgCCUAOGaZc6qU0uQU0uGWxYUp+6aWf2/b1sYve3uHydKgCCUAEEoAYJQAgShBAhCCRCEEiAIJUAQSoDwumUO301fWuy+g+eWxciq3Z/L2z4/J0qAIJQAQSgBglACBKEECEIJEIQSIAglQBBKgGCZc7lTd7ncspDZvQg65dRdQqumPwdOlABBKAGCUAIEoQQIQgkQhBIgCCVAEEqAIJQA4XXLnOkLgN1OLUumL1rcIfMzb3u/TpQAQSgBglACBKEECEIJEIQSIAglQBBKgCCUAOHzDP+J/fS7SE459bXdchfOLUukWz7n6e/DiRIgCCVAEEqAIJQAQSgBglACBKEECEIJEIQSIIxf5gCc5kQJEIQSIAglQBBKgCCUAEEoAYJQAgShBAhCCRCEEiAIJUAQSoAglABBKAGCUAIEoQQIQgkQhBIgCCVAEEqAIJQAQSgBglACBKEECEIJEIQSIAglQBBKgCCUAEEoAYJQAgShBAhCCRCEEiAIJUAQSoAglABBKAGCUAKE324IcJK96Ks7AAAAAElFTkSuQmCC";
                                    String encode = qrcode;
                                    byte[] data = Base64.decode(encode.trim(), Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    show_qrcode_image.setImageBitmap(bitmap);
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byte[] byteArray = stream.toByteArray();
                                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                        File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/doormasterQRPhoto");
                                        if(!dir.isFile())
                                        {
                                            dir.mkdir();
                                        }else
                                        {
                                            dir.delete();
                                            dir.mkdir();
                                        }
                                        File file = new File(dir, "qrPhoto.png");
                                        try{
                                            FileOutputStream fos = new FileOutputStream(file);
                                            fos.write(byteArray, 0, byteArray.length);
                                            fos.flush();
                                            fos.close();
                                        }catch(IOException e){
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                finish();

                            }else if (ret == 1042){
                                Toast.makeText(mActivity, getString(R.string.device_no_support), Toast.LENGTH_SHORT).show();
                            } else
                            {
                                Toast.makeText(mActivity, "ret" + ret, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (JSONException e) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                        LogUtils.e("发送临时密码出错了：" + e.toString());
                        Toast.makeText(mActivity, R.string.send_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

            });

        } catch (JSONException e) {
            Log.e("jsonObject error","jsonObject error");
            if (pd.isShowing()) {
                pd.dismiss();
            }
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (pd.isShowing()) {
                pd.dismiss();
            }
            e.printStackTrace();
        }
    }
    SimpleDateFormat sdf_showdate = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
    SimpleDateFormat sdf_showtime = new SimpleDateFormat("HH:mm",Locale.getDefault());
    SimpleDateFormat sdf_senddate = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
    SimpleDateFormat sdf_sendtime = new SimpleDateFormat("HHmmss",Locale.getDefault());

    protected void showDatePickDlg(final int position) {
        if (position >= 0 && position < 2) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(Act_VisitorPass.this, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    cChange.set(year, monthOfYear, dayOfMonth);
                    String date = sdf_senddate.format(cChange.getTime());//Need to upload date
                    tvList.get(position).setText(sdf_showdate.format(cChange.getTime()));//Set to view
                    LogUtils.d(TAG, date);
                    if (position == 0) {
//                        startDate = year + "" + monthOfYear + dayOfMonth;
                        startDate = date;
                    } else if (position == 1) {
                        endDate = date;
                    }
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }

    }
    protected void showTimePickDlg(final int position) {
        if (position < 4 && position >= 2) {
            TimePickerDialog timePicker = new TimePickerDialog(Act_VisitorPass.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    cChange.set(Calendar.HOUR_OF_DAY, i);
                    cChange.set(Calendar.MINUTE, i1);
                    tvList.get(position).setText(sdf_showtime.format(cChange.getTime()));//Set to view
                    cChange.set(Calendar.HOUR_OF_DAY, i-1);//提交服务器需要-1，精确到小时，分钟无效
                    String time = sdf_sendtime.format(cChange.getTime());//Need to upload date
                    if (position == 2) {
                        startTime = time;
                    }
                    if (position == 3) {
                        endTime = time;
                        LogUtils.d(TAG, endTime);
                    }
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePicker.setTitle(getString(R.string.select_time));
            timePicker.show();
        }

    }

    private void myTimePicker(final int position) {
        //自定义控件
        AlertDialog.Builder builder = new AlertDialog.Builder(Act_VisitorPass.this);
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.time_dialog, null,true);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        //初始化时间
        calendar.setTimeInMillis(System.currentTimeMillis());
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(0);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.setMinute(0);
                } else {
                    view.setCurrentMinute(0);
                }
//                view.findViewById(R.id.minutes);
            }
        });
        hidDay(timePicker);
        //设置time布局
        builder.setView(view);
        builder.setTitle(getString(R.string.select_time));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int mHour = timePicker.getCurrentHour();
                int mMinute = timePicker.getCurrentMinute();
                //时间小于10的数字 前面补0 如01:12:00
                LogUtils.d(TAG, "mHour=" + mHour + ",mMinute=" + mMinute);
                cChange.set(Calendar.HOUR_OF_DAY, mHour);
                cChange.set(Calendar.MINUTE, mMinute);
                tvList.get(position).setText(sdf_showtime.format(cChange.getTime()));//Set to view
                cChange.set(Calendar.HOUR_OF_DAY, mHour-1);//提交服务器需要-1，精确到小时，分钟无效
                String time = sdf_sendtime.format(cChange.getTime());//Need to upload date
                if (position == 2) {
                    startTime = time;
                }
                if (position == 3) {
                    endTime = time;
                    LogUtils.d(TAG, endTime);
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void hidDay(TimePicker timePicker2) {

        Field[] datePickerfFields =timePicker2.getClass().getDeclaredFields();

        for (Field datePickerField :datePickerfFields) {

            if ("mDelegate".equals(datePickerField.getName())) {

//这里这个变量是系统的定义的变量，不是自己定义的​,点击时间源码进去就可以看到很多定义的属性，这个就是分钟属性，控制这个就可以了

                datePickerField.setAccessible(true);

                Object dayPicker = new Object();
                try{
                    dayPicker =datePickerField.get(timePicker2);
                    Field[] mDelegateFields =dayPicker.getClass().getDeclaredFields();
                    for (Field mDelegate :mDelegateFields) {
                        if ("mMinuteView".equals(mDelegate.getName())) {

//这里这个变量是系统的定义的变量，不是自己定义的​,点击时间源码进去就可以看到很多定义的属性，这个就是分钟属性，控制这个就可以了

                            mDelegate.setAccessible(true);

                            Object minutePicker = new Object();

                            try{

                                minutePicker =mDelegate.get(dayPicker);

                            } catch(IllegalAccessException e) {

                                e.printStackTrace();

                            } catch(IllegalArgumentException e) {

                                e.printStackTrace();

                            }

                            //datePicker.getCalendarView().setVisibility(View.GONE);

                            ((View)minutePicker).setVisibility(View.GONE);

                        }

                    }


                } catch(IllegalAccessException e) {

                    e.printStackTrace();

                } catch(IllegalArgumentException e) {

                    e.printStackTrace();

                }

                //datePicker.getCalendarView().setVisibility(View.GONE);

                ((View)dayPicker).setVisibility(View.GONE);

            }

        }

    }
    private void setupQrcodeUI(){
        tvList.get(4).setText(curDev.getDevName());
        devFuncMap = AccessDevBean.getDevFuncMap(curDev.getFunction());

        visitorpass_title_bar.setTitle(getString(R.string.visitor_pass));

        type = 0;//默认为普通设备

        etList.get(0).setEnabled(true);

        tv_transit_times.setVisibility(View.GONE);
        etList.get(1).setVisibility(View.GONE);//可通行次数

        rvList.get(0).setVisibility(View.GONE);
        rvList.get(2).setVisibility(View.GONE);

        tvList0.get(0).setVisibility(View.GONE);
        tvList0.get(2).setVisibility(View.GONE);

        view2.setVisibility(View.GONE);

        qrcodeLayout.setVisibility(View.GONE);

        if(devFuncMap.containsKey(TimerMsgConstants.DEV_FUNC_TEMP_QRCODE))
        {
            visitorpass_title_bar.setTitle(getString(R.string.activity_device_send_qrcode));
            if(curDev.getNetWorkSupport() == 1) //使用次数和开始时间，仅限在线版二维码设备
            {
                type = 1;//二维码设备
                etList.get(0).setText(username);
                etList.get(0).setEnabled(false);

                tv_transit_times.setVisibility(View.VISIBLE);
                etList.get(1).setVisibility(View.VISIBLE);//可通行次数

                rvList.get(0).setVisibility(View.VISIBLE);
                rvList.get(2).setVisibility(View.VISIBLE);

                tvList0.get(0).setVisibility(View.VISIBLE);
                tvList0.get(2).setVisibility(View.VISIBLE);

                view2.setVisibility(View.VISIBLE);

                qrcodeLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void share(String platform)
    {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("分享二维码");

//		 //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
////		 oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
//		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//		 // url仅在微信（包括好友和朋友圈）中使用
//		 oks.setUrl("http://sharesdk.cn");
//		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		 oks.setComment("我是测试评论文本");
//		 // site是分享此内容的网站名称，仅在QQ空间使用
//		 oks.setSite("ShareSDK");
//		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		 oks.setSiteUrl("http://sharesdk.cn");

        oks.setImagePath(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/doormasterQRPhoto/qrPhoto.png");

//		 oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
//
//			@Override
//			public void onShare(Platform platform, ShareParams paramsToShare) {
//				// TODO Auto-generated method stub
//				paramsToShare.setText(null);
//               paramsToShare.setTitle(null);
//               paramsToShare.setImagePath(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/doormasterQRPhoto/qrPhoto.png");
//			}
//		});

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                paramsToShare.setText(null);
                paramsToShare.setTitle(null);
                paramsToShare.setTitleUrl(null);
                paramsToShare.setImagePath(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/doormasterQRPhoto/qrPhoto.png");

            }
        });

        oks.setPlatform(platform); //这样可以直接分享，而不经过选择

        // 启动分享GUI
        oks.show(BaseApplication.getContext());

    }

}
