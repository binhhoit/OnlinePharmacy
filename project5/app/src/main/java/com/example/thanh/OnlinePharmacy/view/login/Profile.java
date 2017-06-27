package com.example.thanh.OnlinePharmacy.view.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.thanh.OnlinePharmacy.view.login.fragments.ChangePasswordDialog;
import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.model.User;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.view.login.fragments.ChangePasswordDialog_;
import com.example.thanh.OnlinePharmacy.view.main.MainActivity_;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

@EActivity(R.layout.activity_profile)
public class Profile extends AppCompatActivity implements ChangePasswordDialog.Listener {

    @ViewById(R.id.activity_receiver_presciption_details_tv_name)
    protected TextView tvName;
    @ViewById(R.id.activity__presciption_details_tv_email)
    protected TextView tvEmail;
    @ViewById(R.id.tv_date)
    protected TextView tvDate;
    @ViewById(R.id.progress)
    protected ProgressBar progressbar;

    private SharedPreferences sharedPreferences;
    private String token;
    private String email;
    private CompositeSubscription subscriptions;

    @AfterViews
    void init() {

        subscriptions = new CompositeSubscription();
        initSharedPreferences();
        loadProfile();

    }

    private void initSharedPreferences() {

        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");
        email = sharedPreferences.getString(Constants.EMAIL, "");
    }

    @Click(R.id.btn_logout)
    void logout() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.EMAIL, "");
        editor.putString(Constants.TOKEN, "");
        editor.apply();
        Intent intent = new Intent(Profile.this, MainActivity_.class);
        startActivity(intent);
        finish();
    }

    @Click(R.id.btn_change_password)
    void showDialog() {

        ChangePasswordDialog fragment = new ChangePasswordDialog_();
        // Bundle pass data to activity
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, email);
        bundle.putString(Constants.TOKEN, token);
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);
    }

    // các phản hồi đưa lên đc load
    private void loadProfile() {

        subscriptions.add(NetworkUtil.getRetrofit(token).getProfile(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    // phản hồi của user lên server
    private void handleResponse(User user) {

        progressbar.setVisibility(View.GONE);
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvDate.setText(user.getCreatedAt());
    }

    private void handleError(Throwable error) {

        progressbar.setVisibility(View.GONE);

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
        subscriptions.unsubscribe();
    }

    @Override
    public void onPasswordChanged() {
        showSnackBarMessage("Password Changed Successfully !");
    }
}


