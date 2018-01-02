package com.example.formandocodigo.psicotimes.post.net;

import com.example.formandocodigo.psicotimes.data.entity.HistoricStateEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by FormandoCodigo on 26/12/2017.
 */

public class HistoricStateOrder {

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    @SerializedName("quantity_screen_unlock")
    private int quantityScreenUnlock;

    @SerializedName("historic_state")
    private HistoricStateEntity historicState;

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

    public int getQuantityScreenUnlock() {
        return quantityScreenUnlock;
    }

    public void setQuantityScreenUnlock(int quantityScreenUnlock) {
        this.quantityScreenUnlock = quantityScreenUnlock;
    }

    public HistoricStateEntity getHistoricState() {
        return historicState;
    }

    public void setHistoricState(HistoricStateEntity historicState) {
        this.historicState = historicState;
    }
}
