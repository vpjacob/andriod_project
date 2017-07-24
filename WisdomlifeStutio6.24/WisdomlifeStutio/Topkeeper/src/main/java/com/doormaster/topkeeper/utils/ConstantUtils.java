package com.doormaster.topkeeper.utils;

import android.content.Context;

import com.doormaster.topkeeper.R;


public class ConstantUtils {
	public static final int MOBILE_PHONE_ACCESS = 0x00;
	public static final int TOPKEEPER = 0x01; //达管家
	public static final int DINGDING_OPEN = 0x02;//叮叮开门
	public static final int BCKJ = 0x03;//BCKJ
	public static final int XFGJ = 0x04;//科马科技
//	public static final int YOHO = 0x05;//YOHO
	public static final int HSH = 0x05;//科马科技

	/**
	 * 修改时，同时修改values和values-en中string.xml里的app_name属性,android manifest中application的icon属性,更换包名
	 * */
	private static int logo = TOPKEEPER;

	// app英文名称
	private static String[] app_name_en = {
			/*getResString(R.string.app_name_en_mobile_access),
			getResString(R.string.app_name_en),
			getResString(R.string.app_name_en_doorshopkeeper),
			getResString(R.string.app_name_en_mobile_access),
			getResString(R.string.app_name_boke_en), "",
			getResString(R.string.app_name_en),
			getResString(R.string.app_name_en_mobile_access),
			getResString(R.string.app_name_en_xulong),// xulong
			getResString(R.string.app_name_en_access_norson)*/ }; // 诺森

	// app图标
	private static int[] iconResource = {
		R.drawable.logo_dgj,
		R.drawable.logo_dgj,
		R.drawable.logo_clkj,
		R.drawable.logo_bckj,
		R.drawable.logo_xfgj,
		R.drawable.logo_hsh};


	// 启动界面
	private static int[] splashResource = { 
		R.drawable.welcome,
		R.drawable.welcome,
		R.drawable.welcome_clkj,
		R.drawable.welcome_bckj,
		R.drawable.welcome_xfgj,
		R.drawable.welcome_hsh
	};
	//登陆背景图片
	private static int[] loginResource = {
		R.drawable.welcome,
		R.drawable.welcome,
		R.drawable.login_logo_clkj,
		R.drawable.welcome_bckj,
		R.drawable.welcome_xfgj,
		R.drawable.welcome_hsh
	};

	// 设备名称--门管家
	public static int[] dev_type_name_doormaster = {
			R.string.activity_device_type_reader,
			R.string.activity_device_type_access_controller,
			R.string.activity_device_type_lift_controller,
			R.string.activity_device_type_door,
			R.string.activity_device_type_ble_controller,
			R.string.activity_device_type_controller,
			R.string.activity_device_type_touch_controller,
			R.string.activity_device_type_qc_device,
			R.string.activity_device_type_qr_device,
			R.string.activity_device_type_dm_controller,
			R.string.activity_device_type_wifi_touch_controller,
			R.string.activity_device_type_wifi_dm_controller,
			R.string.activity_device_type_wifi_access_controller };
	// 设备名称--中性
	public static int[] dev_type_name_neutral = {
			R.string.activity_device_type_reader_neutral,
			R.string.activity_device_type_access_controller_neutral,
			R.string.activity_device_type_lift_controller_neutral,
			R.string.activity_device_type_door_neutral,
			R.string.activity_device_type_ble_controller_neutral,
			R.string.activity_device_type_controller_neutral,
			R.string.activity_device_type_touch_controller_neutral,
			R.string.activity_device_type_qc_device_neutral,
			R.string.activity_device_type_qr_device_neutral,
			R.string.activity_device_type_dm_controller_neutral,
			R.string.activity_device_type_wifi_touch_controller_neutral,
			R.string.activity_device_type_wifi_dm_controller_neutral,
			R.string.activity_device_type_wifi_access_controller_neutral };

	// 公司名称
	private static String[] company_name = { /*"",
			getResString(R.string.about_doormaster_company),
			getResString(R.string.about_doormaster_company_doorshopkeeper), "",
			getResString(R.string.about_doormaster_boke), "",
			getResString(R.string.about_doormaster_company), "",
			getResString(R.string.about_doormaster_company_xulong),
			getResString(R.string.about_norson_company)*/ };

	// 公司名称(返回id)
	private static int[] company_nameId = { /*R.string.noneString,
			R.string.about_doormaster_company,
			R.string.about_doormaster_company_doorshopkeeper,
			R.string.noneString, R.string.about_doormaster_boke,
			R.string.noneString, R.string.about_doormaster_company,
			R.string.noneString, R.string.about_doormaster_company_xulong,
			R.string.about_norson_company*/ };

