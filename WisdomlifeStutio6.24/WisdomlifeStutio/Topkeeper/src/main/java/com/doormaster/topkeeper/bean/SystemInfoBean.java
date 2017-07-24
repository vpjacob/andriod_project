package com.doormaster.topkeeper.bean;

public class SystemInfoBean {
	private String username = null;
	
	private String dev_mac = null;
	private int wiegand = WIEGAND_26;
	private int open_delay = 0x05;
	private int reg_card_num = 0x00;
	private int reg_phone_num = 0x00;
	private int version = 0x01;
	private int control_way = 0x00;
	private int max_container = 0x03E8;
	
	public static final int WIEGAND_26 = 0x1a;
	public static final int WIEGAND_34 = 0x22;
	public static final int CONTROL_lOCK = 0X00;
	public static final int CONTROL_ELECTRIC = 0X01; 
	
//	public static final String WIEGAND = "com.intelligoo.app.domain.SystemInfoDom.WIEGAND";
//	public static final String CONTROL = "com.intelligoo.app.domain.SystemInfoDom.CONTROL";
//	public static final String OPEN_DELAY = "com.intelligoo.app.domain.SystemInfoDom.OPEN_DELAY";
//	public static final String REG_CARDS_NUMS = 
//			"com.intelligoo.app.domain.SystemInfoDom.REG_CARDS_NUMS";
//	public static final String REG_PHONE_NUMS = 
//			"com.intelligoo.app.domain.SystemInfoDom.REG_PHONE_NUMS";
//	public static final String MAX_CONTAINER = 
//			"com.intelligoo.app.domain.SystemInfoDom.MAX_CONTAINER";
//	public static final String DEV_SYSTEM_VERSION = 
//			"com.intelligoo.app.domain.SystemInfoDom.DEV_VERSION";
	
	public static final String WIEGAND = "com.intelligoo.sdk.ConstantsUtils.WIEGAND";
	public static final String CONTROL = "com.intelligoo.sdk.ConstantsUtils.CONTROL";
	public static final String OPEN_DELAY = "com.intelligoo.sdk.ConstantsUtils.OPEN_DELAY";
	public static final String REG_CARDS_NUMS =
			"com.intelligoo.sdk.ConstantsUtils.REG_CARDS_NUMS";
	public static final String REG_PHONE_NUMS =
			"com.intelligoo.sdk.ConstantsUtils.REG_PHONE_NUMS";
	public static final String MAX_CONTAINER =
			"com.intelligoo.sdk.ConstantsUtils.MAX_CONTAINER";
	public static final String DEV_SYSTEM_VERSION =
			"com.intelligoo.sdk.ConstantsUtils.DEV_VERSION";
	
	
	public void reinit()
	{
		wiegand = WIEGAND_26;
		open_delay = 0x05;
		version = 0x01;
		control_way = 0x00;
		max_container = 0x03E8;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDevMac() {
		return dev_mac;
	}
	
	public void setDevMac(String dev_mac) {
		this.dev_mac = dev_mac;
	}
	
	public int getWiegand() {
		return wiegand;
	}
	
	public void setWiegand(int wiegand) {
		this.wiegand = wiegand;
	}
	
	public int getOpenDelay() {
		return open_delay;
	}
	
	public void setOpenDelay(int open_delay) {
		this.open_delay = open_delay;
	}
	
	public int getRegCardNum() {
		return reg_card_num;
	}
	
	public void setRegCardNum(int reg_card_num) {
		this.reg_card_num = reg_card_num;
	}
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public int getControlWay() {
		return control_way;
	}
	
	public void setControlWay(int control_way) {
		this.control_way = control_way;
	}
	
	public int getMaxContainer() {
		return max_container;
	}
	
	public void setMaxContainer(int max_container) {
		this.max_container = max_container;
	}
	
	public int getRegPhoneNum() {
		return reg_phone_num;
	}
	
	public void setRegPhoneNum(int num_reg_by_phone) {
		this.reg_phone_num = num_reg_by_phone;
	}
	
	@Override
	public String toString() {
		String info = "username:" + username +"dev_mac:" + dev_mac + "wiegand:" + wiegand
				+ "open_delay:" + open_delay +"reg_card_num" +reg_card_num +"version:" + version 
				+"reg_phone_num" + reg_phone_num + "max_container"+max_container;
		return info;
	}
}
