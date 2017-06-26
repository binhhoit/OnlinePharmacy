package com.example.thanh.OnlinePharmacy.model;

/**
 * Created by thanh on 3/22/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Prescription implements Serializable {
    @SerializedName("_id")
    private String idDatabaseCreate;
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
    @SerializedName("prescription")
    private List<MiniPrescription> miniPrescription = new ArrayList<>();

    public String getIdDatabaseCreate() {
        return idDatabaseCreate;
    }

    public void setIdDatabaseCreate(String idDatabaseCreate) {
        this.idDatabaseCreate = idDatabaseCreate;
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
        // status = "false";
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

    public List<MiniPrescription> getMiniPrescription() {
        return miniPrescription;
    }

    public void setMiniPrescription(List<MiniPrescription> miniPrescription) {
        this.miniPrescription = miniPrescription;
    }
}
