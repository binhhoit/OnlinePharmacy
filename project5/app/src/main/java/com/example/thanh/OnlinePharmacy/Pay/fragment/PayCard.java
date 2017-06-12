package com.example.thanh.OnlinePharmacy.Pay.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.Login.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.Menu.Menu;
import com.example.thanh.OnlinePharmacy.Pay.model.Pay_Card;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.ResponseStatus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayCard extends AppCompatActivity {

    private TextView tv_submit;
    private EditText et_Pin;
    private EditText et_Serial;
    private Spinner spn_Type;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);
        FindViewById();
        submitPayCard();
    }

    private void FindViewById() {
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        et_Pin = (EditText) findViewById(R.id.et_Pin);
        et_Serial = (EditText) findViewById(R.id.et_serial);
        spn_Type = (Spinner) findViewById(R.id.spn_type);
    }

    private void postPayCard(Pay_Card card) {
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
                    Toast.makeText(PayCard.this,
                            "Đã nạp thành công thẻ cào mệnh giá: " + response.body().getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();
                    Intent menu = new Intent(PayCard.this, Menu.class);
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
        Pay_Card card = new Pay_Card();
        String[] types = getResources().getStringArray(R.array.TypeCard);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        spn_Type.setAdapter(arrayAdapter);
        spn_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add pin add serial
                card.setPin(et_Pin.getText().toString());
                card.setSerial(et_Serial.getText().toString());
                if (et_Pin.getText().toString().equals("") == true || et_Serial.getText().toString().equals("") == true || card.getType().equals("chon") == true) {
                    Toast.makeText(getApplication(), "Các trường chưa phù hợp", Toast.LENGTH_SHORT).show();
                } else {
                    //submit info card transfer server
                    postPayCard(card);
                }
            }
        });
    }
}
