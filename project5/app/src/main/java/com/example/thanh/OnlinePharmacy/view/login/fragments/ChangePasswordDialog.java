package com.example.thanh.OnlinePharmacy.view.login.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.model.User;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wang.avi.AVLoadingIndicatorView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.thanh.OnlinePharmacy.utils.Validation.validateFields;

@EFragment(R.layout.dialog_fragment_change_password)
public class ChangePasswordDialog extends DialogFragment {

    public interface Listener {

        void onPasswordChanged();
    }

    public static final String TAG = ChangePasswordDialog.class.getSimpleName();

    @ViewById(R.id.et_old_password)
    protected EditText etOldPassword;

    @ViewById(R.id.et_new_password)
    protected EditText etNewPassword;

    @ViewById(R.id.ti_old_password)
    protected TextInputLayout tiOldPassword;

    @ViewById(R.id.ti_new_password)
    protected TextInputLayout tiNewPassword;

    @ViewById(R.id.tv_message)
    protected TextView tvMessage;

    @ViewById(R.id.activity_change_pass_avi_loading)
    protected AVLoadingIndicatorView avLoadingIndicatorView;

    private CompositeSubscription subscriptions;

    private String token;
    private String id;

    private Listener listener;

    @AfterViews
    void init() {

        subscriptions = new CompositeSubscription();

        getListten();

        getData();
    }

    private void getData() {

        Bundle bundle = getArguments();

        token = bundle.getString(Constants.TOKEN);
        id = bundle.getString(Constants.ID);
    }


    public void getListten() {
         listener = (Listener) getActivity();
    }

    @Click(R.id.btn_cancel)
    protected void cancel() {
        dismiss();
    }

    @Click(R.id.btn_change_password)
    protected void changePassword() {
        setError();

        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();

        int err = 0;

        if (!validateFields(oldPassword)) {
            err++;
            tiOldPassword.setError("Password should not be empty !");
        }

        if (!validateFields(newPassword)) {
            err++;
            tiNewPassword.setError("Password should not be empty !");
        }

        if (err == 0) {
            User user = new User();
            user.setPassword(oldPassword);
            user.setNewPassword(newPassword);
            changePasswordProgress(user);
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
        }
    }

    private void setError() {
        tiOldPassword.setError(null);
        tiNewPassword.setError(null);
    }

    private void changePasswordProgress(User user) {
        subscriptions.add(NetworkUtil.getRetrofit(token).changePassword(id, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {
        avLoadingIndicatorView.setVisibility(View.GONE);
        listener.onPasswordChanged();
        ChangePasswordDialog.this.dismiss();
    }

    private void handleError(Throwable error) {
        avLoadingIndicatorView.setVisibility(View.GONE);

        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                showMessage(response.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showMessage("Network Error !");
        }
    }

    private void showMessage(String message) {
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}