	private static String[] company_reserved = { /*"",
			getResString(R.string.about_doormaster_copyright),
			getResString(R.string.about_doormaster_copyright_doorshopkeeper),
			"", "", "", getResString(R.string.about_doormaster_copyright), "",
			"", getResString(R.string.about_doormaster_copyright_norson)*/ };

	// 公司网站链接
	private static String[] company_weblink = { /*"",
			getResString(R.string.about_doormaster_link), "", "", "", "",
			getResString(R.string.about_doormaster_link), "", "",
			getResString(R.string.about_doormaster_link_norson)*/ };

	/**
	private static String[] access_token = {
			"3cc49785c3d917a75ab59e0ddcb66796fbe953d859e7b6e736f83c0e",// MOBILE_PHONE_ACCESS通用
			"a83d47f73bd161d393fa1c2fde8236d8dd847f4L155d080e8ad83356", // intelligoo
	 		"6be6675031a8f141f82a3cfL6286b3a142ceccc1620a65a927b2c819", // topkeeper
			// "6fcfe8058311b1304d8f03bb4651e390b9dabba8804a3ae9e64bf267",
			// //厦门测试
			"db5b911L5c01d4dd8a0a2c308ac6faf74db3f6aae95a8f53fec642e7",// DOORSHOPKEEPER
			"fa9e3b9138683efde5a99cb7d3c3eb27", // 开拓者专用
			"2851bd122d9eea8f84ce20c2313cd7c493a2ecbdad16dbc95daf415a",// 深圳博科
			"a1050db22a918a64c92d376226c95fbLa56fca83a554ca8d3b417900",// 深圳永博
			"65b099041d9c120162415626f27ca0da7ac8a58d20c8c6ba298a9b4c",// 北京汇投access_token
			"96db14faf20cc6e1f0a2d5c84900618aa9a4f1d5f10e1d23", // 微细
			"212614aL87b720d798ce76315d1b046Lcdd77d1dffb9811e73675597",// 旭龙
			"1aaab50d86472cfed06b62492d139d52f786248af230218L",// 诺森
			"f64b1db035b3e191def7bacb61994e86dbe1f5737bf975dcea00c7a3",// 宜居
			"aa9202396bbd852c2b872ef9180f0b12c652e8d35099bfd5a97f66d2"//yoho
	};*/

	private static String[] access_token = {
			"3cc49785c3d917a75ab59e0ddcb66796fbe953d859e7b6e736f83c0e",// MOBILE_PHONE_ACCESS通用
			"6be6675031a8f141f82a3cfL6286b3a142ceccc1620a65a927b2c819", // topkeeper
			"c510132fdb61880eb86d8b69a6a3948952aa87cffade982bb3d8149f", // dingdingkaimen
			"410c2da61babd4f940954878fd4106f75e6ff246620715c9a5263a25",//宝存科技
			"6be6675031a8f141f82a3cfL6286b3a142ceccc1620a65a927b2c819",//幸福管家
//			"aa9202396bbd852c2b872ef9180f0b12c652e8d35099bfd5a97f66d2",//yoho
			"593b28c54f3d6b6418112a94be9801ddd143ba7412f5413a"//hsh
	};

	// "68434f6d2f00d2468792c49dafb037f8672254821706b4da75131f12"};测试

	/*public static boolean isKTZApp() {
		if (logo == ACCESS_CONTROLLER) {
			return true;
		} else {
			return false;
		}
	}*/

	public static boolean isTagApp(int tag) {
		return (logo == tag);
	}

	// 获取英文名
	public static String getAppNameEN() {
		return app_name_en[logo];
	}

	// 获取icon
	public static int getIconRes() {
		return iconResource[logo];
	}

	// splash
	public static int getSplashResource() {
		return splashResource[logo];
	}
	
	//获取登陆背景图片
	public static int getLoginResource(){
		return loginResource[logo];
	}

	// 获取公司名
	public static String getCompanyName() {
		return company_name[logo];
	}

	// 获取公司名(返回id)
	public static int getIdCompanyName() {
		return company_nameId[logo];
	}

	// 获取公司Reserver
	public static String getCompanyResver() {
		return company_reserved[logo];
	}

	// 获取公司网址链接
	public static String getCompanyWebLink() {
		return company_weblink[logo];
	}

	// 获取Access_Token
	public static String getAccessToken() {
		return access_token[logo];
	}

	public static String getResString(Context context, int resource_id) {
		return context.getResources()
				.getString(resource_id);
	}

	// 获取设备类型名称
	public static int[] getDevTypeName() {
		if (logo == TOPKEEPER) {
			return dev_type_name_doormaster;
		} else {
			return dev_type_name_neutral;
		}
	}
}
