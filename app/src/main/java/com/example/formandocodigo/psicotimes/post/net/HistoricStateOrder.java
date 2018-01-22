package com.example.formandocodigo.psicotimes.post.net;

import com.example.formandocodigo.psicotimes.data.entity.HistoricStateEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by FormandoCodigo on 26/12/2017.
 */

public class HistoricStateOrder {

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    @SerializedName("historic_state")
    private List<HistoricStateEntity> historicStateEntities;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<HistoricStateEntity> getHistoricStateEntities() {
        return historicStateEntities;
    }

    public void setHistoricStateEntities(List<HistoricStateEntity> historicStateEntities) {
        this.historicStateEntities = historicStateEntities;
    }
}
