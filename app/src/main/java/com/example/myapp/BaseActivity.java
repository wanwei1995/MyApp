package com.example.myapp;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import com.example.myapp.util.AppManager;
import com.example.myapp.util.sound.Sound;

public class BaseActivity extends AbstractActivity {

    private Sound sound;

    private ProgressDialog progressDialog;


    private Vibrator mVibrator;  //声明一个振动器对象


    protected void loadSound() {
        sound = new Sound(this);
        sound.setInitialSoundPool();
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
    }

    public void mVibrator(){
        //调用震动
        if(mVibrator == null){
            mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(500);
    }
}
