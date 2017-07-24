package com.doormaster.topkeeper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义标题栏
 * Created by Liukebing on 2017/2/16.
 */

public class TitleBar extends RelativeLayout {
    protected RelativeLayout leftLayout;
    protected ImageView leftImage;
    protected RelativeLayout rightLayout;
    protected ImageView rightImage;
    protected TextView titleView;
    protected RelativeLayout titleLayout;

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(com.doormaster.topkeeper.R.layout.topkeeper_widget_title_bar, this);
        leftLayout = (RelativeLayout) findViewById(com.doormaster.topkeeper.R.id.left_layout);
        leftImage = (ImageView) findViewById(com.doormaster.topkeeper.R.id.left_image);
        rightLayout = (RelativeLayout) findViewById(com.doormaster.topkeeper.R.id.right_layout);
        rightImage = (ImageView) findViewById(com.doormaster.topkeeper.R.id.right_image);
        titleView = (TextView) findViewById(com.doormaster.topkeeper.R.id.title);
        titleLayout = (RelativeLayout) findViewById(com.doormaster.topkeeper.R.id.root);

        parseStyle(context, attrs);
    }

    private void parseStyle(Context context, AttributeSet attrs){
        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, com.doormaster.topkeeper.R.styleable.TopkeeperTitleBar);
            String title = ta.getString(com.doormaster.topkeeper.R.styleable.TopkeeperTitleBar_titleBarTitle);
            titleView.setText(title);

            Drawable leftDrawable = ta.getDrawable(com.doormaster.topkeeper.R.styleable.TopkeeperTitleBar_titleBarLeftImage);
            if (null != leftDrawable) {
                leftImage.setImageDrawable(leftDrawable);
            }
            Drawable rightDrawable = ta.getDrawable(com.doormaster.topkeeper.R.styleable.TopkeeperTitleBar_titleBarRightImage);
            if (null != rightDrawable) {
                rightImage.setImageDrawable(rightDrawable);
            }

            Drawable background = ta.getDrawable(com.doormaster.topkeeper.R.styleable.TopkeeperTitleBar_titleBarBackground);
            if(null != background) {
                titleLayout.setBackgroundDrawable(background);
            }

            ta.recycle();
        }
    }

    public void setLeftImageResource(int resId) {
        leftImage.setImageResource(resId);
    }

    public void setRightImageResource(int resId) {
        rightImage.setImageResource(resId);
    }

    public void setLeftLayoutClickListener(OnClickListener listener){
        leftLayout.setOnClickListener(listener);
    }

    public void setRightLayoutClickListener(OnClickListener listener){
        rightLayout.setOnClickListener(listener);
    }

    public void setLeftLayoutVisibility(int visibility){
        leftLayout.setVisibility(visibility);
    }

    public void setRightLayoutVisibility(int visibility){
        rightLayout.setVisibility(visibility);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setBackgroundColor(int color){
        titleLayout.setBackgroundColor(color);
    }

    public RelativeLayout getLeftLayout(){
        return leftLayout;
    }

    public RelativeLayout getRightLayout(){
        return rightLayout;
    }
}
