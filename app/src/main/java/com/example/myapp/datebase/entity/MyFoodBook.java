package com.example.myapp.datebase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_food_book")
public class MyFoodBook {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "create_time")
    private Long createTime;

    @ColumnInfo(name = "food_id_str")
    private String foodIdStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodIdStr() {
        return foodIdStr;
    }

    public void setFoodIdStr(String foodIdStr) {
        this.foodIdStr = foodIdStr;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

}
