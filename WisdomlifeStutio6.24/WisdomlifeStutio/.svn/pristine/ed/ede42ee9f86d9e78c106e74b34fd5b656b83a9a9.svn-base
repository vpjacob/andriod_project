package com.doormaster.topkeeper.bean;


import com.doormaster.topkeeper.constant.TimerMsgConstants;

import java.util.HashMap;
import java.util.Map;

public class AccessDevBean {
	private String devSn = null;
	private String devMac = null;
	private String devName = null;
	private int devType = 0x01;
	private int privilege = 0x04;
	private int openType = 0x01;
	private int verified = 0x03;
	private String startDate = null;
	private String endDate = null;
	private int useCount = 0x00;
	private String eKey = null;
	private int encrytion = 0x00;
	private int netWorkSupport = 0;

	private int orderNum = 1000;
	private String username;

	private String dbname_company = null;
	private int door_no = -1;
	private String cardno = null;
	private String function = null;
	private String devManagerPassword = null;
	private int enable = 0x01;
	private int shakeOpen = 0;
	private int autoOpen = 0;
	//来源
	public static final int DEV_FROM_DISTRIBUTE = 1;    //云端分配
	public static final int DEV_FROM_SCAN = 2;    //扫描添加
	public static final int DEV_FROM_APP = 3;    //app消息发送
		
	//权限
	public static final int DEV_PRIVILEGE_SUPER = 1;
	public static final int DEV_PRIVILEGE_ADMIN = 2;
	public static final int DEV_PRIVILEGE_USER = 4;

	//设备认证方式
	public static final String DEV_VALID_TYPE =
			"com.intelligoo.app.domain.DeviceDom.DEV_VALID_TYPE";
	public static final int DEV_VALID_TYPE_TIME = 0x01;
	public static final int DEV_VALID_TYPE_COUNTS = 0x02;
	public static final int DEV_VALID_TYPE_MULT = 0x03;

	public static final int WIFI_UNENABLE = 0;
	public static final int WIFI_ENABLE = 1;
//	 (1, (u'门禁读头')),
//	    (2, (u'门禁一体机')),
//	    (3, (u'梯控读头(离线)')),
//	    (4, (u'无线锁')),
//	    (5, (u'蓝牙遥控模块')),
//	    (6, (u'门禁控制器')),
//	    (7, (u'触摸开关门禁')),
//	    (8, (u'可视对讲')),
//	    (9, (u'二维码设备')),
	//类型
public static final int DEV_TYPE_READER = 1;    //D100门禁读头
	public static final int DEV_TYPE_ACCESS_CONTROLLER = 2;    //M100门禁一体机
	public static final int DEV_TYPE_LIFT_CONTROLLER = 3;    //LC100梯控读头
	public static final int DEV_TYPE_LOCK = 4;    //BL100蓝牙锁
	public static final int DEV_TYPE_BLE_CONTROLER = 5; //BC100蓝牙控制模块  0x06为服务器定义的控制器
	public static final int Dev_Type_CONTROLER = 6;//门禁控制器
	public static final int DEV_TYPE_TOUCH_SWITCH = 7; //T100触摸门禁
	public static final int DEV_TYPE_QCCODE_DEVICE = 8; //Q200二维码一体机
	public static final int DEV_TYPE_QRCODE_DEVICE = 9;//二QD100二维码读头
	public static final int DEV_TYPE_DM_DEVICE = 10; //M160门禁一体机

	public static final int DEV_TYPE_TOUCH_CONTROLLER = 11;//'T200触摸门禁'
	public static final int DEV_TYPE_M260_WIFI_ACCESS_DEVICE = 12;//'M260 WiFi门禁一体机
	public static final int DEV_TYPE_M200_WIFI_ACCESS_DEVICE = 13;//'M200 WiFi门禁一体机'
	public static final int DEV_TYPE_DM_MVDP = 30; //可视对讲,'门口机'
	public static final int DEV_TYPE_INDOOR_DEVICE = 31;//'室内机'
		
	public static final String USER_PHONE = "com.intelligoo.sdk.DeviceModel.USER_PHONE";
	public static final String DEVICE_KEY = "com.intelligoo.sdk.DeviceModel.DEVICE_KEY";
	public static final String DEV_FROM = "com.intelligoo.sdk.DeviceModel.DEV_FROM";
	public static final String DEV_FROM_PHONE = "com.intelligoo.sdk.DeviceModel.DEV_FROM_PHONE";

