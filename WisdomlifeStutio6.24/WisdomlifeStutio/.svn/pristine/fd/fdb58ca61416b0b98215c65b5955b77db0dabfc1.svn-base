package com.doormaster.topkeeper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.bean.MessageBean;
import com.doormaster.topkeeper.constant.Constants;
import com.doormaster.topkeeper.db.MessageData;
import com.doormaster.topkeeper.fragment.MsgActivity;
import com.doormaster.topkeeper.receiver.TimerMsgReceiver;
import com.doormaster.topkeeper.utils.LogUtils;
import com.doormaster.topkeeper.utils.SPUtils;
import com.doormaster.topkeeper.view.BadgeView;
import com.doormaster.topkeeper.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liukebing on 2017/5/4.
 */

public class ContactsActivity extends BaseActivity {

    //界面设置
    private ListView msgList;
    private ContactAdapter adapter;
    private Activity mActivity;
    //	private ImageButton mImgFind;
//	private LinearLayout mBack;
	public TitleBar titleBar;
    private BroadcastReceiver contactRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.d("contacts: " + action);

            if (action.equalsIgnoreCase(TimerMsgReceiver.MSG_REC_EXTRA)) {
                msgList = (ListView) findViewById(R.id.frag_contacts_list);
                adapter = new ContactAdapter(mActivity);
                msgList.setAdapter(adapter);
            }
        }
    };

    private IntentFilter filter = new IntentFilter(TimerMsgReceiver.MSG_REC_EXTRA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mActivity = this;
        msgList = (ListView) findViewById(R.id.frag_contacts_list);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        adapter = new ContactAdapter(mActivity);
        msgList.setAdapter(adapter);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(contactRec, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(contactRec);
    }

    private static class ContactAdapter extends BaseAdapter {

        private static ArrayList<String> senderList;
        private LayoutInflater mInflator;
        private MessageData messageData;
        private Activity context;
        private BadgeView badgeView;
        private String username;

        public ContactAdapter(Activity context){

            this.context = context;
            senderList = new ArrayList<String>();
            messageData = new MessageData(context.getApplicationContext());
            username = SPUtils.getString(Constants.USERNAME);
            List<String> list = messageData.getSenderList(username);
            if(!list.isEmpty()){
                senderList.addAll(list);
            }
            mInflator = LayoutInflater.from(context);
        }

//		public void addSenderList(ArrayList<String> sendList){
//			if( sendList!=null ){
//				senderList.addAll(sendList);
//			}
//		}

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return senderList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return senderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder=null;
            if(convertView ==null){
                convertView = mInflator.inflate(R.layout.item_frag_contactslist, null);
                viewHolder = new ViewHolder();
                viewHolder.contact = (LinearLayout) convertView.findViewById(R.id.item_contact_badgeview);
                viewHolder.sender = (TextView) convertView.findViewById(R.id.frag_msg_name);
                viewHolder.sendTime = (TextView) convertView.findViewById(R.id.frag_msg_time);
                viewHolder.content =  (TextView) convertView.findViewById(R.id.frag_msg_info);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if(senderList!=null){

                String sender = senderList.get(position);
                if(sender!=null){
                    viewHolder.sender.setText(sender);
                    if (messageData.getNotReadCount(sender) != 0) {
                        int notRead = messageData.getNotReadCount(sender);
                        View target = viewHolder.contact.findViewById(R.id.item_contact_badgeview);
                        badgeView = new BadgeView(context, target);
                        badgeView.setText(Integer.toString(notRead));
                        badgeView.setBadgeMargin(30);
                        badgeView.show();
                        badgeView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                badgeView.hide();
                            }
                        });
                    }
                    List<MessageBean> messageList = messageData.getMsgContent(username,sender);

                    if(messageList.size()>0){
                        MessageBean message = messageList.get(0);
                        viewHolder.content.setText(message.getContent());
                        viewHolder.sendTime.setText(message.getSendTime());
                    }
                }
            }


            viewHolder.contact.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String sender = senderList.get(position);
//                    Fragment msgFragment = new MessageFragment();
                    messageData.setMsgHasRead(sender);
                    if (badgeView!=null) {
                        badgeView.hide();
                    }
//                    Bundle bundle = new Bundle();
//                    bundle.putString("sender", sender);
//                    msgFragment.setArguments(bundle);
                    //主界面处理
//                    ((TextView) context.findViewById(R.id.ib_frag_title)).setText(sender);
//                    context.findViewById(R.id.ib_frag_back_img).setVisibility(View.VISIBLE);
//                    context.findViewById(R.id.ib_activity_scan_add).setVisibility(View.INVISIBLE);
//					context.findViewById(R.id.main_bottom).setVisibility(View.GONE);

//                    FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
//                    transaction.add(R.id.message_content, msgFragment);
//                    transaction.addToBackStack("contacts");
//                    transaction.commit();
                    Intent intent = new Intent(context, MsgActivity.class);
                    intent.putExtra("sender", sender);
                    context.startActivity(intent);
                }
            });

            viewHolder.contact.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    String sender = senderList.get(position);
                    messageData.setMsgHasRead(sender);
                    if (badgeView!=null) {
                        badgeView.hide();
                    }
                    AlertDialog.Builder deletMsgDialog = new AlertDialog.Builder(context);
                    deletMsgDialog.setTitle(R.string.remind);
                    deletMsgDialog.setMessage(R.string.remind_of_delete_the_contact);
                    deletMsgDialog.setPositiveButton(R.string.lock_info_input_ensure, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            messageData.deleteSenderMsg(username, senderList.get(position));
                            senderList.remove(position);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    deletMsgDialog.setNegativeButton(R.string.lock_info_input_cancel, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                    deletMsgDialog.create().show();
                    return true;
                }
            });

            return convertView;
        }

//		public void clear(){
//
//			senderList.clear();
//		}

        class ViewHolder {

            TextView sender;
            TextView content;
            TextView sendTime;
            LinearLayout contact;
        }

    }
}
