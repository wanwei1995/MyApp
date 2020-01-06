package com.example.myapp.ui.main.dto;

public class HanZiDto {

    private String text;

    private String value;

    public HanZiDto(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
