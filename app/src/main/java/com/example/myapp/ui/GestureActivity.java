package com.example.myapp.ui;

import android.content.Intent;
import android.gesture.*;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.util.FileUtil;

import java.util.ArrayList;

//手势认证页面
public class GestureActivity extends BaseActivity {

    String code;

    String activity;
    @BindView(R.id.gesture)
    GestureOverlayView gesture;
    @BindView(R.id.show)
    ImageView show;

    private GestureLibrary gestureLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);

        gestureLibrary = GestureLibraries.fromFile(FileUtil.getGesture());
        if (!gestureLibrary.load()) {
            Toast.makeText(this, "手势库加载失败", Toast.LENGTH_SHORT).show();
        }
        //获取传值
        code = getIntent().getExtras().getString("code");
        activity = getIntent().getExtras().getString("activity");

        //根据code 判断手势
        gesture.setGestureColor(Color.GREEN);
        gesture.setGestureStrokeWidth(5);
        gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {
                Bitmap bitmap = gesture.toBitmap(128, 128, 10, 0xffff0000);
                //识别用户刚绘制的手势
                ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
                ArrayList<String> result = new ArrayList<String>();
                //遍历所有找到的Prediction对象
                for (Prediction pred : predictions) {
                    if (pred.score > 2.0) {
                        result.add(pred.name);
                    }
                }
                if (result.contains(code)) {
                    //进入对应页面
                    try {
                        //跳转至对应页面
                        Intent intent = new Intent(GestureActivity.this, Class.forName(activity));
                        Bundle bundle = new Bundle();
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    show.setImageBitmap(bitmap);
                }
            }
        });

    }
}
