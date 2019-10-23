package com.example.myapp.data.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.myapp.data.sqLite.MyDBOpenHelper;
import com.example.myapp.ui.main2.dto.FoodBookDto;
import com.example.myapp.ui.main2.dto.MyFoodBookDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyFoodGoodService {

    private MyDBOpenHelper myDBOpenHelper;

    public MyFoodGoodService(Context context) {
        this.myDBOpenHelper = new MyDBOpenHelper(context);
    }

    public void saveHeader(MyFoodBookDto myFoodBookDto) {
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        try {
            db.execSQL("insert into my_food_good(food_ids,create_time) values (?,?)", new Object[]{myFoodBookDto.getFoodIdStr(), myFoodBookDto.getCreateTime()});
        }finally {
            db.close();
        }

    }

    public void update(MyFoodBookDto myFoodBookDto) {
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("update my_food_good set food_ids = ? where id = ?", new Object[]{myFoodBookDto.getFoodIdStr(),myFoodBookDto.getId()});
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            db.close();
        }

    }


    public List<MyFoodBookDto> findList() {
        SQLiteDatabase db = myDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from my_food_good order by create_time desc", new String[]{});
        List<MyFoodBookDto> myFoodBookDtos = new ArrayList<>();
        while (cursor.moveToNext()) //类似ResultSet.next()
        {
            Long id = cursor.getLong(cursor.getColumnIndex("id"));
            String foodIds = cursor.getString(cursor.getColumnIndex("food_ids"));
            Long createTime = cursor.getLong(cursor.getColumnIndex("create_time"));
            myFoodBookDtos.add(new MyFoodBookDto(id, createTime,foodIds));
        }
        cursor.close();
        return myFoodBookDtos;
    }

    public MyFoodBookDto findLastOne() {
        SQLiteDatabase db = myDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from my_food_good order by create_time desc limit 1", new String[]{});
        MyFoodBookDto myFoodBookDto = null;
        while (cursor.moveToNext()) //类似ResultSet.next()
        {
            myFoodBookDto = new MyFoodBookDto();
            myFoodBookDto.setId(cursor.getLong(cursor.getColumnIndex("id")));
            myFoodBookDto.setFoodIdStr(cursor.getString(cursor.getColumnIndex("food_ids")));
            myFoodBookDto.setCreateTime(cursor.getLong(cursor.getColumnIndex("create_time")));
        }
        cursor.close();
        return myFoodBookDto;
    }

    public List<MyFoodBookDto> findById(String id) {
        SQLiteDatabase db = myDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from my_food_good where id = ? order by create_time desc", new String[]{id});
        List<MyFoodBookDto> myFoodBookDtos = new ArrayList<>();
        while (cursor.moveToNext()) //类似ResultSet.next()
        {
            String foodIds = cursor.getString(cursor.getColumnIndex("food_ids"));
            Long createTime = cursor.getLong(cursor.getColumnIndex("create_time"));
            myFoodBookDtos.add(new MyFoodBookDto(Long.valueOf(id), createTime,foodIds));
        }
        cursor.close();
        return myFoodBookDtos;
    }

    public List<MyFoodBookDto> findByTime(Long createTimeOld) {
        SQLiteDatabase db = myDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from my_food_good where create_time >? order by create_time desc", new String[]{String.valueOf(createTimeOld)});
        List<MyFoodBookDto> myFoodBookDtos = new ArrayList<>();
        while (cursor.moveToNext()) //类似ResultSet.next()
        {
            String foodIds = cursor.getString(cursor.getColumnIndex("food_ids"));
            Long createTime = cursor.getLong(cursor.getColumnIndex("create_time"));
            Long id = cursor.getLong(cursor.getColumnIndex("id"));
            myFoodBookDtos.add(new MyFoodBookDto(id, createTime,foodIds));
        }
        cursor.close();
        return myFoodBookDtos;
    }




}
