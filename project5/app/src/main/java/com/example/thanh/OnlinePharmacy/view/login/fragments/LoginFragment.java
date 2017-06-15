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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.menu.Menu;
import com.example.thanh.OnlinePharmacy.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.MODE_PRIVATE;
import static com.example.thanh.OnlinePharmacy.utils.Validation.validateEmail;
import static com.example.thanh.OnlinePharmacy.utils.Validation.validateFields;

public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();

    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtLogin;
    private TextView mTvRegister;
    private TextView mTvForgotPassword;
    private TextInputLayout mTiEmail;
    private TextInputLayout mTiPassword;
    private ProgressBar mProgressBar;

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;

// khởi tạo 1 view để gọi các id từ layout

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_fragment, container, false);
        mSubscriptions = new CompositeSubscription();
        initViews(view);
        initSharedPreferences();
        return view;
    }

    private void initViews(View v) {

        mEtEmail = (EditText) v.findViewById(R.id.et_email);
        //      mEtEmail.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_user1), null, null, null);
        mEtPassword = (EditText) v.findViewById(R.id.et_password);
        //      mEtPassword.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_pass), null, null, null);
        mBtLogin = (Button) v.findViewById(R.id.btn_login);
        mTiEmail = (TextInputLayout) v.findViewById(R.id.ti_email);
        mTiPassword = (TextInputLayout) v.findViewById(R.id.ti_password);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
        mTvRegister = (TextView) v.findViewById(R.id.tv_register);
        mTvForgotPassword = (TextView) v.findViewById(R.id.tv_forgot_password);


        mBtLogin.setOnClickListener(view -> login());
        mTvRegister.setOnClickListener(view -> goToRegister());
        mTvForgotPassword.setOnClickListener(view -> showDialog());
    }

    private void initSharedPreferences() {

        mSharedPreferences = getActivity().getSharedPreferences("account", MODE_PRIVATE);
    }

    private void login() {

        setError();

        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        mProgressBar.setVisibility(View.VISIBLE);

        int err = 0;
// xữ lý các lỗi tại client như mail rổng ko đúng ....
        if (!validateEmail(email)) {

            err++;
            mTiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            mTiPassword.setError("Password should not be empty !");
        }

        if (err == 0) {
//không có lỗi vào xữ lý đăng nhập
            loginProcess(email, password);
            mProgressBar.setVisibility(View.VISIBLE);

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }
    }

    // lỗi
    private void setError() {

        mTiEmail.setError(null);
        mTiPassword.setError(null);
    }

    // hàm xữ lý đăng nhập
    private void loginProcess(String email, String password) {
        // khởi tạo gọi class NetworkUtil
        mSubscriptions.add(NetworkUtil.getRetrofit(email, password).login() //gọi ngược lại các lớp class đã gọi để xuất ra các đường đẫn
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)); // hiển thị các phản hồi trên server đưa về
    }

    private void handleResponse(Response response) {

        mProgressBar.setVisibility(View.GONE);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN, response.getToken());
        editor.putString(Constants.EMAIL, response.getMessage());
        editor.putString(Constants.ID, response.getId());
        editor.apply();

        Toast.makeText(getActivity(), "đăng nhập hoàn thành", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), Menu.class);
        startActivity(intent);
        //gán rỗng
        mEtEmail.setText(null);
        mEtPassword.setText(null);
        getActivity().finish();
    }

    private void handleError(Throwable error) {

        Toast.makeText(getActivity(), "ERROR" + error.toString(), Toast.LENGTH_SHORT).show();

        mProgressBar.setVisibility(View.GONE);

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

    private void goToRegister() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RegisterFragment fragment = new RegisterFragment();
        ft.replace(R.id.fragmentFrame, fragment, RegisterFragment.TAG);
        ft.commit();
    }

    private void showDialog() {

        ResetPasswordDialog fragment = new ResetPasswordDialog();
        fragment.show(getFragmentManager(), ResetPasswordDialog.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
