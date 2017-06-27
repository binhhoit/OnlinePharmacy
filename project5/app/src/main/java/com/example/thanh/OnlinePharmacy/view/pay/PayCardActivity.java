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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.PayCard;
import com.example.thanh.OnlinePharmacy.model.ResponseStatus;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.menu.Menu_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @AfterViews
    protected void init() {
        setToolbar();

        initSharedPreferences();

        submitPayCard();
    }

    private void initSharedPreferences() {
        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        mail = sharedPreferences.getString(Constants.EMAIL, "");
        tv_email.setText(mail);
    }

    protected void setToolbar() {
        toolbar.setTitle("Thanh toán bằng thẻ cào");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
    }
    private void postPayCard(PayCard card) {
        Call<ResponseStatus> call = NetworkUtil.getRetrofit().postPayCard(card);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it
                Log.e("Note", "Truy cập lấy thông tin mệnh giá về");
                Log.e("Note", "Truy cập lấy thông tin mệnh giá về" + response.body());
                if (response.isSuccessful()) {
                    Log.e("Mã Trạng thái:  ", "" + response.body().getStatus());
                    Log.e("Trạng thái:  ", "" + response.body().getMessage());
                    Toast.makeText(PayCardActivity.this,
                            "Đã nạp thành công thẻ cào mệnh giá: " + response.body().getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();
                    Menu_.intent(PayCardActivity.this).start();
                }
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                Log.e("ERROR", t.getMessage());
            }
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
            //add pin add serial
            card.setPin(etPin.getText().toString());
            card.setSerial(etSerial.getText().toString());
            if (etPin.getText().toString().equals("") == true ||
                    etSerial.getText().toString().equals("") == true ||
                    card.getType().toString().equals("chọn") == true) {
               // Toast.makeText(getApplication(), "Các trường chưa phù hợp", Toast.LENGTH_SHORT).show();
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
