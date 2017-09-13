package com.example.thanh.OnlinePharmacy.view.prescription.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.pay.PayActivity_;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.example.thanh.OnlinePharmacy.utils.Constants.MY_PERMISSIONS_REQUEST_CAMERA;

/**
 * Created by PC_ASUS on 6/23/2017.
 */
@EFragment(R.layout.fragment_qrcode_prescription)
public class QRcodePrescriptionFragment extends Fragment {

    @ViewById(R.id.activity_scan_qr_code_view_barcode_scanner)
    protected CompoundBarcodeView viewQRcode;

    private SharedPreferences sharedPreferences;
    private String id;
    private String token;
    private String email;
    private ArrayAdapterListview arrayAdapterListview;

    @AfterViews
    protected void init() {

        initSharedPreferences();

        checkCamera();
    }

    protected void checkCamera() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            runCameraQRCode();
        }
    }

    private void runCameraQRCode() {
        viewQRcode.decodeContinuous(callback);
        viewQRcode.setStatusText("");
        viewQRcode.getViewFinder().setVisibility(View.GONE);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                Log.d("QR code result", result.toString());
                handleResult(result);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            System.out.println("Possible Result points = " + resultPoints);
        }
    };

    private void initSharedPreferences() {

        sharedPreferences = getActivity().getSharedPreferences("account", MODE_PRIVATE);
        id = sharedPreferences.getString(Constants.ID, "");
        token = sharedPreferences.getString(Constants.TOKEN, "");
        email = sharedPreferences.getString(Constants.EMAIL, "");
    }


    public void handleResult(BarcodeResult result) {

        if (result != null) {
                Log.e("kết quả", "onActivityResult: " + "QR");
                Prescription prescription = new Prescription();
                prescription.setEmail(email);   //get mail
                prescription.setId(id);     //get id
                prescription.setAddressReceive("nghĩ cách điền sau");   //getaddress
                prescription.setNumberBuy(time());      //number buy
                prescription.setStatus("false");

                //parse json
                try {

                    Gson gson = new Gson();
                    parseJson(prescription, result);
                    //show dialog
                    showDialogInfoPrescription(prescription);

                } catch (Exception e) {
                    Log.e("Error QR", "" + e.getMessage());
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.qr_faile),
                            Toast.LENGTH_LONG)
                            .show();
                }
        } else {

        }
    }

    private void parseJson(Prescription prescription, BarcodeResult result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<MiniPrescription>>() {
        }.getType();
        ArrayList<MiniPrescription> miniPrescriptions = gson.fromJson(result.toString(), type);
        prescription.setMiniPrescription(miniPrescriptions);

        //method old
       /* ArrayList<MiniPrescription> miniPrescriptions = new ArrayList<>();
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
            miniPrescriptions.addView(tempMiniPrescription);
        }
        prescription.setMiniPrescription(miniPrescriptions);*/
    }

    private void showDialogInfoPrescription(Prescription prescription) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View customDialogView = li.inflate(R.layout.dialog_send_presciption, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
                R.layout.custom_listview_prescription,
                prescription.getMiniPrescription());
        lvSendPresciption.setAdapter(arrayAdapterListview);

        alertDialogBuilder.setCancelable(false).setPositiveButton("Đồng Ý",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //accept
                        //submit server
                        postPrescription(prescription, dialog);
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

    private void postPrescription(Prescription prescription, DialogInterface dialog) {

        Observable.defer(() -> NetworkUtil.getRetrofit(token).postPrescription(prescription, id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseStatus -> {
                    Toast.makeText(
                            getActivity(),
                            "Thành Công: " +
                                    responseStatus.getStatus() +
                                    "  " +
                                    responseStatus.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    PayActivity_.intent(getActivity()).start();
                    dialog.dismiss();
                }, throwable -> {
                    Toast.makeText(
                            getActivity(),
                            "Thất Bại " +
                                    throwable.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", "" + throwable.getMessage());
                }, () -> {
                    Log.e("send action", "Load success");
                });
    }


    private String time() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        return timeFormat.format(today.getTime());
    }
}

