package com.example.thanh.OnlinePharmacy.view.prescription;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.ArrayAdapterListview;
import com.example.thanh.OnlinePharmacy.model.Prescription;
import com.example.thanh.OnlinePharmacy.view.pay.PayActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_receiver_presciption_details)
public class ReceiverPrescriptionDetailsActivity extends AppCompatActivity {

    @ViewById(R.id.activity_receiver_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_receiver_presciption_details_tv_name)
    protected TextView tvName;
    @ViewById(R.id.activity_presciption_details_tv_email)
    protected TextView tvEmail;
    @ViewById(R.id.activity_presciption_details_tv_address)
    protected TextView tvAddress;
    @ViewById(R.id.activity_presciption_details_tv_numberbuy)
    protected TextView tvNumberBuy;
    @ViewById(R.id.activity_presciption_details_lv_prescription)
    protected ListView lvReceiver;

    private ArrayAdapterListview arrayAdapterListview;

    @Extra
    protected Prescription prescription;

    @AfterViews
    void init() {

        setToolBar();

        showDetails();

    }

    private void setToolBar() {
        toolbar.setTitle("Chi tiết đơn thuốc");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void showDetails() {

        tvName.setText(prescription.getIdDatabaseCreate());
        tvEmail.setText(prescription.getEmail());
        tvAddress.setText(prescription.getAddressReceive());
        tvNumberBuy.setText(prescription.getNumberBuy());

        arrayAdapterListview = new ArrayAdapterListview(
                ReceiverPrescriptionDetailsActivity.this,
                R.layout.custom_listview_prescription,
                prescription.getMiniPrescription());
        lvReceiver.setAdapter(arrayAdapterListview);

    }

    @Click(R.id.activity_receiverPresciption_tv_btn_selectPay)
    protected void selectPay() {
        Intent intent = new Intent(getApplication(), PayActivity_.class);
                startActivity(intent);

    }
}
