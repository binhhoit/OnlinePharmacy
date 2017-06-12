package com.example.thanh.OnlinePharmacy.Prescription.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ASUS_K46CM on 5/8/2017.
 */

public class photoPrescription {
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
    @SerializedName("number_buy")
    private String number_buy;
    @SerializedName("photo")
    private String photo;

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

    public String getNumber_buy() {
        return number_buy;
    }

    public void setNumber_buy(String number_buy) {
        this.number_buy = number_buy;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
