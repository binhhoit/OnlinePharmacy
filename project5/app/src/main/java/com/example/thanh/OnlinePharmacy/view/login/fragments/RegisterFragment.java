package com.example.thanh.OnlinePharmacy.view.login.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;

import android.support.design.widget.Snackbar;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.model.User;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.orhanobut.hawk.Hawk;

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

@EFragment(R.layout.activity_register_fragment)
public class RegisterFragment extends Fragment implements Validator.ValidationListener {

    public static final String TAG = RegisterFragment.class.getSimpleName();

    @NotEmpty(message = "Trường này chưa điền")
    @ViewById(R.id.et_name)
    protected EditText etName;

    @NotEmpty(message = "Trường này chưa điền")
    @Email(message = "Email chưa đúng")
    @ViewById(R.id.et_email)
    protected EditText etEmail;

    @NotEmpty(message = "Trường này chưa điền")
    @Password(min = 6, message = "Mật khẩu yêu cầy 6 ký tự")
    @ViewById(R.id.et_password)
    protected EditText etPassword;

    @NotEmpty(message = "Trường này chưa điền")
    @ConfirmPassword(message = "Mật khẩu chưa khớp")
    @ViewById(R.id.et_check_password)
    protected EditText etCheckPassword;

    @ViewById(R.id.progress)
    protected ProgressBar progressbar;

    private CompositeSubscription subscriptions;
    private Validator validator;

    @AfterViews
    void init() {
        validator = new Validator(this);
        validator.setValidationListener(this);
        Hawk.init(getActivity()).build();
        subscriptions = new CompositeSubscription();
    }

    @Click(R.id.tv_register)
    void register() {

        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        //save pass and user
        Hawk.put("user", email);
        Hawk.put("pass", password);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        progressbar.setVisibility(View.VISIBLE);
        registerProcess(user);

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

    private void registerProcess(User user) {

        subscriptions.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        progressbar.setVisibility(View.GONE);
        showSnackBarMessage(response.getMessage());

        goToLogin();
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
