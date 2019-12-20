package com.example.myapp.webDav;


import android.os.Handler;
import android.os.Message;
import com.example.myapp.util.WebDavUtil;


//提供一个handle对外入口
public class WebDavService {

    private Handler handler;

    public static int upMyFood_what = 0;

    public static int downMyFood_what = 1;

    public WebDavService(Handler handler) {
        this.handler = handler;
    }


    //上传菜单明细
    public void upMyFoodInfo(String info) {

        new Thread(() -> {
            try {
                //上传点菜信息
                WebDavUtil.upInfo(info, WebDavUtil.FOOD_PATH, WebDavUtil.MYFOOD_FILENAME);
                Message message = new Message();
                message.what = upMyFood_what;
                message.obj = Result.setSuccess();
                //上传成功
                handler.sendMessage(message);
            } catch (Exception e) {
                //失败
                Message message = new Message();
                message.what = upMyFood_what;
                message.obj = Result.setFailure(e.getMessage());
                handler.sendMessage(message);
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
                message.what = downMyFood_what;
                message.obj = Result.setSuccess(info);
                //上传成功
                handler.sendMessage(message);
            } catch (Exception e) {
                //失败
                Message message = new Message();
                message.what = downMyFood_what;
                message.obj = Result.setFailure(e.getMessage());
                handler.sendMessage(message);
            }
        }).start();
    }


}
