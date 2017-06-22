package com.example.thanh.OnlinePharmacy.view.login.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.view.login.Profile;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.model.User;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobsandgeeks.saripaar.ValidationError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

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

    @ViewById(R.id.progress)
    protected ProgressBar progressBar;

    private CompositeSubscription subscriptions;

    private String token;
    private String email;

    private Listener listener;

    @AfterViews
    void init() {

        subscriptions = new CompositeSubscription();
        getData();
    }

    private void getData() {

        Bundle bundle = getArguments();

        token = bundle.getString(Constants.TOKEN);
        email = bundle.getString(Constants.EMAIL);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Profile) context;
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
            progressBar.setVisibility(View.VISIBLE);

        }
    }

    private void setError() {

        tiOldPassword.setError(null);
        tiNewPassword.setError(null);
    }

    private void changePasswordProgress(User user) {

        subscriptions.add(NetworkUtil.getRetrofit(token).changePassword(email, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        progressBar.setVisibility(View.GONE);
        listener.onPasswordChanged();
        dismiss();
    }

    private void handleError(Throwable error) {

        progressBar.setVisibility(View.GONE);

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

