package com.example.myapp.webDav;


import android.os.Handler;
import android.os.Message;
import com.example.myapp.util.WebDavUtil;
import com.thegrizzlylabs.sardineandroid.Sardine;


//提供一个handle对外入口
public class WebDavService {

    private Handler handler;

    public WebDavService(Handler handler) {
        this.handler = handler;
    }


    //上传菜单明细
    public void upMyFoodInfo(String info) {

        new Thread(() -> {
            try {
                //上传点菜信息
                WebDavUtil.upInfo(info, WebDavUtil.FOOD_PATH, WebDavUtil.MYFOOD_FILENAME);
                //上传成功
                handler.sendEmptyMessage(0);
            } catch (Exception e) {
                //失败
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    //获取点菜明细
    public void downMyFoodInfo() {
        new Thread(() -> {
            try {
                //下载点菜信息
                String info = WebDavUtil.downInfo(WebDavUtil.FOOD_PATH, WebDavUtil.MYFOOD_FILENAME);
                Message message = new Message();
                message.what = 0;
                message.obj = info;
                //上传成功
                handler.sendMessage(message);
            } catch (Exception e) {
                //失败
                handler.sendEmptyMessage(1);
            }
        }).start();
    }


}
