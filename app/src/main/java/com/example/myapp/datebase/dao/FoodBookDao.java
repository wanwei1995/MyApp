package com.example.myapp.datebase.dao;

import androidx.room.*;
import com.example.myapp.datebase.entity.FoodBook;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface FoodBookDao {

    @Insert
    void insert(FoodBook foodBook);

    @Insert
    void insertList(List<FoodBook> foodBooks);

    @Update
    void update(FoodBook foodBook);

    @Delete
    void delete(FoodBook foodBook);

    @Transaction
    @Query("SELECT * from food_book")
    Flowable<List<FoodBook>> findList();

    @Transaction
    @Query(value = "select * from food_book where id in (:ids)")
    Flowable<List<FoodBook>> findListByIds(List<Long> ids);

    @Transaction
    @Query(value = "select * from food_book where id in (:ids)")
    List<FoodBook> findListByIdsSimple(List<Long> ids);
}
