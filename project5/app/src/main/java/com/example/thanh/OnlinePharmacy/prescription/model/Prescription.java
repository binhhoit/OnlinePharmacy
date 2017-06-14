package com.example.thanh.OnlinePharmacy.prescription.model;

/**
 * Created by thanh on 3/22/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Prescription {
    @SerializedName("_id")
    private String _id;
    @SerializedName("id")
    private String id;
    @SerializedName("email")
    private String email;
    @SerializedName("status")
    private String status;
    @SerializedName("addressReceive")
    private String addressReceive;
    @SerializedName("numberBuy")
    private String numberBuy;
    @SerializedName("price")
    private String price;
    @SerializedName("Prescription")
    private ArrayList<MiniPrescription> miniPrescription = new ArrayList<>();

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressReceive() {
        return addressReceive;
    }

    public void setAddressReceive(String addressReceive) {
        this.addressReceive = addressReceive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = "false";
        this.status = status;
    }

    public String getNumberBuy() {
        return numberBuy;
    }

    public void setNumberBuy(String numberBuy) {
        this.numberBuy = numberBuy;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        price = "0";
        this.price = price;
    }

    public ArrayList<MiniPrescription> getMiniPrescription() {
        return miniPrescription;
    }

    public void setMiniPrescription(ArrayList<MiniPrescription> miniPrescription) {
        this.miniPrescription = miniPrescription;
    }


}
