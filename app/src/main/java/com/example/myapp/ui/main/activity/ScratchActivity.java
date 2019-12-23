package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.myView.FogView;

public class ScratchActivity extends BaseActivity {

    @BindView(R.id.RubFog)
    FogView rubFog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

    }
}
