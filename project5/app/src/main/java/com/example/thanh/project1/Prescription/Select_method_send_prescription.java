package com.example.thanh.project1.Prescription;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.thanh.project1.Prescription.fragments.QRcode_Prescription;
import com.example.thanh.project1.Prescription.fragments.Take_Photo_sent_prescription;
import com.example.thanh.project1.Prescription.fragments.sendPresciption;

import com.example.thanh.project1.R;

public class Select_method_send_prescription extends AppCompatActivity {
    Button btn_photo,btn_QR,btn_handInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_method_send_prescription);
        FindViewByIds();
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TakePhoto = new Intent(Select_method_send_prescription.this, Take_Photo_sent_prescription.class);
                startActivity(TakePhoto);
            }
        });
        btn_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent QRcode = new Intent(Select_method_send_prescription.this, QRcode_Prescription.class);
                startActivity(QRcode);
            }
        });
        btn_handInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HandInput = new Intent(Select_method_send_prescription.this, sendPresciption.class);
                startActivity(HandInput);
            }
        });

    }
    private void FindViewByIds(){
        btn_photo = (Button) findViewById(R.id.btn_photo);
        btn_QR = (Button) findViewById(R.id.btn_QR);
        btn_handInput = (Button) findViewById(R.id.btn_handInput);
    }
}
