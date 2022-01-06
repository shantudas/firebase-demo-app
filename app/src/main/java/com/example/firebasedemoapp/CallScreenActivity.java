package com.example.firebasedemoapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebasedemoapp.util.Constants;

public class CallScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG=CallScreenActivity.class.getSimpleName();

    private ImageButton btnHandleRejectCall, btnHandleAcceptCall;
    private TextView tvCallerName,tvCallerContactNumber;
    private MediaPlayer mp;
    private Vibrator v;


    private String callerName;
    private String callerContactNumber;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);

        init();

        initIntent();

        vibratePhone(15000);

        playSound();

        closeActivity(15000);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        setData();
    }

    private void setData() {
        tvCallerName.setText(callerName);
        tvCallerContactNumber.setText(callerContactNumber);
    }

    private void initIntent(){
        callerName=getIntent().getStringExtra(Constants.Intent.INTENT_FROM_SERVICE_CALLER_NAME);
        callerContactNumber=getIntent().getStringExtra(Constants.Intent.INTENT_FROM_SERVICE_CALLER_CONTACT_NUMBER);
        Log.d(TAG, " initIntent :: "+callerName+", "+callerContactNumber);
    }

    private void closeActivity(long milliSeconds) {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, milliSeconds);
    }

    private void playSound() {
        mp.start();
    }

    private void stopSound() {
        mp.stop();
    }

    private void stopVibrate() {
        v.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void vibratePhone(long milliSeconds) {

        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliSeconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliSeconds);
        }
    }

    private void init() {
        mp = MediaPlayer.create(this, R.raw.mi_ringtone);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        tvCallerName = findViewById(R.id.tvCallerName);
        tvCallerContactNumber = findViewById(R.id.tvCallerContactNumber);


        btnHandleRejectCall = findViewById(R.id.btnHandleRejectCall);
        btnHandleRejectCall.setOnClickListener(this);
        btnHandleAcceptCall = findViewById(R.id.btnHandleAcceptCall);
        btnHandleAcceptCall.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHandleRejectCall:
                rejectCall();
                break;
            case R.id.btnHandleAcceptCall:
                acceptCall();
                break;
        }
    }

    private void rejectCall() {
        stopSound();
        stopVibrate();
        finish();
    }

    private void acceptCall() {
        stopSound();
        stopVibrate();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.isFinishing()){
        stopSound();
        stopVibrate();
        }
    }
}