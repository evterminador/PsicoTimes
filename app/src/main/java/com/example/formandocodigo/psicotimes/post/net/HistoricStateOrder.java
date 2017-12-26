package com.example.formandocodigo.psicotimes.post.net;

import com.example.formandocodigo.psicotimes.entity.StateUse;
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

    @SerializedName("stateUse")
    private List<StateUse> stateUses;

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

    public List<StateUse> getStateUses() {
        return stateUses;
    }

    public void setStateUses(List<StateUse> stateUses) {
        this.stateUses = stateUses;
    }
}
