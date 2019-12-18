package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;

public class GestureTestActivity extends BaseActivity implements View.OnTouchListener {

    @BindView(R.id.test)
    LinearLayout test;
    @BindView(R.id.tv_test)
    TextView tvTest;

    private static final int FLING_MIN_DISTANCE = 50;   //最小距离
    private static final int FLING_MIN_VELOCITY = 0;  //最小速度

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_test);
        mGestureDetector = new GestureDetector(this, myGestureListener);
        test.setOnTouchListener(this);//将主容器的监听交给本activity，本activity再交给mGestureDetector
        test.setLongClickable(true);   //必需设置这为true 否则也监听不到手势
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener myGestureListener = new GestureDetector.SimpleOnGestureListener(){
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float x = e1.getX()-e2.getX();
            float x2 = e2.getX()-e1.getX();
            if(x>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                Toast.makeText(GestureTestActivity.this, "向左手势", Toast.LENGTH_SHORT).show();
            }else if(x2>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                Toast.makeText(GestureTestActivity.this, "向右手势", Toast.LENGTH_SHORT).show();
            }
            return false;
        };
    };
}