	public static final String DEVICE_SN = "com.intelligoo.app.domain.DeviceDom.DEVICE_SN";
	public static final String DEVICE_MAC = "com.intelligoo.app.domain.DeviceDom.DEVICE_MAC";
	public static final String DEVICE_NAME = "com.intelligoo.app.domain.DeviceDom.DEVICE_NAME";
	public static final String DEVICE_MANAGER_PWD = "com.intelligoo.app.domain.DeviceDom.DEVICE_MANAGER_PWD";
	public static final String DEVICE_SYC_TIME = "com.intelligoo.app.domain.DeviceDom.DEVICE_SYC_TIME";
	public static final String USER_NAME = "com.intelligoo.app.domain.DeviceDom.USER_NAME";

	public static final String DEVICE_TYPE = "com.intelligoo.app.domain.DeviceDom.DEVICE_TYPE";
	public static final String PRIVILEGE = "com.intelligoo.app.domain.DeviceDom.PRIVILEGE";

	public static final int USEFUL = 0x00;
	public static final int NOT_ARRIVED = 0x01;
	public static final int OUT_DATE = 0x02;

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	public String getDevMac() {
		return devMac;
	}

	public void setDevMac(String devMac) {
		this.devMac = devMac;
	}

	public int getDevType() {
		return devType;
	}

	public void setDevType(int devType) {
		this.devType = devType;
	}

	public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}

	public int getOpenType() {
		return openType;
	}

	public void setOpenType(int openType) {
		this.openType = openType;
	}

	public int getVerified() {
		return verified;
	}

	public void setVerified(int verified) {
		this.verified = verified;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	public String geteKey() {
		return eKey;
	}

	public void seteKey(String eKey) {
		this.eKey = eKey;
	}

	public int getEncrytion() {
		return encrytion;
	}

	public void setEncrytion(int encrytion) {
		this.encrytion = encrytion;
	}

	public int getNetWorkSupport() {
		return netWorkSupport;
	}

	public void setNetWorkSupport(int netWorkSupport) {
		this.netWorkSupport = netWorkSupport;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getDevManagerPassword() {
		return devManagerPassword;
	}

	public void setDevManagerPassword(String devManagerPassword) {
		this.devManagerPassword = devManagerPassword;
	}

	public String getDbname_company() {
		return dbname_company;
	}

	public void setDbname_company(String dbname_company) {
		this.dbname_company = dbname_company;
	}

	public int getDoor_no() {
		return door_no;
	}

	public void setDoor_no(int door_no) {
		this.door_no = door_no;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public int getShakeOpen() {
		return shakeOpen;
	}

	public void setShakeOpen(int shakeOpen) {
		this.shakeOpen = shakeOpen;
	}

	public int getAutoOpen() {
		return autoOpen;
	}

	public void setAutoOpen(int autoOpen) {
		this.autoOpen = autoOpen;
	}

	@Override
	public String toString() {
		return "AccessDevBean{" +
				"devSn='" + devSn + '\'' +
				", devMac='" + devMac + '\'' +
				", devName='" + devName + '\'' +
				", devType=" + devType +
				", privilege=" + privilege +
				", openType=" + openType +
				", verified=" + verified +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", useCount=" + useCount +
				", eKey='" + eKey + '\'' +
				", encrytion=" + encrytion +
				", netWorkSupport=" + netWorkSupport +
				", orderNum=" + orderNum +
				", username='" + username + '\'' +
				", dbname_company='" + dbname_company + '\'' +
				", door_no=" + door_no +
				", cardno='" + cardno + '\'' +
				", function='" + function + '\'' +
				", devManagerPassword='" + devManagerPassword + '\'' +
				", enable=" + enable +
				", shakeOpen=" + shakeOpen +
				", autoOpen=" + autoOpen +
				'}';
	}

	/**
	 * 重写的equals方法 只比较mac
	 * */
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}

		if (o instanceof AccessDevBean){
			AccessDevBean device = (AccessDevBean) o;
			return device.getDevMac().equalsIgnoreCase(devMac);
		}

		return false;
	}

	// 获取设备支持的功能参数
	public static Map<String, String> getDevFuncMap(String function)
	{
		Map<String, String> funcMap = new HashMap<String, String>();
		if (function == null || "".equals(function))
			return funcMap;
		String binStr = "", tmp;
		for (int i = 0; i < function.length(); i++)
		{
			tmp = "0000" + Integer.toBinaryString(Integer.parseInt(function.substring(i, i + 1), 16));
			binStr += tmp.substring(tmp.length() - 4);
		}
//        System.out.println("========binStr:" + binStr);
		int index = 0;
		for (int i = binStr.length()-1; i >= 0; i--)
		{
//        	System.out.println("========index:" + index);
			if (TimerMsgConstants.paramList.size() > index && "1".equals(binStr.substring(i, i+1)))
			{
				funcMap.put(TimerMsgConstants.paramList.get(index), "");
			}
			index++;
		}
		return funcMap;
	}

}
