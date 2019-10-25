package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;


public abstract class AbstractActivity extends AppCompatActivity {

    void appendTitileInfo(String msg) {
        String splitSign = "-";

        String title = this.getTitle().toString();
        if (title.split(splitSign).length > 1) {
            this.setTitle(title.split(splitSign)[0] + splitSign + msg);
        } else {
            this.setTitle(title + splitSign + msg);
        }
    }

}
