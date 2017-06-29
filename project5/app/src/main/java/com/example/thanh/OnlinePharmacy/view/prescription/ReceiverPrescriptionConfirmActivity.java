package com.example.thanh.OnlinePharmacy.view.prescription;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.Prescription;
import com.example.thanh.OnlinePharmacy.model.RecyclerReceiverAdapter;
import com.example.thanh.OnlinePharmacy.model.RecyclerReceiverConfirmAdapter;
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

@EActivity(R.layout.activity_receiver_prescription_confirm)
public class ReceiverPrescriptionConfirmActivity extends AppCompatActivity {

    @ViewById(R.id.activity_receiver_confirm_toolbar)
    Toolbar toolbar;
    @ViewById(R.id.activity_receiver_confirm_rcv)
    RecyclerView recyclerViewReceiverConfirm;

    private List<Prescription> prescription = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String id;
    @AfterViews
    protected void init() {

        setToolBar();

        initSharedPreferences();

        getPrescription();

    }

    private void setToolBar() {
        toolbar.setTitle("Đơn thuốc đã duyệt");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initSharedPreferences() {
        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        id = sharedPreferences.getString(Constants.ID, "");
    }

    private void getPrescription() {

        Call<List<Prescription>> call = NetworkUtil.getRetrofit().getPrescriptionConfirm(id);
        call.enqueue(new Callback<List<Prescription>>() {
            @Override
            public void onResponse(Call<List<Prescription>> call, retrofit2.Response<List<Prescription>> response) {
                // TODO: use the repository list and display it
                Log.e("Note", "Truy cập lấy đơn thuốc về");
                Log.e("Note", "" + response.body());
                if (response.isSuccessful()) {
                    prescription = response.body();
                    if (prescription.size() == 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                ReceiverPrescriptionConfirmActivity.this)
                                .create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setTitle("Thông Báo");
                        alertDialog.setMessage("Bạn chưa có đơn thuốc nào !");
                        alertDialog.setIcon(R.drawable.ic_warning);
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                (dialog, which) -> {

                                    Menu_.intent(ReceiverPrescriptionConfirmActivity.this).start();

                                });
                        alertDialog.show();

                    } else {

                        //swap array
                        Collections.reverse(prescription);
                        showData();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Prescription>> call, Throwable t) {
                // TODO: handle error
                Log.e("ERROR", t.getMessage());
            }
        });

    }

    private void showData(){

        recyclerViewReceiverConfirm.setHasFixedSize(true);
        recyclerViewReceiverConfirm.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewReceiverConfirm.setAdapter(new RecyclerReceiverConfirmAdapter(prescription, this));
    }
}
