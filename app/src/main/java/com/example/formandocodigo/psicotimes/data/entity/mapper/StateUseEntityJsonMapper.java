package com.example.formandocodigo.psicotimes.data.entity.mapper;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by FormandoCodigo on 29/11/2017.
 */

public class StateUseEntityJsonMapper {
    private final Gson gson;

    @Inject
    public StateUseEntityJsonMapper() {
        this.gson = new Gson();
    }

    /**
     * Transform from valid json string to {@link StateUseEntity}.
     *
     * @param stateUseJsonResponse A json representing a user profile.
     * @return {@link StateUseEntity}.
     * @throws JsonSyntaxException if the json string is not a valid json structure.
     */
    public StateUseEntity transformStateUse(String stateUseJsonResponse) throws JsonSyntaxException {
        final Type userEntityType = new TypeToken<StateUseEntity>() {}.getType();
        return this.gson.fromJson(stateUseJsonResponse, userEntityType);
    }

    /**
     * Transform from valid json string to List of {@link StateUseEntity}.
     *
     * @param stateUseListJsonResponse A json representing a collection of users.
     * @return List of {@link StateUseEntity}.
     * @throws JsonSyntaxException if the json string is not a valid json structure.
     */
    public ArrayList<StateUseEntity> transformStateUseCollection(String stateUseListJsonResponse)
            throws JsonSyntaxException {
        final Type listOfStateUseType = new TypeToken<ArrayList<StateUseEntity>>() {}.getType();
        return this.gson.fromJson(stateUseListJsonResponse, listOfStateUseType);
    }
}
