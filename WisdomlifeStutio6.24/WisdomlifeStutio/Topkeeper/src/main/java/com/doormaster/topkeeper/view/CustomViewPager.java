package com.doormaster.topkeeper.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义不可滑动viewPager
 * 
 * @author 黄轩
 * 
 */
public class CustomViewPager extends ViewPager {

  private boolean isScrollable = false;

  public CustomViewPager(Context context) {
    super(context);
  }

  public CustomViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    return isScrollable;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return isScrollable;
  }

  public boolean isScrollable() {
    return isScrollable;
  }

  public void setScrollable(boolean isScrollable) {
    this.isScrollable = isScrollable;
  }
}
