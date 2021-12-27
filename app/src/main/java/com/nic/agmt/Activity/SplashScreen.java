package com.nic.agmt.Activity;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.nic.agmt.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MODIFY_AUDIO_SETTINGS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/** Created By Dileep Kumar
 * **/

public class SplashScreen extends AppCompatActivity{
    private TextView textView;
    private Button button;
    private static int SPLASH_TIME_OUT = 2000;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        AppScreenLockChecking();

    }


    private void showSignInScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, MainActivity.class);

                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }, SPLASH_TIME_OUT);
    }


    ///Screen Lock Device Have/Haven't Condition Check
    public void AppScreenLockChecking(){
       /* KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if(keyguardManager.isDeviceSecure()){
             Intent screenLockIntent = keyguardManager.createConfirmDeviceCredentialIntent("Secure", "Your Pattern/Password");
        startActivityForResult(screenLockIntent, 101);
        }
        else {
            showSignInScreen();
        }*/
       if(CheckPermissions()){
           showSignInScreen();
       }
       else {
           RequestPermissions();
       }

        //showSignInScreen();

    }

    ////Screen Lock Received Result


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(101 == requestCode){
            if (resultCode == RESULT_OK) {
                //Authentication is successful
                showSignInScreen();
            } else {
                //Authentication failed
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), MODIFY_AUDIO_SETTINGS);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestPermissions() {
        ActivityCompat.requestPermissions(SplashScreen.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE,CAMERA,MODIFY_AUDIO_SETTINGS}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionTocapture = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionTomodify = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore && permissionTocapture && permissionTomodify) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                        showSignInScreen();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                        //RequestPermissions();
                    }
                }
                break;
        }
    }


}
