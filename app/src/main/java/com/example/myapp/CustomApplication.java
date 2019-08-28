package com.example.myapp;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import com.simple.spiderman.CrashModel;
import com.simple.spiderman.SpiderMan;

import java.util.Map;

public class CustomApplication extends Application {

    private Map<String, String> permissionMap;

    private Map<String, String> configurationMap;//系统参数map

    public Map<String, String> getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(Map<String, String> permissionMap) {
        this.permissionMap = permissionMap;
    }

    public Map<String, String> getConfigurationMap() {
        return configurationMap;
    }

    public void setConfigurationMap(Map<String, String> configurationMap) {
        this.configurationMap = configurationMap;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate() {
        super.onCreate();
        //崩溃日志显示
        SpiderMan.getInstance()
                .init(this)
                //设置是否捕获异常，不弹出崩溃框
                .setEnable(true)
                //设置是否显示崩溃信息展示页面
                .showCrashMessage(true)
                //是否回调异常信息，友盟等第三方崩溃信息收集平台会用到,
                .setOnCrashListener(new SpiderMan.OnCrashListener() {
                    @Override
                    public void onCrash(Thread t, Throwable ex, CrashModel model) {
                        //CrashModel 崩溃信息记录，包含设备信息
                    }
                });
    }
}
