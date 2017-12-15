package com.example.formandocodigo.psicotimes.data.entity.mapper;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by FormandoCodigo on 14/12/2017.
 */

public class StateUserEntityJsonMapper {
    private final Gson gson;

    @Inject
    public StateUserEntityJsonMapper() {
        this.gson = new Gson();
    }

}
