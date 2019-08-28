package com.example.myapp.util.sound;

import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

/**
 * Created by michaelstokes on 17/05/15.
 */
public class SoundRecent {

    /**
     * Initialize the SoundPool for later API versions
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SoundPool initializeRecentAPISoundPool(SoundPool soundPool) {

        Log.d("Sound", "Initialize Audio Attributes.");
        // Initialize AudioAttributes.
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        Log.d("Sound", "Set AudioAttributes for SoundPool.");
        // Set the audioAttributes for the SoundPool and specify maximum number of streams.
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(7)
                .build();

        return soundPool;
    }
}