package com.doormaster.topkeeper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.utils.ActivityManager;
import com.doormaster.topkeeper.activity.Act_Login;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.vphone.inter.DMVPhoneModel;

public class IntelligentFragment extends BaseFragment {
	private Button bt_exit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView=inflater.inflate(com.doormaster.topkeeper.R.layout.frag_intelligent, container,false);
		bt_exit=(Button) rootView.findViewById(com.doormaster.topkeeper.R.id.bt_exit);
		bt_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DMVPhoneModel.exit();
				//退出时将门禁列表清空
//				DBBiz mDbDao = DBBiz.getInstanter(getActivity().getApplicationContext(), Constants.DB_NAME, 1);
//				mDbDao.deleteAccessDevData();
				SPUtils.put(Constants.IS_AUTOLOGIN, false, getActivity());
				getActivity().finish();
				ActivityManager.getInstance().clearAllActivity();
				openActivity(Act_Login.class);
			}
		});
		return rootView;
	}
}
