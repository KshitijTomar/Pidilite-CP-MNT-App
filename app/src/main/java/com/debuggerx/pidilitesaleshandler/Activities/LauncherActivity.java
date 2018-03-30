package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.debuggerx.pidilitesaleshandler.R;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {

    String isf;
    ImageView Launcher_Image;
    ImageView Debugger_Image;
    Animation myFadeInAnimation;
    Animation myFadeOutAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //calling for objects
        Launcher_Image= (ImageView) findViewById(R.id.Launcher_Image);
        Debugger_Image=(ImageView) findViewById(R.id.DebuggerX_Image);
        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_launcher);
        myFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        //coding for animation
        Launcher_Image.startAnimation(myFadeInAnimation);
        Debugger_Image.startAnimation(myFadeInAnimation);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Launcher_Image.startAnimation(myFadeOutAnimation);
                Debugger_Image.startAnimation(myFadeOutAnimation);
            }
        }, 2800);

        //calling for a Permission Check
        try {
            checkAndRequestPermissions();       //check permission via permission check
        } catch (Exception e) {
            e.printStackTrace();                //open app settings for user to enable permissions manually
            Toast.makeText(LauncherActivity.this, "Select \"Permissions\" and\nEnable Storage", Toast.LENGTH_LONG).show();
            Toast.makeText(LauncherActivity.this, "Select \"Permissions\" and\nEnable Storage", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }

        int splash_time_out = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref= getSharedPreferences("launchInfo", Context.MODE_PRIVATE);
                isf= sharedPref.getString("isfirstrun","true");             //checks for a first run
                if (isf.equals("true")){
                    Intent launcherIntent = new Intent(LauncherActivity.this, LoginActivity.class);             // if first run intent to Login Activity
                    startActivity(launcherIntent);
                }
                else {
                    Intent launcherIntent = new Intent(LauncherActivity.this, Main2Activity.class);             // if not first run intent to main Activity
                    startActivity(launcherIntent);
                }
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);      //transistion fade in/out btween activities
                finish();
            }
        }, splash_time_out);
    }


    private  boolean checkAndRequestPermissions() {
        int storagewr = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storagerd = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storagewr != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (storagerd != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), LoginActivity.REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
