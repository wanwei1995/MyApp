package com.example.myapp.data.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.myapp.data.sqLite.MyDBOpenHelper;
import com.example.myapp.ui.main2.dto.FoodBookDto;

import java.util.ArrayList;
import java.util.List;

public class FoodGoodService {

    private MyDBOpenHelper myDBOpenHelper;

    public FoodGoodService(Context context) {
        this.myDBOpenHelper = new MyDBOpenHelper(context);
    }

    public void saveHeader(FoodBookDto foodBookDto) {
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        try {
            db.execSQL("insert into food_good_header(name,create_time,introduction,pic_url) values (?,?,?,?)", new Object[]{foodBookDto.getName(), foodBookDto.getCreateTime(),foodBookDto.getIntroduction(),foodBookDto.getPicUrl()});
        }finally {
            db.close();
        }

    }

    public void deleteById(Long id) {
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("delete from food_good_header where id = ?", new Object[]{id});
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            db.close();
        }

    }

    public List<FoodBookDto> findList() {
        SQLiteDatabase db = myDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from food_good_header order by create_time desc", new String[]{});
        List<FoodBookDto> foodBookDtos = new ArrayList<>();
        while (cursor.moveToNext()) //类似ResultSet.next()
        {
            Long id = cursor.getLong(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String picUrl = cursor.getString(cursor.getColumnIndex("pic_url"));
            Long createTime = cursor.getLong(cursor.getColumnIndex("create_time"));
            foodBookDtos.add(new FoodBookDto(id, name, introduction,picUrl,createTime));
        }
        cursor.close();
        return foodBookDtos;
    }

    public List<FoodBookDto> findListByIds(String ids) {
        String[] isList = ids.split(",");
        SQLiteDatabase db = myDBOpenHelper.getReadableDatabase();
        StringBuilder sql = new StringBuilder("select * from food_good_header where id in (");
        for(int i = 0;i<isList.length;i++){
            sql.append("?").append(",");
        }
        StringBuilder sql2 = new StringBuilder(sql.substring(0,sql.length()-1));
        sql2.append(") order by create_time desc");

        Cursor cursor = db.rawQuery(sql2.toString(),isList);
        List<FoodBookDto> foodBookDtos = new ArrayList<>();
        while (cursor.moveToNext()) //类似ResultSet.next()
        {
            Long id = cursor.getLong(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String picUrl = cursor.getString(cursor.getColumnIndex("pic_url"));
            Long createTime = cursor.getLong(cursor.getColumnIndex("create_time"));
            foodBookDtos.add(new FoodBookDto(id, name, introduction,picUrl,createTime));
        }
        cursor.close();
        return foodBookDtos;
    }

}
