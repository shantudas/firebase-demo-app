package com.example.firebasedemoapp.services;

import static com.example.firebasedemoapp.util.Constants.TYPE_FCM_CALL;
import static com.example.firebasedemoapp.util.Constants.TYPE_FCM_GENERAL;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.firebasedemoapp.R;
import com.example.firebasedemoapp.util.CallScreenReceiver;
import com.example.firebasedemoapp.util.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

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
            int type = json.getInt("type");
            String name = json.getString("customer_name");
            String phone = json.getString("customer_phone");
            Log.d(TAG, " handleDataMessage :: " + type + "\n" + name + json.getString("customer_phone"));

            switch (type) {
                case TYPE_FCM_CALL:
                    Log.d(TAG, " TYPE_FCM_CALL : called");

                    Intent intent = new Intent(getApplicationContext(), ForegroundService.class);
                    intent.putExtra(Constants.Intent.INTENT_FROM_SERVICE_CALLER_NAME,name);
                    intent.putExtra(Constants.Intent.INTENT_FROM_SERVICE_CALLER_CONTACT_NUMBER,phone);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    } else {
                        startService(intent);
                    }


//                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
//                            .setSmallIcon(R.drawable.ic_launcher_foreground)
//                            .setContentTitle(name)
//                            .setContentText(phone);
//
//                    NotificationManager notificationManager =
//                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.notify(2, notificationBuilder.build());
//
//
//                    Intent intent = new Intent(this, CallScreenReceiver.class);
//                    intent.setAction("com.example.callScreen");
//                    sendBroadcast(intent);

                    break;

                case TYPE_FCM_GENERAL:
                    Log.d(TAG, " TYPE_FCM_GENERAL : called");
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //todo :: store firebase token somewhere
        Log.d(TAG, " onNewToken :: " + " called c1vin0y2SueddRKSrU9IjF:APA91bGJiigOKbjzAU-XT5NhOPTzva5mFZKgO1zGDVILDmuM668JvdibewOlSpC9_FaVKJeZKkEFagjyjOKcOef8s4u2HpTp-jEMOQNojdKSOcIxaMDdqLlhSuGG1y0wW5ug5sF0hN-a");
        Log.d(TAG, " onNewToken :: " + s);


    }
}
