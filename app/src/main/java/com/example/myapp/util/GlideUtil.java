package com.example.myapp.util;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapp.R;
import com.example.myapp.myView.CenterCropRoundCornerTransform;

public class GlideUtil {

    public static RequestOptions getCircle(){
        return getCircle(R.mipmap.icon_loading);
    }

    public static RequestOptions getCircle(int loading){
        RequestOptions options = new RequestOptions()
                .placeholder(loading)//加载成功之前占位图
                .error(R.mipmap.icon_loading_fail)//加载错误之后的错误图
                .circleCrop();//指定图片的缩放类型为centerCrop （圆形）
        return options;
    }

    public static RequestOptions getCrop(){
        return getCrop(R.mipmap.icon_loading);
    }

    public static RequestOptions getCrop(int loading){
        //矩形圆角
        RequestOptions options = new RequestOptions()
                .placeholder(loading)//加载成功之前占位图
                .error(R.mipmap.icon_loading_fail)//加载错误之后的错误图
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的宽高都大于等于ImageView的宽度，然后截取中间的显示。）
                .bitmapTransform(new CenterCropRoundCornerTransform(10));

        return options;
    }

    public static RequestOptions getCropSimple(){
        //矩形圆角
        RequestOptions options = new RequestOptions()
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的宽高都大于等于ImageView的宽度，然后截取中间的显示。）
                .bitmapTransform(new CenterCropRoundCornerTransform(10));
        return options;
    }

}
