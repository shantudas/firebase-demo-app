package com.example.firebasedemoapp.services;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.firebasedemoapp.MyApplication;
import com.example.firebasedemoapp.R;

public class IncomingCallService extends BroadcastReceiver {

    private static final String TAG = IncomingCallService.class.getSimpleName();
    public static final String ACTION_START_INCOMING_CALL = "com.example.firebasedemoapp.services.ACTION_START_INCOMING_CALL";
    public static final String ACTION_STOP_INCOMING_CALL = "com.example.firebasedemoapp.services.IncomingCallService.ACTION_STOP_INCOMING_CALL";
    public static final String ACTION_ACCEPT_INCOMING_CALL = "com.example.firebasedemoapp.services.IncomingCallService.ACTION_ACCEPT_INCOMING_CALL";
    private String action;
    private int notificationId;


    Context ctx;
    MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, " onReceive called");
        ctx = context;
        mp = MediaPlayer.create(ctx, R.raw.mi_ringtone);

        getIntent(intent);
    }

    private void getIntent(Intent intent) {
        action = intent.getAction();
        notificationId = intent.getIntExtra("notificationId", 0);
        Log.i(TAG, "getIntent ::" + action + "\n" + "notificationId " + notificationId);
        if (action != null) {
            switch (action) {
                case ACTION_START_INCOMING_CALL:
//                    playSoundWithVibration();
                    startIncomingCallActivity();
                    break;
                case ACTION_STOP_INCOMING_CALL:
//                    stopSoundWithVibration();
                    dismissNotification();
                    break;
                case ACTION_ACCEPT_INCOMING_CALL:
                    dismissNotification();
//                    stopSoundWithVibration();
                    startHomeActivity();
                    break;
            }
        }
    }

    private void startHomeActivity() {
        Intent i = new Intent();
        i.setClassName("com.example.firebasedemoapp", "com.example.firebasedemoapp.MainActivity");
        i.addFlags(FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    private void startIncomingCallActivity() {
        Intent i = new Intent();
        i.setClassName("com.example.firebasedemoapp", "com.example.firebasedemoapp.CallScreenActivity");
        i.addFlags(FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    private void dismissNotification() {
        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
    }

    private void stopSoundWithVibration() {
        Log.i(TAG, " stopSoundWithVibration called");
        Log.i(TAG, " stopSoundWithVibration called" + mp.isPlaying());
        mp.isPlaying();
    }

    private void playSoundWithVibration() {
        Log.i(TAG, " playSoundWithVibration called");
        mp.start();
    }
}
