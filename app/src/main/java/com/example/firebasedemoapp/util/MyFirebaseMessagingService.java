package com.example.firebasedemoapp.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.firebasedemoapp.CallScreenActivity;
import com.example.firebasedemoapp.MainActivity;
import com.example.firebasedemoapp.R;
import com.example.firebasedemoapp.services.IncomingCallService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 2;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, " onMessageReceived :: " + remoteMessage.toString());

//        String messageID = remoteMessage.getMessageId();
//        String messageType = remoteMessage.getMessageType();
//        String title = remoteMessage.getNotification().getTitle();
//        String body = remoteMessage.getNotification().getBody();
//        Log.d(TAG, " onMessageReceived :: " + messageID + "\n" + title + "\n" + body + "\n" + messageType);

        if (remoteMessage.getData().size() > 0) {
            try {
                Log.i(TAG, "json: " + remoteMessage.getData());
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) {
        try {
            String type = json.getString("type");
            String name = json.getString("customer_name");
            String phone = json.getString("customer_phone");
            Log.d(TAG, " handleDataMessage :: " + type + "\n" + name + json.getString("customer_phone"));

            switch (type) {
                case "1":
                    Log.i(TAG, "handleDataMessage : called");
                    try {
                        boolean foreground = new ForegroundCheckTask().execute(getApplicationContext()).get();
                        if (foreground) {
                            Log.i(TAG, "ForegroundCheckTask : foreground");
                            //startCallScreenUsingBroadCastReceiver();
                            startIncomingCallService();
                        } else {
                            Log.i(TAG, "ForegroundCheckTask : Background");
                            startCallScreen(name, phone);
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startIncomingCallService() {
        Intent intent = new Intent(getApplicationContext(), IncomingCallService.class);
        intent.setAction(IncomingCallService.ACTION_START_INCOMING_CALL);
        sendBroadcast(intent);
    }


    /*
     * Starting a broadcast receiver
     * This method will start broadcast receiver to start CallScreenReceiver
     *
     * This method will invoke when app is in foreground
     * This method will invoke for every os version for android
     *
     * */
    private void startCallScreenUsingBroadCastReceiver() {
        Intent intent = new Intent(this, IncomingCallService.class);
//        intent.setAction("com.example.callScreen");
        intent.setAction(IncomingCallService.ACTION_START_INCOMING_CALL);
        sendBroadcast(intent);
    }


    /*
     * First, id OS level is below 29 start activity
     *
     *
     * otherwise, create a foreground notification
     * Play sound
     * Vibrate on
     *
     * */


    private void startCallScreen(String name, String phone) {
        Log.d(TAG, "startCallScreen called");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            createNotification(name, phone);
        } else {
            Intent intent = new Intent(getApplicationContext(), CallScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }

    private void createNotification(String name, String phone) {
        Log.i(TAG, "createNotification called");

        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.mi_ringtone);
        mp.start();


        Intent intent = new Intent(getApplicationContext(), CallScreenActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(name)
                .setContentText(phone);

        notificationBuilder.setTimeoutAfter(15000);

//        notificationBuilder.setAutoCancel(true);
//        notificationBuilder.setOngoing(true);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setCategory(Notification.CATEGORY_CALL);
        notificationBuilder.setFullScreenIntent(pendIntent, true); // notification shows heads up until click

        notificationBuilder.setVibrate(new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500});



       /* Intent intentForAccept = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntentAccept = PendingIntent.getActivity(this, 0, intentForAccept, 0);*/


        //accept
        Intent acceptIntent = new Intent(getBaseContext(), IncomingCallService.class);
        acceptIntent.setAction(IncomingCallService.ACTION_ACCEPT_INCOMING_CALL);
        acceptIntent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, acceptIntent, 0);


        //button
        Intent dismissIntent = new Intent(getBaseContext(), IncomingCallService.class);
        dismissIntent.setAction(IncomingCallService.ACTION_STOP_INCOMING_CALL);
        dismissIntent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, dismissIntent, 0);

        notificationBuilder.setContentIntent(pendIntent);
        notificationBuilder.addAction(android.R.drawable.ic_delete, "DISMISS", dismissPendingIntent);
        notificationBuilder.addAction(android.R.drawable.ic_menu_view, "ACCEPT", acceptPendingIntent);


        //notification manager
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //todo :: store firebase token somewhere
        Log.d(TAG, " onNewToken :: " + s);
    }
}
