package com.doormaster.topkeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.bean.RecordBean;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * notce
 * Created by Liukebing on 2017/2/21.
 */

public class RecordDoorAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener{

    private Context context;
//    private List<JSONObject> infoList;
    private List<RecordBean> recordList;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public RecordDoorAdapter(Context context, List<RecordBean> recordList) {
        this.context = context;
        this.recordList = recordList;
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
        View view= LayoutInflater.from(context).inflate(R.layout.recyclerview_door_record_list_item, parent, false);
        view.setOnClickListener(this);
        viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        RecordBean item = recordList.get(position);
//        String community_code = item.optString("community_code");
//        String name = item.optString("name");
//        String start_date = item.optString("start_date");
//        String end_date = item.optString("end_date");
//        String content = item.optString("content");

        String title = (item.getDevName()!=null)?item.getDevName():item.getDevSn();
        String str = item.getEventTime();
        String state;
        int op_ret = item.getOpRet();
        if (op_ret == 0) {
            state = context.getString(R.string.open_succeed);
        } else {
            state = context.getString(R.string.open_fail);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
//        SimpleDateFormat sdf_date = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        String strDate = null;
        String strTime = null;
        try {
            Date date=sdf.parse(str);
            strDate = sdf_date.format(date);
            strTime = sdf_time.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        itemViewHolder.tvList.get(1).setText(title + ":");
        itemViewHolder.tvList.get(0).setText(strDate + " " + strTime);
        itemViewHolder.tvList.get(2).setText(state);
        if (op_ret == 0)
        {
            itemViewHolder.tvList.get(2).setTextColor(0xff0eaae3);
        }else {
            itemViewHolder.tvList.get(2).setTextColor(0xffc32900);
        }

        itemViewHolder.itemView.setTag(item);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(JSONObject) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return recordList.size();
//        return 10;
    }

    /**
     * 图文的ViewHolder
     */
    static class ItemViewHolder extends ViewHolder{

//        RelativeLayout recyclerview_record_list_item;
        List<TextView> tvList = new ArrayList<>();


        //itemView表示对应的item布局
        ItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            tvList.add((TextView) itemView.findViewById(R.id.tv_item_date));
            tvList.add((TextView) itemView.findViewById(R.id.tv_item_title));
            tvList.add((TextView) itemView.findViewById(R.id.tv_item_open_state));
//            recyclerview_notice_list_item = (RelativeLayout) itemView.findViewById(R.id.recyclerview_record_list_item);
//            tv_item_date = (TextView) itemView.findViewById(R.id.tv_item_date);
//            tv_item_time = (TextView) itemView.findViewById(R.id.tv_item_time);
//            tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, JSONObject data);
    }
}
