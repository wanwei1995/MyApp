package com.example.myapp;

public class AppContext {
    public static CustomApplication APP;

    public static CustomApplication getINSTANCE() {
        return APP;
    }

    static void init(CustomApplication app) {
        APP = app;
    }
}