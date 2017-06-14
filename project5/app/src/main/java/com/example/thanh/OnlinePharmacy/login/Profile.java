package com.example.thanh.OnlinePharmacy.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.thanh.OnlinePharmacy.login.fragments.ChangePasswordDialog;
import com.example.thanh.OnlinePharmacy.login.model.Response;
import com.example.thanh.OnlinePharmacy.login.model.User;
import com.example.thanh.OnlinePharmacy.login.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.login.utils.Constants;
import com.example.thanh.OnlinePharmacy.MainActivity;
import com.example.thanh.OnlinePharmacy.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class Profile extends AppCompatActivity implements ChangePasswordDialog.Listener {

    public static final String TAG = Profile.class.getSimpleName();

    private TextView mTvName;
    private TextView mTvEmail;
    private TextView mTvDate;
    private Button mBtChangePassword;
    private Button mBtLogout;
    private ProgressBar mProgressbar;
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;


    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mSubscriptions = new CompositeSubscription();
        initViews();
        initSharedPreferences();
        loadProfile();

    }

    //khởi tạo findviewbyid
    private void initViews() {

        mTvName = (TextView) findViewById(R.id.activity_receiverPresciption_tv_name);
        mTvEmail = (TextView) findViewById(R.id.activity_receiverPresciption_tv_email);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mBtChangePassword = (Button) findViewById(R.id.btn_change_password);
        mBtLogout = (Button) findViewById(R.id.btn_logout);
        mProgressbar = (ProgressBar) findViewById(R.id.progress);
        mBtChangePassword.setOnClickListener(view -> showDialog());
        mBtLogout.setOnClickListener(view -> logout());
    }


    private void initSharedPreferences() {

        mSharedPreferences = getApplication().getSharedPreferences("account",MODE_PRIVATE);
        mToken = mSharedPreferences.getString(Constants.TOKEN, "");
        mEmail = mSharedPreferences.getString(Constants.EMAIL, "");
    }

    private void logout() {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL, "");
        editor.putString(Constants.TOKEN, "");
        editor.apply();
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void showDialog() {

        ChangePasswordDialog fragment = new ChangePasswordDialog();
        // Bundle dùng để đẩy dữ liệu qua các activity
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, mEmail);
        bundle.putString(Constants.TOKEN, mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);
    }

    // các phản hồi đưa lên đc load
    private void loadProfile() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    // phản hồi của user lên server
    private void handleResponse(User user) {

        mProgressbar.setVisibility(View.GONE);
        mTvName.setText(user.getName());
        mTvEmail.setText(user.getEmail());
        mTvDate.setText(user.getCreated_at());
    }

    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_profile), message, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    @Override
    public void onPasswordChanged() {

        showSnackBarMessage("Password Changed Successfully !");
    }


}


