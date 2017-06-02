package com.example.thanh.project1.Pay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.thanh.project1.Prescription.fragments.QRcode_Prescription;
import com.example.thanh.project1.R;

public class Pay extends AppCompatActivity {
    Button btn_paycard;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        FindViewById();
        payCard();

    }

    private void FindViewById() {
        btn_paycard = (Button) findViewById(R.id.btn_pay);
    }

    private void payCard() {
        btn_paycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passIntentPayCard = new Intent(getApplication(), Pay_card.class);
                startActivity(passIntentPayCard);
                finish();
            }
        });
    }
}
