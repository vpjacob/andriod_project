package com.doormaster.topkeeper.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleFragment extends BaseFragment implements OnClickListener {

  private View mContentView;

  private ImageView imgFinish; // 结束

  private TextView tvContent;// 中间文本内容

  private Button imgBtnRight;// 右边imageButton

  private ImageView imgBtnRights;// 右边ImageView

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mContentView = inflater.inflate(com.doormaster.topkeeper.R.layout.main_title, container, false);
    initView();
    return mContentView;
  }

  public TitleFragment() {
    super();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    String title = getActivity().getIntent().getStringExtra("title");

    if (!TextUtils.isEmpty(title)) {
      tvContent.setText(title);
    }
  }

  /**
   * 初始化view
   */
  public void initView() {
    imgFinish = (ImageView) mContentView.findViewById(com.doormaster.topkeeper.R.id.title_finish);

    imgFinish.setOnClickListener(this);

    tvContent = (TextView) mContentView.findViewById(com.doormaster.topkeeper.R.id.title_content);

    imgBtnRight = (Button) mContentView.findViewById(com.doormaster.topkeeper.R.id.title_right);

    imgBtnRights = (ImageView) mContentView.findViewById(com.doormaster.topkeeper.R.id.title_right_s);
  }

  public Button getRightView() {
    if (imgBtnRights.getVisibility() != View.GONE) {
      imgBtnRights.setVisibility(View.GONE);
    }
    return imgBtnRight;
  }

  public View getParentView() {
    return mContentView;
  }

  public ImageView getLeftView() {
    return imgFinish;
  }

  public TextView getContentView() {
    return tvContent;
  }

  public ImageView getRightImageView() {
    if (imgBtnRight.getVisibility() != View.GONE) {
      imgBtnRight.setVisibility(View.GONE);
    }
    return imgBtnRights;
  }

  @Override
  public void onClick(View view) {
    int i = view.getId();
    if (i == com.doormaster.topkeeper.R.id.title_finish) {
      getActivity().finish();

    } else {
    }
  }

}
