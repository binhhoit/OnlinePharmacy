package com.example.thanh.OnlinePharmacy.view.prescription;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.Prescription;
import com.example.thanh.OnlinePharmacy.model.RecyclerReceiverAdapter;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.menu.Menu_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

@EActivity(R.layout.activity_receiver_prescription)
public class ReceiverPrescriptionActivity extends AppCompatActivity {

    @ViewById(R.id.activity_receiver_rcv)
    RecyclerView recyclerViewReceiver;

    private List<Prescription> prescription = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String id;
    @AfterViews
    protected void init() {
        initSharedPreferences();

        getPrescription();

    }

    private void initSharedPreferences() {

        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        id = sharedPreferences.getString(Constants.ID, "");
    }

    private void getPrescription() {

        Call<List<Prescription>> call = NetworkUtil.getRetrofit().getPrescription(id);
        call.enqueue(new Callback<List<Prescription>>() {
            @Override
            public void onResponse(Call<List<Prescription>> call, retrofit2.Response<List<Prescription>> response) {
                // The network call was a success and we got a response
                Log.e("Note", "Truy cập lấy đơn thuốc về");
                Log.e("Note", "Truy cập lấy đơn thuốc về" + response.body());
                if (response.isSuccessful()) {

                    prescription = response.body();

                    if (prescription.size() == 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ReceiverPrescriptionActivity.this).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setTitle("Thông Báo");
                        alertDialog.setMessage("Bạn chưa có đơn thuốc nào !");
                        alertDialog.setIcon(R.drawable.ic_warning);
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent menu = new Intent(ReceiverPrescriptionActivity.this, Menu_.class);
                                        startActivity(menu);
                                        finish();

                                    }
                                });
                        alertDialog.show();

                    } else {
                        //đảo mảng
                        Collections.reverse(prescription);
                         //chuyển dữ liệu qua kia
                        showData();
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

    private void showData(){

        recyclerViewReceiver.setHasFixedSize(true);
        recyclerViewReceiver.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewReceiver.setAdapter(new RecyclerReceiverAdapter(prescription, getApplicationContext()));
    }
}
