package com.example.thanh.OnlinePharmacy.view.login.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.model.User;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

@EFragment(R.layout.activity_register_fragment)
public class RegisterFragment extends Fragment {

    public static final String TAG = RegisterFragment.class.getSimpleName();

    @ViewById(R.id.et_name)
    protected EditText etName;

    @ViewById(R.id.et_email)
    protected EditText etEmail;

    @ViewById(R.id.et_password)
    protected EditText etPassword;

    @ViewById(R.id.ti_name)
    protected TextInputLayout tiName;

    @ViewById(R.id.ti_email)
    protected TextInputLayout tiEmail;

    @ViewById(R.id.ti_password)
    protected TextInputLayout tiPassword;

    @ViewById(R.id.progress)
    protected ProgressBar progressbar;

    private CompositeSubscription subscriptions;

    @AfterViews
    void init() {

        subscriptions = new CompositeSubscription();
    }

    @Click(R.id.btn_register)
    void register() {

        setError();

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        int err = 0;

        if (!validateFields(name)) {

            err++;
            tiName.setError("Name should not be empty !");
        }

        if (!validateEmail(email)) {

            err++;
            tiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            tiPassword.setError("Password should not be empty !");
        }

        if (err == 0) {

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            progressbar.setVisibility(View.VISIBLE);
            registerProcess(user);


        } else {

            showSnackBarMessage("Enter Valid Details !");
        }


    }

    private void setError() {

        tiName.setError(null);
        tiEmail.setError(null);
        tiPassword.setError(null);
    }

    private void registerProcess(User user) {

        subscriptions.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        progressbar.setVisibility(View.GONE);
        showSnackBarMessage(response.getMessage());


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

        if (getView() != null) {

            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }

    }

    @Click(R.id.tv_login)
    void goToLogin() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        LoginFragment fragment = new LoginFragment_();
        ft.replace(R.id.fragmentFrame, fragment, LoginFragment.TAG);
        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}
