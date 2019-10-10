package com.example.myapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import androidx.core.app.ActivityCompat;
import com.example.myapp.AppContext;

public class Global {
    public static void errorLog(Exception e) {
        if (e == null) {
            return;
        }

        e.printStackTrace();
        Log.e("", "" + e);
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activityNetwork = mConnectivityManager.getActiveNetworkInfo();
            return activityNetwork != null && activityNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String getVersionName() {
        try {
            return AppContext.APP.getPackageManager().getPackageInfo(AppContext.APP.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Global.errorLog(e);
        }
        return "0.0.0";
    }

    public static int getVersionCode() {
        try {
            return AppContext.APP.getPackageManager().getPackageInfo(AppContext.APP.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Global.errorLog(e);
        }
        return 0;
    }

    public static boolean isEnterDown(KeyEvent keyEvent) {
        return KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_DOWN == keyEvent.getAction();
    }

    public static boolean isEnterUp(KeyEvent keyEvent){
        return KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_UP == keyEvent.getAction();
    }


    public static Activity getActivity(View view) {
        Context context = view.getContext();
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            if (baseContext instanceof Activity) {
                return (Activity) baseContext;
            }
        }
        return null;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String[] PERMISSTIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE
    };

    private static String[] PERMISSTIONS_CALL = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CALL_PRIVILEGED
    };

    public static void verifyAllPermissions(Activity activity){
        verifyStoragePermissions(activity);
        verifyCameraPermissions(activity);
        verifyCallPhonePermissions(activity);
    }

    //校验存储权限:如果没有存储权限，则弹窗请求获取权限
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static void verifyCameraPermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSTIONS_CAMERA, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static void verifyCallPhonePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSTIONS_CALL, REQUEST_EXTERNAL_STORAGE);
        }
    }
}