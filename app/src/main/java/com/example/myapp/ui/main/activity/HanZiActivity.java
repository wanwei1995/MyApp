package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;

public class HanZiActivity extends BaseActivity {

    @BindView(R.id.search)
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_han_zi);
        
    }


}
