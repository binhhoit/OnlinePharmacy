package com.example.thanh.OnlinePharmacy.prescription;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.thanh.OnlinePharmacy.R;

public class SelectMethodSendPrescriptionActivity extends AppCompatActivity {
    Button btnPhoto, btnQR, btnHandInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_method_send_prescription);
        FindViewByIds();
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TakePhoto = new Intent(SelectMethodSendPrescriptionActivity.this, TakePhotoSentPrescriptionActivity.class);
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
    private void FindViewByIds(){
        btnPhoto = (Button) findViewById(R.id.btn_photo);
        btnQR = (Button) findViewById(R.id.btn_QR);
        btnHandInput = (Button) findViewById(R.id.btn_handInput);
    }
}
