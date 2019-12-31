package com.example.myapp.webDav;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.myapp.util.FileUtil;
import com.example.myapp.util.StringUtil;
import com.example.myapp.util.WebDavUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


//提供一个handle对外入口
public class WebDavService {

    private Handler handler;

    public static int upMyFood_what = 0;

    public static int downMyFood_what = 1;

    public static int upFoodBook_what = 2;

    public static int downFoodBook_what = 3;

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

    //获取菜谱数据
    public void downFoodBookInfo() {
        new Thread(() -> {
            try {
                //下载点菜信息
                String info = WebDavUtil.downInfo(WebDavUtil.FOOD_PATH, WebDavUtil.FOODBOOK_FILENAME);
                Message message = new Message();
                message.what = downFoodBook_what;
                message.obj = Result.setSuccess(info);
                //上传成功
                handler.sendMessage(message);
            } catch (Exception e) {
                //失败
                Message message = new Message();
                message.what = downFoodBook_what;
                message.obj = Result.setFailure(e.getMessage());
                handler.sendMessage(message);
            }
        }).start();
    }

    //上传菜谱明细
    public void upFoodBookInfo(String info) {

        new Thread(() -> {
            try {
                //上传点菜信息
                WebDavUtil.upInfo(info, WebDavUtil.FOOD_PATH, WebDavUtil.FOODBOOK_FILENAME);
                Message message = new Message();
                message.what = upFoodBook_what;
                message.obj = Result.setSuccess();
                //上传成功
                handler.sendMessage(message);
            } catch (Exception e) {
                //失败
                Message message = new Message();
                message.what = upFoodBook_what;
                message.obj = Result.setFailure(e.getMessage());
                handler.sendMessage(message);
            }
        }).start();
    }

    public void upFoodBookPic(String url) {
        new Thread(() -> {
            try {
                File file = new File(url);
                byte[] data = FileUtils.readFileToByteArray(file);

                //上传点菜信息
                WebDavUtil.upInfo(data, WebDavUtil.PIC_PATH, file.getName());
            } catch (Exception e) {
                //失败
                System.out.println(e);
            }
        }).start();


    }

    private int count = 0;

    public void downFoodBookPic(String url) {

        new Thread(() -> {
            InputStream inputStream = null;
            OutputStream os = null;
            try {
                //判断本地文件不存在再下载
                File file = new File(url);
                if (file != null && file.exists()) {
                    return;
                }
                //
                String fileName = StringUtil.getPathName(url);
                //下载图片
                inputStream = WebDavUtil.downPic(WebDavUtil.PIC_PATH, fileName);
                //

                //如果目录不存在，则生成目录
                if (!new File(FileUtil.getSystemUrl() + FileUtil.FOODBOOK).exists()) {
                    FileUtil.createAppFolder(FileUtil.FOODBOOK);
                }

                os = new FileOutputStream(new File(url));
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                inputStream.close();

            } catch (Exception e) {
                if (count == 1) {
                    return;
                }
                count++;
                try {
                    Thread.sleep(2000);
                    //失败则继续重试一次,再次失败则将其放入下载任务列表,等待手动重试
                    downFoodBookPic(url);
                } catch (Exception e2) {

                }
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (Exception e) {

                }
            }
        }).start();


    }
}
