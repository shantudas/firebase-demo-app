package com.example.firebasedemoapp.services;

import static com.example.firebasedemoapp.util.Constants.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.firebasedemoapp.R;

public class ForegroundService extends Service {
    private static final String TAG = ForegroundService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, " called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String inputTitle = intent.getStringExtra("inputExtraTitle");
        String inputBody = intent.getStringExtra("inputExtraBody");
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(inputTitle)
                .setContentText(inputBody)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
