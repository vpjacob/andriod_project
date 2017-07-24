package com.doormaster.topkeeper.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.device_manager.Act_Device_Info;
import com.doormaster.topkeeper.activity.Act_Main;
import com.doormaster.topkeeper.bean.AccessDevBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.AccessDevMetaData;
import com.doormaster.topkeeper.task.ManagerDev;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.utils.TopkeeperModel;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack;

import java.util.ArrayList;

import static com.doormaster.topkeeper.db.AccessDevMetaData.AccessDev.ACCESS_DEV_MAC;
import static com.doormaster.topkeeper.db.AccessDevMetaData.AccessDev.ACCESS_DEV_SN;
import static com.doormaster.topkeeper.db.AccessDevMetaData.AccessDev.ACCESS_DEV_TYPE;

/**
 * Created by Liukebing on 2017/3/31.
 */

public class AccessDeviceAdapter extends BaseAdapter {
    //界面设置
    public ArrayList<SortDev> devList = new ArrayList<>();
    private Activity act;
    private AccessDevMetaData deviceData;
//    private DevListFragment fragment;
    private LayoutInflater mInflator;
    private static final boolean SCANED = true;
    private static final boolean SCANED_NULL = false;

    //蓝牙设置
    BluetoothManager manager;
    BluetoothAdapter mAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    //二次点击、开门处理
    public static boolean pressed = false;
    private String username;
    public AccessDeviceAdapter( Activity activity)
    {
        super();
//        MyLog.Assert(activity != null);
//        MyLog.Assert(devFragment != null);

        act = activity;
//        fragment = devFragment;
        mInflator = LayoutInflater.from(activity);
        manager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = manager.getAdapter();

        deviceData = new AccessDevMetaData(act);
        username = SPUtils.getString(Constants.USERNAME);
        ArrayList<AccessDevBean> devSnList = deviceData.getAllAccessDevList(username);
        if (devSnList == null || devSnList.isEmpty())
        {
            return;
        }
        for (AccessDevBean device : devSnList) {
            SortDev sortDev = new SortDev(device, SCANED_NULL);
            devList.add(sortDev);
        }
    }

