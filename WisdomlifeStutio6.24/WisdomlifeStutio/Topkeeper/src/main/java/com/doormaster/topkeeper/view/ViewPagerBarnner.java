package com.doormaster.topkeeper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ViewPagerBarnner extends RelativeLayout implements OnPageChangeListener{
	/**
	 * ViewPager对象
	 */
	private ViewPager viewPager;
	/**
	 * 指示器
	 */
	private LinearLayout indicatorView;
	private Context context;
	/**
	 * 设定的总循环个数
	 */
	private int pageCount;
	/**
	 * 设定的总页面数
	 */
	private int numberPages;
	/**
	 * 存放Url地址
	 */
	private LinkedList<Bitmap> imageUrls = new LinkedList<Bitmap>();
	private List<String> rls = new ArrayList<>();
	/**
	 * ViewPager的点击事件
	 */
	private ViewPagerClick viewPagerClick;
	/**
	 * 指示器的大小
	 */
	private float indicatorSize;
	/**
	 * 指示器的背景
	 */
	private int indicatorDrawable;
	/**
	 * 指示器间隔
	 */
	private float indicatorInterval;
	/**
	 * 容器的高度
	 */
	private float containerHeight;
	/**
	 * 指示器距离底部的距离
	 */
	private float indicatorMarginBottom;
	/**
	 * 设置背景图片
	 */
	private int indicatorBackgroud;
	/**
	 * 指示器变大的大小
	 */
	private float indicatorBigSize;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
		   int currentPosition = viewPager.getCurrentItem();
		   currentPosition = (currentPosition + 1) % pageCount;
           if (currentPosition == pageCount - 1) {
        	   viewPager.setCurrentItem(numberPages - 1, false);
           } else {
        	   viewPager.setCurrentItem(currentPosition);
           }
		   mHandler.sendEmptyMessageDelayed(0, 3000);
		}
	};
	
	public ViewPagerBarnner(Context context) {
		this(context,null);
	}

	public ViewPagerBarnner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.doormaster.topkeeper.R.styleable.ViewPager);
        indicatorSize = typedArray.getDimension(com.doormaster.topkeeper.R.styleable.ViewPager_indicatorSize, 10);
        indicatorBigSize = typedArray.getDimension(com.doormaster.topkeeper.R.styleable.ViewPager_indicatorBigSize, 0);
        indicatorInterval = typedArray.getDimension(com.doormaster.topkeeper.R.styleable.ViewPager_indicatorInterval, 15);
        indicatorDrawable = typedArray.getResourceId(com.doormaster.topkeeper.R.styleable.ViewPager_indicatorDrawable, 0);
		indicatorBackgroud = typedArray.getResourceId(com.doormaster.topkeeper.R.styleable.ViewPager_indicatorBackgroud, 0);
        containerHeight = typedArray.getDimension(com.doormaster.topkeeper.R.styleable.ViewPager_containerHeight, 20);
        indicatorMarginBottom = typedArray.getDimension(com.doormaster.topkeeper.R.styleable.ViewPager_indicatorMarginBottom, 5);
        typedArray.recycle();
		initViews();
	}
	
	/**
	 * 初始化View的视图
	 */
	private void initViews(){
		viewPager = new ViewPager(context);
		LayoutParams viewPagerParams = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 
												RelativeLayout.LayoutParams.MATCH_PARENT);
		viewPager.setLayoutParams(viewPagerParams);
