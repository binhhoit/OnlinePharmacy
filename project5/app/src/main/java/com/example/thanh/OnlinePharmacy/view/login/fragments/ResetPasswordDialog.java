package com.example.thanh.OnlinePharmacy.view.login.fragments;

import android.widget.EditText;
import android.app.DialogFragment;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.model.User;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.thanh.OnlinePharmacy.utils.Validation.validateEmail;
import static com.example.thanh.OnlinePharmacy.utils.Validation.validateFields;

@EFragment(R.layout.dialog_fragment_reset_password)
public class ResetPasswordDialog extends DialogFragment {

    public interface Listener {

        void onPasswordReset(String message);
    }

    public static final String TAG = ResetPasswordDialog.class.getSimpleName();

    @ViewById(R.id.et_email)
    protected EditText etEmail;

    @ViewById(R.id.et_token)
    protected  EditText etToken;

    @ViewById(R.id.et_password)
    protected EditText etPassword;

    @ViewById(R.id.progress)
    protected ProgressBar progressBar;

    @ViewById(R.id.tv_message)
    protected TextView tvMessage;

    @ViewById(R.id.ti_email)
    protected TextInputLayout tiEmail;

    @ViewById(R.id.ti_token)
    protected TextInputLayout tiToken;

    @ViewById(R.id.ti_password)
    protected TextInputLayout tiPassword;

    private CompositeSubscription subscriptions;
    private String email;
    private boolean isInit = true;
    private Listener listner;

    @AfterViews
    protected void init() {

        subscriptions = new CompositeSubscription();
    }

    @Click(R.id.btn_reset_password)
    protected void resetPassword() {

        if (isInit) resetPasswordInit();
        else resetPasswordFinish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* listner = (LoginActivity) context;*/
    }

    private void setEmptyFields() {

        tiEmail.setError(null);
        tiToken.setError(null);
        tiPassword.setError(null);
        tvMessage.setText(null);
    }

    public void setToken(String token) {

        etToken.setText(token);
    }

    private void resetPasswordInit() {

        setEmptyFields();

        email = etEmail.getText().toString();

        int err = 0;

        if (!validateEmail(email)) {

            err++;
            tiEmail.setError("Email Should be Valid !");
        }

        if (err == 0) {

            progressBar.setVisibility(View.VISIBLE);
            resetPasswordInitProgress(email);
        }
    }

    private void resetPasswordFinish() {

        setEmptyFields();

        String token = etToken.getText().toString();
        String password = etPassword.getText().toString();

        int err = 0;

        if (!validateFields(token)) {

            err++;
            tiToken.setError("Token Should not be empty !");
        }

        if (!validateFields(password)) {

            err++;
            tiEmail.setError("Password Should not be empty !");
        }

        if (err == 0) {

            progressBar.setVisibility(View.VISIBLE);

            User user = new User();
            user.setPassword(password);
            user.setToken(token);
            resetPasswordFinishProgress(user);
        }
    }

    private void resetPasswordInitProgress(String email) {

        subscriptions.add(NetworkUtil.getRetrofit().resetPasswordInit(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void resetPasswordFinishProgress(User user) {

        subscriptions.add(NetworkUtil.getRetrofit().resetPasswordFinish(email, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        progressBar.setVisibility(View.GONE);

        if (isInit) {

            isInit = false;
            showMessage(response.getMessage());
            tiEmail.setVisibility(View.GONE);
            tiToken.setVisibility(View.VISIBLE);
            tiPassword.setVisibility(View.VISIBLE);

        } else {

            listner.onPasswordReset(response.getMessage());
            dismiss();
        }
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