    public void sortList (AccessDevBean device) {
        if (device == null || device.getDevSn() == null
                || devList == null || devList.isEmpty()) {
            return;
        }
        SortDev scanDoor = new SortDev(device, SCANED);
        ArrayList<SortDev> temp_list = new ArrayList<SortDev>();
        for (SortDev door : devList) {    //之前扫描到的先添加
            if (door.scaned == SCANED &&
                    (!door.device.getDevSn().equals(device.getDevSn()))) {
                temp_list.add(door);
            }
        }
        temp_list.add(scanDoor);    //加入新扫描的设备
        for (SortDev door : devList ) {    //加入未扫描的设备
            if (!door.device.getDevSn().equals(device.getDevSn()) && door.scaned == SCANED_NULL  ) {
                temp_list.add(door);
            }
        }
        devList = new ArrayList<SortDev>(temp_list);    //更新列表
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return devList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return devList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private SortDev getDev(int position)
    {
        return devList.get(position);
    }
    private int getResId(SortDev dev_sort)
    {
		/*if (dev_sort.device.getDevType() != AccessDevBean.DEV_TYPE_ACCESS_CONTROLLER)
		{
			//处理扫描变色
			if (dev_sort.scaned) {
				return (ContentUtils.getScandImg());
			} else {
				return (ContentUtils.getNotScandImg());
			}
		}*/
        int id;
        switch (dev_sort.device.getPrivilege())
        {
            case AccessDevBean.DEV_PRIVILEGE_SUPER:
                if (dev_sort.scaned) {
                    id = (R.drawable.icon_y1);
                } else {
                    id =  (R.drawable.icon_y);
                }
                break;

            case AccessDevBean.DEV_PRIVILEGE_ADMIN:
                if (dev_sort.scaned) {
                    id = (R.drawable.icon_t);
                } else {
                    id =  (R.drawable.icon_t1);
                }
                break;

            case AccessDevBean.DEV_PRIVILEGE_USER:
            default:
                if (dev_sort.scaned) {
                    id = R.drawable.icon_b;
                } else {
                    id = R.drawable.icon_b1;
                }
                break;
        }

        return id;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if(convertView ==null){
            convertView = mInflator.inflate(R.layout.item_frag_locklist, null);
            viewHolder = new ViewHolder();
            viewHolder.devName = (TextView) convertView.findViewById(R.id.frag_lock_info);
            viewHolder.item = (LinearLayout) convertView.findViewById(R.id.frag_item);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.frag_img_lock);
            viewHolder.devInfoImage = (ImageView) convertView.findViewById(R.id.frag_img_lock_info);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final SortDev dev_sort = getDev(position);
        final int dev_type = dev_sort.device.getDevType();
        final String dev_sn = dev_sort.device.getDevSn();
        final String dev_name = dev_sort.device.getDevName();

        //处理显示名称
        viewHolder.devName.setText(dev_name);
        //处理显示高亮样式
        if(getResId(dev_sort)==R.mipmap.open_blue||dev_sort.scaned){
            int highlightcolor = act.getResources().getColor(R.color.main_highlight_textcolor);
            viewHolder.devName.setTextColor(highlightcolor);
        }else{
            int maintextcolor = act.getResources().getColor(R.color.main_textcolor);
            viewHolder.devName.setTextColor(maintextcolor);
        }
        viewHolder.image.setImageResource(getResId(dev_sort));

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(mAdapter==null||!mAdapter.isEnabled()){
                    Intent enable_ble = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    act.startActivityForResult( enable_ble,REQUEST_ENABLE_BT  );
                    return;
                }
//                fragment.getScanList(false);
                LogUtils.d("touch:" + dev_sort.device.toString());
                if (dev_sort.device.getEnable() == AccessDevBean.NOT_ARRIVED)
                {
                    Toast.makeText(act, R.string.unreach_the_useful_time,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dev_sort.device.getEnable() == AccessDevBean.OUT_DATE)
                {
                    AlertDialog.Builder deletMsgDialog = new AlertDialog.Builder(act);
                    deletMsgDialog.setTitle(R.string.remind);
                    deletMsgDialog.setMessage(R.string.out_of_limit_date);
                    deletMsgDialog.setPositiveButton(R.string.lock_info_input_ensure,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    deviceData.deleteDevice(username, dev_sort.device);
                                    devList.remove(position);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                    deletMsgDialog.setNegativeButton(R.string.lock_info_input_cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            });
                    deletMsgDialog.create().show();
                    return;
                }
                LogUtils.d("open:"+dev_type);
                if (dev_type  != AccessDevBean.DEV_TYPE_LOCK) {
                    //如果是读头
                    LogUtils.d("open:"+dev_type);
                    if (!pressed) {
                        LogUtils.d("open:"+dev_type);
                        pressed = true;
                        TopkeeperModel.openDevice(dev_sort.device, new DMModelCallBack.DMCallback() {
                            @Override
                            public void setResult(int i, DMException e) {
                                pressed = false;
                            }
                        });
                    } else {
                        Toast.makeText(act,R.string.operationing, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    AccessDevBean dev = deviceData.queryAccessDeviceByDevSn(username,dev_sn);
                    if (dev != null) {
                        dev.setDevType(AccessDevBean.DEV_TYPE_ACCESS_CONTROLLER);
                        deviceData.saveAccessDev(dev);
                    }

                }
            }
        });

        viewHolder.devInfoImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act,Act_Device_Info.class);
                intent.putExtra(ACCESS_DEV_SN, dev_sn);
                intent.putExtra(ACCESS_DEV_MAC, getDev(position).device.getDevMac());
                intent.putExtra(ACCESS_DEV_TYPE, dev_type);
                act.startActivityForResult(intent, Act_Main.MAIN_ACTIVITY_REQ_CODE);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView devName;
        LinearLayout item;
        ImageView image;
        ImageView devInfoImage;
    }

    public interface onTouchListener {
        void onTouch(AccessDevBean device, ManagerDev.ManagerCallback callback);
    }

    public static void setButton() {
        pressed = false;
    }

    public class SortDev {
        AccessDevBean device = null;
        boolean scaned = false;
        public SortDev(AccessDevBean device, boolean scaned) {
            this.device = device;
            this.scaned = scaned;
        }
    }
}
