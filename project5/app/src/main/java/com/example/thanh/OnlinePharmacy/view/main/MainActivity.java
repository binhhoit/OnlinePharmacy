package com.example.thanh.OnlinePharmacy.view.main;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.view.login.fragments.LoginFragment;
import com.example.thanh.OnlinePharmacy.view.login.fragments.LoginFragment_;
import com.example.thanh.OnlinePharmacy.view.login.fragments.ResetPasswordDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements ResetPasswordDialog.Listener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private int TEMP = 0;

    private LoginFragment loginFragment;
    private ResetPasswordDialog resetPasswordDialog;

    @AfterViews
    void init() {

        loadFragment();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TEMP++;
            if (TEMP == 1) {
                String string = getString(R.string.back);
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
            }
            if (TEMP == 2) {
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

        if (loginFragment == null) {
            loginFragment = new LoginFragment_();
        } //load fragment login frist
        getFragmentManager().beginTransaction().replace(R.id.fragmentFrame, loginFragment, LoginFragment.TAG).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String data = intent.getData().getLastPathSegment();
        Log.d(TAG, "onNewIntent: " + data);

        resetPasswordDialog = (ResetPasswordDialog) getFragmentManager().findFragmentByTag(ResetPasswordDialog.TAG);

        if (resetPasswordDialog != null) {
            resetPasswordDialog.setToken(data);
        }
    }

    @Override
    public void onPasswordReset(String message) {

        showSnackBarMessage(message);
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_main), message, Snackbar.LENGTH_SHORT).show();

    }
}
