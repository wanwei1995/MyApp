package com.example.myapp.util;

import com.example.myapp.common.exception.BusinessException;
import com.example.myapp.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WebDavUtil {

    private static final String USERNAME = "2971973441@qq.com";

    private static final String PASSWORD = "aafx92k94jsiispj";

    public static final String FOOD_PATH = "food/";

    public static final String PIC_PATH = "pic/";

    public static final String WEBDAV_PATH = "https://dav.jianguoyun.com/dav/myApp/";

    public static final String MYFOOD_FILENAME = "myFood.txt";

    public static final String FOODBOOK_FILENAME = "foodBook.txt";


    public static void upInfo(String info, String path, String fileName) throws Exception {
        //登录
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(USERNAME, PASSWORD);
        //目录不存在，则新建目录
        if (!sardine.exists(WEBDAV_PATH + path)) {
            sardine.createDirectory(WEBDAV_PATH + path);
        }
        //上传信息
        byte[] bytes = info.getBytes();
        sardine.put(WEBDAV_PATH + path + fileName, bytes);
    }

    public static void upInfo(byte[] bytes, String path, String fileName) throws Exception {
        //登录
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(USERNAME, PASSWORD);
        //目录不存在，则新建目录
        if (!sardine.exists(WEBDAV_PATH + path)) {
            sardine.createDirectory(WEBDAV_PATH + path);
        }
        //如果文件不存在才不传
        if (!sardine.exists(WEBDAV_PATH + path + fileName)) {
            sardine.put(WEBDAV_PATH + path + fileName, bytes);
        }
    }

    public static String downInfo(String path, String fileName) throws Exception {
        //登录
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(USERNAME, PASSWORD);
        //文件不存在则抛异常
        if (!sardine.exists(WEBDAV_PATH + path + fileName)) {
            return "0";
        }
        InputStream is = null;
        ByteArrayOutputStream result = null;
        try {
            is = sardine.get(WEBDAV_PATH + path + fileName);

            result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        } finally {
            if (is != null) {
                is.close();
            }
            if (result != null) {
                result.close();
            }
        }

    }

    public static InputStream downPic(String path, String fileName) throws Exception {
        //判断云盘上文件是否存在
        //登录
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(USERNAME, PASSWORD);
        //文件不存在则抛异常
        if (!sardine.exists(WEBDAV_PATH + path + fileName)) {
            throw new BusinessException("文件不存在");
        }
        return sardine.get(WEBDAV_PATH + path + fileName);
    }
}
