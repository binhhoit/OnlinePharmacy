package com.example.thanh.OnlinePharmacy.view.pay;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.view.menu.Menu;
import com.example.thanh.OnlinePharmacy.model.PayCard;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.ResponseStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_pay_card)
public class PayCardActivity extends AppCompatActivity {
    @ViewById(R.id.tv_submit)
    TextView tvSubmit;
    @ViewById(R.id.et_Pin)
    EditText etPin;
    @ViewById(R.id.et_serial)
    EditText etSerial;
    @ViewById(R.id.spn_type)
    Spinner spnType;
    private ArrayAdapter<String> arrayAdapter;

    @AfterViews
    void init() {
        submitPayCard();
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
                    Intent menu = new Intent(PayCardActivity.this, Menu.class);
                    startActivity(menu);
                    finish();
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
        String[] types = getResources().getStringArray(R.array.TypeCard);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        spnType.setAdapter(arrayAdapter);
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.BLACK);

                if (position == 0) {
                    Toast.makeText(getApplication(), "Vui Lòng Chọn Loại Thẻ Cào", Toast.LENGTH_SHORT).show();
                } else {
                    card.setType(types[position]);
                    Toast.makeText(getApplication(), card.getType(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add pin add serial
                card.setPin(etPin.getText().toString());
                card.setSerial(etSerial.getText().toString());
                if (etPin.getText().toString().equals("") == true || etSerial.getText().toString().equals("") == true || card.getType().equals("chon") == true) {
                    Toast.makeText(getApplication(), "Các trường chưa phù hợp", Toast.LENGTH_SHORT).show();
                } else {
                    //submit info card transfer server
                    postPayCard(card);
                }
            }
        });
    }
}
