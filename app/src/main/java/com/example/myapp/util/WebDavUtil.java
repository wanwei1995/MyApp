package com.example.myapp.util;

import com.example.myapp.common.exception.BusinessException;
import com.example.myapp.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WebDavUtil {

    private static final String USERNAME = "1254837494@qq.com";

    private static final String PASSWORD = "aacqkt2pjkdqwgwg";

    public static final String FOOD_PATH = "food/";

    public static final String WEBDAV_PATH = "https://dav.jianguoyun.com/dav/myApp/";

    public static final String MYFOOD_FILENAME = "myFood.txt";


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
}
