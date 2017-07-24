package com.doormaster.topkeeper.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.doormaster.topkeeper.R;


public class PopupUtil {
	
	private static PopupWindow popup = null;
	
	private static Button add_list = null ;
	
	private static Button scan_dev = null ;
	
	private static Button video_dev = null;
			
	public static PopupWindow getPopup(Context context)
	{
			View newView  = View.inflate(context, R.layout.menu_popup, null);
			add_list = (Button)newView.findViewById(R.id.add_dev);
			scan_dev = (Button)newView.findViewById(R.id.scan_dev);
			video_dev = (Button)newView.findViewById(R.id.video_dev);
			popup = new PopupWindow(newView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// 设置此参数获得焦点，否则无法点击  
			popup.setFocusable(true);
			 // 需要设置一下此参数，点击外边可消失  
			popup.setBackgroundDrawable(new BitmapDrawable());
			 //设置点击窗口外边窗口消失  
			popup.setOutsideTouchable(true);
		return popup;
	}
	
	public static void setEnable(boolean enable)
	{
		float alpha = 0f;
		if (enable)
		{
			alpha = 1f;
		}
		else
		{
			alpha = 0.4f;
		}
		if (add_list != null)
		{
			add_list.setEnabled(enable);
			add_list.setAlpha(alpha);
		}
		if (scan_dev != null)
		{
			scan_dev.setEnabled(enable);
			scan_dev.setAlpha(alpha);
		}
	
	}
}
