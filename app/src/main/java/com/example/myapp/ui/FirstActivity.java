package com.example.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.myapp.R;
import com.example.myapp.ui.main.MainActivity;
import com.example.myapp.ui.main2.Main2Activity;
import com.example.myapp.util.AppConfig;
import com.example.myapp.util.StringUtil;

import java.nio.file.FileStore;

public class FirstActivity extends AppCompatActivity {

    @BindView(R.id.test)
    ImageButton test;
    @BindView(R.id.use)
    ImageButton use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);

        if (StringUtil.isNotEmpty(AppConfig.getEnvironment()) && "1".equals(AppConfig.getEnvironment())) {
            Intent intent2 = new Intent(FirstActivity.this, Main2Activity.class);
            startActivity(intent2);
        } else if (StringUtil.isNotEmpty(AppConfig.getEnvironment()) && "0".equals(AppConfig.getEnvironment())) {
            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @OnClick({R.id.test, R.id.use})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.test:
                AppConfig.set(AppConfig.ENVIRONMENT, "0");
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.use:
                AppConfig.set(AppConfig.ENVIRONMENT, "1");
                Intent intent2 = new Intent(FirstActivity.this, Main2Activity.class);
                startActivity(intent2);
                break;
        }
    }
}
