package com.example.thanh.project1.Prescription.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thanh.project1.Prescription.model.miniPrescription;
import com.example.thanh.project1.R;

import java.util.List;

/**
 * Created by thanh on 3/22/2017.
 */

public class ArrayAdapter  extends android.widget.ArrayAdapter<miniPrescription> {
    TextView tv_Medical;
    TextView tv_Number;
    Context context;

    public ArrayAdapter(Context context, int resource, List<miniPrescription> miniPrescription) {
        super(context, resource, miniPrescription);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_listview, null, false);
        }
        miniPrescription u = getItem(position);

        tv_Medical = (TextView) convertView.findViewById(R.id.tv_medical);
        tv_Number = (TextView) convertView.findViewById(R.id.tv_number);

        tv_Medical.setText(u.getNameMedical());
        tv_Number.setText(u.getNumber());

        return convertView;

    }

}
