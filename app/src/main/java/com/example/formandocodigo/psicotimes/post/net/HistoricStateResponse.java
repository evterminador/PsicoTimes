package com.example.formandocodigo.psicotimes.post.net;

import com.example.formandocodigo.psicotimes.data.entity.HistoricStateEntity;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by FormandoCodigo on 26/12/2017.
 */

public class HistoricStateResponse {
    @Json(name = "message")
    String message;

    @SerializedName("historic_order")
    List<HistoricStateEntity> historicStateEntities;

    public String getMessage() {
        return message;
    }

    public List<HistoricStateEntity> getHistoricStateEntities() {
        return historicStateEntities;
    }
}
