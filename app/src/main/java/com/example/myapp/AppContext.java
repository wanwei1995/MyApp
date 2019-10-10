package com.example.myapp;

public class AppContext {
    public static CustomApplication APP;

    static void init(CustomApplication app) {
        APP = app;
    }
}