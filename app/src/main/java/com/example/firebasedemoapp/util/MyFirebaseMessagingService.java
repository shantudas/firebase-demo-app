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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.firebasedemoapp.CallScreenActivity;
import com.example.firebasedemoapp.MainActivity;
import com.example.firebasedemoapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

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
                            startCallScreenUsingBroadCastReceiver();
                        } else {
                            startCallScreen(name, phone);
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }



                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle(name)
                            .setContentText(phone);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(2, notificationBuilder.build());


                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     *  You can start your activity by calling ‘startActivity(intent)’ method in onMessageReceived method.
     *  This case will work in every Android OS. You can simply start activity by sending a broadcast from
     *  firebase service and open activity on receiving that broadcast.
     * */
    private void startCallScreenUsingBroadCastReceiver() {
        Intent intent = new Intent(this, CallScreenReceiver.class);
        intent.setAction("com.example.callScreen");
        sendBroadcast(intent);
    }

    /*
    * You can start your activity by simply calling ‘startActivity(intent)’ method in onMessageReceived method.
    *  Make sure you have set priority to high for this type of notifications.
    * Simply send a broadcast from firebase service and open activity on receiving broadcast.
    * Note: works fine in API level < 29 (Android 10)
    * */
    private void startCallScreen(String name, String phone) {
        Intent intent = new Intent(getApplicationContext(), CallScreenActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //todo :: store firebase token somewhere
        Log.d(TAG, " onNewToken :: " + " called c1vin0y2SueddRKSrU9IjF:APA91bGJiigOKbjzAU-XT5NhOPTzva5mFZKgO1zGDVILDmuM668JvdibewOlSpC9_FaVKJeZKkEFagjyjOKcOef8s4u2HpTp-jEMOQNojdKSOcIxaMDdqLlhSuGG1y0wW5ug5sF0hN-a");
        Log.d(TAG, " onNewToken :: " + s);


    }
}
