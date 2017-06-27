package com.example.thanh.OnlinePharmacy.view.pay;

import android.support.v7.app.AppCompatActivity;

import com.example.thanh.OnlinePharmacy.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_pay)
public class PayActivity extends AppCompatActivity {

    @Click(R.id.btn_pay)
    protected void payCard() {

        PayCardActivity_.intent(this).start();

    }
}
