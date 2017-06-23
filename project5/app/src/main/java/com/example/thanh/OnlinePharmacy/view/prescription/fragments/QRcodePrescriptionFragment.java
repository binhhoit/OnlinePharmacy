package com.example.thanh.OnlinePharmacy.view.prescription.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.ArrayAdapterListview;
import com.example.thanh.OnlinePharmacy.model.MiniPrescription;
import com.example.thanh.OnlinePharmacy.model.Prescription;
import com.example.thanh.OnlinePharmacy.model.ResponseStatus;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.pay.PayActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by PC_ASUS on 6/23/2017.
 */
@EFragment(R.layout.fragment_qrcode_prescription)
public class QRcodePrescriptionFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private String id;
    private String email;
    private ArrayAdapterListview arrayAdapterListview;

    @AfterViews
    protected void init() {
        initSharedPreferences();
        scannerQrCode(getActivity());
    }

    private void scannerQrCode(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan QR");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    private void initSharedPreferences() {

        sharedPreferences = getActivity().getSharedPreferences("account", MODE_PRIVATE);
        id = sharedPreferences.getString(Constants.ID, "");
        email = sharedPreferences.getString(Constants.EMAIL, "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "you cancelled the scanning", Toast.LENGTH_SHORT).show();
            } else {

                Prescription prescription = new Prescription();
                prescription.setEmail(email);   //get mail
                prescription.setId(id);     //get id
                prescription.setAddressReceive("nghĩ cách điền sau");   //getaddress
                prescription.setNumberBuy(time().toString());      //number buy
                prescription.setStatus("false");
                ArrayList<MiniPrescription> miniPrescriptions = new ArrayList<MiniPrescription>();

                //parse json
                try {
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonArray = (JsonArray) jsonParser.parse(result.getContents());
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Gson gson = new Gson();
                        MiniPrescription tempMiniPrescription = gson.fromJson(
                                jsonArray
                                        .get(i)
                                        .getAsJsonObject()
                                        .toString(),
                                MiniPrescription.class);
                        miniPrescriptions.add(tempMiniPrescription);
                    }
                    prescription.setMiniPrescription(miniPrescriptions);

                    //show dialog
                    LayoutInflater li = LayoutInflater.from(getActivity());
                    View customDialogView = li.inflate(R.layout.dialog_send_presciption, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Đơn Thuốc Bạn Đặt Mua");
                    alertDialogBuilder.setView(customDialogView);

                    // gán các thuộc tính vào dialog
                    ListView lvSendPresciption = (ListView) customDialogView
                            .findViewById(R.id.dialog_lv_sendPresciption);
                    TextView tvNameSend = (TextView) customDialogView
                            .findViewById(R.id.dialog_tv_nameSend);
                    TextView tvAddressSend = (TextView) customDialogView
                            .findViewById(R.id.dialog_tv_addressSend);
                    TextView tvNumberSend = (TextView) customDialogView
                            .findViewById(R.id.dialog_tv_numberSend);

                    tvNameSend.setText(prescription.getEmail());
                    tvAddressSend.setText(prescription.getAddressReceive());
                    tvNumberSend.setText(prescription.getNumberBuy());

                    arrayAdapterListview = new ArrayAdapterListview(
                            getActivity(),
                            R.layout.custom_listview,
                            prescription.getMiniPrescription());
                    lvSendPresciption.setAdapter(arrayAdapterListview);

                    alertDialogBuilder.setCancelable(false).setPositiveButton("Đồng Ý",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //accept
                                    //submit server
                                    Call<ResponseStatus> call = NetworkUtil.getRetrofit().postPrescription(prescription);
                                    call.enqueue(new Callback<ResponseStatus>() {
                                        @Override
                                        public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                                            Toast.makeText(
                                                    getActivity(),
                                                    "Thành Công: " +
                                                            response.body().getStatus() +
                                                            "  " +
                                                            response.body().getMessage(),
                                                    Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getActivity(), PayActivity.class);
                                            dialog.dismiss();
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseStatus> call, Throwable t) {
                                            Toast.makeText(
                                                    getActivity(),
                                                    "Thất Bại " +
                                                            t.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                            Log.e("ERROR", "" + t.getMessage());
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Sửa hoặc Hủy",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            scannerQrCode(getActivity());
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } catch (Exception e) {
                    Log.e("Error QR", "" + e.getMessage());
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.qr_faile),
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("finish", "ket thuc");
        //finish();
    }

    private String time() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        return timeFormat.format(today.getTime());
    }
}

