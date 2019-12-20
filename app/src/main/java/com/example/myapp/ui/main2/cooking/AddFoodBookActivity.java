package com.example.myapp.ui.main2.cooking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.datebase.AppDatabase;
import com.example.myapp.datebase.entity.FoodBook;
import com.example.myapp.myView.SimpleToolbar;
import com.example.myapp.util.AlertDialogUtil;
import com.example.myapp.util.FileUtil;
import com.example.myapp.util.Global;
import com.zhihu.matisse.Matisse;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

//添加菜单
public class AddFoodBookActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    SimpleToolbar simpleToolbar;
    @BindView(R.id.add_image)
    ImageView addImage;
    @BindView(R.id.food_name)
    EditText foodName;
    @BindView(R.id.food_introduction)
    EditText foodIntroduction;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_book);
        addImage.setOnClickListener(v -> {
            //打开相册选择图片
            openPic(1);
        });

        initActionBar();
    }

    public void initActionBar() {
        simpleToolbar.setLeftTitleClickListener(v -> {
            finish();
        });
        simpleToolbar.setRightTitleClickListener(v -> {
            //保存菜单
            AlertDialogUtil.YesOrNo("是否保存菜单", AddFoodBookActivity.this, (dialog, which) -> {
                //保存菜单
                //保存图片
                String url = null;
                if (uri != null) {
                    FileOutputStream out = null;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        File file = FileUtil.createPicPath(FileUtil.FOODBOOK, foodName.getText().toString());
                        url = file.getAbsolutePath();
                        out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                FoodBook foodBook = new FoodBook();
                foodBook.setCreateTime(new Date().getTime());
                foodBook.setIntroduction(foodIntroduction.getText().toString());
                foodBook.setName(foodName.getText().toString());
                foodBook.setPicUrl(url);

                mDisposable.add(Completable.fromAction(() -> {
                    AppDatabase.getInstance().foodBookDao().insert(foodBook);
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    //保存后关闭页面并返回刷新上级菜单
                                    Intent intent = new Intent();
                                    //把返回数据存入Intent(未盘完)
                                    intent.putExtra("result", "0");
                                    //设置返回数据
                                    setResult(RESULT_FIRST_USER, intent);
                                    //关闭Activity
                                    finish();
                                },
                                throwable -> {
                                    Toast.makeText(this, "新增数据失败", Toast.LENGTH_SHORT).show();
                                }
                        ));
            });
        });
        simpleToolbar.setMainTitle("创建菜单");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 选择系统图片并解析
         */
        if (requestCode == Global.OPEN_ALBUM) {
            if (data != null) {
                addImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                uri = Matisse.obtainResult(data).get(0);
                Glide.with(this)
                        .asBitmap() // some .jpeg files are actually gif
                        .load(uri)
                        .into(addImage);
            }
        }
    }
}
