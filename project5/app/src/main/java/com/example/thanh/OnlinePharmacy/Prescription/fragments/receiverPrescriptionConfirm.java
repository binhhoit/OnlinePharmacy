package com.example.thanh.OnlinePharmacy.Prescription.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.Login.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.Login.utils.Constants;
import com.example.thanh.OnlinePharmacy.Menu.Menu;
import com.example.thanh.OnlinePharmacy.Pay.fragment.Pay;
import com.example.thanh.OnlinePharmacy.Prescription.model.ArrayAdapter;
import com.example.thanh.OnlinePharmacy.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class receiverPrescriptionConfirm extends AppCompatActivity {

    private TextView tv_name, tv_email, tv_address,tv_price;
    private Spinner spn_numberBuy;
    private ListView lv_receiver;
    private List<com.example.thanh.OnlinePharmacy.Prescription.model.prescription> prescription = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private android.widget.ArrayAdapter spn_arrayAdapter;
    private Button btn_selectPay;
    private SharedPreferences mSharedPreferences;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_prescription_confirm);
        FindViewByID();
        initSharedPreferences();
        GetPrescription();
        selectPay();

    }

    private void initSharedPreferences() {

        mSharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        mId = mSharedPreferences.getString(Constants.ID, "");

    }

    private void FindViewByID() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_price = (TextView) findViewById(R.id.tv_price);
        spn_numberBuy = (Spinner) findViewById(R.id.spn_numberbuy);
        lv_receiver = (ListView) findViewById(R.id.lv_prescription);
        btn_selectPay = (Button) findViewById(R.id.btn_selectPay);
    }

    private void GetPrescription() {
        final List<String> number_buy = new ArrayList<>();
        Call<List<com.example.thanh.OnlinePharmacy.Prescription.model.prescription>> call = NetworkUtil.getRetrofit().getPrescriptionConfirm(mId);
        call.enqueue(new Callback<List<com.example.thanh.OnlinePharmacy.Prescription.model.prescription>>() {
            @Override
            public void onResponse(Call<List<com.example.thanh.OnlinePharmacy.Prescription.model.prescription>> call, retrofit2.Response<List<com.example.thanh.OnlinePharmacy.Prescription.model.prescription>> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it
                Log.e("Note", "Truy cập lấy đơn thuốc về");
                Log.e("Note", "Truy cập lấy đơn thuốc về" + response.body());
                if (response.isSuccessful()) {
                    prescription = response.body();
                    if (prescription.size() == 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(receiverPrescriptionConfirm.this).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setTitle("Thông Báo");
                        alertDialog.setMessage("Bạn chưa có đơn thuốc nào !");
                        alertDialog.setIcon(R.drawable.ic_warning);
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent menu = new Intent(receiverPrescriptionConfirm.this, Menu.class);
                                        startActivity(menu);
                                        finish();

                                    }
                                });
                        alertDialog.show();

                    } else {
                        for (int size = 0; size < prescription.size(); size++) {
                            Log.e("Number :", "" + prescription.get(size).getNumber_buy());
                            number_buy.add(size, prescription.get(size).getNumber_buy());
                        }
                        number_buy.add(prescription.size(), "---Chọn lần mua---");
                        //đảo mảng
                        Collections.reverse(number_buy);
                        Collections.reverse(prescription);
                        //lưa chọn lần mua để xem
                        spn_arrayAdapter = new android.widget.ArrayAdapter(receiverPrescriptionConfirm.this, android.R.layout.simple_spinner_item, number_buy);
                        spn_arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                        spn_numberBuy.setAdapter(spn_arrayAdapter);
                        spn_numberBuy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    Toast.makeText(receiverPrescriptionConfirm.this, "Vui lòng chọn lần mua phía trên", Toast.LENGTH_SHORT).show();
                                } else {
                                    position = position - 1;
                                    tv_name.setText(prescription.get(position).get_id());
                                    tv_email.setText(prescription.get(position).getEmail());
                                    tv_address.setText(prescription.get(position).getAddressReceive());
                                    tv_price.setText(prescription.get(position).getPrice());
                                    arrayAdapter = new ArrayAdapter(receiverPrescriptionConfirm.this, R.layout.custom_listview, prescription.get(position).getMiniPrescription());
                                    lv_receiver.setAdapter(arrayAdapter);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }


                }

            }

            @Override
            public void onFailure(Call<List<com.example.thanh.OnlinePharmacy.Prescription.model.prescription>> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                Log.e("ERROR", t.getMessage());
            }
        });


    }

    private void selectPay() {
        btn_selectPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication(), Pay.class);
                startActivity(intent);
            }
        });

    }
}
