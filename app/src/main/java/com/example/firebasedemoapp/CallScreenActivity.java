package com.example.firebasedemoapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class CallScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnHandleRejectCall, btnHandleAcceptCall;
/*    private MediaPlayer mp;
    private Vibrator v;*/


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);


        init();

//        vibratePhone(15000);
//
//        playSound();

        closeActivity(15000);
        setFlags();
    }

    private void setFlags() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
    }

    private void closeActivity(long milliSeconds) {
        Handler handler = new Handler();

        handler.postDelayed(() -> finish(), milliSeconds);
    }

    /*private void playSound() {
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
    }*/

    private void init() {
     /*   mp = MediaPlayer.create(this, R.raw.mi_ringtone);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);*/
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
     /*   stopSound();
        stopVibrate();*/
        finish();
    }

    private void acceptCall() {
    /*    stopSound();
        stopVibrate();*/
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.isFinishing()){
  /*      stopSound();
        stopVibrate();*/
        }
    }
}