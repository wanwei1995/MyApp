package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.R;
import com.example.myapp.util.BitmapUtil;

public class BigPicActivity extends AppCompatActivity {

    @BindView(R.id.bigPic)
    ImageView bigPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_pic);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        bigPic.setImageBitmap(BitmapUtil.changePic(url));
    }
}
