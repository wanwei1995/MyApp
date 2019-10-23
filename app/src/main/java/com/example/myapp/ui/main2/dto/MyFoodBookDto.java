package com.example.myapp.ui.main2.dto;

import java.util.List;

public class MyFoodBookDto {

    private Long id;

    private Long createTime;

    private List<String> foodIdList;

    private String foodIdStr;


    public MyFoodBookDto(Long id,Long createTime,String foodIdStr){
        this.id = id;
        this.createTime = createTime;
        this.foodIdStr = foodIdStr;
    }
    public MyFoodBookDto(){
    }

    public MyFoodBookDto(Long createTime,String foodIdStr){
        this.createTime = createTime;
        this.foodIdStr = foodIdStr;
    }

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

    public List<String> getFoodIdList() {
        return foodIdList;
    }

    public void setFoodIdList(List<String> foodIdList) {
        this.foodIdList = foodIdList;
    }
}
