package com.example.myapp.util;

import com.bumptech.glide.request.RequestOptions;
import com.example.myapp.R;

public class GlideUtil {

    private static int defaultValue = 0;

    public static RequestOptions getCircle(){
        return getCircle(defaultValue);
    }

    public static RequestOptions getCircle(int loading){
        if(loading == defaultValue){
            loading = R.mipmap.icon_loading;
        }
        RequestOptions options =  options = new RequestOptions()
                .placeholder(loading)//加载成功之前占位图
                .error(R.mipmap.icon_loading_fail)//加载错误之后的错误图
                .circleCrop();//指定图片的缩放类型为centerCrop （圆形）
        return options;
    }

    public static RequestOptions getCrop(){
        return getCrop(0);
    }

    public static RequestOptions getCrop(int loading){
        if(loading == defaultValue){
            loading = R.mipmap.icon_loading;
        }
        RequestOptions options =  options = new RequestOptions()
                .placeholder(loading)//加载成功之前占位图
                .error(R.mipmap.icon_loading_fail)//加载错误之后的错误图
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                .centerCrop();
        return options;
    }
    public static RequestOptions getCrop(int loading,int fail){
        if(loading == defaultValue){
            loading = R.mipmap.icon_loading;
        }
        if(fail == defaultValue){
            fail = R.mipmap.icon_loading_fail;
        }
        RequestOptions options =  options = new RequestOptions()
                .placeholder(loading)//加载成功之前占位图
                .error(fail)//加载错误之后的错误图
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                .centerCrop();
        return options;
    }
}
