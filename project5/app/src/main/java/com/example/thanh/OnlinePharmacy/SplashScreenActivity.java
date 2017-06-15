package com.example.thanh.OnlinePharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.thanh.OnlinePharmacy.view.main.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Thien Long on 3/22/2017.
 */

@EActivity(R.layout.activity_splash)
public class SplashScreenActivity extends AppCompatActivity {

    @AfterViews
    void init() {

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        //TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
