package com.example.thanh.OnlinePharmacy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ASUS_K46CM on 5/8/2017.
 */

public class PhotoPrescription {
    @SerializedName("idDatabaseCreate")
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
    @SerializedName("photo")
    private String photo;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddressReceive() {
        return addressReceive;
    }

    public void setAddressReceive(String addressReceive) {
        this.addressReceive = addressReceive;
    }

    public String getNumberBuy() {
        return numberBuy;
    }

    public void setNumberBuy(String numberBuy) {
        this.numberBuy = numberBuy;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
