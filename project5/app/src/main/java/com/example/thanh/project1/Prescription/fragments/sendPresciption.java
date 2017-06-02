package com.example.thanh.project1.Prescription.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.project1.Login.network.NetworkUtil;
import com.example.thanh.project1.Login.utils.Constants;
import com.example.thanh.project1.Pay.fragment.Pay;
import com.example.thanh.project1.Prescription.model.Prescription;
import com.example.thanh.project1.Prescription.model.miniPrescription;
import com.example.thanh.project1.R;
import com.example.thanh.project1.ResponseStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sendPresciption extends AppCompatActivity {

    private Button btnSubmit;
    private ImageButton btnAdd;
    private TextView et_Username;
    private EditText et_Address;
    private TextView et_Number;
    private ArrayAdapter arrayAdapter;
    private SharedPreferences mSharedPreferences;
    private String mId;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_presciption);
        findViewById();
        initSharedPreferences();
        add(this, btnAdd);
        submit(this, btnSubmit);

    }

    private void initSharedPreferences() {

        mSharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        mId = mSharedPreferences.getString(Constants.ID, "");
        mEmail = mSharedPreferences.getString(Constants.EMAIL, "");
    }

    private void findViewById() {
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        et_Username = (TextView) findViewById(R.id.et_name_transfer);
        et_Address = (EditText) findViewById(R.id.et_address_transfer);
        et_Number = (TextView) findViewById(R.id.et_number_transfer);
    }

    public void submit(final Activity activity, Button btn) {
        et_Username.setText(mEmail);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LinearLayout scrollViewlinerLayout = (LinearLayout) activity.findViewById(R.id.linearLayoutForm);
                Prescription prescription = new Prescription();
                prescription.setEmail(et_Username.getText().toString());//tên Người dùng (lấy giá trị email)
                prescription.setId(mId); //lấy id đăng nhập gán cứng vào
                prescription.setAddressReceive(et_Address.getText().toString()); //địa chỉ nhận thuốc tự điền
                et_Number.setText(time());//gán hiển thị thông tin ngày giờ mua(số hiệu lần mua)
                prescription.setNumber_buy(et_Number.getText().toString());// số lần mua lấy thời gian ngày giờ
                prescription.setStatus("false");
                ArrayList<miniPrescription> miniPrescriptions = new ArrayList<miniPrescription>();

                for (int i = 0; i < scrollViewlinerLayout.getChildCount(); i++) {
                    LinearLayout innerLayout = (LinearLayout) scrollViewlinerLayout.getChildAt(i);
                    EditText medicine = (EditText) innerLayout.findViewById(R.id.et_medicine);
                    EditText number = (EditText) innerLayout.findViewById(R.id.et_number);
                    miniPrescription miniPrescription_ = new miniPrescription();
                    miniPrescription_.setNameMedical(medicine.getText().toString());// Lay thong tin medicine(thuốc)
                    miniPrescription_.setNumber(number.getText().toString());// Lay thong tin so luong
                    miniPrescriptions.add(miniPrescription_);
                    prescription.setMiniPrescription(miniPrescriptions);

                }
                LayoutInflater li = LayoutInflater.from(sendPresciption.this);
                View customDialogView = li.inflate(R.layout.custom_dialog_send_presciption, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(sendPresciption.this);
                alertDialogBuilder.setTitle("Đơn Thuốc Bạn Đặt Mua");
                alertDialogBuilder.setView(customDialogView);
                // gán các thuộc tính vào dialog
                ListView lv_sendPresciption = (ListView) customDialogView.findViewById(R.id.lv_sendPresciption);
                TextView tv_nameSend = (TextView) customDialogView.findViewById(R.id.tv_nameSend);
                TextView tv_addressSend = (TextView) customDialogView.findViewById(R.id.tv_addressSend);
                TextView tv_numberSend = (TextView) customDialogView.findViewById(R.id.tv_numberSend);
                tv_nameSend.setText(prescription.getEmail());
                tv_addressSend.setText(prescription.getAddressReceive());
                tv_numberSend.setText(prescription.getNumber_buy());
                arrayAdapter = new ArrayAdapter(sendPresciption.this, R.layout.custom_listview, prescription.getMiniPrescription());
                lv_sendPresciption.setAdapter(arrayAdapter);
                //
                alertDialogBuilder.setCancelable(false).setPositiveButton("Đồng Ý",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //hành động khi Đồng Ý
                                //submit server
                                Call<ResponseStatus> call = NetworkUtil.getRetrofit().postPrescription(prescription);
                                call.enqueue(new Callback<ResponseStatus>() {
                                    @Override
                                    public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                                        Toast.makeText(activity.getApplicationContext(), "Thành Công: " + response.body().getStatus() +
                                                "  " + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(sendPresciption.this, Pay.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseStatus> call, Throwable t) {
                                        Toast.makeText(activity.getApplicationContext(), "Thất Bại " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("ERROR", "" + t.getMessage());
                                    }
                                });

                                Toast.makeText(activity.getApplicationContext(),
                                        prescription.getMiniPrescription().get(0).getNameMedical().toString()
                                                + prescription.getMiniPrescription().get(0).getNumber().toString(), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Sửa hoặc Hủy",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

    }

    private String time() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        return timeFormat.format(today.getTime());
    }

    public static void add(final Activity activity, ImageButton btn) {
        final LinearLayout linearLayoutForm = (LinearLayout) activity.findViewById(R.id.linearLayoutForm);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final LinearLayout newView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.rowdetail, null);
                newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                ImageButton btnRemove = (ImageButton) newView.findViewById(R.id.btnRemove);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayoutForm.removeView(newView);
                    }
                });
                linearLayoutForm.addView(newView);
            }
        });
    }
}
