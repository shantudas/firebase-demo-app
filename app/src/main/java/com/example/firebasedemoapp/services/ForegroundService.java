package com.example.firebasedemoapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.firebasedemoapp.CallScreenActivity;
import com.example.firebasedemoapp.R;
import com.example.firebasedemoapp.util.Constants;

import java.util.Timer;

public class ForegroundService extends Service {
    private static final String TAG = ForegroundService.class.getSimpleName();
    public static final int ID_FOREGROUND_INCOMING_CALL_SERVICE = 1002;

    private LocalBroadcastManager mLocalBoradcastManager;
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;


    private boolean foregroundServiceStarted = false;
    private Timer foregroundServiceTimer;
    private boolean isForegroundServiceTimerStarted;

    private String callerName;
    private String callerContactNumber;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, " called");
        mLocalBoradcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initIntent(intent);
        startIncomingCallService();


//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle(callerName)
//                .setContentText(callerContactNumber)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .build();
//        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    private void initIntent(Intent intent) {
        callerName = intent.getStringExtra(Constants.Intent.INTENT_FROM_SERVICE_CALLER_NAME);
        callerContactNumber = intent.getStringExtra(Constants.Intent.INTENT_FROM_SERVICE_CALLER_CONTACT_NUMBER);
        Log.d(TAG, " initIntent :: " + callerName + ", " + callerContactNumber);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startThisServiceAsForeground(Notification notification) {
        Log.d(TAG, "\t>> startThisServiceAsForeground" + true);
        foregroundServiceStarted = true;
        startForeground(ID_FOREGROUND_INCOMING_CALL_SERVICE, notification);
    }

    private void sendBroadcastEventToReceiver(Intent intent) {
        mLocalBoradcastManager.sendBroadcast(intent);
    }

    private void startIncomingCallService() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(callerName)
                .setContentText(callerContactNumber);

        if (!foregroundServiceStarted) {
            startThisServiceAsForeground(notificationBuilder.build());
        }
        //notificationBuilder.setFullScreenIntent(pendingIntent, true);
        notificationManager.notify(ID_FOREGROUND_INCOMING_CALL_SERVICE, notificationBuilder.build());
        startIncomingCallActivity();

    }

    private void startIncomingCallActivity() {
        Intent intent = new Intent(this, CallScreenActivity.class);
        intent.putExtra(Constants.Intent.INTENT_FROM_SERVICE_CALLER_NAME, callerName);
        intent.putExtra(Constants.Intent.INTENT_FROM_SERVICE_CALLER_CONTACT_NUMBER, callerContactNumber);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
