package com.example.firebasedemoapp.util;

public class Constants {
    //shared pref
    public static final String SHARED_PREF_USER = "APP_USER";
    public static final String USER_FIREBASE_CLOUD_MESSAGE_TOKEN = "user_firebase_token";
    public static final String USER_FOREGROUND_SERVICE = "user_foreground_service";

    //notification
    public static final String CHANNEL_ID = "DEMO_APPLICATION_CHANNEL";
    public static final String CHANNEL_NAME = "Foreground Service Chanel";


    //type
    public static final int TYPE_FCM_GENERAL = 1;
    public static final int TYPE_FCM_CALL = 2;


    public class Intent{
        public static final String INTENT_FROM_SERVICE_CALLER_NAME="CallerName";
        public static final String INTENT_FROM_SERVICE_CALLER_CONTACT_NUMBER="CallerContactNumber";
    }
}
