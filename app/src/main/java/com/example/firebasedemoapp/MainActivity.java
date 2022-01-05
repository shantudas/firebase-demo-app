package com.example.firebasedemoapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemoapp.services.ForegroundService;
import com.example.firebasedemoapp.util.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public final static int SYSTEM_OVERLAY_PERMISSION_REQUEST_CODE = 101;
    /*
     * Resources
     * https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase
     *
     *
     * */

    SharedPrefManager sharedPrefManager;
    private Button btnHandleForegroundService;
    private TextView tvForegroundServiceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setForegroundServiceState();


        TextView tvFirebaseToken= findViewById(R.id.tvFirebaseToken);
        String token="";

        if (TextUtils.isEmpty(sharedPrefManager.getUserFirebaseToken())) {
            storeFirebaseTokenIntoSharedPref();
        } else {
             token = sharedPrefManager.getUserFirebaseToken();
            Log.i(TAG, "firebase token :: " + token);
            tvFirebaseToken.setText(token);
        }
        String finalToken = token;
        tvFirebaseToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", finalToken);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "copied", Toast.LENGTH_SHORT).show();
            }
        });

        if (isMiUi()){
            //show a dialog to enable settings
            Toast.makeText(MainActivity.this, "Enable show on lock screen", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isMiUi() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            java.lang.Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }

    private void setForegroundServiceState() {
        if (isServiceRunning()){
            tvForegroundServiceState.setText("Foreground Service Running");
            btnHandleForegroundService.setText("Stop Service");
        }else {
            tvForegroundServiceState.setText("Foreground Service Closed");
            btnHandleForegroundService.setText("Start Service");
        }
    }

    private void init(){
        sharedPrefManager = new SharedPrefManager(MainActivity.this);
        tvForegroundServiceState=findViewById(R.id.tvForegroundServiceState);
        btnHandleForegroundService=findViewById(R.id.btnHandleForegroundService);
        btnHandleForegroundService.setOnClickListener(this);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkDrawOverlayPermission();
        }
    }

    private void storeFirebaseTokenIntoSharedPref() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.i(TAG, "firebase token :: " + token);
                        sharedPrefManager.setUserFirebaseToken(token);
                        Log.i(TAG, "firebase token from shared pref :: " + sharedPrefManager.getUserFirebaseToken());
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        Log.v(TAG+ " App", "Package Name: " + getApplicationContext().getPackageName());

        // check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(MainActivity.this)) {
            Log.v(TAG+" App", "Requesting Permission" + Settings.canDrawOverlays(MainActivity.this));
            showDialogForActivateSystemOverlay();
        } else {
            Log.v(TAG+" App", "We already have permission for it.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showDialogForActivateSystemOverlay() {
        new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setTitle("Display Over Other Apps")
                .setMessage("Please allow display over other apps to continue.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Continue with operation
                    // if not construct intent to request permission
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getApplicationContext().getPackageName()));
                    someActivityResultLauncher.launch(intent);
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.v("App", "OnActivity Result.");
//        //check if received result code
//        //  is equal our requested code for draw permission
//        if (requestCode == SYSTEM_OVERLAY_PERMISSION_REQUEST_CODE) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (Settings.canDrawOverlays(this)) {
//                    disablePullNotificationTouch();
//                }
//            }
//        }
//    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    Log.i(TAG, "RESULT OK");
                }
            });

    private void stopForegroundService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);

        sharedPrefManager.setForegroundServiceRunning(false);
        setForegroundServiceState();
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtraTitle", "Firebase Demo App");
        serviceIntent.putExtra("inputExtraBody", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);

        sharedPrefManager.setForegroundServiceRunning(true);
        setForegroundServiceState();
    }

    private boolean isServiceRunning(){
        boolean isRunning=sharedPrefManager.isForegroundServiceRunning();
        Log.i(TAG, String.valueOf(isRunning));
        return isRunning;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnHandleForegroundService){
            if (isServiceRunning()){ //running
                stopForegroundService();
            }else {
                startForegroundService();
            }
        }
    }
}