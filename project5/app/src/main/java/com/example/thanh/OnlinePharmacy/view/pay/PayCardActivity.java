package com.example.thanh.OnlinePharmacy.view.pay;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.PayCard;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.menu.MenuActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_pay_card)
public class PayCardActivity extends AppCompatActivity {

    @ViewById(R.id.activity_pay_card_ll_root)
    protected FrameLayout llRoot;
    @ViewById(R.id.activity_pay_card_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_pay_cart_tv_user)
    protected TextView tv_email;
    @ViewById(R.id.activity_pay_card_tv_submit)
    protected TextView tvSubmit;
    @ViewById(R.id.activity_pay_card_et_Pin)
    protected EditText etPin;
    @ViewById(R.id.activity_pay_card_et_serial)
    protected EditText etSerial;
    @ViewById(R.id.activity_pay_card_spn_type)
    protected Spinner spnType;
    private ArrayAdapter<String> arrayAdapter;
    private SharedPreferences sharedPreferences;
    private String mail;
    private String token;
    private String id;

    @AfterViews
    protected void init() {
        setToolbar();

        initSharedPreferences();

        submitPayCard();
    }

    private void initSharedPreferences() {
        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        mail = sharedPreferences.getString(Constants.EMAIL, "");
        token = sharedPreferences.getString(Constants.TOKEN, "");
        id = sharedPreferences.getString(Constants.ID, "");
        tv_email.setText(mail);
    }

    protected void setToolbar() {
        toolbar.setTitle("Thanh toán bằng thẻ cào");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    private void postPayCard(PayCard card) {

        Observable.defer(() -> NetworkUtil.getRetrofit(token).postPayCard(card, id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseStatus -> {
                    Log.e("Mã Trạng thái:  ", "" + responseStatus.getStatus());
                    Log.e("Trạng thái:  ", "" + responseStatus.getMessage());
                    Toast.makeText(PayCardActivity.this,
                            getResources().getString(R.string.pay_card_info) + responseStatus.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();
                    MenuActivity_.intent(PayCardActivity.this).start();

                }, throwable -> {
                    Toast.makeText(
                            PayCardActivity.this,
                            "Thất Bại " +
                                    throwable.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", "" + throwable.getMessage());
                });
    }

    private void submitPayCard() {
        //selection type card
        PayCard card = new PayCard();
        String[] types = getResources().getStringArray(R.array.type_card);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        spnType.setAdapter(arrayAdapter);
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.BLACK);

                if (position == 0) {
                    Toast.makeText(getApplication(), "Vui Lòng Chọn Loại Thẻ Cào", Toast.LENGTH_SHORT).show();
                    card.setType(types[position]);
                } else {
                    card.setType(types[position]);
                    Toast.makeText(getApplication(), card.getType(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvSubmit.setOnClickListener(v -> {
            //addView pin addView serial
            card.setPin(etPin.getText().toString());
            card.setSerial(etSerial.getText().toString());
            if (etPin.getText().toString().equals("") == true ||
                    etSerial.getText().toString().equals("") == true ||
                    card.getType().toString().equals("chọn") == true) {
                showSnackBarMessage("Các trường chưa phù hợp");
            } else {
                //submit info card transfer server
                postPayCard(card);
            }
        });
    }
    private void showSnackBarMessage(String message) {

            Snackbar.make(llRoot, message, Snackbar.LENGTH_SHORT).show();

    }
}
