package com.thinkdiffai.futurelove.model;

import com.google.gson.annotations.SerializedName;

public class ErrorModel {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
