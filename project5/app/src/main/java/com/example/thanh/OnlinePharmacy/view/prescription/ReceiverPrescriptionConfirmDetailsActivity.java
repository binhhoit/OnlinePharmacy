package com.example.thanh.OnlinePharmacy.view.prescription;

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

@EActivity(R.layout.activity_receiver_prescription_details_confirm)
public class ReceiverPrescriptionConfirmDetailsActivity extends AppCompatActivity {

    @ViewById(R.id.activity_receiver_confirm_details_toolbar)
    Toolbar toolbar;
    @ViewById(R.id.activity_receiver_confirm_detail_tv_name)
    protected TextView tvName;
    @ViewById(R.id.activity_receiver_confirm_detail_tv_email)
    protected TextView tvEmail;
    @ViewById(R.id.activity_receiver_confirm_detail_tv_address)
    protected TextView tvAddress;
    @ViewById(R.id.activity_receiver_confirm_detail_tv_price)
    protected TextView tvPrice;
    @ViewById(R.id.activity_receiver_confirm_detail_tv_numberbuy)
    protected TextView tvNumberBuy;
    @ViewById(R.id.activity_receiver_confirm_detail_lv_prescription)
    protected ListView lvReceiver;

    @Extra
    protected Prescription prescription;

    private ArrayAdapterListview arrayAdapterListview;

    @AfterViews
    void init() {

        setToolBar();

        showData();
    }

    private void setToolBar() {
        toolbar.setTitle("Đơn thuốc đã duyệt");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void showData() {

        tvName.setText(prescription.getIdDatabaseCreate());
        tvEmail.setText(prescription.getEmail());
        tvAddress.setText(prescription.getAddressReceive());
        tvNumberBuy.setText(prescription.getNumberBuy());
        tvPrice.setText(prescription.getPrice());

        arrayAdapterListview = new ArrayAdapterListview(
                ReceiverPrescriptionConfirmDetailsActivity.this,
                R.layout.custom_listview_prescription,
                prescription.getMiniPrescription());

        lvReceiver.setAdapter(arrayAdapterListview);

    }

    @Click(R.id.activity_receiverPresciptionConfirm_btn_selectPay)
    protected void selectPay() {

        PayActivity_.intent(this).start();
    }
}
