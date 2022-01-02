package com.example.firebasedemoapp.util;

import static com.example.firebasedemoapp.util.Constants.SHARED_PREF_USER;
import static com.example.firebasedemoapp.util.Constants.USER_FIREBASE_CLOUD_MESSAGE_TOKEN;
import static com.example.firebasedemoapp.util.Constants.USER_FOREGROUND_SERVICE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    SharedPreferences userPref;
    SharedPreferences.Editor editorUser;
    private Context _context;
    private final int PRIVATE_MODE = 0;
    private final int DEFAULT_VALUE_INT = 0;
    private final boolean DEFAULT_VALUE_BOOLEAN = false;
    private final long DEFAULT_VALUE_LONG = 0;
    private final String DEFAULT_VALUE_STRING = "";

    public SharedPrefManager(Context context) {
        this._context = context;
        userPref = _context.getSharedPreferences(SHARED_PREF_USER, PRIVATE_MODE);
        editorUser = userPref.edit();
    }

    public String getUserFirebaseToken() {
        return userPref.getString(USER_FIREBASE_CLOUD_MESSAGE_TOKEN, DEFAULT_VALUE_STRING);
    }

    public void setUserFirebaseToken(String token) {
        editorUser.putString(USER_FIREBASE_CLOUD_MESSAGE_TOKEN, token);
        editorUser.commit();
    }

    public boolean isForegroundServiceRunning(){
        return userPref.getBoolean(USER_FOREGROUND_SERVICE, DEFAULT_VALUE_BOOLEAN);
    }

    public void setForegroundServiceRunning(boolean isRunning){
        editorUser.putBoolean(USER_FOREGROUND_SERVICE,isRunning);
        editorUser.commit();
    }
}
