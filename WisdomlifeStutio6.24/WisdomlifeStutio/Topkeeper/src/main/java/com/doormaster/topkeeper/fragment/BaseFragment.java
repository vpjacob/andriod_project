package com.doormaster.topkeeper.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment{
	 protected String TAG = "BaseFragment";
	  protected View rootView;
	  private Intent intent;

	  // protected static ExecutorService executors =
	  // Executors.newFixedThreadPool(3);

	  protected BaseFragment() {
	    TAG = getClass().getSimpleName();
	  }

	  @SuppressWarnings("deprecation")
	@Override
	  public void onAttach(Activity activity) {
	    // TODO Auto-generated method stub
	    super.onAttach(activity);
	    Log.d(TAG, TAG + "onAttach 依附 ");
	  }

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    Log.d(TAG, TAG + "onCreate 创建");
	  }

	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onActivityCreated(savedInstanceState);
	    Log.d(TAG, TAG + "onActivityCreated fragment");
	  }

	  @Override
	  public void onStart() {
	    // TODO Auto-generated method stub
	    super.onStart();
	    Log.d(TAG, TAG + "onStart fragment");
	  }

	  @Override
	  public void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();
	    Log.d(TAG, TAG + "onResume fragment");
	  }

	  @Override
	  public void onPause() {
	    // TODO Auto-generated method stub
	    super.onPause();
	    Log.d(TAG, TAG + "onPause fragment");
	  }

	  @Override
	  public void onStop() {
	    // TODO Auto-generated method stub
	    super.onStop();
	    Log.d(TAG, TAG + "onStop fragment");
	  }

	  @Override
	  public void onDestroy() {
	    // TODO Auto-generated method stub
	    super.onDestroy();
	    Log.d(TAG, TAG + "onDestroy");
	  }

	  @Override
	  public void onDestroyView() {
	    // TODO Auto-generated method stub
	    super.onDestroyView();
	    Log.d(TAG, TAG + "onDestroyView fragment");
	    if (rootView != null) {
	      ViewGroup viewGroup = (ViewGroup) rootView.getParent();
	      viewGroup.removeView(rootView);
	    }
	  }

	  protected void showToast(String string) {
	    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
	  }

	  @Override
	  public void onDetach() {
	    // TODO Auto-generated method stub
	    super.onDetach();
	    Log.d(TAG, TAG + "onDetach 分离");
	  }

	  protected void openActivity(Class<?> c) {
	    openActivity(c, null);
	  }

	  protected void openActivity(Class<?> c, Bundle bundle) {
	    if (intent == null) {
	      intent = new Intent();
	    }
	    if (bundle != null) {
	      intent.putExtras(bundle);
	    }
	    intent.setClass(getActivity(), c);
	    startActivity(intent);
	  }

	  protected void openActivity(Intent intent) {
	    startActivity(intent);
	  }
}
