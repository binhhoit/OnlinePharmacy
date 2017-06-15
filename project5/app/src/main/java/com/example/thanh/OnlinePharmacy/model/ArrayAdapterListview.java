package com.example.thanh.OnlinePharmacy.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thanh.OnlinePharmacy.R;

import java.util.List;

/**
 * Created by thanh on 3/22/2017.
 */

public class ArrayAdapterListview extends android.widget.ArrayAdapter<MiniPrescription> {
    TextView tvMedical;
    TextView tvNumber;
    Context context;

    public ArrayAdapterListview(Context context, int resource, List<MiniPrescription> miniPrescription) {
        super(context, resource, miniPrescription);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_listview, null, false);
        }
        MiniPrescription item = getItem(position);

        tvMedical = (TextView) convertView.findViewById(R.id.custom_listvew_tv_medical);
        tvNumber = (TextView) convertView.findViewById(R.id.custom_listvew_tv_number);

        tvMedical.setText(item.getNameMedical());
        tvNumber.setText(item.getNumber());

        return convertView;

    }

}
