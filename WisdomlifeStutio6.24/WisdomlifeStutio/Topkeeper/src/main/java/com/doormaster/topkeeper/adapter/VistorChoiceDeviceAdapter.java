package com.doormaster.topkeeper.adapter;

import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.utils.LogUtils;

import java.util.List;


/**
 * notce
 * Created by Liukebing on 2017/2/21.
 */

public class VistorChoiceDeviceAdapter extends RecyclerView.Adapter<ViewHolder>{

    private static final int TYPE_ACCESS = 0;
    private static final int TYPE_DEVKEY = 1;
    private Context context;

    //Contains all types of dev
    private List<AccessDevBean> allList;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public VistorChoiceDeviceAdapter(Context context, List<AccessDevBean> allList) {

        this.context = context;
        this.allList = allList;
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
        View view= LayoutInflater.from(context).inflate(com.doormaster.topkeeper.R.layout.recyclerview_device_list_item, parent, false);
        viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        AccessDevBean device = allList.get(position);

        if (device != null && device.getDevName() != null && device.getDevName().length() > 0) {
            itemViewHolder.tv_item_title.setText(device.getDevName());
            itemViewHolder.tv_item_content.setText(device.getDevSn());
        } else if (device != null && device.getDevSn() != null && device.getDevSn().length() > 0) {
            itemViewHolder.tv_item_title.setText(device.getDevSn());
            itemViewHolder.tv_item_content.setText(device.getDevSn());
        }
        itemViewHolder.iv_item.setImageResource(com.doormaster.topkeeper.R.mipmap.yoho_device2_grey);
        itemViewHolder.itemView.setTag(device);
        itemViewHolder.iv_item_explain.setTag(device);


    }

    @Override
    public int getItemCount() {
        return allList.size();
//        return 10;
    }

    /**
     * 图文的ViewHolder
     */
    public class ItemViewHolder extends ViewHolder implements View.OnClickListener{

        PercentRelativeLayout handleView;
        ImageView iv_item;
        TextView tv_item_title;
//        TextView tv_item_time;
        TextView tv_item_content;

        ImageView iv_item_explain;

        //itemView表示对应的item布局
        ItemViewHolder(View itemView) {
            super(itemView);
            handleView = (PercentRelativeLayout) itemView.findViewById(com.doormaster.topkeeper.R.id.recyclerview_device_list_item);
            tv_item_title = (TextView) itemView.findViewById(com.doormaster.topkeeper.R.id.tv_item_title);
            tv_item_content = (TextView) itemView.findViewById(com.doormaster.topkeeper.R.id.tv_item_content);
            iv_item = (ImageView) itemView.findViewById(com.doormaster.topkeeper.R.id.iv_item);
            iv_item_explain = (ImageView) itemView.findViewById(com.doormaster.topkeeper.R.id.iv_item_explain);

            handleView.setOnClickListener(this);
            iv_item_explain.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == com.doormaster.topkeeper.R.id.recyclerview_device_list_item) {
                LogUtils.d("ItemViewHolder", "getTag=" + view.getTag());
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, view.getTag(), iv_item, tv_item_title, tv_item_content);
                }

            } else if (i == com.doormaster.topkeeper.R.id.iv_item_explain) {
                LogUtils.d("ItemViewHolder", "getTag=" + view.getTag());
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onDetailClick(view.getTag());
                }

            } else {
            }
        }


    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Object data, ImageView iv_item, TextView tv_item_title, TextView tv_item_content);

        void onDetailClick(Object data);
    }

}
