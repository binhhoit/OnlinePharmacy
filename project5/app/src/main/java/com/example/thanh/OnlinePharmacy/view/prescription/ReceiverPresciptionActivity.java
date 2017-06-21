package com.example.thanh.OnlinePharmacy.view.prescription;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.menu.Menu;
import com.example.thanh.OnlinePharmacy.view.pay.PayActivity;
import com.example.thanh.OnlinePharmacy.model.ArrayAdapterListview;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.Prescription;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

@EActivity(R.layout.activity_receiver_presciption)
public class ReceiverPresciptionActivity extends AppCompatActivity {

    @ViewById(R.id.activity_receiverPresciption_tv_name)
    protected TextView tvName;
    @ViewById(R.id.activity_receiverPresciption_tv_email)
    protected TextView tvEmail;
    @ViewById(R.id.activity_receiverPresciption_tv_address)
    protected TextView tvAddress;
    @ViewById(R.id.activity_receiverPresciption_spn_numberbuy)
    protected Spinner spnNumberBuy;
    @ViewById(R.id.activity_receiverPresciption_lv_prescription)
    protected ListView lvReceiver;
    @ViewById(R.id.activity_receiverPresciption_btn_selectPay)
    protected Button btnSelectPay;

    private List<Prescription> prescription = new ArrayList<>();
    private ArrayAdapterListview arrayAdapterListview;
    private ArrayAdapter spnArrayAdapter;
    private SharedPreferences sharedPreferences;
    private String id;

    @AfterViews
    void init() {
        initSharedPreferences();
        getPrescription();
        selectPay();

    }

    private void initSharedPreferences() {

        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        id = sharedPreferences.getString(Constants.ID, "");
    }

    private void getPrescription() {
        List<String> numberBuy = new ArrayList<>();
        Call<List<Prescription>> call = NetworkUtil.getRetrofit().getPrescription(id);
        call.enqueue(new Callback<List<Prescription>>() {
            @Override
            public void onResponse(Call<List<Prescription>> call, retrofit2.Response<List<Prescription>> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it
                Log.e("Note", "Truy cập lấy đơn thuốc về");
                Log.e("Note", "Truy cập lấy đơn thuốc về" + response.body());
                if (response.isSuccessful()) {
                    prescription = response.body();
                    if (prescription.size() == 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ReceiverPresciptionActivity.this).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setTitle("Thông Báo");
                        alertDialog.setMessage("Bạn chưa có đơn thuốc nào !");
                        alertDialog.setIcon(R.drawable.ic_warning);
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent menu = new Intent(ReceiverPresciptionActivity.this, Menu.class);
                                        startActivity(menu);
                                        finish();

                                    }
                                });
                        alertDialog.show();

                    } else {
                        for (int size = 0; size < prescription.size(); size++) {
                            Log.e("Number :", "" + prescription.get(size).getNumberBuy());
                            numberBuy.add(size, prescription.get(size).getNumberBuy());
                        }
                        numberBuy.add(prescription.size(), "---Chọn lần mua---");
                        //đảo mảng
                        Collections.reverse(numberBuy);
                        Collections.reverse(prescription);
                        //lưa chọn lần mua để xem
                        spnArrayAdapter = new ArrayAdapter(
                                ReceiverPresciptionActivity.this,
                                android.R.layout.simple_spinner_item,
                                numberBuy);
                        spnArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                        spnNumberBuy.setAdapter(spnArrayAdapter);
                        spnNumberBuy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    String string = getString(R.string.choose_top);
                                    Toast.makeText(ReceiverPresciptionActivity.this, string, Toast.LENGTH_SHORT).show();
                                } else {
                                    position = position - 1;
                                    tvName.setText(prescription.get(position).getIdDatabaseCreate());
                                    tvEmail.setText(prescription.get(position).getEmail());
                                    tvAddress.setText(prescription.get(position).getAddressReceive());
                                    arrayAdapterListview = new ArrayAdapterListview(
                                            ReceiverPresciptionActivity.this,
                                            R.layout.custom_listview,
                                            prescription.get(position).getMiniPrescription());
                                    lvReceiver.setAdapter(arrayAdapterListview);
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
            public void onFailure(Call<List<Prescription>> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void selectPay() {
        btnSelectPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), PayActivity.class);
                startActivity(intent);
            }
        });

    }
}
