package com.example.formandocodigo.psicotimes.data.cache.serializer;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by FormandoCodigo on 29/11/2017.
 */

@Singleton
public class Serializer {

    private final Gson gson = new Gson();

    @Inject
    public Serializer() {}

    /**
     * Serialize an object to Json.
     *
     * @param object to serialize.
     */
    public String serialize(Object object, Class clazz) {
        return gson.toJson(object, clazz);
    }

    public String serializeAll(ArrayList<StateUseEntity> objects) {
        return gson.toJson(objects);
    }

    /**
     * Deserialize a json representation of an object.
     *
     * @param string A json string to deserialize.
     */
    public <T> T deserialize(String string, Class<T> clazz) {
        return gson.fromJson(string, clazz);
    }

    public ArrayList<StateUseEntity> deserializeAll(String string) {
        Type type = new TypeToken<ArrayList<StateUseEntity>>() {}.getType();
        return gson.fromJson(string, type);
    }
}
