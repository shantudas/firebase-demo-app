package com.example.firebasedemoapp;

import static com.example.firebasedemoapp.util.Constants.CHANNEL_ID;
import static com.example.firebasedemoapp.util.Constants.CHANNEL_NAME;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.mi_ringtone);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public  void stopSoundWithVibration() {
        Log.i(TAG, " stopSoundWithVibration called");
        Log.i(TAG, " stopSoundWithVibration called" + mp.isPlaying());
        mp.isPlaying();
    }

    public  void playSoundWithVibration() {
        Log.i(TAG, " playSoundWithVibration called");
        mp.start();
    }
}
