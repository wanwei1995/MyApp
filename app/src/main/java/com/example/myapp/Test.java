package com.example.myapp;

import com.example.myapp.util.HanZiToPinYin;

public class Test {

    public static void main(String[] args) {

        String text = "我爱记歌词vv";
        System.out.println(HanZiToPinYin.getFirstSpell(text));
    }
}
