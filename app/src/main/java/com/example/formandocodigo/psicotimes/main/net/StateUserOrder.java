package com.example.formandocodigo.psicotimes.main.net;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by FormandoCodigo on 03/12/2017.
 */

public class StateUserOrder {

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    @SerializedName("state_use")
    private List<StateUseEntity> stateUseEntities;

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

    public List<StateUseEntity> getStateUseEntities() {
        return stateUseEntities;
    }

    public void setStateUseEntities(List<StateUseEntity> stateUseEntities) {
        this.stateUseEntities = stateUseEntities;
    }
}
