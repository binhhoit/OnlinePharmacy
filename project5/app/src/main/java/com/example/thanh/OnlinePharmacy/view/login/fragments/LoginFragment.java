package com.example.thanh.OnlinePharmacy.view.login.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.model.User;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.view.menu.Menu_;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.orhanobut.hawk.Hawk;
import com.wang.avi.AVLoadingIndicatorView;

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

import static android.content.Context.MODE_PRIVATE;
import static com.example.thanh.OnlinePharmacy.utils.Validation.validateEmail;
import static com.example.thanh.OnlinePharmacy.utils.Validation.validateFields;

@EFragment(R.layout.activity_login_fragment)
public class LoginFragment extends Fragment implements Validator.ValidationListener {

    public static final String TAG = LoginFragment.class.getSimpleName();

    @NotEmpty(message = "Trường này chưa điền")
    @Email(message = "Email chưa đúng")
    @ViewById(R.id.et_email)
    protected EditText etEmail;

    @NotEmpty(message = "Trường này chưa điền")
    @Password(min = 6, message = "Mật khẩu chưa chuẩn")
    @ViewById(R.id.et_password)
    protected EditText etPassword;

    @ViewById(R.id.activity_login_avi_loading)
    protected AVLoadingIndicatorView avi;

    private CompositeSubscription subscriptions;
    private SharedPreferences sharedPreferences;
    private Validator validator;

    @AfterViews
    void init() {
        validator = new Validator(this);
        validator.setValidationListener(this);

        subscriptions = new CompositeSubscription();

        Hawk.init(getActivity()).build();
        initSharedPreferences();

        etEmail.setText(Hawk.get("user"));
        etPassword.setText(Hawk.get("pass"));
    }

    private void initSharedPreferences() {

        sharedPreferences = getActivity().getSharedPreferences("account", MODE_PRIVATE);
    }

    @Click(R.id.btn_login)
    void login() {

        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        loginProcess(email, password);
        startAnim();

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        showSnackBarMessage("Enter Valid Details !");

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    // process login
    private void loginProcess(String email, String password) {
        // khởi tạo gọi class NetworkUtil
        subscriptions.add(NetworkUtil
                .getRetrofit(email, password)
                .login() //gọi ngược lại các lớp class đã gọi để xuất ra các đường đẫn
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)); // hiển thị các phản hồi trên server đưa về
    }

    private void handleResponse(Response response) {

        stopAnim();

        SharedPreferences.Editor editor = sharedPreferences.edit();
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

    private void startAnim() {
        avi.show();
        // or avi.smoothToShow();
    }

    private void stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }

}
