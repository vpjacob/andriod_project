package com.doormaster.topkeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doormaster.topkeeper.bean.NoticeBean;
import com.doormaster.topkeeper.utils.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * notce
 * Created by Liukebing on 2017/2/21.
 */

public class NoticeAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<NoticeBean> infoList;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    private SimpleDateFormat sdf_datetime;
    private SimpleDateFormat sdf_date;
    private SimpleDateFormat sdf_time;
    private Calendar calendar;
    private Date date;
    public NoticeAdapter(Context context, List<NoticeBean> infoList) {
        this.context = context;
        this.infoList = infoList;
        sdf_datetime = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
        sdf_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf_time = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        calendar = Calendar.getInstance();
        date = calendar.getTime();
    }

    /**
     * 设置item点击事件
     * @param listener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view= LayoutInflater.from(context).inflate(com.doormaster.topkeeper.R.layout.recyclerview_notice_list_item, parent, false);
        view.setOnClickListener(this);
        viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        NoticeBean item = infoList.get(position);
        String community_code = item.getCommunity_code();
        String name = item.getName();
        String start_date = item.getStart_date();
        String end_date = item.getEnd_date();
        String content = item.getContent();

        //处理日期和时间
        String dateStr;
        String timeStr;
        try {
            date = sdf_datetime.parse(start_date);
            dateStr = sdf_date.format(date);
            timeStr = sdf_time.format(date);
        } catch (ParseException e) {
            LogUtils.w(TAG, "Server date format not available");
            dateStr = sdf_date.format(date);
            timeStr = sdf_time.format(date);
            e.printStackTrace();
        }

        itemViewHolder.tv_item_title.setText(name);
        itemViewHolder.tv_item_date.setText(dateStr);
        itemViewHolder.tv_item_time.setText(timeStr);
        itemViewHolder.itemView.setTag(item);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(NoticeBean) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return infoList.size();
//        return 10;
    }

    /**
     * 图文的ViewHolder
     */
    private static class ItemViewHolder extends ViewHolder{

        RelativeLayout recyclerview_notice_list_item;
        TextView tv_item_date;
        TextView tv_item_time;
        TextView tv_item_title;

        //itemView表示对应的item布局
        ItemViewHolder(View itemView) {
            super(itemView);
            recyclerview_notice_list_item = (RelativeLayout) itemView.findViewById(com.doormaster.topkeeper.R.id.recyclerview_notice_list_item);
            tv_item_date = (TextView) itemView.findViewById(com.doormaster.topkeeper.R.id.tv_item_date);
            tv_item_time = (TextView) itemView.findViewById(com.doormaster.topkeeper.R.id.tv_item_time);
            tv_item_title = (TextView) itemView.findViewById(com.doormaster.topkeeper.R.id.tv_item_title);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, NoticeBean data);
    }
}
