package com.example.myapp.ui.main2.cooking;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.view.listener.OnBigImageClickListener;
import cc.shinichi.library.view.listener.OnBigImageLongClickListener;
import cc.shinichi.library.view.listener.OnBigImagePageChangeListener;
import cc.shinichi.library.view.listener.OnOriginProgressListener;
import com.bumptech.glide.Glide;
import com.example.myapp.R;
import com.example.myapp.data.service.FoodGoodService;
import com.example.myapp.myView.SimpleToolbar;
import com.example.myapp.ui.main.activity.SeeActivity;
import com.example.myapp.ui.main2.dto.FoodBookDto;
import com.example.myapp.util.AlertDialogUtil;
import com.example.myapp.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

//菜单详情
public class FoodBookDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    SimpleToolbar simpleToolbar;
    @BindView(R.id.add_image)
    ImageView addImage;
    @BindView(R.id.food_name)
    TextView foodName;
    @BindView(R.id.food_introduction)
    TextView foodIntroduction;

    private FoodBookDto foodBookDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_book_detail);
        ButterKnife.bind(this);

        foodBookDto = (FoodBookDto) getIntent().getExtras().getSerializable("foodBookDto");

        initActionBar();

        initView();

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击查看大图
                ImagePreview.getInstance()
                        // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好
                        .setContext(FoodBookDetailActivity.this)
                        .setIndex(0)
                        .setImage(foodBookDto.getPicUrl())

                        // 加载策略，默认为手动模式
                        .setLoadStrategy(ImagePreview.LoadStrategy.Default)

                        // 缩放动画时长，单位ms
                        .setZoomTransitionDuration(300)

                        // 是否启用点击图片关闭。默认启用
                        .setEnableClickClose(false)
                        // 是否启用下拉关闭。默认不启用
                        .setEnableDragClose(true)
                        // 是否启用上拉关闭。默认不启用
                        .setEnableUpDragClose(true)

                        // 是否显示下载按钮，在页面右下角。默认显示
                        .setShowDownButton(false)
                        // 设置是否显示顶部的指示器（1/9）默认显示
                        .setShowIndicator(false)

                        // 开启预览
                        .start();
            }
        });
    }

    private void initView() {

        Glide.with(this)
                .asBitmap() // some .jpeg files are actually gif
                .load(foodBookDto.getPicUrl())
                .into(addImage);
        foodName.setText(String.valueOf(foodBookDto.getName()));
        foodIntroduction.setText(String.valueOf(foodBookDto.getIntroduction()));
    }

    public void initActionBar() {
        simpleToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        simpleToolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存菜单
                AlertDialogUtil.YesOrNo("是否删除菜单", FoodBookDetailActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FoodGoodService foodGoodService = new FoodGoodService(FoodBookDetailActivity.this);
                        foodGoodService.deleteById(foodBookDto.getId());
                        //保存后关闭页面并返回刷新上级菜单
                        Intent intent = new Intent();
                        //把返回数据存入Intent(未盘完)
                        intent.putExtra("result", "0");
                        //设置返回数据
                        setResult(RESULT_FIRST_USER, intent);
                        //关闭Activity
                        finish();
                    }
                });

            }
        });
        simpleToolbar.setMainTitle("菜单详情");
    }
}
