package com.example.thanh.OnlinePharmacy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thanh on 3/29/2017.
 */

public class PayCard {
    @SerializedName("pin")
    private String pin;
    @SerializedName("serial")
    private String serial;
    @SerializedName("type")
    private String type;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
