package com.doormaster.topkeeper.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * 类的描述：json处理工具类
 */
public class JsonUtil {
	
	private static GsonBuilder gsonBuilder = new GsonBuilder();
	
	/**
	 * 
	 * 描述：将对象转化为json.
	 * @param src
	 * @return
	 */
	public static String toJson(Object src) {
		String json = null;
		try {
			Gson gson = gsonBuilder.create();
			json = gson.toJson(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 
	 * 描述：将列表转化为json.
	 * @param list
	 * @return
	 */
	public static String toJson(List<?> list) {
		String json = null;
		try {
			Gson gson = gsonBuilder.create();
			json = gson.toJson(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 
	 * 描述：将json转化为列表.
	 * @param json
	 * @param typeToken new TypeToken<ArrayList<?>>() {};
	 * @return
	 */
	public static <T> T fromJson(String json,TypeToken<T> typeToken) {
		List<?> list = null;
		try {
			Gson gson = gsonBuilder.create();
			Type type = typeToken.getType();
			list = gson.fromJson(json,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T)list;
	}
	
	/**
	 * 
	 * 描述：将json转化为对象.
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String json,Class<T> clazz) {
		Object obj = null;
		try {
			Gson gson = new Gson();
			obj = gson.fromJson(json,clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T)obj;
	}
	
	public static void setGsonBuilderDateFormat(String format) {
		  gsonBuilder.setDateFormat(format);
	}
}
