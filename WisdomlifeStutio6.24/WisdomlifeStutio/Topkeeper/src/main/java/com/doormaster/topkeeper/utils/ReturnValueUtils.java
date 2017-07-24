package com.doormaster.topkeeper.utils;

import java.util.HashMap;
import java.util.Map;


public class ReturnValueUtils {
	public static final int RET_SUCCESS= 0;
	public static final int ACCESS_TOKEN_NULL = 1009;
	public static final int ACCOUNT_PWD_FAILED = 1020;
	public static final int PHONE_NO_VERIFY = 1021;
	public static final int VERIFY_NUM_WRONG = 1022;
	public static final int VERIFY_NUM_EX_VALID = 1023;
	public static final int ACCOUNT_REGISTERED = 1024;
	public static final int FAILED_CRERATE_COUNT = 1025;
	public static final int FAILED_SEND_SMS_VERIFY = 1026;
	public static final int CHANGE_PWD_FAILED = 1027;
	public static final int CLIENT_ID_NO_EXIST = 1028;
	public static final int PWD_FAILED = 1029;
	public static final int CLIENT_MSG_NOT_MATCH = 1030;
	public static final int ACCOUNT_NO_EXIST = 1031;
	public static final int	ACCOUNT_INCORRECT = 1032;
	public static final int BACK_UP_NO_EXIT = 1033;
	public static final int TIME_FORMAT_ERROR = 1034;
	public static final int ACCOUNT_TOKEN_NO_EXIST = 1035;
	public static final int FEATURE_NO_CURRENTLY_SUPPORT = 1036;
	public static final int USER_OR_DEVICE_WITHOUT_AUTH = 1037;
	
	public static final int THE_ACOUNT_HAS_SEND_KEY = 1039;
	public static final int RECEIVER_IS_NOT_EXIST = 1040;
	
	public static final int CANT_SEND_THE_KEY_TOSELF = 1041;
	public static final int DEVICE_HAVE_NO_FEATURE = 1042;
	public static final int HAVE_NO_PERMISSION_OPERATION = 1043;
	
	public static final int SERVER_FAILED_PROC = 1500;
	public static final int MUST_USE_PARAM = 1501;
	public static final int JSON_DATA_FORMAT_ERROR = 1502;
	public static final int PARAM_VALUE_IS_NO_DEFINE = 1503;
	public static final int PARAM_TYPE_ERROR = 1504;
	public static final int DOES_NOT_SUPPORT_THE_LANGUAGE = 1505;
	public static final int PARAM_IS_NULL = 1506;
	public static final int PARAM_VALUE_MUST_BE_NUMDER = 1507;
	public static final int SERVER_NO_RESONSE = 10061;
	
	public static final int CHECK_NETWORK = -1;

	//验证码获取提示
	public static final int VERIFY_ONE_MININUTES =408;
	public static final int VERIFY_OUT_TWO_ONESEC = 4080;
	public static final int VERIFY_OUT_TWO_ONEMIN = 4081;
	public static final int VERIFY_OUT_FIVE_ONEDAY = 4082;
	public static final int VERIFY_ONE_ONEMIN_ONE = 4083;
	public static final int VERIFY_FIVE_ONEDAY = 4084;
	public static final int VERIFY_OUT_FIVE_PHONE = 4085;
	public static final int VERIFY_OUT_FREQUENT = 4086;
	
	private static Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
	
