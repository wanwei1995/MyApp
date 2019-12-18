package com.example.myapp.datebase.dao;

import androidx.room.*;
import com.example.myapp.datebase.entity.FoodBook;
import com.example.myapp.datebase.entity.MyFoodBook;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface MyFoodBookDao {

    @Insert
    void insert(MyFoodBook myFoodBook);

    @Update
    void update(MyFoodBook myFoodBook);

    @Delete
    void delete(MyFoodBook myFoodBook);

    @Transaction
    @Query("SELECT * from my_food_book")
    Flowable<List<MyFoodBook>> findList();

    @Query("select * from my_food_book order by create_time desc limit 1")
    MyFoodBook findLastOne();

    @Query("select * from my_food_book where create_time > :createTime order by create_time desc")
    Flowable<List<MyFoodBook>> findByTime(Long createTime);
}
