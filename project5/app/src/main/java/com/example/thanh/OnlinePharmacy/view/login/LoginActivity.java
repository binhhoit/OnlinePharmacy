package com.example.thanh.OnlinePharmacy.view.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.model.User;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.login.fragments.ResetPasswordDialog;
import com.example.thanh.OnlinePharmacy.view.login.fragments.ResetPasswordDialog_;
import com.example.thanh.OnlinePharmacy.view.menu.MenuActivity_;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener, GoogleApiClient.OnConnectionFailedListener {


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

    @ViewById(R.id.activity_login_cb_save_user)
    protected CheckBox cbSave;

    @ViewById(R.id.activity_login_ll_avi_loading)
    protected LinearLayout llLoading;

    private CompositeSubscription subscriptions;
    private SharedPreferences sharedPreferences;
    private Validator validator;

    protected String fbId = null;
    protected String fbBirthday = null;
    protected String fbLocation = null;
    protected String fbEmail = null;
    protected String fbName = null;
    private boolean loginFacebook = false;

    private String personNameGoogle;
    private String personGivenNameGoogle;
    private String personFamilyNameGoogle;
    private String personEmailGoogle;
    private String personIdGoogle;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private boolean loginGoogle = false;

    public static final String TAG = LoginActivity.class.getSimpleName();
    private int TEMP = 0;

    private ResetPasswordDialog resetPasswordDialog;
    private CallbackManager callbackManager;

    @AfterViews
    protected void init() {

        infoStart();

        initSharedPreferences();

        loginFacebook();

        setLoginGoogle();

    }

    private void initSharedPreferences() {

        sharedPreferences = this.getSharedPreferences("account", MODE_PRIVATE);
    }

    private void infoStart() {

        validator = new Validator(this);
        validator.setValidationListener(this);

        subscriptions = new CompositeSubscription();

        Hawk.init(this).build();

        etEmail.setText(Hawk.get("user"));
        etPassword.setText(Hawk.get("pass"));
        cbSave.setChecked(Boolean.parseBoolean(Hawk.get("check")));
    }

    private void saveUser() {
        if (cbSave.isChecked() == true) {
            Hawk.put("user", etEmail.getText().toString());
            Hawk.put("pass", etPassword.getText().toString());
            Hawk.put("check", "true");
        } else {
            Hawk.put("user", "");
            Hawk.put("pass", "");
            Hawk.put("check", "faise");
        }
    }

    @Click(R.id.btn_login)
    void login() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        saveUser();
        loginProcess(email, password);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        showSnackBarMessage("Enter Valid Details !");

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loginProcess(String email, String password) {
        startAnim();
        // khởi tạo gọi class NetworkUtil
        subscriptions.add(NetworkUtil
                .getRetrofit(email, password)
                .login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        stopAnim();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TOKEN, response.getToken());
        editor.putString(Constants.EMAIL, response.getMessage());
        editor.putString(Constants.ID, response.getId());
        editor.apply();

        Toast.makeText(this, "đăng nhập hoàn thành", Toast.LENGTH_SHORT).show();

        //sign out facebook
        LoginManager.getInstance().logOut();

        MenuActivity_.intent(this).start();

        etEmail.setText(null);
        etPassword.setText(null);
        this.finish();
    }

    private void handleError(Throwable error) {

        Toast.makeText(this, "ERROR" + error.toString(), Toast.LENGTH_SHORT).show();
        Log.e("Error", "" + error.getMessage());
        stopAnim();

        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                showSnackBarMessage(response.getMessage());
                Log.e("ERROR", "" + response.getMessage());

                if (loginFacebook) {
                    registerFacebook(response.getMessage());
                }
                if (loginGoogle) {
                    registerGoogle(response.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showSnackBarMessage("Network Error !");
        }
    }

    @Override
    public void onBackPressed() {
        TEMP++;
        if (TEMP == 1) {
            String string = getString(R.string.back);
            Toast.makeText(LoginActivity.this, string, Toast.LENGTH_SHORT).show();
        }
        if (TEMP == 2) {
            //thoát khỏi chương trình
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startActivity(startMain);
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String data = intent.getData().getLastPathSegment();
        Log.d(TAG, "onNewIntent: " + data);

        resetPasswordDialog = (ResetPasswordDialog) getFragmentManager().findFragmentByTag(ResetPasswordDialog.TAG);

        if (resetPasswordDialog != null) {
            resetPasswordDialog.setToken(data);
        }
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_login), message, Snackbar.LENGTH_SHORT).show();

    }

    @Click(R.id.activity_login_iv_facebook)
    protected void onClickFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_about_me", "user_birthday", "user_location", "email"));
        Log.e("permission", "");
    }

    private void loginFacebook() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginFacebook = true;
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                AccessToken.getCurrentAccessToken().getPermissions();
                getProfileInformationFacebook(accessToken);

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getProfileInformationFacebook(AccessToken accToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Log.e("object", object.toString());
                        try {
                            fbId = object.getString("id");
                            fbEmail = object.getString("email");
                            fbName = object.getString("name");
                            fbBirthday = object.getString("birthday");
                            JSONObject jsonObject = object.getJSONObject("location");
                            fbLocation = jsonObject.getString("name");

                            loginProcess(fbEmail, "password");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,location,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void registerFacebook(String messenger) {
        if (loginFacebook == true && messenger.equals("User Not Found !")) {
            User user = new User();
            user.setEmail(fbEmail);
            user.setName(fbName);
            user.setPassword("password");
            user.setBrithDay(fbBirthday);
            user.setLocation(fbLocation);
            registerProcess(user);
        } else {
            showSnackBarMessage(getResources().getString(R.string.info_user));
            LoginManager.getInstance().logOut();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void setLoginGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Click(R.id.activity_login_iv_google_plus)
    protected void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        loginGoogle = true;
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            personNameGoogle = acct.getDisplayName();
            personGivenNameGoogle = acct.getGivenName();
            personFamilyNameGoogle = acct.getFamilyName();
            personEmailGoogle = acct.getEmail();
            personIdGoogle = acct.getId();

            Log.e("login google.com", "dang nhap thanh cong" + personNameGoogle + "\n" + personGivenNameGoogle + "\n" + personFamilyNameGoogle + "\n" + personEmailGoogle + "\n" + personIdGoogle);

            //Uri personPhoto = acct.getPhotoUrl();
            loginProcess(personEmailGoogle, "passwordGoogle");
            signOut();
            // updateUI(true);
        } else {
            Log.e("login google.com", "dang nhap that bai");
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.e("signOut", "sign out google");
                    }
                });
    }

    private void registerGoogle(String messenger) {
        if (loginGoogle == true && messenger.equals("User Not Found !")) {
            User user = new User();
            user.setEmail(personEmailGoogle);
            user.setName(personNameGoogle);
            user.setId(personIdGoogle);
            user.setPassword("passwordGoogle");
            //user.setBrithDay(fbBirthday);
            //user.setLocation(fbLocation);
            registerProcess(user);
        } else {
            signOut();
        }
    }


    private void registerProcess(User user) {

        subscriptions.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseRegister, this::handleErrorRegister));
    }

    private void handleResponseRegister(Response response) {

        showSnackBarMessage(response.getMessage());
        if (loginFacebook) {
            loginProcess(fbEmail, "password");
        }
        if (loginGoogle) {
            loginProcess(personEmailGoogle, "passwordGoogle");
        }

    }

    private void handleErrorRegister(Throwable error) {
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

    @Click(R.id.tv_register)
    void goToRegister() {
        RegisterActivity_.intent(this).start();
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
        llLoading.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    private void stopAnim() {
        llLoading.setVisibility(View.GONE);
        avi.hide();
        // or avi.smoothToHide();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
