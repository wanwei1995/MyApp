package com.example.myapp.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

public class FileUtil {

    private static final String ERROR = "error";

    public static String getSystemUrl() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/ZWLL/";
    }

    public static File createPhotoPath(String path, String fileName) throws IOException {
        String folder = createFolders(getSystemUrl(), path);
        if (ERROR.equals(folder)) {
            throw new IOException("文件夹创建失败.");
        }
        String uuid = UUID.randomUUID().toString();
        File file = new File(folder+ File.separator+fileName + "-" + DateUtils.getFormatDateTime(DateUtils.getNowTime(), DateUtils.YMDHMS) + "-" + uuid + ".jpg");
        file.createNewFile();
        return file;
    }

    /**
     * 多级目录创建
     *
     * @param folderPath 准备要在本级目录下创建新目录的目录路径 例如 c:myf
     * @param paths      无限级目录参数，各级目录以单数线区分 例如 a/b/c
     * @return 返回创建文件后的路径 例如 c:myfac
     */
    public static String createFolders(String folderPath, String paths) {
        String txts;
        try {
            String txt;
            txts = folderPath;
            if (txts.lastIndexOf(File.separator) != -1) {
                txts = txts + "/";
            }
            StringTokenizer st = new StringTokenizer(paths, "/");
            for (int i = 0; st.hasMoreTokens(); i++) {
                txt = st.nextToken().trim();
                if (txt.lastIndexOf(File.separator) != -1) {
                    txts = createFolder(txts + txt);
                } else {
                    txts = createFolder(txts + txt + File.separator);
                }
            }
        } catch (Exception e) {
            txts = ERROR;
        }
        return txts;
    }

    /**
     * 新建目录
     *
     * @param folderPath 目录
     * @return 返回目录创建后的路径
     */
    public static String createFolder(String folderPath) {
        String txt = folderPath;
        try {
            java.io.File myFilePath = new java.io.File(txt);
            txt = folderPath;
            if (!myFilePath.exists()) {
                myFilePath.mkdirs();
            }
        } catch (Exception e) {
            txt = ERROR;
        }
        return txt;
    }
}
