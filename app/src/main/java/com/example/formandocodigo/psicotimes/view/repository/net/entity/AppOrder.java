package com.example.formandocodigo.psicotimes.view.repository.net.entity;

import com.example.formandocodigo.psicotimes.model.StateUse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 03/12/2017.
 */

public class AppOrder {

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    @SerializedName("stateUse")
    private ArrayList<StateUse> stateUses;

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

    public ArrayList<StateUse> getStateUses() {
        return stateUses;
    }

    public void setStateUses(ArrayList<StateUse> stateUses) {
        this.stateUses = stateUses;
    }
}
