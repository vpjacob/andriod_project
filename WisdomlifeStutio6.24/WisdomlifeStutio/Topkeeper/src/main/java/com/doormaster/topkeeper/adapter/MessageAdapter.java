package com.doormaster.topkeeper.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doormaster.topkeeper.R;
import com.doormaster.topkeeper.activity.BaseApplication;
import com.doormaster.topkeeper.bean.MessageBean;
import com.doormaster.topkeeper.db.MessageData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;


public class MessageAdapter extends BaseAdapter {
	
	private List<MessageBean> messageList;
	private LayoutInflater mInflator;
	private static MessageData msgData;
	private Activity context;
	private static Map<Integer, Bitmap> imagesMap = new HashMap<Integer, Bitmap>();

	public MessageAdapter(Activity context,List<MessageBean> messageList){
		
		super();
		imagesMap.clear();
		MessageAdapter.this.context = context;
		this.messageList = messageList;
//		msgData = new MessageData(BaseApplication.getContext());
//		if(sender!=null){
//			messageList.addAll(msgData.getMsgContent(username, sender));
//		}
//		else{
//			return;
//		}
		mInflator = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public MessageBean getMsg(int position) {
		// TODO Auto-generated method stub
		return messageList.get(position);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(convertView ==null){
			convertView = mInflator.inflate(R.layout.item_frag_msglist, null);
			viewHolder = new ViewHolder();
			viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.item_single_msg);
			viewHolder.sendTime = (TextView) convertView.findViewById(R.id.frag_contact_msg_time);
			viewHolder.content =  (TextView) convertView.findViewById(R.id.frag_contact_msg_content);
			viewHolder.shareTips = (TextView) convertView.findViewById(R.id.frag_contact_msg_tips);
			viewHolder.imageViewMsg = (ImageView) convertView.findViewById(R.id.frag_contact_msg_content_img);
			viewHolder.mLayoutContent = (LinearLayout) convertView.findViewById(R.id.frag_ly_contact_msg);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(messageList!=null){
			MessageBean message = getMsg(position);
			if (message.getImageType() == MessageBean.MSG_IMG_QRCODE &&
					message.getImageContent() !=null && message.getImageContent().length() != 0 ) 
			{
				//这里设置二维码图片
				String encode = message.getImageContent();
				byte[] data = Base64.decode(encode.trim(), Base64.DEFAULT);
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				viewHolder.imageViewMsg.setImageBitmap(bitmap);
				viewHolder.imageViewMsg.setVisibility(View.VISIBLE);
				viewHolder.shareTips.setVisibility(View.VISIBLE);
				final ImageView view = (ImageView) View.inflate(context, R.layout.qrcode_view, null);
				view.setImageDrawable(viewHolder.imageViewMsg.getDrawable());
				imagesMap.put(Integer.valueOf(position), bitmap);
				final AlertDialog dialog = new AlertDialog.Builder(context).setView(view).setTitle("请求的二维码").create();
				viewHolder.mLayoutContent.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.show();
					}
				});

				viewHolder.mLayoutContent.setOnLongClickListener(new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View arg0) {
						// TODO Auto-generated method stub
//						ImageView imageView =
//						imageView.buildDrawingCache();
						Bitmap saveBitmap = imagesMap.get(Integer.valueOf(position));
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
						byte[] byteArray = stream.toByteArray();
						if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
							File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/doormasterQRPhoto");
							if(!dir.isFile())
							{
								dir.mkdir();
							}else
							{
								dir.delete();
								dir.mkdir();
							}
							File file = new File(dir, "qrPhoto.png");
							try{
								FileOutputStream fos = new FileOutputStream(file);
								fos.write(byteArray, 0, byteArray.length);
								fos.flush();
								fos.close();
							} catch(IOException e){
								e.printStackTrace();
							}
						}
						showShare();
						return false;
					}
				});
			}
			else
			{
				viewHolder.imageViewMsg.setVisibility(View.GONE);
				viewHolder.shareTips.setVisibility(View.GONE);
				viewHolder.mLayoutContent.setOnClickListener(null);
				viewHolder.mLayoutContent.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {

						AlertDialog.Builder deletMsgDialog = new Builder(context);
						deletMsgDialog.setTitle(R.string.remind);
						deletMsgDialog.setMessage(R.string.remind_of_delete_msg);
						deletMsgDialog.setPositiveButton(R.string.lock_info_input_ensure, new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								msgData.deleteSingleContact(messageList.get(position).getId());
								messageList.remove(position);
								notifyDataSetChanged();
								dialog.dismiss();
							}
						});
						
						deletMsgDialog.setNegativeButton(R.string.lock_info_input_cancel, new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						deletMsgDialog.create().show();
						return true;
					}
				});
			}
			viewHolder.content.setText(message.getContent());
			viewHolder.sendTime.setText(message.getSendTime());
		}
		
		viewHolder.layout.setLongClickable(true);
		
		
		return convertView;
	}
	
	private void showShare() {
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		 // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
		 oks.setTitle("标题");
		 // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
		 oks.setTitleUrl("http://www.baidu.com");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("分享二维码");
		 
//		 //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
////		 oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
//		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//		 // url仅在微信（包括好友和朋友圈）中使用
//		 oks.setUrl("http://sharesdk.cn");
//		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		 oks.setComment("我是测试评论文本");
//		 // site是分享此内容的网站名称，仅在QQ空间使用
//		 oks.setSite("ShareSDK");
//		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		 oks.setSiteUrl("http://sharesdk.cn");
		 
		 oks.setImagePath(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/doormasterQRPhoto/qrPhoto.png");
		 
//		 oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
//			
//			@Override
//			public void onShare(Platform platform, ShareParams paramsToShare) {
//				// TODO Auto-generated method stub
//				paramsToShare.setText(null);  
//                paramsToShare.setTitle(null);  
//                paramsToShare.setImagePath(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/doormasterQRPhoto/qrPhoto.png"); 
//			}
//		});
		 
		 oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
	            @Override
	            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
	            	paramsToShare.setText(null);  
                    paramsToShare.setTitle(null);  
                    paramsToShare.setTitleUrl(null);  
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/doormasterQRPhoto/qrPhoto.png");
	  
	            }  
	        }); 
		 
//		 oks.setPlatform("Wechat"); //这样可以直接分享，而不经过选择
		 
		// 启动分享GUI
		 oks.show(BaseApplication.getInstance());
	}
	
	public void clear(){
		
		messageList.clear();
	}
	
	class ViewHolder {
		TextView content;
		TextView sendTime;
		ImageView imageViewMsg;
		LinearLayout mLayoutContent;
		LinearLayout layout;
		TextView shareTips;
	}
	
}
