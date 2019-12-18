/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.myapp.util;

import com.example.myapp.AppContext;
import com.example.myapp.datebase.AppDatabase;
import com.example.myapp.datebase.entity.BackupBean;
import com.snatik.storage.Storage;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 备份相关工具类
 *
 * @author Bakumon https:/bakumon.me
 */
public class BackupUtil {
    public static final String BACKUP_DIR = "backup_myApp";
    public static final String AUTO_BACKUP_PREFIX = "MyAppBackupAuto";
    public static final String USER_BACKUP_PREFIX = "MyAppBackupUser";
    public static final String SUFFIX = ".db";
    public static final String BACKUP_SUFFIX = "_恢复之前";

    //备份：将运行中的db文件复制到指定文件夹
    private static boolean backupDB(String fileName) {
        Storage storage = new Storage(AppContext.getINSTANCE());
        boolean isWritable = Storage.isExternalWritable();
        if (!isWritable) {
            return false;
        }
        String path = storage.getExternalStorageDirectory() + File.separator + BACKUP_DIR;
        if (!storage.isDirectoryExists(path)) {
            storage.createDirectory(path);
        }
        String filePath = path + File.separator + fileName;
        if (!storage.isFileExist(filePath)) {
            // 创建空文件，在模拟器上测试，如果没有这个文件，复制的时候会报 FileNotFound
            storage.createFile(filePath, "");
        }
        return copys(storage,AppContext.getINSTANCE().getDatabasePath(AppDatabase.DB_NAME).getPath(), path + File.separator + fileName);
    }

    //需要将.db  .db-shm  .db-wal全部备份
    private static boolean copys(Storage storage, String fromPath, String toPath) {
        return storage.copy(fromPath, toPath) &&
                storage.copy(fromPath + "-shm", toPath + "-shm") &&
                storage.copy(fromPath + "-wal", toPath + "-wal");
    }

    public static boolean autoBackup() {
        String fileName = AUTO_BACKUP_PREFIX + SUFFIX;
        return backupDB(fileName);
    }

    public static boolean autoBackupForNecessary() {
        String fileName = AUTO_BACKUP_PREFIX + SUFFIX;
        Storage storage = new Storage(AppContext.getINSTANCE());
        boolean isWritable = Storage.isExternalWritable();
        if (!isWritable) {
            return false;
        }
        String path = storage.getExternalStorageDirectory() + File.separator + BACKUP_DIR;
        if (!storage.isDirectoryExists(path)) {
            storage.createDirectory(path);
        }
        String filePath = path + File.separator + fileName;
        if (!storage.isFileExist(filePath)) {
            // 创建空文件，在模拟器上测试，如果没有这个文件，复制的时候会报 FileNotFound
            storage.createFile(filePath, "");
            return storage.copy(AppContext.getINSTANCE().getDatabasePath(AppDatabase.DB_NAME).getPath(), path + File.separator + fileName);
        }
        return true;
    }

    public static boolean userBackup() {
        String fileName = USER_BACKUP_PREFIX + SUFFIX;
        return backupDB(fileName);
    }
    //恢复备份：将指定文件夹的db文件替换运行中的文件
    public static boolean restoreDB(String restoreFile) {
        Storage storage = new Storage(AppContext.getINSTANCE());
        if (storage.isFileExist(restoreFile)) {
            // 恢复之前，备份一下最新数据
            String fileName = "MyAppBackup" + DateUtils.getCurrentDateString() + BACKUP_SUFFIX + SUFFIX;
            boolean isBackupSuccess = backupDB(fileName);
            boolean isRestoreSuccess = copys(storage,restoreFile, AppContext.getINSTANCE().getDatabasePath(AppDatabase.DB_NAME).getPath());
            return isBackupSuccess && isRestoreSuccess;
        }
        return false;
    }

    public static List<BackupBean> getBackupFiles() {
        Storage storage = new Storage(AppContext.getINSTANCE());
        String dir = storage.getExternalStorageDirectory() + File.separator + BACKUP_DIR;
        List<BackupBean> backupBeans = new ArrayList<>();
        BackupBean bean;
        List<File> files = storage.getFiles(dir, "[\\S]*\\.db");
        if (files == null) {
            return backupBeans;
        }
        File fileTemp;
        PrettyTime prettyTime = new PrettyTime();
        for (int i = 0; i < files.size(); i++) {
            fileTemp = files.get(i);
            bean = new BackupBean();
            bean.file = fileTemp;
            bean.name = fileTemp.getName();
            bean.size = storage.getReadableSize(fileTemp);
            bean.time = prettyTime.format(new Date(fileTemp.lastModified()));
            backupBeans.add(bean);
        }
        return backupBeans;
    }
}
