package com.doormaster.topkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.bean.AccessDevBean;

import java.util.ArrayList;
import java.util.List;


public class SelectAutoDeviceAdapter extends BaseAdapter {

    private Context mContext;

    private List<AccessDevBean> mDatas;

    private LayoutInflater mInflater;

    private List<String> dataIds;

    public SelectAutoDeviceAdapter(Context mContext, List<AccessDevBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;

        mInflater = LayoutInflater.from(this.mContext);

        dataIds = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.list_item_select_device, null);

            holder = new ViewHolder();

            holder.checkboxOperateData = (CheckBox) convertView.findViewById(R.id.checkbox_operate_data);
            holder.textTitle = (TextView) convertView.findViewById(R.id.text_title);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final AccessDevBean dataBean = mDatas.get(position);
        if (dataBean != null) {
            holder.textTitle.setText(dataBean.getDevName());

            holder.checkboxOperateData.setChecked(dataBean.getAutoOpen()==1);

            //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
            holder.checkboxOperateData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataBean.getAutoOpen()==1) {
                        dataBean.setAutoOpen(0);
                    } else {
                        dataBean.setAutoOpen(1);
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {

        public CheckBox checkboxOperateData;

        public TextView textTitle;

        public TextView textDesc;
    }
}
