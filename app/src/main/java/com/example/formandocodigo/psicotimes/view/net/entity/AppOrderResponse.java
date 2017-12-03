package com.example.formandocodigo.psicotimes.view.net.entity;

import com.example.formandocodigo.psicotimes.model.StateUse;
import com.squareup.moshi.Json;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 03/12/2017.
 */

public class AppOrderResponse {

    @Json(name = "message")
    String message;

    @Json(name = "appOrder")
    ArrayList<StateUse> stateUses;

    public String getMessage() {
        return message;
    }

    public ArrayList<StateUse> getStateUses() {
        return stateUses;
    }
}
