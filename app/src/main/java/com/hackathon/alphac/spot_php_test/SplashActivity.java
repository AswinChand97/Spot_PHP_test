package com.hackathon.alphac.spot_php_test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    String[] permissionsString = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        if(!hasPermissions(this,permissionsString))
        {
            ActivityCompat.requestPermissions(this,permissionsString,131);
        }
        else {
            startMainActivity();
        }
    }
    void startMainActivity(){
        Handler handler = new Handler();
        Runnable r  = new Runnable() {
            @Override
            public void run() {
                Intent startAct;
                startAct = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(startAct);
            }
        };
        handler.postDelayed(r,3500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 131)
        {
            if(grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED )
            {
                startMainActivity();
            }
            else
            {
                Toast.makeText(this,"Please grant the requested permissions",Toast.LENGTH_LONG).show();
                this.finish();
            }
        }
        else
        {
            Toast.makeText(this,"Something went wrong!",Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    boolean hasPermissions(Context context, String[] permissions)
    {
        boolean hasAllPermissions = true;
        for(String x : permissions)
        {
            int status = context.checkCallingOrSelfPermission(x);
            if(status != PackageManager.PERMISSION_GRANTED)
            {
                hasAllPermissions = false;
            }
        }
        return hasAllPermissions;
    }
}
