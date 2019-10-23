package com.example.myapp.data.sqLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author wanwei
 * @TODO
 * @date: 2019/7/11 10:22
 */
public class MyDBOpenHelper extends SQLiteOpenHelper {
    public MyDBOpenHelper(Context context) {super(context, "my.db", null, 1); }

    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {super(context, "my.db", null, 1); }
    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `food_good_header` (\n" +
                "  `id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  `name` varchar(50) DEFAULT NULL,\n" +
                "  `create_time` INTEGER DEFAULT NULL,\n" +
                "  `introduction` varchar(500) DEFAULT NULL,\n" +
                "  `pic_url` INTEGER DEFAULT NULL\n" +
                ")");
        db.execSQL("CREATE TABLE `my_food_good` (\n" +
                "  `id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  `food_ids` varchar(50) DEFAULT NULL,\n" +
                "  `create_time` INTEGER DEFAULT NULL\n" +
                ")");
    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //改变表结构

    }
}
