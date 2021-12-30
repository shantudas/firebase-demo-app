package com.example.firebasedemoapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SHARED_PREF_USER = "APP_USER";
    public static final String USER_FIREBASE_CLOUD_MESSAGE_TOKEN = "user_firebase_token";
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
}
