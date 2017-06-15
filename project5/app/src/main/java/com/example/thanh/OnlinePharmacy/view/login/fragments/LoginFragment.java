package com.example.thanh.OnlinePharmacy.view.login.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.view.menu.Menu_;
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

import static android.content.Context.MODE_PRIVATE;
import static com.example.thanh.OnlinePharmacy.utils.Validation.validateEmail;
import static com.example.thanh.OnlinePharmacy.utils.Validation.validateFields;

@EFragment(R.layout.activity_login_fragment)
public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();

    @ViewById(R.id.et_email)
    EditText etEmail;
    @ViewById(R.id.et_password)
    EditText etPassword;
    @ViewById(R.id.ti_email)
    TextInputLayout tiEmail;
    @ViewById(R.id.ti_password)
    TextInputLayout tiPassword;
    @ViewById(R.id.activity_login_avi_loading)
    AVLoadingIndicatorView avi;


    private CompositeSubscription subscriptions;
    private SharedPreferences SharedPreferences;


    @AfterViews
    void init() {
        subscriptions = new CompositeSubscription();
        initSharedPreferences();
    }


    private void initSharedPreferences() {

        SharedPreferences = getActivity().getSharedPreferences("account", MODE_PRIVATE);
    }

    @Click(R.id.btn_login)
    void login() {

        setError();

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        startAnim();

        int err = 0;
        // error no fill
        if (!validateEmail(email)) {

            err++;
            tiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            tiPassword.setError("Password should not be empty !");
        }

        if (err == 0) {
            //no error --> login
            loginProcess(email, password);
            startAnim();

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }
    }

    // error
    private void setError() {

        tiEmail.setError(null);
        tiPassword.setError(null);
    }

    // process login
    private void loginProcess(String email, String password) {
        // khởi tạo gọi class NetworkUtil
        subscriptions.add(NetworkUtil.getRetrofit(email, password).login() //gọi ngược lại các lớp class đã gọi để xuất ra các đường đẫn
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)); // hiển thị các phản hồi trên server đưa về
    }

    private void handleResponse(Response response) {

        stopAnim();

        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.putString(Constants.TOKEN, response.getToken());
        editor.putString(Constants.EMAIL, response.getMessage());
        editor.putString(Constants.ID, response.getId());
        editor.apply();

        Toast.makeText(getActivity(), "đăng nhập hoàn thành", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), Menu_.class);
        startActivity(intent);
        //gán rỗng
        etEmail.setText(null);
        etPassword.setText(null);
        getActivity().finish();
    }

    private void handleError(Throwable error) {

        Toast.makeText(getActivity(), "ERROR" + error.toString(), Toast.LENGTH_SHORT).show();
        Log.e("Error", "" + error.getMessage());
        stopAnim();

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                showSnackBarMessage(response.getMessage());
                Log.e("ERROR", "" + response.getMessage());

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

    @Click(R.id.tv_register)
    void goToRegister() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RegisterFragment fragment = new RegisterFragment_();
        ft.replace(R.id.fragmentFrame, fragment, RegisterFragment.TAG);
        ft.commit();
    }

    @Click(R.id.tv_forgot_password)
    void showDialog() {

        ResetPasswordDialog fragment = new ResetPasswordDialog_();
        fragment.show(getFragmentManager(), ResetPasswordDialog.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    void startAnim() {
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }
}
