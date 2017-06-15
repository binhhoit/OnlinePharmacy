package com.example.thanh.OnlinePharmacy.view.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.thanh.OnlinePharmacy.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_pay)
public class PayActivity extends AppCompatActivity {

    @ViewById(R.id.btn_pay)
    Button btnPaycard;


    @AfterViews
    void init() {

        payCard();

    }

    private void payCard() {
        btnPaycard.setOnClickListener(v -> {
            Intent passIntentPayCard = new Intent(getApplication(), PayCardActivity.class);
            startActivity(passIntentPayCard);
            finish();
        });
    }
}
