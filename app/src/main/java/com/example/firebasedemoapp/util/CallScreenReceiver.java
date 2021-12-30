package com.example.firebasedemoapp.util;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallScreenReceiver extends BroadcastReceiver {
    private static final String TAG=CallScreenReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "called");
        Intent i= new Intent();
        i.setClassName("com.example.firebasedemoapp","com.example.firebasedemoapp.CallScreenActivity");
        i.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
