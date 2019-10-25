package com.example.myapp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.UUID;

public class FileUtil {

    public static final String FAMILY = "family";

    public static final String PRIVATE1 = "bb";

    public static final String FOODBOOK = "foodbook";

    public static final String MYGESTURE = "mygesture";



    private static final String ERROR = "error";



    public static String getSystemUrl() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/ZWLL/";
    }

    public static String getPrivate(){
        return "."+ PRIVATE1;
    }

    public static String getAllPrivate(){
        return getSystemUrl()+"."+ PRIVATE1;
    }
    public static File makeGesture() {
        return new File(getSystemUrl(), MYGESTURE);
    }

    public static String getGesture() {
        return getSystemUrl() + MYGESTURE;
    }

    public static File createPhotoPath(String path, String fileName) throws IOException {
        return createPhotoPath(path, fileName, false);
    }

    public static File createPhotoPath(String path, String fileName, Boolean isPrivate) throws IOException {
        String format = ".jpg";
        //带"."的文件夹是android默认的隐藏文件夹
        if (isPrivate) {
            path = "." + path;
            format += ".private";
        }
        String folder = createFolders(getSystemUrl(), path);
        if (ERROR.equals(folder)) {
            throw new IOException("文件夹创建失败.");
        }
        String uuid = UUID.randomUUID().toString();
        File file = new File(folder + File.separator + fileName + "-" + DateUtils.getFormatDateTime(DateUtils.getNowTime(), DateUtils.M_D_HM) + "-" + uuid + format);
        file.createNewFile();
        return file;
    }

    public static File getAppImagePath(String path, Boolean isPrivate) {
        String folder = getSystemUrl() + path;
        if (isPrivate) {
            folder = getSystemUrl()+ "." + path;
        }
        return new File(folder);
    }

    public static File getAppImagePath(String path) throws IOException {
        return getAppImagePath(path, false);
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
     * 多级目录创建
     *
     */
    public static String createAppFolder(String paths,Boolean isPrivate) {
        String folder = getSystemUrl() + paths;
        if (isPrivate) {
            folder = getSystemUrl() + "." + paths;
        }
        String txt = folder;
        try {
            java.io.File myFilePath = new java.io.File(txt);
            txt = folder;
            if (!myFilePath.exists()) {
                myFilePath.mkdirs();
            }
        } catch (Exception e) {
            txt = ERROR;
        }
        return txt;
    }

    /**
     * 多级目录创建
     *
     */
    public static String createAppFolder(String paths) {
        return createAppFolder(paths,false);
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


    public static void deletePicture(String localPath, Context context) {
        if (!TextUtils.isEmpty(localPath)) {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            String url = MediaStore.Images.Media.DATA + "=?";
            int deleteRows = contentResolver.delete(uri, url, new String[]{localPath});
            if (deleteRows == 0) {//当生成图片时没有通知(插入到）媒体数据库，那么在图库里面看不到该图片，而且使用contentResolver.delete方法会返回0，此时使用file.delete方法删除文件
                File file = new File(localPath);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    public static void deletePicture(Uri uri, Context context) {
        String localPath = ImageUtil.getImageAbsolutePath(context, uri);
        if (!TextUtils.isEmpty(localPath)) {
            ContentResolver contentResolver = context.getContentResolver();
            String url = MediaStore.Images.Media.DATA + "=?";
            int deleteRows = contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, url, new String[]{localPath});
            if (deleteRows == 0) {//当生成图片时没有通知(插入到）媒体数据库，那么在图库里面看不到该图片，而且使用contentResolver.delete方法会返回0，此时使用file.delete方法删除文件
                File file = new File(localPath);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }



    /**
     * 递归删除文件和文件夹
     *
     * @param file
     *            要删除的根目录
     */
    public static void recursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                recursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     *            要删除的根目录
     */
    public static void recursionDeleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                recursionDeleteFile(f);
            }
            file.delete();
        }
    }
}