	/**
	 * 
	 * */
	private static final int[][] req_result_id = 
		{
		{RET_SUCCESS, com.doormaster.topkeeper.R.string.operation_success},
		{ACCESS_TOKEN_NULL, com.doormaster.topkeeper.R.string.access_token_null},
		{PHONE_NO_VERIFY, com.doormaster.topkeeper.R.string.phone_no_verify},
		{VERIFY_NUM_WRONG, com.doormaster.topkeeper.R.string.verify_num_wrong},
		{VERIFY_NUM_EX_VALID, com.doormaster.topkeeper.R.string.verify_num_ex_valid},
		{ACCOUNT_REGISTERED, com.doormaster.topkeeper.R.string.account_has_registered},
		{FAILED_CRERATE_COUNT, com.doormaster.topkeeper.R.string.failed_create_account},
		{FAILED_SEND_SMS_VERIFY, com.doormaster.topkeeper.R.string.failed_send_sms_verify_num},
		{CHANGE_PWD_FAILED, com.doormaster.topkeeper.R.string.failed_to_modify_pwd},
		{CLIENT_ID_NO_EXIST, com.doormaster.topkeeper.R.string.client_id_is_null},
		{PWD_FAILED, com.doormaster.topkeeper.R.string.password_failed},
		{CLIENT_MSG_NOT_MATCH, com.doormaster.topkeeper.R.string.client_id_message_id_no_match},
		{ACCOUNT_NO_EXIST, com.doormaster.topkeeper.R.string.phone_does_not_exist},
		{ACCOUNT_INCORRECT, com.doormaster.topkeeper.R.string.phone_num_is_incorrect},
		{BACK_UP_NO_EXIT, com.doormaster.topkeeper.R.string.backup_key_does_not_exist},
		{TIME_FORMAT_ERROR, com.doormaster.topkeeper.R.string.time_format_error},
		{ACCOUNT_NO_EXIST, com.doormaster.topkeeper.R.string.access_token_no_exist},
		{FEATURE_NO_CURRENTLY_SUPPORT, com.doormaster.topkeeper.R.string.this_feature_no_currently_support},
		{USER_OR_DEVICE_WITHOUT_AUTH, com.doormaster.topkeeper.R.string.user_or_device_without_authorization},
		{THE_ACOUNT_HAS_SEND_KEY, com.doormaster.topkeeper.R.string.other_manager_send_the_key},
		{RECEIVER_IS_NOT_EXIST, com.doormaster.topkeeper.R.string.rec_account_does_not_register},
		{CANT_SEND_THE_KEY_TOSELF, com.doormaster.topkeeper.R.string.can_not_send_the_key_to_self},
		{DEVICE_HAVE_NO_FEATURE, com.doormaster.topkeeper.R.string.device_can_not_support_the_feature},
		{HAVE_NO_PERMISSION_OPERATION, com.doormaster.topkeeper.R.string.have_not_permission_to_the_feature},
		{SERVER_FAILED_PROC, com.doormaster.topkeeper.R.string.server_failed_to_process_info},
		{MUST_USE_PARAM, com.doormaster.topkeeper.R.string.must_use_param},
		{JSON_DATA_FORMAT_ERROR, com.doormaster.topkeeper.R.string.json_data_format_error},
		{PARAM_VALUE_IS_NO_DEFINE, com.doormaster.topkeeper.R.string.param_value_is_no_define},
		{PARAM_TYPE_ERROR, com.doormaster.topkeeper.R.string.param_type_error},
		{DOES_NOT_SUPPORT_THE_LANGUAGE, com.doormaster.topkeeper.R.string.does_not_supprt_the_language},
		{PARAM_IS_NULL, com.doormaster.topkeeper.R.string.param_is_null},
		{PARAM_VALUE_MUST_BE_NUMDER, com.doormaster.topkeeper.R.string.param_value_must_be_number},
		{SERVER_NO_RESONSE, com.doormaster.topkeeper.R.string.server_not_response},
		{CHECK_NETWORK, com.doormaster.topkeeper.R.string.check_network_error},
		};
	
	static 
	{
		for (int i = 0;i < (req_result_id.length);i++) {
			resultMap.put(req_result_id[i][0], req_result_id[i][1]);
		}
	}
	
	/*public static void reqError(int result)
	{
		Integer rStringId = resultMap.get(result);
		if (rStringId != null) 
		{
			Toast.makeText(MyApplication.getInstance(), 
					rStringId,Toast.LENGTH_SHORT).show();
		} 
		else
		{
			String unknow_error = MyApplication.getInstance().getResources().getString(R.string.unknown_error) + result;
			Toast.makeText(MyApplication.getInstance(), 
					unknow_error,Toast.LENGTH_SHORT).show();
		}
	}*/
}
