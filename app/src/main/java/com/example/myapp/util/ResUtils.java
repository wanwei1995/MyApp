package com.example.myapp.util;

import com.example.myapp.CustomApplication;

/**
 * 描述：获取文件资源工具类
 *
 * @author CoderPig on 2018/02/14 11:07.
 */

public class ResUtils {
    /* 获取文件资源 */
    public static String getString(int strId) {
        return CustomApplication.getContext().getResources().getString(strId);
    }
}
