package com.example.thanh.OnlinePharmacy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thanh on 3/22/2017.
 */

public class MiniPrescription {
    @SerializedName("nameMedical")
    private String nameMedical;
    @SerializedName("number")
    private String number;
    @SerializedName("_id")
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
