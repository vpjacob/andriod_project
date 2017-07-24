package com.doormaster.topkeeper.bean;

public class RecordBean {
	
	public static final int AUTO_UPLOAD = 0;
	public static final int AUTO_UPLOAD_CANCEL = 1;
	public static final int UPLOAD_SUCCESS = 0x00;
	public static final int UPLOAD_FAILED = 0x01;
	
	private int id = 0;
	private String dev_name = null;
	private String dev_mac = null;
	private String dev_sn = null;
	private int upload = UPLOAD_FAILED;
	private String event_time = null;
	private int action_time = 0x00;
	private int op_time = 0;
	private int op_ret = 0x02;
	private String op_user = null;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDevName() {
		return dev_name;
	}
	public void setDevName(String dev_name) {
		this.dev_name = dev_name;
	}
	public String getDevMac() {
		return dev_mac;
	}
	public String getDevSn() {
		return dev_sn;
	}
	public void setDevSn(String dev_sn) {
		this.dev_sn = dev_sn;
	}
	public void setDevMac(String dev_mac) {
		this.dev_mac = dev_mac;
	}
	public int getUpload() {
		return upload;
	}
	public void setUpload(int upload) {
		this.upload = upload;
	}
	public String getEventTime() {
		return event_time;
	}
	public void setEventTime(String event_time) {
		this.event_time = event_time;
	}
	public int getActionTime() {
		return action_time;
	}
	public void setActionTime(int action_time) {
		this.action_time = action_time;
	}
	public int getOptime() {
		return op_time;
	}
	public void setOptime(int op_time) {
		this.op_time = op_time;
	}
	public int getOpRet() {
		return op_ret;
	}
	public void setOpRet(int op_ret) {
		this.op_ret = op_ret;
	}
	public String getOpUser() {
		return op_user;
	}
	public void setOpUser(String op_user) {
		this.op_user = op_user;
	}
	
	@Override
	public String toString() {

		return "id:" + id+ "dev_name:" + dev_name + " dev_sn:"+ dev_sn + "dev_mac:" + dev_mac
				+ " comm_id:"  +upload + " event_time:" + event_time
				+ " action_time:" + action_time +  " op_time:" +op_time
				+ " op_ret:" + op_ret +"op_user:" + op_user;
	}
}
