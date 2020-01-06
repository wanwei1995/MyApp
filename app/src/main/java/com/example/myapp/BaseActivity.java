package com.example.myapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import butterknife.ButterKnife;
import com.example.myapp.glide.GlideLoadEngine;
import com.example.myapp.ui.main2.dto.MenuDto;
import com.example.myapp.util.AppManager;
import com.example.myapp.util.Global;
import com.example.myapp.util.sound.Sound;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import io.reactivex.disposables.CompositeDisposable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AbstractActivity {

    public String TAG = "";

    private Sound sound;

    private ProgressDialog progressDialog;

    private Vibrator mVibrator;  //声明一个振动器对象

    protected final CompositeDisposable mDisposable = new CompositeDisposable();


    protected void loadSound() {
        sound = new Sound(this);
        sound.setInitialSoundPool();
    }

    protected void playSoundByResId(int soundId) {
        sound.playSoundByResId(soundId);
    }


    /**
     * 声音播放，【错误】
     */
    public void playSoundError() {
        mVibrator();
        playSoundByResId(R.raw.error);
    }

    /**
     * 声音播放，【正确】
     */
    public void playSoundOk() {
        playSoundByResId(R.raw.ok);
    }


    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", "Please Wait...", true);
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 收起软键盘
     */
    protected void hideInputBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);

        loadSound();

        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
    }

    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initActionBar();
    }

    public void mVibrator(){
        //调用震动
        if(mVibrator == null){
            mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(500);
    }

    public List<MenuDto> loadMenus(int id) {
        InputStreamReader inputReader = new InputStreamReader(getResources().openRawResource(id));
        BufferedReader bufReader = new BufferedReader(inputReader);
        String line = "";
        String jsonStr = "";
        List<MenuDto> menuDTOs = new ArrayList<>();
        try {
            while ((line = bufReader.readLine()) != null)
                jsonStr += line;
            menuDTOs = new Gson().fromJson(jsonStr, new TypeToken<List<MenuDto>>() {
            }.getType());
        } catch (IOException e) {
            Log.e("normal", "parseJson", e);

            Toast.makeText(this, "Render Main Menu Eror", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                inputReader.close();
                bufReader.close();
            } catch (IOException e) {
                Log.e("normal", "parseJson", e);

                Toast.makeText(this, "Render Main Menu Eror", Toast.LENGTH_SHORT).show();
            }
        }
        return menuDTOs;
    }

    public void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
    }

    public void openPic(int max) {
        //打开相册，选择图片并保存
        //进入相册选择图片
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            requestPermissions(Global.PERMISSTIONS_CAMERA, Global.REQUEST_EXTERNAL_STORAGE);
        } else {

            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true, "cc.shinichi.bigimageviewpager.fileprovider"))
                    .countable(true)
                    .maxSelectable(max)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideLoadEngine())
                    .theme(com.zhihu.matisse.R.style.Matisse_Zhihu)
                    .showSingleMediaType(true)
                    .originalEnable(true)
                    .forResult(Global.OPEN_ALBUM);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
