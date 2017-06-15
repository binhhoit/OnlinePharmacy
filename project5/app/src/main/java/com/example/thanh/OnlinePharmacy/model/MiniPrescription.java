package com.example.thanh.OnlinePharmacy.model;

/**
 * Created by thanh on 3/22/2017.
 */

import com.google.gson.annotations.SerializedName;



public class MiniPrescription {
    @SerializedName("nameMedical")
    private String nameMedical;
    @SerializedName("number")
    private String number;
    @SerializedName("_id")
    private String _id;

    public String getNameMedical() {
        return nameMedical;
    }

    public void setNameMedical(String nameMedical) {
        this.nameMedical = nameMedical;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
