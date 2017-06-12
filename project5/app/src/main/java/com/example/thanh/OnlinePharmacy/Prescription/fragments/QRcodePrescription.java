package com.example.thanh.OnlinePharmacy.Prescription.fragments;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.Login.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.Login.utils.Constants;
import com.example.thanh.OnlinePharmacy.Pay.fragment.Pay;
import com.example.thanh.OnlinePharmacy.Prescription.model.ArrayAdapter;
import com.example.thanh.OnlinePharmacy.Prescription.model.prescription;
import com.example.thanh.OnlinePharmacy.Prescription.model.miniPrescription;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.ResponseStatus;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QRcodePrescription extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    private String mId;
    private String mEmail;
    private ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode__prescription);
        initSharedPreferences();

        final Activity activity = this;
        ScannerQRcode(activity);


    }

    private void ScannerQRcode(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan QR");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    ;

    private void initSharedPreferences() {

        mSharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        mId = mSharedPreferences.getString(Constants.ID, "");
        mEmail = mSharedPreferences.getString(Constants.EMAIL, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "you cancelled the scanning", Toast.LENGTH_SHORT).show();
            } else {

                prescription prescription = new prescription();
                prescription.setEmail(mEmail);//tên Người dùng (lấy giá trị email)
                prescription.setId(mId); //lấy id đăng nhập gán cứng vào
                prescription.setAddressReceive("nghĩ cách điền sau"); //địa chỉ nhận thuốc tự điền
                prescription.setNumber_buy(time().toString());// số lần mua lấy thời gian ngày giờ
                prescription.setStatus("false");
                // thêm tạm vào test
                ArrayList<miniPrescription> miniPrescriptions = new ArrayList<miniPrescription>();
                miniPrescription miniPrescription_ = new miniPrescription();
                //parse json
                try {
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonArray = (JsonArray) jsonParser.parse(result.getContents());
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Gson gson = new Gson();
                        miniPrescription_ = gson.fromJson(jsonArray.get(i).getAsJsonObject().toString(), miniPrescription.class);
                        /*miniPrescription_.setNameMedical(jsonArray.get(i).getAsJsonObject().get("nameMedical").toString());
                        miniPrescription_.setNumber(jsonArray.get(i).getAsJsonObject().get("number").toString());// Lay thong tin so luong*/
                        miniPrescriptions.add(miniPrescription_);
                    }
                    prescription.setMiniPrescription(miniPrescriptions);

                    LayoutInflater li = LayoutInflater.from(QRcodePrescription.this);
                    View customDialogView = li.inflate(R.layout.custom_dialog_send_presciption, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QRcodePrescription.this);
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
                    arrayAdapter = new ArrayAdapter(QRcodePrescription.this, R.layout.custom_listview, prescription.getMiniPrescription());
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
                                            Toast.makeText(QRcodePrescription.this, "Thành Công: " + response.body().getStatus() +
                                                    "  " + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(QRcodePrescription.this, Pay.class);
                                            dialog.dismiss();
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseStatus> call, Throwable t) {
                                            Toast.makeText(QRcodePrescription.this, "Thất Bại " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("ERROR", "" + t.getMessage());
                                        }
                                    });


                                }
                            })
                            .setNegativeButton("Sửa hoặc Hủy",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
                                            ScannerQRcode(QRcodePrescription.this);
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                } catch (Exception e) {
                    Log.e("Error QR", "" + e.getMessage());
                    Toast.makeText(getApplication(), "QR định dạng ko chính xác, vui lòng thử lại", Toast.LENGTH_LONG).show();
                }


            }
        } else

        {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("finish", "ket thuc");
        finish();
    }

    private String time() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        return timeFormat.format(today.getTime());
    }
}
