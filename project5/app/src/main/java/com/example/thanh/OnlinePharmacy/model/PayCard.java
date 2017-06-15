package com.example.thanh.OnlinePharmacy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thanh on 3/29/2017.
 */

public class PayCard {
    @SerializedName("pin")
    private String Pin;
    @SerializedName("serial")
    private String Serial;
    @SerializedName("type")
    private String Type;

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
