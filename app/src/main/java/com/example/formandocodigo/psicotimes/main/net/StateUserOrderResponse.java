package com.example.formandocodigo.psicotimes.main.net;

import com.example.formandocodigo.psicotimes.data.entity.StateUserEntity;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by FormandoCodigo on 03/12/2017.
 */

public class StateUserOrderResponse {

    @Json(name = "message")
    String message;

    @SerializedName("app_order")
    List<StateUserEntity> stateUses;

    public String getMessage() {
        return message;
    }

    public List<StateUserEntity> getStateUses() {
        return stateUses;
    }
}