//		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(this);
		
		indicatorView = new LinearLayout(context);
		indicatorView.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams layoutParams = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int)containerHeight);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.bottomMargin = (int) indicatorMarginBottom;
		indicatorView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		indicatorView.setLayoutParams(layoutParams);
		if(indicatorBackgroud != 0){
			indicatorView.setBackgroundDrawable(context.getResources().getDrawable(indicatorBackgroud));
		}
		addView(viewPager);
		addView(indicatorView);

	}
	
	/**
	 * ViewPager的适配器
	 */
	private PagerAdapter viewPagerAdapter = new PagerAdapter() {
		
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public int getCount() {
			return numberPages > 1 ? pageCount : 1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			if(numberPages == 1)return;
			int location = viewPager.getCurrentItem();
			if(location == pageCount - 1){//为最后一张的时候，转换到顶部
				location = numberPages -1;
				viewPager.setCurrentItem(location,false);
			}else if(location == 0){//为第一张的时候，切换到中间
				location = numberPages;
				viewPager.setCurrentItem(location,false);
			}
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position %= numberPages;
			ImageView imageView = null;
			imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_XY);

			if (rls != null&&rls.size() != 0) {
//					imageView.setBackgroundDrawable(new BitmapDrawable(rls.get(position)));
//					imageView.setImageURI(Uri.parse(rls.get(position)));
				Utils.setImage(context, rls.get(position), imageView);
			} else {
				imageView.setBackgroundDrawable(new BitmapDrawable(imageUrls.get(position)));
			}
			imageView.setTag(position);
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					if(viewPagerClick != null){
						viewPagerClick.viewPagerOnClick(view);
					}
				}
			});

			container.addView(imageView);
			return imageView;
		}
	};
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int location) {
		setSelectPage(location % numberPages);
	}
	
	
	/** 
	 * 从url地址创建imageview对象，同时初始化指示器
	 */
	private void createImageView(List<Bitmap> imageUrlList){
		if(imageUrlList != null && imageUrlList.size() > 0){
			View pointView;
			int position = 0;
			while(position < imageUrlList.size()){
				pointView = new View(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(indicatorSize),(int)(indicatorSize));
				if(position != imageUrlList.size() -1)//由于是居中，所以最后一个不需要margin
				params.rightMargin = (int)indicatorInterval;
				pointView.setLayoutParams(params);
				pointView.setBackgroundDrawable(context.getResources().getDrawable(indicatorDrawable));
				pointView.setEnabled(false);
				indicatorView.addView(pointView);
				position++;
			}
			viewPager.setAdapter(viewPagerAdapter);
//			viewPagerAdapter.notifyDataSetChanged();
			if (mHandler.hasMessages(0)) {
				mHandler.removeMessages(0);
			}
			mHandler.sendEmptyMessageDelayed(0, 3000);
		}
	}

	/**
	 * 从url地址创建imageview对象，同时初始化指示器
	 */
	private void createImageView2(List<String> rls){
		if(rls != null && rls.size() > 0){
			View pointView;
			int position = 0;
			while(position < rls.size()){
				pointView = new View(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(indicatorSize),(int)(indicatorSize));
				if(position != rls.size() -1)//由于是居中，所以最后一个不需要margin
					params.rightMargin = (int)indicatorInterval;
				pointView.setLayoutParams(params);
				pointView.setBackgroundDrawable(context.getResources().getDrawable(indicatorDrawable));
				pointView.setEnabled(false);
				indicatorView.addView(pointView);
				position++;
			}

			viewPager.setAdapter(viewPagerAdapter);
//			viewPagerAdapter.notifyDataSetChanged();
			if (mHandler.hasMessages(0)) {
				mHandler.removeMessages(0);
			}
			mHandler.sendEmptyMessageDelayed(0, 3000);
		}
	}
	
	/**
	 * 设置当前选中页面
	 * @param position
	 */
	private void setSelectPage(int position){
		for(int index=0; index < indicatorView.getChildCount();index++){
			if(position == index){
				if(indicatorBigSize > 0){//增加变大判断
					LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) indicatorView.getChildAt(index).getLayoutParams();
					params.width = params.height = (int)indicatorBigSize;
				}
				indicatorView.getChildAt(index).setEnabled(true);
			}else{
				if(!indicatorView.getChildAt(index).isEnabled())continue;
				if(indicatorBigSize > 0){//恢复初始状态
					LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) indicatorView.getChildAt(index).getLayoutParams();
					params.width = params.height = (int)indicatorSize;
				}
				indicatorView.getChildAt(index).setEnabled(false);
			}
		}
	}
	
	/**
	 * 设置图片的地址，从网络加载图片
	 * @param imageUrls
	 */
	public void addImageUrls(List<Bitmap> imageUrls) {
		this.imageUrls.addAll(imageUrls);
		numberPages = this.imageUrls.size();
		pageCount = 2 * numberPages;
		createImageView(imageUrls);
	}

	/**
	 * 设置图片的地址，从网络加载图片
	 * @param rls
	 */
	public void addUrls(List<String> rls) {
		this.rls.addAll(rls);
		numberPages = this.rls.size();
		LogUtils.d("ViewPagerBanner", "numberPages=" + numberPages);
		pageCount = 2 * numberPages;
		createImageView2(rls);
	}

	/**
	 * 获取ViewPager对象
	 * @return
	 */
	public ViewPager getViewPager() {
		return viewPager;
	}

	/**
	 * 获取指示器
	 * @return
	 */
	public LinearLayout getIndicatorView() {
		return indicatorView;
	}

	/**
	 * 设置点击ViewPager中的ImageView的监听
	 * @param viewPagerClick
	 */
	public void setViewPagerClick(ViewPagerClick viewPagerClick) {
		this.viewPagerClick = viewPagerClick;
	}

	public interface ViewPagerClick{
		public void viewPagerOnClick(View view);
	}
}
