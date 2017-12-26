package com.example.formandocodigo.psicotimes.post.net;

import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;


import java.util.Map;

/**
 * Created by FormandoCodigo on 26/12/2017.
 */

public class HistoricStateResponse {
    @Json(name = "message")
    String message;

    @Json(name = "historicOrder")
    Map<String, String> info;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }
}
