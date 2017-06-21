package com.example.thanh.OnlinePharmacy.view.prescription;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.pay.PayActivity;
import com.example.thanh.OnlinePharmacy.model.ArrayAdapterListview;
import com.example.thanh.OnlinePharmacy.model.Prescription;
import com.example.thanh.OnlinePharmacy.model.MiniPrescription;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.ResponseStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_send_presciption)
public class SendPresciptionActivity extends AppCompatActivity {

    @ViewById(R.id.activity_send_prescript_btn_Add)
    protected ImageButton btnAdd;
    @ViewById(R.id.activity_send_prescript_btn_Submit)
    protected Button btnSubmit;
    @ViewById(R.id.activity_send_prescript_tv_name_transfer)
    protected TextView etUserName;
    @ViewById(R.id.activity_send_prescript_et_address_transfer)
    protected EditText etAddress;
    @ViewById(R.id.activity_send_prescript_et_number_transfer)
    protected TextView etNumber;

    private ArrayAdapterListview arrayAdapterListview;
    private SharedPreferences sharedPreferences;
    private String Id;
    private String Email;

    @AfterViews
    void init() {

        initSharedPreferences();
        add(this, btnAdd);
        submit(this, btnSubmit);

    }

    private void initSharedPreferences() {
        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        Id = sharedPreferences.getString(Constants.ID, "");
        Email = sharedPreferences.getString(Constants.EMAIL, "");
    }

    public void submit(final Activity activity, Button btn) {
        etUserName.setText(Email);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout scrollViewlinerLayout = (LinearLayout) activity.findViewById(R.id.linearLayoutForm);
                Prescription prescription = new Prescription();
                prescription.setEmail(etUserName.getText().toString());     //user name get email
                prescription.setId(Id);     //get ID
                prescription.setAddressReceive(etAddress.getText().toString());     //get address
                etNumber.setText(time());      //get time buy
                prescription.setNumberBuy(etNumber.getText().toString());// set number buy
                prescription.setStatus("false");
                ArrayList<MiniPrescription> miniPrescriptions = new ArrayList<MiniPrescription>();

                for (int i = 0; i < scrollViewlinerLayout.getChildCount(); i++) {
                    LinearLayout innerLayout = (LinearLayout) scrollViewlinerLayout.getChildAt(i);
                    EditText medicine = (EditText) innerLayout.findViewById(R.id.et_medicine);
                    EditText number = (EditText) innerLayout.findViewById(R.id.et_number);
                    MiniPrescription tempMiniPrescription = new MiniPrescription();
                    tempMiniPrescription.setNameMedical(medicine.getText().toString());// Lay thong tin medicine(thuốc)
                    tempMiniPrescription.setNumber(number.getText().toString());// Lay thong tin so luong
                    miniPrescriptions.add(tempMiniPrescription);
                    prescription.setMiniPrescription(miniPrescriptions);

                }
                LayoutInflater li = LayoutInflater.from(SendPresciptionActivity.this);
                View customDialogView = li.inflate(R.layout.dialog_send_presciption, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SendPresciptionActivity.this);
                alertDialogBuilder.setTitle("Đơn Thuốc Bạn Đặt Mua");
                alertDialogBuilder.setView(customDialogView);
                // gán các thuộc tính vào dialog
                ListView lvSendPresciption = (ListView) customDialogView.findViewById(R.id.dialog_lv_sendPresciption);
                TextView tvNameSend = (TextView) customDialogView.findViewById(R.id.dialog_tv_nameSend);
                TextView tvAddressSend = (TextView) customDialogView.findViewById(R.id.dialog_tv_addressSend);
                TextView tvNumberSend = (TextView) customDialogView.findViewById(R.id.dialog_tv_numberSend);
                tvNameSend.setText(prescription.getEmail());
                tvAddressSend.setText(prescription.getAddressReceive());
                tvNumberSend.setText(prescription.getNumberBuy());
                arrayAdapterListview = new ArrayAdapterListview(
                        SendPresciptionActivity.this,
                        R.layout.custom_listview,
                        prescription.getMiniPrescription());
                lvSendPresciption.setAdapter(arrayAdapterListview);
                //
                alertDialogBuilder.setCancelable(false).setPositiveButton("Đồng Ý",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //submit server
                                Call<ResponseStatus> call = NetworkUtil.getRetrofit().postPrescription(prescription);
                                call.enqueue(new Callback<ResponseStatus>() {
                                    @Override
                                    public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                                        Toast.makeText(
                                                activity.getApplicationContext(),
                                                "Thành Công: " +
                                                        response.body().getStatus() +
                                                        "  " +
                                                        response.body().getMessage(),
                                                Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SendPresciptionActivity.this, PayActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseStatus> call, Throwable t) {
                                        Toast.makeText(
                                                activity.getApplicationContext(),
                                                "Thất Bại " + t.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        Log.e("ERROR", "" + t.getMessage());
                                    }
                                });

                                Toast.makeText(
                                        activity.getApplicationContext(),
                                        prescription
                                                .getMiniPrescription()
                                                .get(0)
                                                .getNameMedical()
                                                .toString() +
                                                prescription
                                                        .getMiniPrescription()
                                                        .get(0)
                                                        .getNumber()
                                                        .toString(),
                                        Toast.LENGTH_SHORT).show();

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
                final LinearLayout newView = (LinearLayout) activity
                        .getLayoutInflater()
                        .inflate(R.layout.rowdetail, null);
                newView.setLayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

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
