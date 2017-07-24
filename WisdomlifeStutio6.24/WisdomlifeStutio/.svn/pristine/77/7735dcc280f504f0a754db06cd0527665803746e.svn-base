package com.doormaster.topkeeper.bean;

import com.doormaster.topkeeper.constant.Constants;

public class UserBean {
	
	private String userName = null;
	private String passWord = null;
	private String nickName = null;
	private String clientID = null;
//	private String cardNo = null;
	private boolean shake_button = false;
	private String identity = null;
	private String token_pwd = null;
	private String pin = null;
	private String cardno = null;
	private String auto_upload_event = null;
//	private int isMD5 = ;

	private int shake_open = 1;//0:开启   1：关闭
	private int shake_open_progress = Constants.SHAKE_DEFAUT_PROGRESS;

	private int auto_open = 1;//0:开启   1：关闭
	private int auto_open_progress = Constants.AUTO_DEFAUT_PROGRESS;
	private int enable_auto_open_space=1;//自动开门间隔开关, 0:开启   1：关闭
	private int auto_open_space_time=30;//自动开门间隔时间（单位：秒）
	
	public static final String USER_IDENTITY =
			"com.intelligoo.app.domain.UserDom.USER_IDENTITY";
	
	public static final int MD5_NO = 0;
    public static final int MD5_YES = 1;
    
    public static final int SHAKE_ON = 0;
    public static final int SHAKE_OFF= 1;

    public static final int AUTO_OPEN_ON = 0;
    public static final int AUTO_OPEN_OFF= 1;

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
//	public String getIsMD5() {
//		return isMD5;
//	}
//	public void setIsMD5(String isMD5) {
//		this.isMD5 = isMD5;
//	}
	//cardno 改存在用户卡列表里
	/*public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}*/
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public boolean isShake_button() {
		return shake_button;
	}
	public void setShake_button(boolean shake_button) {
		this.shake_button = shake_button;
	}

	public String getToken_pwd() {
		return token_pwd;
	}

	public void setToken_pwd(String token_pwd) {
		this.token_pwd = token_pwd;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getAuto_upload_event() {
		return auto_upload_event;
	}

	public void setAuto_upload_event(String auto_upload_event) {
		this.auto_upload_event = auto_upload_event;
	}

	public int getShake_open() {
		return shake_open;
	}

	public void setShake_open(int shake_open) {
		this.shake_open = shake_open;
	}

	public int getShake_open_progress() {
		return shake_open_progress;
	}

	public void setShake_open_progress(int shake_open_progress) {
		this.shake_open_progress = shake_open_progress;
	}

	public int getAuto_open() {
		return auto_open;
	}

	public void setAuto_open(int auto_open) {
		this.auto_open = auto_open;
	}

	public int getAuto_open_progress() {
		return auto_open_progress;
	}

	public void setAuto_open_progress(int auto_open_progress) {
		this.auto_open_progress = auto_open_progress;
	}

	public int getEnable_auto_open_space() {
		return enable_auto_open_space;
	}

	public void setEnable_auto_open_space(int enable_auto_open_space) {
		this.enable_auto_open_space = enable_auto_open_space;
	}

	public int getAuto_open_space_time() {
		return auto_open_space_time;
	}

	public void setAuto_open_space_time(int auto_open_space_time) {
		this.auto_open_space_time = auto_open_space_time;
	}

	@Override
	public String toString() {
		String userdom = "username:"+ userName +" password:"+ passWord +" nickName:"
				+nickName + " clientID:"+ clientID+ " identity:" + identity;
		return userdom;
	}
}
