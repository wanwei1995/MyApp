package com.example.myapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.myapp.AppContext;

public class AppConfig {

	public static final String LOVE_PHONE = "lovePhone";

	public static final String ENVIRONMENT = "environment";

	public static final String LOVE_PIC = "lovePic";

	public static final String CONFIG_FILE = "settings";

	public static String get(String key) {
		return get(key, null);
	}

	public static String get(String key, String defaultValue) {
		SharedPreferences sp = AppContext.APP.getSharedPreferences(CONFIG_FILE, Context.MODE_APPEND);
		return sp.getString(key, defaultValue);
	}

	public static void set(String key, String value) {
		SharedPreferences sp = AppContext.APP.getSharedPreferences(CONFIG_FILE, Context.MODE_APPEND);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static Integer getYesNo(boolean isYes) {
		return isYes ? Integer.valueOf(1) : Integer.valueOf(0);
	}

    public static Boolean string2Boolean(String key) {
        return key.equalsIgnoreCase("Y") || key.equalsIgnoreCase("1") || key.equalsIgnoreCase("YES");
    }

	public static String getLovePhone(){
		return get(LOVE_PHONE);
	}

	public static String getLovePic(){
		return get(LOVE_PIC);
	}

	public static String getEnvironment(){
		return get(ENVIRONMENT);
	}

}