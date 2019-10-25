package com.example.myapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author wanwei
 * @TODO
 * @date: 2019/1/25 14:53
 */
public class AlertDialogUtil {

    public static void YesOrNo(String title, final Context context, DialogInterface.OnClickListener onClickListener){

        //弹框 是否打印
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(title);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", (dialog, which) -> {});
        builder.show();
    }
}
