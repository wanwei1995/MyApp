package com.example.myapp;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapp.util.sound.Sound;


public abstract class AbstractActivity extends AppCompatActivity {
    private Sound sound;

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
        playSoundByResId(R.raw.error);
    }

    /**
     * 声音播放，【正确】
     */
    public void playSoundOk() {
        playSoundByResId(R.raw.ok);
    }

    String getTextById(int id) {
        View view = this.findViewById(id);
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString();
        } else if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        }
        return "";
    }

    void setTextById(int id, String text) {
        View view = this.findViewById(id);
        if (view instanceof EditText) {
            ((EditText) view).setText(text);
        } else if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }

    void appendTitileInfo(String msg) {
        String splitSign = "-";

        String title = this.getTitle().toString();
        if (title.split(splitSign).length > 1) {
            this.setTitle(title.split(splitSign)[0] + splitSign + msg);
        } else {
            this.setTitle(title + splitSign + msg);
        }
    }

    public void showError(int msgId) {
        playSoundError();
        this.showToast(this.getString(msgId), Toast.LENGTH_SHORT);
    }

    public void showError(String msg) {
        playSoundError();
        this.showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(String msg, int duration) {
        Toast.makeText(this, msg, duration).show();
    }

}
