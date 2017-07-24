package com.doormaster.topkeeper.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.Act_DoorList;
import com.doormaster.topkeeper.activity.Act_Notice;
import com.doormaster.topkeeper.activity.Act_OpenRecord;
import com.doormaster.topkeeper.activity.Act_Setting;
import com.doormaster.topkeeper.activity.Act_VisitorPass;
import com.doormaster.topkeeper.activity.ContactsActivity;
import com.doormaster.topkeeper.adapter.CommonAdapter;
import com.doormaster.topkeeper.adapter.ViewHolder;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.ConstantUtils;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.OkhttpHelper;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.ToastUtils;
import com.doormaster.topkeeper.view.ViewPagerBarnner;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.LinphoneManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CommunityFragment extends BaseFragment {
	private String TAG = "CommunityFragment";
	private EditText et_callsip;
	private Button bt_callout;
	private ViewPagerBarnner vpb;
	private List<Bitmap> imgs;
	private List<String> urls;
	private GridView gv_main;
	private ArrayList<String> titles;
	private int[] itemImg = {R.drawable.main_pwd_open_door, R.drawable.main_notice, R.drawable.main_contact_property,
			R.drawable.main_door_voice, R.drawable.main_open_record,R.drawable.main_record, R.mipmap.yoho_setting,R.drawable.main_more};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.frag_community, container, false);
		initView(rootView);
		initData();
		initEven();
		String clientId = SPUtils.getString(Constants.CLIENT_ID, getActivity());
		LogUtils.d(TAG, "clientId=" + clientId);
		OkhttpHelper.getAdvertisementList(new StringCallback() {
			@Override
			public void onResponse(String result) {
				try {
					LogUtils.d(TAG, "result=" + result);
					JSONObject ret = new JSONObject(result);
					if (!ret.optString("msg").equals("ok")) {
						vpb.addImageUrls(imgs);
                        return;
                    }
					JSONObject da = ret.optJSONObject("data");
					if (da == null || da.length() == 0) {
						vpb.addImageUrls(imgs);
                        return;
                    }
					JSONArray data = da.optJSONArray("advertisement_list");
					for (int i = 0; i < data.length(); i++) {
						JSONObject item = data.getJSONObject(i);
						JSONArray readList = item.optJSONArray("image_link_list");
						if (readList == null || readList.length() == 0) {
                            return;
                        }
                        for (int rdPos = 0; rdPos < readList.length(); rdPos++) {
//                            JSONObject reader = readList.getJSONObject(rdPos);
//							String community_code = reader.optString("community_code");
//							String start_date = reader.optString("start_date");
//							String end_date = reader.optString("end_date");
							String image_link_list = readList.getString(rdPos);
							urls.add(image_link_list);
							LogUtils.d(TAG, "image_link_list=" + image_link_list);
                        }
                    }
					if (urls.size() == 0) {
						vpb.addImageUrls(imgs);
					} else {
						imgs = null;
						vpb.addUrls(urls);
					}
				} catch (JSONException e) {
					vpb.addImageUrls(imgs);
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Call arg0, Exception arg1) {
				LogUtils.d(TAG, "获取数据失败，arg1=" + arg1);
				vpb.addImageUrls(imgs);
			}
		}, clientId);
		return rootView;
	}

	private void initView(View view) {
		et_callsip = (EditText) view.findViewById(R.id.et_callsip);
		bt_callout = (Button) view.findViewById(R.id.bt_callout);
		vpb = (ViewPagerBarnner) view.findViewById(R.id.vpb);
		gv_main = (GridView) view.findViewById(R.id.gv_main);
		titles = new ArrayList<String>();
	}

	private void initData() {
		imgs = new ArrayList<>();
		urls = new ArrayList<>();
		imgs.add(BitmapFactory.decodeResource(getResources(), R.drawable.banner1));
		imgs.add(BitmapFactory.decodeResource(getResources(), R.drawable.banner2));
		imgs.add(BitmapFactory.decodeResource(getResources(), R.drawable.banner3));
		imgs.add(BitmapFactory.decodeResource(getResources(), R.drawable.banner4));
//		vpb.addImageUrls(imgs);

		titles.add(ConstantUtils.getResString(getActivity().getApplicationContext(), R.string.authorize_visitor));
		titles.add(ConstantUtils.getResString(getActivity().getApplicationContext(), R.string.community_message));
		titles.add(ConstantUtils.getResString(getActivity().getApplicationContext(), R.string.contact_tenement));
		titles.add(ConstantUtils.getResString(getActivity().getApplicationContext(), R.string.door_video));
		titles.add(ConstantUtils.getResString(getActivity().getApplicationContext(), R.string.open_record));
		titles.add(ConstantUtils.getResString(getActivity().getApplicationContext(), R.string.system_messages));
		titles.add(ConstantUtils.getResString(getActivity().getApplicationContext(), R.string.setting));
		titles.add(ConstantUtils.getResString(getActivity().getApplicationContext(), R.string.community_more));
		gv_main.setAdapter(new CommonAdapter<String>(getActivity(), titles, R.layout.item_gridview_main) {
			@Override
			public void convert(ViewHolder helper, String item, int position) {
				helper.setText(R.id.tv_item, titles.get(position));
				helper.setImageBg(R.id.iv_item, itemImg[position]);
			}
		});
		gv_main.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:    //访客授权
						openActivity(Act_VisitorPass.class);
						break;
					case 1:    //小区公告
//						openActivity(Act_PlotInfo.class);
						openActivity(Act_Notice.class);
						break;
					case 2:    //联系物业
						ToastUtils.showMessage(getContext(), R.string.no_property_info);
//						CallPhoneDialog();
						break;
					case 3:    //门口视频
						openActivity(Act_DoorList.class);
						break;
					case 4:    //开门记录
						openActivity(Act_OpenRecord.class);
						break;
					case 5:    //setting
						openActivity(ContactsActivity.class);
						break;
					case 6:    //setting
						openActivity(Act_Setting.class);
						break;
					case 7:    //setting
						ToastUtils.showMessage(getContext(), R.string.open_yet);
						break;
					default:
						break;
				}
			}
		});
	}

	private void initEven() {
		bt_callout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sipID = et_callsip.getText().toString().trim();
				String addr = "sip:" + sipID + "@114.55.106.95:55060";
				LinphoneManager.getInstance().newOutgoingCall(addr, SPUtils.getString(Constants.USERNAME));
			}
		});
	}

	/*
	 * 联系物业
	 */
	private void CallPhoneDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle(R.string.contact_tenement);
		builder.setMessage("0755-89385386");
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0755-89385386"));
				if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				getActivity().startActivity(intent);
			}
		} );
		  builder.show();
	}
}
