package com.example.thanh.OnlinePharmacy;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thanh on 4/5/2017.
 */

public class ResponseStatus {
    @SerializedName("status")
    private String Status;
    @SerializedName("message")
    private String Message;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
