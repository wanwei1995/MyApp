package com.example.myapp.ui.main2.dto;

public class SettingDto {

    public SettingDto(String title1,String title2){
        this.title1 = title1;
        this.title2 = title2;
    }

    private String title1;

    private String title2;

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }
}
