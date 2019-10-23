package com.example.myapp.ui.main2.dto;

import java.io.Serializable;

public class FoodBookDto implements Serializable {

    //唯一标识
    private Long id;
    //菜名
    private String name;
    //简介
    private String introduction;
    //图片地址
    private String picUrl;

    private Long createTime;

    public FoodBookDto(Long id,String name,String introduction,String picUrl,Long createTime){
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.picUrl = picUrl;
        this.createTime = createTime;
    }

    public FoodBookDto(String name,String introduction,String picUrl,Long createTime){
        this.name = name;
        this.introduction = introduction;
        this.picUrl = picUrl;
        this.createTime = createTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
