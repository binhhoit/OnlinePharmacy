package com.example.thanh.OnlinePharmacy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.thanh.OnlinePharmacy.view.main.MainActivity_;
import com.wang.avi.AVLoadingIndicatorView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Thien Long on 3/22/2017.
 */

@EActivity(R.layout.activity_splash)
public class SplashScreenActivity extends AppCompatActivity {
    @ViewById(R.id.activity_start_avi_loading)
    AVLoadingIndicatorView avi;

    @AfterViews
    void init() {
        startAnim();

        Thread timerThread = new Thread() {
            public void run() {
                try {

                    sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //stopAnim();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("Start","start handler");
                            stopAnim();
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity_.class);
                            startActivity(intent);
                        }
                    });

                }
            }
        };
        timerThread.start();

    }

    void startAnim() {
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        //avi.hide();
        avi.smoothToHide();
    }

    @Override
    protected void onPause() {
        //TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
