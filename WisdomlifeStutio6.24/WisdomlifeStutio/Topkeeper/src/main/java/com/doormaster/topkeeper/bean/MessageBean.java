package com.doormaster.topkeeper.bean;


public class MessageBean {
	
	private String username = null;
	private int id = -1;
	private String sender = null;
	private String send_time = null;
	private int imageType = -1;
	private String imageContent = null;
	private String content = null;
	private String door_no = null;
	private int read = 0;
	
	public static final int MSG_IMG_NULL = -1;
	public static final int MSG_IMG_QRCODE = 0;
	
	public static final int NOT_READ = 0x00;
	public static final int HAS_READ = 0x01;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getSendTime() {
		return send_time;
	}
	public void setSendTime(String send_time) {
		this.send_time = send_time;
	}
	public int getImageType() {
		return imageType;
	}
	public void setImageType(int imageType) {
		this.imageType = imageType;
	}
	public String getImageContent() {
		return imageContent;
	}
	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDoorNo() {
		return door_no;
	}
	public void setDoorNo(String doorNo) {
		this.door_no = doorNo;
	}
	public Integer getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	@Override
	public String toString() {
		return "MessageDom [username=" + username + ", id=" + id + ", sender="
				+ sender + ", send_time=" + send_time + ", imageType="
				+ imageType + ", content="
				+ content + ", door_no=" + door_no + ", read=" + read + "]";
	}
	
}
