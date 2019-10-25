package com.example.myapp.util.sound;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import com.example.myapp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 19/03/15.
 */
public class Sound {

    /**
     * The sound pool
     */
    private SoundPool soundPool = null;

    private Map<Integer, Integer> soundIdMap = new HashMap<>();


    /**
     * The current Sound.
     */
    private int sound = 0;

    /**
     * false Boolean.
     */
    private boolean loaded = false;

    /**
     * The context.
     */
    private Context context = null;

    /**
     * Audio Manager.
     */
    private AudioManager audioManager = null;

    /**
     * Literal Volume.
     */
    private float literalVolume = 0;

    /**
     * Maximum Volume.
     */
    private float maximumVolume = 0;

    /**
     * Volume.
     */
    private float volume = 0;

    /**
     * A constructor for creating a new Sound object
     */
    public Sound(Context context) {

        this.context = context;

        this.audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);

        // Set Literal Volume for Audio Manager.
        this.literalVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // Set Maximum Volume for Audio Manager.
        this.maximumVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        // Set volume for GameSound Pool.
        this.volume = literalVolume / maximumVolume;

    }

    /**
     * Initialize the SoundPool for later API versions
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeRecentAPISoundPool() {

        Log.d("SoundPool", "Initialize recent API Sound Pool");
        this.soundPool = new SoundRecent().initializeRecentAPISoundPool(this.soundPool);
    }

    /**
     * Intialize SoundPool for older API      versions
     */
    @SuppressWarnings("deprecation")
    private void initializeDeprecatedAPISoundPool() {
        // Initialize SoundPool.
        this.soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * Load Sounds into the SoundPool.
     */
    private void addSoundsToSoundPool() {
        Log.d("Sound", "Load Sounds.");
        soundIdMap.clear();

        addSoundsToSoundPool(R.raw.right);
        addSoundsToSoundPool(R.raw.error);
        addSoundsToSoundPool(R.raw.ok);
    }

    public void addSoundsToSoundPool(int resId) {
        soundIdMap.put(resId, soundPool.load(context, resId, 1));
        Log.d("Sound", "Sound " + soundIdMap.get(resId) + " Loaded.");
    }

    /**
     * Set the initial SoundPool.
     * Call to Method differs dependent on API Version.
     */
    public void setInitialSoundPool() {

        Log.d("Sound", "Initialize Recent or Deprecated API SoundPool.");

        // Initialize SoundPool, call specific dependent on SDK Version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("Sound", "Initialize Recent API SoundPool.");
            initializeRecentAPISoundPool();
        } else {
            Log.d("Sound", "Initialize Old API SoundPool.");
            initializeDeprecatedAPISoundPool();
        }

        this.soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) ->  {
                loaded = true;
        });

        // Add and Load sounds into SoundPool.
        this.addSoundsToSoundPool();
    }

    /**
     * Plays the sound
     *
     * @param resId   - the sound res id
     */
    public void playSoundByResId(int resId) {
        int soundId = soundIdMap.get(resId);
        Log.d("Sound", "Play GameSound " + soundId + ".");
        soundPool.play(soundId, this.volume, this.volume, 1, 0, 1f);
        Log.d("Sound", "GameSound " + soundId + " Played");
    }
}