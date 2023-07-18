package com.example.musichub.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.musichub.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Splash extends AppCompatActivity {

    private String[] permission13 = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.POST_NOTIFICATIONS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Log.d(TAG, "onCreate: "+Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            hasPermission();
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            getAdditionalPermission();
        }
    }

    private boolean getAdditionalPermission() {
        List<String> stringList = new ArrayList<>();
        for (String permission : permission13) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                stringList.add(permission);
            }
        }
        if (!stringList.isEmpty()) {
            ActivityCompat.requestPermissions(this, stringList.toArray(new String[stringList.size()]), 101);
            return false;
        }
        return true;
    }

    private void hasPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            moveToNext();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        }
    }

    private void moveToNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, Landing.class);
                startActivity(intent);
            }
        }, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                moveToNext();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            }
        } else if (requestCode == 101) {
            HashMap<String, Integer> map = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    map.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount != 0) {
                for (Map.Entry<String, Integer> l1 : map.entrySet()) {
                    String name = l1.getKey();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, name)) {
                        new AlertDialog.Builder(this).setCancelable(false).setMessage("Please Allow the Permissions")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        hasPermission();
                                    }
                                }).create().show();
                    } else {
                        new AlertDialog.Builder(this).setTitle("Permission").setMessage("We Need Permission").setCancelable(false)
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finishAffinity();
                                    }
                                }).setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create().show();
                    }
                }
            } else {
                moveToNext();
            }
        }
    }
}