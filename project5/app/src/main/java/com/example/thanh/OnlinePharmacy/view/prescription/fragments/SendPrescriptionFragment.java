package com.example.thanh.OnlinePharmacy.view.prescription.fragments;


import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.thanh.OnlinePharmacy.view.pay.PayActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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
@EFragment(R.layout.fragment_sent_prescription)
public class SendPrescriptionFragment extends Fragment {

    @ViewById(R.id.fragment_send_prescript_tv_name_transfer)
    protected TextView etUserName;
    @ViewById(R.id.fragment_send_prescript_et_address_transfer)
    protected EditText etAddress;
    @ViewById(R.id.fragment_send_prescript_et_number_transfer)
    protected TextView etNumber;

    private ArrayAdapterListview arrayAdapterListview;
    private SharedPreferences sharedPreferences;
    private String id;
    private String email;

    @AfterViews
    void init() {
        initSharedPreferences();

        etUserName.setText(email);
        etNumber.setText(time());

        addView();
    }

    private void initSharedPreferences() {
        sharedPreferences = getActivity().getSharedPreferences("account", MODE_PRIVATE);
        id = sharedPreferences.getString(Constants.ID, "");
        email = sharedPreferences.getString(Constants.EMAIL, "");
    }

    @Click(R.id.fragment_send_prescript_btn_Submit)
    protected void submit() {

        LinearLayout scrollViewlinerLayout = (LinearLayout) getActivity()
                .findViewById(R.id.linearLayoutForm);

        Prescription prescription = new Prescription();
        prescription.setEmail(etUserName.getText().toString());     //user name get email
        prescription.setId(id);     //get ID
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
            tempMiniPrescription.setNameMedical(medicine.getText().toString());// get info medicine(thuốc)
            tempMiniPrescription.setNumber(number.getText().toString());// get info number
            miniPrescriptions.add(tempMiniPrescription);
            prescription.setMiniPrescription(miniPrescriptions);

        }

        dialogShow(prescription);
    }

    private void dialogShow(Prescription prescription) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View customDialogView = li.inflate(R.layout.dialog_send_presciption, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
                getActivity(),
                R.layout.custom_listview_prescription,
                prescription.getMiniPrescription());
        lvSendPresciption.setAdapter(arrayAdapterListview);
        //
        alertDialogBuilder.setCancelable(false).setPositiveButton("Đồng Ý",
                (dialog, id1) -> {
                    //submit server
                    submitServer(prescription);
                })
                .setNegativeButton("Sửa hoặc Hủy",
                        (dialog, id12) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void submitServer(Prescription prescription) {

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

                PayActivity_.intent(getActivity()).start();
                getActivity().finish();
                //thêm thông báo bên kia đặt đơn thuốc thành công
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                Toast.makeText(
                        getActivity(),
                        "Thất Bại " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "" + t.getMessage());
            }
        });

    }

    private String time() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        return timeFormat.format(today.getTime());
    }

    @Click(R.id.fragment_send_prescript_btn_Add)
    protected void addView() {
        LinearLayout linearLayoutForm = (LinearLayout) getActivity().findViewById(R.id.linearLayoutForm);

        LinearLayout newView = (LinearLayout) getActivity()
                .getLayoutInflater()
                .inflate(R.layout.custom_row_detail_add_prescription, null);
        newView.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageView btnRemove = (ImageView) newView.findViewById(R.id.btnRemove);

        btnRemove.setOnClickListener(v -> linearLayoutForm.removeView(newView));

        linearLayoutForm.addView(newView);
    }

}
