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
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.menu.MenuActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_receiver_prescription)
public class ReceiverPrescriptionActivity extends AppCompatActivity {
    @ViewById(R.id.activity_receiver_toolbar)
    Toolbar toolbar;
    @ViewById(R.id.activity_receiver_rcv)
    RecyclerView recyclerViewReceiver;

    private List<Prescription> prescription = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String id;
    private String token;

    @AfterViews
    protected void init() {
        setToolBar();

        initSharedPreferences();

        getPrescription();

    }

    private void setToolBar() {
        toolbar.setTitle("Lịch sử giao dịch");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initSharedPreferences() {

        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        id = sharedPreferences.getString(Constants.ID, "");
        token = sharedPreferences.getString(Constants.TOKEN, "");
    }

    private void getPrescription() {

        Observable.defer(() -> NetworkUtil.getRetrofit(token).getPrescription(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(prescriptions -> {
                            Log.e("size", "" + prescriptions.size());
                            prescription = prescriptions;

                            if (prescription.size() == 0) {
                                dialogNoPrescription();
                            } else {
                                //swap array List
                                Collections.reverse(prescription);
                                //show data
                                showData();
                            }
                        },
                        throwable -> {
                            // TODO: handle error
                            Log.e("ERROR", throwable.getMessage());
        });
    }

    private void dialogNoPrescription() {
        AlertDialog alertDialog = new AlertDialog.Builder(ReceiverPrescriptionActivity.this).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Thông Báo");
        alertDialog.setMessage("Bạn chưa có đơn thuốc nào !");
        alertDialog.setIcon(R.drawable.ic_warning);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                (dialog, which) -> {
                    MenuActivity_.intent(ReceiverPrescriptionActivity.this).start();
                    finish();

                });
        alertDialog.show();
    }

    private void showData() {

        recyclerViewReceiver.setHasFixedSize(true);
        recyclerViewReceiver.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewReceiver.setAdapter(new RecyclerReceiverAdapter(prescription, this));
    }
}
