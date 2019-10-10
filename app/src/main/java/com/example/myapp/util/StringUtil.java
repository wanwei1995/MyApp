package com.example.myapp.util;

import java.util.Arrays;

/**
 * Created by wei on 2016/6/28.
 */
public class StringUtil {

    public static boolean isEmpty(String s) {
        if (s == null || s.trim().length() == 0)
            return true;
        return false;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }


    public static String getNotEmpty(String... strs) {
        for (String str : strs) {
            if (isNotEmpty(str)) {
                return str;
            }
        }
        return null;
    }

    public static boolean isInSpliStr(String strArray, String split, String str) {
        return ListUtil.isIn(Arrays.asList(strArray.split(split)), str);
    }

    public static String valueOf(Object obj){
        return obj == null ? null : obj.toString();
    }

    public static boolean equal(String s1, String s2) {
        if (isEmpty(s1) && isEmpty(s2)) {
            return  true;
        }
        if (isNotEmpty(s1) && isNotEmpty(s2) && s1.equals(s2)) {
            return  true;
        }
        return  false;
    }

    public static String object2String(Object obj) {
        return object2String(obj,null);
    }

    public static String object2String(Object obj, String defaultStr) {
        return obj == null ? defaultStr : obj.toString();
    }
}
