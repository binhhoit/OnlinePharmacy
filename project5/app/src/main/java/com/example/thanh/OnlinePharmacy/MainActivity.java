package com.example.thanh.OnlinePharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.Login.fragments.LoginFragment;
import com.example.thanh.OnlinePharmacy.Login.fragments.ResetPasswordDialog;


public class MainActivity extends AppCompatActivity implements ResetPasswordDialog.Listener {
    int temp = 0;
    public static final String TAG = MainActivity.class.getSimpleName();

    private LoginFragment mLoginFragment;
    private ResetPasswordDialog mResetPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            loadFragment();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            temp++;
            if (temp == 1) {
                Toast.makeText(MainActivity.this, "Nhấn back 1 lần nữa sẽ thoát chương trình", Toast.LENGTH_SHORT).show();
            }
            if (temp == 2) {
                //thoát khỏi chương trình
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void loadFragment() {

        if (mLoginFragment == null) {

            mLoginFragment = new LoginFragment();
        } //load fragment login frist
        getFragmentManager().beginTransaction().replace(R.id.fragmentFrame, mLoginFragment, LoginFragment.TAG).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String data = intent.getData().getLastPathSegment();
        Log.d(TAG, "onNewIntent: " + data);

        mResetPasswordDialog = (ResetPasswordDialog) getFragmentManager().findFragmentByTag(ResetPasswordDialog.TAG);

        if (mResetPasswordDialog != null)
            mResetPasswordDialog.setToken(data);
    }

    @Override
    public void onPasswordReset(String message) {

        showSnackBarMessage(message);
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_main), message, Snackbar.LENGTH_SHORT).show();

    }
}
