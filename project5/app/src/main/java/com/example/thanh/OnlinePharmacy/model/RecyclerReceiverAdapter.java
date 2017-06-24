package com.example.thanh.OnlinePharmacy.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thanh.OnlinePharmacy.R;

import java.util.List;

/**
 * Created by ASUS_K46CM on 6/24/2017.
 */

public class RecyclerReceiverAdapter extends RecyclerView.Adapter<RecyclerReceiverAdapter.ViewHolder> {

    private List<Prescription> prescription;
    private Context context;

    public RecyclerReceiverAdapter(List<Prescription> prescription, Context context) {
        this.prescription = prescription;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyceler_view_receiver, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvIdPrescription.setText("sjds");
        holder.tvDayBuyPrescription.setText("asjewqdi");
    }

    @Override
    public int getItemCount() {
        return prescription.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView tvIdPrescription;
        private TextView tvDayBuyPrescription;
        private TextView tvDetailsPrescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvIdPrescription = (TextView) itemView.findViewById(R.id.rcl_id_prescription);
            tvDayBuyPrescription = (TextView) itemView.findViewById(R.id.rcl_day_buy_prescription);
            tvDetailsPrescription = (TextView) itemView.findViewById(R.id.rcl_details_prescription);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
