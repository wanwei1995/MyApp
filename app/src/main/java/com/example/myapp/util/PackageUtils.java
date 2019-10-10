package com.example.myapp.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.example.myapp.CustomApplication;

/**
 * 描述：应用包相关的工具类
 *
 * @author CoderPig on 2018/02/28 18:01.
 */

public class PackageUtils {

    public static int packageCode() {
        PackageManager manager = CustomApplication.getContext().getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(CustomApplication.getContext().getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String packageName() {
        PackageManager manager = CustomApplication.getContext().getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(CustomApplication.getContext().getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }
}
