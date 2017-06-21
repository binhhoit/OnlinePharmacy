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
    Button btnPhoto;
    @ViewById(R.id.btn_QR)
    Button btnQR;
    @ViewById(R.id.btn_handInput)
    Button btnHandInput;

    @AfterViews
    void init() {
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TakePhoto = new Intent(
                        SelectMethodSendPrescriptionActivity.this,
                        TakePhotoSentPrescriptionActivity.class);
                startActivity(TakePhoto);
            }
        });
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent QRcode = new Intent(SelectMethodSendPrescriptionActivity.this, QRcodePrescriptionActivity.class);
                startActivity(QRcode);
            }
        });
        btnHandInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HandInput = new Intent(SelectMethodSendPrescriptionActivity.this, SendPresciptionActivity.class);
                startActivity(HandInput);
            }
        });

    }


}
