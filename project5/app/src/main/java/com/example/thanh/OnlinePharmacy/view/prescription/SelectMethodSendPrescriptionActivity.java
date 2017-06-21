package com.example.thanh.OnlinePharmacy.view.prescription;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.thanh.OnlinePharmacy.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_select_method_send_prescription)
public class SelectMethodSendPrescriptionActivity extends AppCompatActivity {
    @ViewById(R.id.btn_photo)
    protected Button btnPhoto;
    @ViewById(R.id.btn_QR)
    protected Button btnQr;
    @ViewById(R.id.btn_handInput)
    protected Button btnHandInput;

    @AfterViews
    protected void init() {
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhoto = new Intent(
                        SelectMethodSendPrescriptionActivity.this,
                        TakePhotoSentPrescriptionActivity.class);
                startActivity(takePhoto);
            }
        });
        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qrCode = new Intent(SelectMethodSendPrescriptionActivity.this, QRcodePrescriptionActivity.class);
                startActivity(qrCode);
            }
        });
        btnHandInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent handInput = new Intent(SelectMethodSendPrescriptionActivity.this, SendPresciptionActivity.class);
                startActivity(handInput);
            }
        });

    }

}
