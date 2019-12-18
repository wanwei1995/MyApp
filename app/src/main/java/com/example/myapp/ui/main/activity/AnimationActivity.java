package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;

public class AnimationActivity extends BaseActivity {

    @BindView(R.id.list)
    LinearLayout list;
    private Animation alphaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        alphaAnimation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.scroll_in);


    }


    public void test(View v) {
        list.startAnimation(alphaAnimation);
    }
}
